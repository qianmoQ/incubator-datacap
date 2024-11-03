<template>
  <ShadcnCard :border="false"
              :title="$t('user.common.modifyUsername')"
              :description="$t('user.tip.modifyUsername')">
    <ShadcnDivider class="my-2"/>
    <div class="p-3">
      <UsernameForm/>
    </div>
  </ShadcnCard>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import UsernameForm from '@/views/pages/admin/user/security/components/UsernameForm.vue'

export default defineComponent({
  name: 'UsernameHome',
  components: { UsernameForm }
})
</script>
