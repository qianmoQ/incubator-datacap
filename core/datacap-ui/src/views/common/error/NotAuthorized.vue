<template>
  <BaseLayout>
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
  </BaseLayout>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import BaseLayout from '@/views/layouts/base/BaseLayout.vue'

export default defineComponent({
  name: 'NotAuthorized',
  components: { BaseLayout }
})
</script>
