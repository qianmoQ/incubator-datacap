<template>
  <ShadcnCard :border="false"
              :title="$t('user.common.modifyPassword')"
              :description="$t('user.tip.modifyPassword')">
    <ShadcnDivider class="my-2"/>
    <div class="p-3">
      <PasswordForm/>
    </div>
  </ShadcnCard>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import PasswordForm from '@/views/pages/admin/user/security/components/PasswordForm.vue'

export default defineComponent({
  name: 'PasswordHome',
  components: { PasswordForm }
})
</script>
