<template>
  <div class="h-screen">
    <ShadcnException type="404"
                     :title="$t('common.pageNotFound')"
                     :description="$t('common.pageNotFoundTip')">
      <template #actions>
        <ShadcnButton v-if="$route.redirectedFrom" type="default"
                      :to="$route.redirectedFrom.fullPath">
          {{ $t('common.backTo') }}
        </ShadcnButton>
        <ShadcnButton type="primary" to="/">
          {{ $t('common.backToHome') }}
        </ShadcnButton>
      </template>
    </ShadcnException>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'NotFound'
})
</script>
