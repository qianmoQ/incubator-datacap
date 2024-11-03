<template>
  <div class="relative">
    <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="oldUsername"
                      class="w-[40%]"
                      :description="$t('user.tip.oldUsername')"
                      :label="$t('user.common.oldUsername')">
        <ShadcnInput v-model="formState.oldUsername"
                     name="oldUsername"
                     disabled
                     :placeholder="$t('user.auth.oldUsername')"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="newUsername"
                      class="w-[40%]"
                      :description="$t('user.tip.newUsername')"
                      :label="$t('user.common.newUsername')"
                      :rules="[
                          { required: true, message: $t('user.auth.usernameTip') },
                          { min: 3, message: $t('user.auth.usernameSizeTip') },
                          { max: 20, message: $t('user.auth.usernameSizeTip') }
                      ]">
        <ShadcnInput v-model="formState.newUsername"
                     name="newUsername"
                     :placeholder="$t('user.auth.usernameTip')"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="password"
                      class="w-[40%]"
                      :label="$t('user.common.password')"
                      :description="$t('user.tip.password')"
                      :rules="[
                          { required: true, message: $t('user.auth.passwordTip') },
                          { min: 6, message: $t('user.auth.passwordSizeTip') },
                          { max: 20, message: $t('user.auth.passwordSizeTip') }
                      ]">
        <ShadcnInput v-model="formState.password"
                     type="password"
                     name="password"
                     :placeholder="$t('user.auth.passwordTip')"/>
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
import { UsernameModel } from '@/model/user'
import Common from '@/utils/common'

export default defineComponent({
  name: 'UsernameForm',
  data()
  {
    return {
      loading: {
        default: false,
        submitting: false
      },
      formState: null as UsernameModel | null
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
                     if (response.data) {
                       this.formState = { oldUsername: response.data.username, newUsername: undefined, password: undefined }
                     }
                   }
                 })
                 .finally(() => this.loading.default = false)
    },
    onSubmit()
    {
      this.loading.submitting = true
      UserService.changeUsername(this.formState as UsernameModel)
                 .then((response) => {
                   if (response.status) {
                     this.$Message.success({
                       content: this.$t('user.tip.changeUsernameSuccessfully') as string,
                       showIcon: true
                     })
                     localStorage.removeItem(Common.token)
                     localStorage.removeItem(Common.menu)
                     this.$router.push('/auth/signin')
                   }
                   else {
                     this.$Message.error({
                       content: response.message,
                       showIcon: true
                     })
                   }
                 })
                 .finally(() => this.loading.submitting = false)
    }
  }
})
</script>
