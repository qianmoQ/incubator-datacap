<template>
  <ShadcnCard :title="$t('user.common.assistant')"
              :description="$t('user.tip.assistant')"
              :border="false">
    <ShadcnDivider class="my-2"/>
    <div class="p-3">
      <AssistantForm/>
    </div>
  </ShadcnCard>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AssistantForm from '@/views/pages/admin/user/profile/components/AssistantForm.vue'

export default defineComponent({
  name: 'AssistantHome',
  components: { AssistantForm }
})
</script>
