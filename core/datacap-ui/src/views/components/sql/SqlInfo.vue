<template>
  <ShadcnModal v-model="visible" title="Show Content">
    <template #content>
      <AceEditor :value="content as string" read-only/>
    </template>

    <template #footer>
      <ShadcnButton type="error" @click="handlerCancel">
        {{ $t('common.cancel') }}
      </ShadcnButton>
    </template>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import AceEditor from '@/views/components/editor/AceEditor.vue'

export default defineComponent({
  name: 'SqlInfo',
  components: {
    AceEditor
  },
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    content: {
      type: String as PropType<string | null>
    }
  },
  methods: {
    handlerCancel()
    {
      this.visible = false
    }
  },
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
  }
})
</script>
