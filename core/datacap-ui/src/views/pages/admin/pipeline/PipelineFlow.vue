<template>
  <ShadcnModal v-model="visible"
               width="60%"
               :title="title"
               @on-close="onCancel">
    <FlowView v-if="configure" :info="configure"/>

    <template #footer>
      <ShadcnButton type="default" @click="onCancel">
        {{ $t('common.cancel') }}
      </ShadcnButton>
    </template>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { PipelineModel } from '@/model/pipeline.ts'
import FlowView from '@/views/components/editor/flow/FlowView.vue'
import { Configuration, ConfigurationRequest } from '@/views/components/editor/flow/Configuration.ts'

export default defineComponent({
  name: 'PipelineFlow',
  components: { FlowView },
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible
      },
      set(value: boolean)
      {
        this.$emit('close', value)
      }
    }
  },
  props: {
    isVisible: {
      type: Boolean
    },
    info: {
      type: Object as () => PipelineModel | null
    }
  },
  data()
  {
    return {
      loading: false,
      title: null as string | null,
      configure: null as Configuration | null
    }
  },
  created()
  {
    if (this.info) {
      this.title = this.$t('pipeline.common.flowInfo').replace('$VALUE', String(this.info.name))

      if (!this.configure) {
        this.configure = ConfigurationRequest.of()
      }
      const flowConfigure = JSON.parse(this.info.flowConfigure)
      flowConfigure.nodes.forEach((node: any) => {
        this.configure?.elements?.push({ id: node.id, type: node.type, label: node.label, position: node.position })
      })
      flowConfigure.edges.forEach((edge: any) => {
        this.configure?.elements?.push({ id: edge.id, source: edge.source, target: edge.target })
      })
      this.configure.transform = flowConfigure?.viewport
      this.configure.fitViewOnInit = true
    }
  },
  methods: {
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>
