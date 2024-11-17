<template>
  <div class="relative">
    <ShadcnSpin v-model="loading" fixed/>

    <DashboardEditor v-if="dataInfo" :info="dataInfo"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import DashboardService from '@/services/dashboard'
import { useRouter } from 'vue-router'
import { DashboardModel } from '@/model/dashboard'
import DashboardEditor from '@/views/pages/admin/dashboard/components/DashboardEditor.vue'

export default defineComponent({
  name: 'DashboardInfo',
  components: { DashboardEditor },
  data()
  {
    return {
      loading: false,
      saving: false,
      dataInfo: null as DashboardModel | null
    }
  },
  created()
  {
    this.handleInitialize()
  },
  methods: {
    handleInitialize()
    {
      const router = useRouter()
      const params = router.currentRoute.value.params
      const code = String(params['code'])
      if (code) {
        this.loading = true
        DashboardService.getByCode(code)
                        .then(response => {
                          if (response.status) {
                            this.dataInfo = response.data
                          }
                          else {
                            this.$Message.error({
                              content: response.message,
                              showIcon: true
                            })
                          }
                        })
                        .finally(() => this.loading = false)
      }
    }
  }
})
</script>
