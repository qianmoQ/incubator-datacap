<template>
  <div class="h-screen flex items-center justify-center">
    <div class="w-full max-w-md px-4 mx-auto">
      <ShadcnCard class="w-full">
        <template #title>
          <div class='flex items-center justify-center'>
            <ShadcnAvatar src="/static/images/logo.png" alt="DataCap"/>
          </div>
        </template>

        <template #description>
          <div class="text-center text-gray-600">
            {{ $t('user.auth.signinTip') }}
          </div>
        </template>

        <div class="px-6 py-8 relative">
          <ShadcnSpin v-if="loading" fixed/>
          <ShadcnForm v-else
                      v-model="formState"
                      ref="formRef"
                      @on-submit="onSubmit"
                      @on-error="onError">
            <ShadcnFormItem name="username"
                            :label="$t('user.common.username')"
                            :rules="[
                                { required: true, message: $t('user.auth.usernameTip') },
                                { min: 3, message: $t('user.auth.usernameSizeTip') },
                                { max: 20, message: $t('user.auth.usernameSizeTip') }
                          ]">
              <ShadcnInput v-model="formState.username"
                           name="username"
                           :placeholder="$t('user.auth.usernameTip')"/>
            </ShadcnFormItem>

            <ShadcnFormItem name="password"
                            :label="$t('user.common.password')"
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

            <ShadcnFormItem v-if="showCaptcha"
                            name="captcha"
                            :label="$t('user.common.captcha')"
                            :rules="[
                                { required: true, message: $t('user.auth.captchaTip') },
                                { min: 1, message: $t('user.auth.captchaSizeTip') },
                                { max: 6, message: $t('user.auth.captchaSizeTip') }
                          ]">
              <div class="flex items-center gap-2">
                <ShadcnInput v-model="formState.captcha"
                             name="captcha"
                             :placeholder="$t('user.auth.captchaTip')"/>
                <ShadcnButton style="padding: 0"
                              type="text"
                              :loading="captchaLoading"
                              :disabled="captchaLoading"
                              @click="initCaptcha">
                  <img v-if="!captchaLoading" style="min-width: 120px; height: 100%;" :src="'data:image/png;base64,' + captchaImage"/>
                </ShadcnButton>
              </div>
            </ShadcnFormItem>

            <ShadcnSpace wrap>
              <ShadcnButton class="w-full"
                            submit
                            :disabled="submitting"
                            :loading="submitting">
                {{ $t('user.common.signin') }}
              </ShadcnButton>

              <ShadcnDivider class="text-sm text-gray-400 py-2"
                             orientation="center"
                             :text="$t('user.auth.notUserTip')"/>

              <ShadcnButton class="w-full text-center" type="default" to="/auth/signup">
                {{ $t('user.common.signup') }}
              </ShadcnButton>
            </ShadcnSpace>
          </ShadcnForm>
        </div>
      </ShadcnCard>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import UserService from '@/services/user'
import CaptchaService from '@/services/captcha'

import CommonUtils from '@/utils/common'
import { HttpUtils } from '@/utils/http'

import router from '@/router'
import { createDefaultRouter } from '@/router/default'

interface Props
{
  username: string
  password: string
  timestamp: number
  captcha: number
}

export default defineComponent({
  name: 'AuthSignin',
  data()
  {
    return {
      formState: {} as Props,
      submitting: false,
      loading: false,
      showCaptcha: false,
      captchaImage: null,
      captchaLoading: false
    }
  },
  created()
  {
    this.loading = true
    this.initCaptcha()
  },
  methods: {
    initCaptcha()
    {
      this.captchaLoading = true
      this.formState.timestamp = Date.parse(new Date().toString())
      CaptchaService.getCaptcha(this.formState.timestamp)
                    .then(response => {
                      if (response.data !== false) {
                        this.showCaptcha = true
                        this.captchaImage = response.data.image
                      }
                    })
                    .finally(() => {
                      this.captchaLoading = false
                      this.loading = false
                    })
    },
    onError(errors: any)
    {
      this.$Message.error({
        content: `Validation error field: [ ${ Object.keys(errors).join(', ') } ]`,
        showIcon: true
      })
    },
    onSubmit()
    {
      this.submitting = true
      UserService.signin(this.formState as any)
                 .then(response => {
                   if (response.status) {
                     localStorage.setItem(CommonUtils.token, JSON.stringify(response.data))
                     // Get user information and user menus
                     const client = new HttpUtils().getAxios()
                     client.all([UserService.getMenus(), UserService.getInfo()])
                           .then(client.spread((fetchMenu, fetchInfo) => {
                             if (fetchMenu.status && fetchInfo.status) {
                               localStorage.setItem(CommonUtils.menu, JSON.stringify(fetchMenu.data))
                               createDefaultRouter(router)
                               localStorage.setItem(CommonUtils.userEditorConfigure, JSON.stringify(fetchInfo.data.editorConfigure))
                               router.push('/home')
                             }
                             else {
                               if (!fetchMenu.status) {
                                 this.$Message.error({
                                   content: fetchMenu.message,
                                   showIcon: true
                                 })
                               }
                               if (!fetchInfo.status) {
                                 this.$Message.error({
                                   content: fetchInfo.message,
                                   showIcon: true
                                 })
                               }
                             }
                           }))
                   }
                   else {
                     this.$Message.error({
                       content: response.message,
                       showIcon: true
                     })
                     this.initCaptcha()
                   }
                 })
                 .finally(() => this.submitting = false)
    }
  }
})
</script>
