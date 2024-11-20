# App.vue
<template>
  <div class="flex h-screen bg-gray-100">
    <!-- 左侧组件面板 -->
    <BigScreenPanel />

    <!-- 中间编辑区域 -->
    <BigScreenEditor
        ref="editorRef"
        :grid-size="20"
        :selected-id="selectedId"
        @select="handleSelect"
        @update:components="handleComponentsUpdate"
    />

    <!-- 右侧配置面板 -->
    <BigScreenConfigure
        :selected-component="selectedComponent"
        @update="handleConfigUpdate"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import BigScreenPanel from './components/BigScreenPanel.vue'
import BigScreenEditor from './components/BigScreenEditor.vue'
import BigScreenConfigure from './components/BigScreenConfigure.vue'

const editorRef = ref(null)
const components = ref([])
const selectedId = ref(null)

// 计算选中的组件
const selectedComponent = computed(() =>
    components.value.find(item => item.id === selectedId.value)
)

// 选择组件
const handleSelect = (component) => {
  selectedId.value = component.id
}

// 更新组件列表
const handleComponentsUpdate = (newComponents) => {
  components.value = newComponents
}

// 更新组件配置
const handleConfigUpdate = (updatedComponent) => {
  editorRef.value?.updateComponent(updatedComponent)
}
</script>
