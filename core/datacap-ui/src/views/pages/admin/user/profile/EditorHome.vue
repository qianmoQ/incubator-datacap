<template>
  <ShadcnCard :border="false"
              :title="$t('user.common.editor')"
              :description="$t('user.tip.editor')">
    <ShadcnDivider class="my-2"/>
    <div class="p-3">
      <EditorForm/>
    </div>
  </ShadcnCard>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import EditorForm from '@/views/pages/admin/user/profile/components/EditorForm.vue'

export default defineComponent({
  name: 'EditorHome',
  components: { EditorForm }
})
</script>
