<template>
  <ShadcnCard class="w-full h-screen">
    <template #title>
      <div class="ml-2">{{ $t('pipeline.common.create') }}</div>
    </template>

    <div class="relative">
      <ShadcnSpin v-model="loading"/>

      <FlowEditor v-if="contextData && !loading" :data="contextData" @onCommit="onSubmit"/>
    </div>
  </ShadcnCard>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import FlowEditor from '@/views/components/editor/flow/FlowEditor.vue'
import SourceService from '@/services/source'
import PipelineService from '@/services/pipeline'
import { Configuration } from '@/views/components/editor/flow/Configuration.ts'
import router from '@/router'
import { FilterModel } from '@/model/filter.ts'

export default defineComponent({
  name: 'PipelineInfo',
  components: { FlowEditor },
  data()
  {
    return {
      loading: false,
      contextData: [] as Configuration[]
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
      this.contextData = []
      const filter = new FilterModel()
      SourceService.getAll(filter)
                   .then((response) => {
                     if (response.status && response.data) {
                       response.data.content.filter((item: any) => item.pipelines)
                               .flatMap((item: any) => item.pipelines.map((pipeline: any) => ({
                                 code: item.code,
                                 name: item.name,
                                 type: item.type,
                                 nodeType: pipeline.type === 'INPUT' ? 'input' : 'output',
                                 configure: pipeline.fields,
                                 protocol: item.protocol
                               })))
                               .forEach((configuration: Configuration) => this.contextData.push(configuration))
                       console.log(response.data)
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
