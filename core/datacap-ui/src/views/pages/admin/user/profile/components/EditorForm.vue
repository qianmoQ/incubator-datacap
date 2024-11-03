<template>
  <div class="relative">
    <ShadcnSpin v-model="loading.default" fixed/>
    <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="fontSize"
                      class="w-[40%]"
                      :label="$t('user.common.fontSize')"
                      :description="$t('user.tip.fontSize')"
                      :rules="[
                          { pattern: /^[0-9]*$/, message: 'Please enter number!' }
                      ]">
        <ShadcnInput v-model="formState.fontSize" name="fontSize"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="theme"
                      :label="$t('user.common.theme')"
                      :description="$t('user.tip.theme')">
        <ShadcnRadioGroup v-model="formState.theme">
          <ShadcnRow>
            <ShadcnCol v-for="theme of themes" span="3" class="mb-2">
              <ShadcnRadio :value="theme">
                <VAceEditor lang="mysql"
                            :theme="theme"
                            :style="{height: '85px', width: '25vh'}"
                            :value="value"
                            :options="{readOnly: true}"/>
              </ShadcnRadio>
            </ShadcnCol>
          </ShadcnRow>
        </ShadcnRadioGroup>
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
import { UserEditor } from '@/model/user'
import { VAceEditor } from 'vue3-ace-editor'
import themes from './AceEditor'
import Common from '@/utils/common'

export default defineComponent({
  name: 'EditorForm',
  components: { VAceEditor },
  data()
  {
    return {
      loading: {
        default: false,
        submitting: false
      },
      formState: null as UserEditor | null,
      themes: themes.themes,
      value: 'SHOW TABLES\nSELECT * FROM table\nCREATE TABLE table\nDROP TABLE table\nALTER TABLE table'
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
                     const configure = response.data.editorConfigure
                     if (response.data && configure) {
                       this.formState = configure as UserEditor
                     }
                   }
                 })
                 .finally(() => this.loading.default = false)
    },
    onSubmit()
    {
      this.loading.submitting = true
      UserService.changeEditor(this.formState as UserEditor)
                 .then((response) => {
                   if (response.status) {
                     this.$Message.success({
                       content: this.$t('common.successfully') as string,
                       showIcon: true
                     })
                     localStorage.setItem(Common.userEditorConfigure, JSON.stringify(this.formState))
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
