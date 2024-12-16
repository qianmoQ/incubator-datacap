<template>
  <div class="w-full h-screen min-h-screen" style="height: 100%;">
    <ShadcnCard>
      <template #title>
        <div class="ml-2">{{ $t('pipeline.common.create') }}</div>
      </template>

      <template #extra>
        <div class="flex items-center space-x-4">
          <div class="flex items-center space-x-2">
            <ShadcnText type="small">{{ $t('common.executor') }}</ShadcnText>

            <ShadcnSelect v-model="formState.executor">
              <template #options>
                <ShadcnSelectOption v-for="executor in installedExecutors"
                                    :key="executor.name"
                                    :label="executor.name"
                                    :value="executor.name"/>
              </template>
            </ShadcnSelect>
          </div>

          <ShadcnButton :disabled="!(workflowState?.validation?.length === 0)" @click="visible = true">
            {{ $t('common.publish') }}
          </ShadcnButton>
        </div>
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

  <ShadcnModal v-model="visible">
    <template #title>{{ $t('common.executor') }}</template>
    <div class="flex items-center justify-center h-32">Content</div>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ConfigurationService from '@/services/configure'
import PipelineService from '@/services/pipeline'
import PluginService from '@/services/plugin'
import router from '@/router'
import HttpUtils from '@/utils/http.ts'

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
      visible: false,
      configuration: null as Configuration | null,
      workflowState: null as any | null,
      installedExecutors: [] as any[],
      formState: {
        executor: 'LocalExecutor'
      }
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
      HttpUtils.all([ConfigurationService.getExecutor(), PluginService.getPlugins()])
               .then(HttpUtils.spread((executor, plugin) => {
                 if (executor.status && executor.data) {
                   this.configuration = executor.data
                 }
                 if (plugin.status && plugin.data) {
                   this.installedExecutors = plugin.data.filter((v: { type: string }) => v.type === 'EXECUTOR')
                 }
               }))
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
