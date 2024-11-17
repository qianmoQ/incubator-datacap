<template>
  <div class="relative min-h-screen">
    <ShadcnSpin v-if="loading" fixed/>

    <DashboardView v-if="data && !loading" :layouts="JSON.parse(String(data.configure))"/>

    <ShadcnAlert v-else-if="!loading" type="error" :title="$t('dashboard.tip.notFound').replace('$VALUE', String($router.currentRoute?.value?.params['code']))"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import DashboardService from '@/services/dashboard'
import { useRouter } from 'vue-router'
import DashboardView from '@/views/pages/admin/dashboard/components/DashboardView.vue'
import { DashboardModel } from '@/model/dashboard'

export default defineComponent({
  name: 'DashboardPreview',
  components: { DashboardView },
  data()
  {
    return {
      loading: true,
      data: null as DashboardModel | null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      const router = useRouter()
      const params = router.currentRoute.value.params
      DashboardService.getByCode(params['code'] as string)
                      .then(response => {
                        if (response.status) {
                          this.data = response.data
                        }
                      })
                      .finally(() => this.loading = false)
    }
  }
})
</script>
