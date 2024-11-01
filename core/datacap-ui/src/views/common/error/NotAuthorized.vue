<template>
  <div class='h-screen'>
    <ShadcnException type="403"
                     :title="$t('common.pageNotAuthorized')"
                     :description="$t('common.pageNotAuthorizedTip')">
      <template #actions>
        <ShadcnButton type="default" :to="$route.query.redirect">
          {{ $t('common.backTo') }}
        </ShadcnButton>
        <ShadcnButton type="primary" to="/">
          {{ $t('common.backToHome') }}
        </ShadcnButton>
        <ShadcnButton type="default" to="/auth/signin">
          {{ $t('common.backToSignin') }}
        </ShadcnButton>
      </template>
    </ShadcnException>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'NotAuthorized'
})
</script>
