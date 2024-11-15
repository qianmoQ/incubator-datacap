<template>
  <div v-if="data" class="space-y-2">
    <ShadcnCard>
      <template #title>
        <span class="text-gray-500 text-sm">{{ $t('pipeline.common.input') }}</span>
      </template>

      <div v-for="node in inputs" :key="node.id" class="dndflow p-1">
        <div class="nodes">
          <div :class="'flex items-center space-x-1 vue-flow__node-' + node.nodeType" :draggable="true" @dragstart="onDragStart($event, node.nodeType, node)">
            <ShadcnAvatar size="small" :src="'/static/images/plugin/' + (node.type as string).split(' ')[0] + '.png'"/>
            <span>{{ node.name }}</span>
          </div>
        </div>
      </div>
    </ShadcnCard>

    <ShadcnCard>
      <template #title>
        <span class="text-gray-500 text-sm">{{ $t('pipeline.common.output') }}</span>
      </template>

      <div v-for="node in outputs" :key="node.id" class="dndflow p-1">
        <div class="nodes">
          <div :class="'flex items-center space-x-1 vue-flow__node-' + node.nodeType" :draggable="true" @dragstart="onDragStart($event, node.nodeType, node)">
            <ShadcnAvatar size="small" :src="'/static/images/plugin/' + (node.type as string).split(' ')[0] + '.png'"/>
            <span>{{ node.name }}</span>
          </div>
        </div>
      </div>
    </ShadcnCard>
  </div>
</template>

<script lang="ts">
import '../style.css'
import { defineComponent, onBeforeMount, ref } from 'vue'
import { Configuration } from '@/views/components/editor/flow/Configuration.ts'

export default defineComponent({
  name: 'FlowSider',
  props: {
    data: {
      type: Array as () => Configuration[]
    }
  },
  setup(props)
  {
    const inputs = ref(Array<Configuration>())
    const outputs = ref(Array<Configuration>())

    onBeforeMount(() => {
      if (props.data) {
        inputs.value = props.data.filter(v => v.nodeType === 'input')
        outputs.value = props.data.filter(v => v.nodeType === 'output')
      }
    })

    /**
     * Sets the data type and value to be transferred during drag start event.
     *
     * @param {object} event - The drag start event object.
     * @param {any} nodeType - The type of node being dragged.
     */
    const onDragStart = (event: any, nodeType: any, configure: any) => {
      if (event.dataTransfer) {
        const data = { type: nodeType, configure: configure }
        event.dataTransfer.setData('application/vueflow', JSON.stringify(data))
        event.dataTransfer.effectAllowed = 'move'
      }
    }

    return {
      inputs,
      outputs,
      onDragStart
    }
  }
})
</script>
