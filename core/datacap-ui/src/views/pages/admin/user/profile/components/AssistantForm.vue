<template>
  <div class="relative">
    <ShadcnSpin v-model="loading.default" fixed/>
    <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="host"
                      class="w-[40%]"
                      :label="$t('user.common.host')"
                      :description="$t('user.tip.host')">
        <ShadcnInput v-model="formState.host" name="host"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="token"
                      class="w-[40%]"
                      :label="$t('user.common.token')"
                      :description="$t('user.tip.token')">
        <ShadcnInput v-model="formState.token" name="token"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="timeout"
                      class="w-[40%]"
                      :label="$t('user.common.timeout')"
                      :description="$t('user.tip.timeout')"
                      :rules="[
                          { pattern: /^[0-9]*$/, message: 'Please enter number!' }
                      ]">
        <ShadcnInput v-model="formState.timeout" name="timeout"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="contentCount"
                      class="w-[40%]"
                      :label="$t('user.common.contentCount')"
                      :description="$t('user.tip.contentCount')"
                      :rules="[
                          { pattern: /^[0-9]*$/, message: 'Please enter number!' }
                      ]">
        <ShadcnInput v-model="formState.contentCount" name="contentCount"/>
      </ShadcnFormItem>

      <ShadcnButton submit :loading="loading.submitting" :disabled="loading.submitting">
        {{ $t('common.save') }}
      </ShadcnButton>
    </ShadcnForm>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import UserService from '@/services/user'
import { UserChatModel } from '@/model/user'
import Common from '@/utils/common'
import { isEmpty } from 'lodash'

export default defineComponent({
  name: 'AssistantForm',
  data()
  {
    return {
      loading: {
        default: false,
        submitting: false
      },
      formState: null as UserChatModel | null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading.default = true
      UserService.getInfo()
                 .then(response => {
                   if (response.status) {
                     const configure = response.data.chatConfigure
                     if (response.data && configure && !isEmpty(configure)) {
                       this.formState = JSON.parse(configure) as UserChatModel
                     }
                     else {
                       this.formState = { host: undefined, token: undefined, timeout: 60, contentCount: 10 }
                     }
                   }
                 })
                 .finally(() => this.loading.default = false)
    },
    onSubmit()
    {
      this.loading.submitting = true
      UserService.changeChart(this.formState as UserChatModel)
                 .then((response) => {
                   if (response.status) {
                     this.$Message.success({
                       content: this.$t('common.successfully') as string,
                       showIcon: true
                     })
                     localStorage.setItem(Common.userEditorConfigure, JSON.stringify(this.formState))
                   }
                   else {
                     this.$Message.error({ content: response.message, showIcon: true })
                   }
                 })
                 .finally(() => this.loading.submitting = false)
    }
  }
})
</script>
