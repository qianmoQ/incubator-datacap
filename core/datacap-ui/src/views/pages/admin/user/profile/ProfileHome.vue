<template>
  <ShadcnCard :border="false"
              :title="$t('user.common.profile')"
              :description="$t('user.tip.profile')">
    <ShadcnDivider class="my-2"/>
    <div class="p-3">
      <ProfileForm/>
    </div>
  </ShadcnCard>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ProfileForm from '@/views/pages/admin/user/profile/components/ProfileForm.vue'

export default defineComponent({
  name: 'ProfileHome',
  components: { ProfileForm }
})
</script>
