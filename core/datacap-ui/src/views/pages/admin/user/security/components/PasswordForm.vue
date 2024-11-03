<template>
  <div class="relative">
    <ShadcnForm v-model="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="oldPassword"
                      class="w-[40%]"
                      :description="$t('user.tip.oldPassword')"
                      :label="$t('user.common.oldPassword')"
                      :rules="[
                            { required: true, message: $t('user.auth.passwordTip') },
                            { min: 6, message: $t('user.auth.passwordSizeTip') },
                            { max: 20, message: $t('user.auth.passwordSizeTip') }
                      ]">
        <ShadcnInput v-model="formState.oldPassword"
                     type="password"
                     name="oldPassword"
                     :placeholder="$t('user.auth.passwordTip')"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="newPassword"
                      class="w-[40%]"
                      :description="$t('user.tip.newPassword')"
                      :label="$t('user.common.newPassword')"
                      :rules="[
                            { required: true, message: $t('user.auth.newPasswordTip') },
                            { min: 6, message: $t('user.auth.passwordSizeTip') },
                            { max: 20, message: $t('user.auth.passwordSizeTip') }
                      ]">
        <ShadcnInput v-model="formState.newPassword"
                     type="password"
                     name="newPassword"
                     :placeholder="$t('user.auth.passwordTip')"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="confirmPassword"
                      class="w-[40%]"
                      :description="$t('user.tip.confirmPassword')"
                      :label="$t('user.common.confirmPassword')"
                      :rules="[
                            { required: true, message: $t('user.auth.confirmPasswordTip') },
                            { validator: validatePassword }
                      ]">
        <ShadcnInput v-model="formState.confirmPassword"
                     type="password"
                     name="confirmPassword"
                     :placeholder="$t('user.auth.passwordTip')"/>
      </ShadcnFormItem>

      <ShadcnButton submit :loading="loading" :disabled="loading">
        {{ $t('common.save') }}
      </ShadcnButton>
    </ShadcnForm>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import UserService from '@/services/user'
import { UserPasswordModel } from '@/model/user'
import Common from '@/utils/common'
import router from '@/router'

export default defineComponent({
  name: 'PasswordForm',
  data()
  {
    return {
      loading: false,
      formState: { oldPassword: null, newPassword: null, confirmPassword: null }
    }
  },
  methods: {
    validatePassword(value: string)
    {
      if (value !== this.formState.newPassword) {
        return Promise.reject(new Error(this.$t('user.auth.passwordNotMatchTip')))
      }
      return Promise.resolve(true)
    },
    async onSubmit()
    {
      this.loading = true
      UserService.changePassword(this.formState as UserPasswordModel)
                 .then((response) => {
                   if (response.status) {
                     this.$Message.success({
                       content: this.$t('user.tip.changePasswordSuccessfully') as string,
                       showIcon: true
                     })
                     localStorage.removeItem(Common.token)
                     localStorage.removeItem(Common.menu)
                     router.push('/auth/signin')
                   }
                   else {
                     this.$Message.error({
                       content: response.message,
                       showIcon: true
                     })
                   }
                 })
                 .finally(() => this.loading = false)
    }
  }
})
</script>
