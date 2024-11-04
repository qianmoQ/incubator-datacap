<template>
  <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit" @on-error="onError">
    <ShadcnFormItem name="username"
                    :label="$t('user.common.username')"
                    :rules="[
                          { required: true, message: $t('user.auth.usernameTip') },
                          { min: 3, message: $t('user.auth.usernameSizeTip') },
                          { max: 20, message: $t('user.auth.usernameSizeTip') }
                    ]">
      <ShadcnInput v-model="formState.username" name="username"/>
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

    <ShadcnFormItem name="confirmPassword"
                    :label="$t('user.common.confirmPassword')"
                    :rules="[
                                { required: true, message: $t('user.auth.passwordTip') },
                                { validator: validatePassword }
                            ]">
      <ShadcnInput v-model="formState.confirmPassword"
                   type="password"
                   name="confirmPassword"
                   :placeholder="$t('user.auth.confirmPasswordTip')"/>
    </ShadcnFormItem>

    <div class="flex justify-end">
      <ShadcnButton submit :loading="loading" :disabled="loading">
        {{ $t('common.save') }}
      </ShadcnButton>
    </div>
  </ShadcnForm>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { UserModel } from '@/model/user'
import UserService from '@/services/user'

export default defineComponent({
  name: 'UserForm',
  props: {
    info: {
      type: Object as () => UserModel
    }
  },
  data()
  {
    return {
      loading: false,
      formState: ref<UserModel>({
        id: undefined,
        username: undefined,
        password: undefined,
        confirmPassword: undefined
      })
    }
  },
  created()
  {
    if (this.info) {
      this.formState.id = this.info.id
      this.formState.username = this.info.username
    }
  },
  methods: {
    validatePassword(value: string)
    {
      if (value !== this.formState.password) {
        return Promise.reject(new Error(this.$t('user.auth.passwordNotMatchTip')))
      }
      return Promise.resolve(true)
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
      this.loading = true
      UserService.saveOrUpdate(this.formState)
                 .then((response) => {
                   if (response.status) {
                     this.$emit('close', true)
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
