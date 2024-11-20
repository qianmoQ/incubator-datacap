<template>
  <BaseLayout>
    <div class="h-screen">
      <ShadcnException type="500"
                       :title="$t('common.pageNotNetwork')"
                       :description="$t('common.tip.pageNotNetwork')">
        <template #icon>
          <ShadcnIcon icon="TriangleAlert" class="text-gray-400" :size="80"/>
        </template>
        <template #actions>
          <ShadcnButton type="default" :to="String($route.query.redirect)">
            {{ $t('common.backTo') }}
          </ShadcnButton>
          <ShadcnButton type="primary" :to="String($route.query.redirect)">
            {{ $t('common.backToHome') }}
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
  name: 'NotNetwork',
  components: { BaseLayout }
})
</script>
