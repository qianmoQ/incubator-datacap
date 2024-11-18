<template>
  <BaseLayout>
    <ShadcnLayout>
      <LayoutHeader/>
      <div class="container my-2 min-h-screen">
        <ShadcnLayoutWrapper>
          <ShadcnLayoutSider class="mr-4">
            <LayoutSidebar/>
          </ShadcnLayoutSider>
          <ShadcnLayoutMain class="ml-2">
            <ShadcnLayoutContent>
              <RouterView/>
            </ShadcnLayoutContent>
          </ShadcnLayoutMain>
        </ShadcnLayoutWrapper>
      </div>
      <LayoutFooter/>
    </ShadcnLayout>
  </BaseLayout>
</template>

<script setup lang="ts">
import LayoutHeader from '@/views/layouts/common/components/LayoutHeader.vue'
import LayoutSidebar from '@/views/layouts/profile/components/LayoutSidebar.vue'
import LayoutFooter from '@/views/layouts/common/components/LayoutFooter.vue'
import BaseLayout from '@/views/layouts/base/BaseLayout.vue'
</script>
