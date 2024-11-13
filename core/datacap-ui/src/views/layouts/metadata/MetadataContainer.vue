<template>
  <ShadcnLayout>
    <LayoutHeader/>
    <div class="container my-2 min-h-screen">
      <ShadcnLayoutWrapper>
        <ShadcnLayoutSider class="mr-4">
          <MetadataSidebar/>
        </ShadcnLayoutSider>
        <ShadcnLayoutMain class="ml-2">
          <ShadcnLayoutContent>
            <MetadataContent/>
          </ShadcnLayoutContent>
        </ShadcnLayoutMain>
      </ShadcnLayoutWrapper>
    </div>
    <LayoutFooter/>
  </ShadcnLayout>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import MetadataSidebar from '@/views/layouts/metadata/components/MetadataSidebar.vue'
import MetadataContent from '@/views/layouts/metadata/components/MetadataContent.vue'

export default defineComponent({
  name: 'MetadataContainer',
  components: { MetadataContent, MetadataSidebar }
})
</script>
