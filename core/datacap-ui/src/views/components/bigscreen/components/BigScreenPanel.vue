<template>
  <div class="w-64 bg-white border-r border-gray-200 p-4">
    <div class="text-lg font-medium mb-4">组件列表</div>
    <div class="space-y-2">
      <div
          v-for="item in componentList"
          :key="item.type"
          class="p-3 bg-gray-50 border border-gray-200 rounded cursor-move text-center hover:bg-gray-100 transition-colors"
          draggable="true"
          @dragstart="handleDragStart($event, item)"
      >
        {{ item.label }}
      </div>
    </div>
  </div>
</template>

<script setup>
const componentList = [
  {type: 'text', label: '文本'},
  {type: 'image', label: '图片'},
  {type: 'chart', label: '图表'},
]

const handleDragStart = (e, component) => {
  e.dataTransfer.setData('componentType', component.type)
  e.dataTransfer.setData('componentLabel', component.label)
}
</script>
