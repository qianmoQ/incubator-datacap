<template>
  <div class="relative min-h-screen">
    <ShadcnSpin v-model="loading" fixed/>

    <div v-if="!loading">
      <DashboardEditor v-if="version === '1.0'" :info="dataInfo"/>

      <DashboardEditorV2 v-else :info="dataInfo"/>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import DashboardService from '@/services/dashboard'
import { DashboardModel } from '@/model/dashboard'
import DashboardEditor from '@/views/pages/admin/dashboard/components/DashboardEditor.vue'
import DashboardEditorV2 from '@/views/pages/admin/dashboard/components/DashboardEditorV2.vue'

export default defineComponent({
  name: 'DashboardInfo',
  components: { DashboardEditorV2, DashboardEditor },
  setup()
  {
    const route = useRoute()
    const loading = ref(false)
    const dataInfo = ref<DashboardModel | null>(null)
    const version = ref('1.0')
    const { proxy } = getCurrentInstance()!

    const handleInitialize = async () => {
      const code = route.params.code as string

      if (!code) {
        dataInfo.value = null
        return
      }

      try {
        loading.value = true
        const response = await DashboardService.getByCode(code)
        if (response.status) {
          dataInfo.value = response.data
          version.value = response.data.version
        }
        else {
          // @ts-ignore
          proxy.$Message.error({
            content: response.message,
            showIcon: true
          })
        }
      }
      catch (error) {
        console.error('Failed to fetch dashboard:', error)
        // @ts-ignore
        proxy.$Message?.error({
          content: 'Failed to load dashboard data',
          showIcon: true
        })
      }
      finally {
        loading.value = false
      }
    }

    onMounted(() => {
      handleInitialize()
    })

    return {
      loading,
      dataInfo,
      version
    }
  }
})
</script>
