<template>
  <ShadcnBreadcrumb>
    <ShadcnBreadcrumbItem>
      <ShadcnLink link="/">{{ $t('common.home') }}</ShadcnLink>
    </ShadcnBreadcrumbItem>
    <ShadcnBreadcrumbItem v-if="breadcrumbs.length > 0"
                          v-for="item in breadcrumbs">
      <ShadcnLink :link="item.path">
        {{ $t(`${ item.meta.title }`) }}
      </ShadcnLink>
    </ShadcnBreadcrumbItem>
  </ShadcnBreadcrumb>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

interface BreadcrumbModel
{
  path: string
  meta: {
    title: string
    isRoot: boolean
  }
}

export default defineComponent({
  name: 'LayoutBreadcrumb',
  watch: {
    $route()
    {
      this.getBreadcrumb()
    }
  },
  data()
  {
    return {
      breadcrumbs: [] as BreadcrumbModel[]
    }
  },
  created()
  {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb()
    {
      this.breadcrumbs = []
      this.$route['matched']
          .filter(route => route.meta.title)
          .forEach(item => {
            const breadcrumb: BreadcrumbModel = {
              path: item.path,
              meta: {
                title: item.meta.title as string,
                isRoot: item.meta.isRoot as boolean
              }
            }
            this.breadcrumbs?.push(breadcrumb)
          })
    }
  }
})
</script>
