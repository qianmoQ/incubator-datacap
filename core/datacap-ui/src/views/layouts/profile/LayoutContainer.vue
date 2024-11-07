<template>
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
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import LayoutHeader from '@/views/layouts/common/components/LayoutHeader.vue'
import LayoutSidebar from '@/views/layouts/profile/components/LayoutSidebar.vue'
import LayoutFooter from '@/views/layouts/common/components/LayoutFooter.vue'

export default defineComponent({
  name: 'ProfileContainer',
  components: {
    LayoutSidebar, LayoutHeader, LayoutFooter
  }
})
</script>
