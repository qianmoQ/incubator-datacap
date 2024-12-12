<template>
  <div class="w-full h-screen min-h-screen" style="height: 100%;">
    <ShadcnCard>
      <template #title>
        <div class="ml-2">{{ $t('pipeline.common.create') }}</div>
      </template>

      <template #extra>
        <ShadcnTooltip :content="$t('common.executor')" @click="visibleExecutor = true">
          <ShadcnButton circle size="small">
            <ShadcnIcon icon="Cog" size="15"/>
          </ShadcnButton>
        </ShadcnTooltip>
      </template>

      <div class="relative">
        <ShadcnSpin v-model="loading"/>

        <ShadcnWorkflowEditor v-if="configuration && !loading"
                              v-model="workflowState"
                              :categories="configuration.categories"
                              :nodes="configuration.nodes"
                              :connections="[]"
                              :configureWidth="380"/>
      </div>
    </ShadcnCard>
  </div>

  <ShadcnModal v-model="visibleExecutor">
    <template #title>{{ $t('common.executor') }}</template>
    <div class="flex items-center justify-center h-32">Content</div>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ConfigurationService from '@/services/configure'
import PipelineService from '@/services/pipeline'
import router from '@/router'

export interface Configuration
{
  categories: any[]
  nodes: any[]
}

export default defineComponent({
  name: 'PipelineInfo',
  data()
  {
    return {
      loading: false,
      visibleExecutor: false,
      configuration: null as Configuration | null
    }
  },
  created()
  {
    this.handleInitialize()
  },
  methods: {
    handleInitialize()
    {
      this.loading = true
      ConfigurationService.getExecutor()
                          .then((response) => {
                            if (response.status && response.data) {
                              this.configuration = response.data
                            }
                          })
                          .finally(() => this.loading = false)
    },
    onSubmit(value: any)
    {
      PipelineService.submit(value)
                     .then((response) => {
                       if (response.status) {
                         this.$Message.success({
                           content: `${ this.$t('pipeline.tip.publishSuccess').replace('$VALUE', response.data) }`,
                           showIcon: true
                         })
                         router.push('/admin/pipeline')
                       }
                       else {
                         this.$Message.error({
                           content: response.message,
                           showIcon: true
                         })
                       }
                     })
    }
  }
})
</script>
