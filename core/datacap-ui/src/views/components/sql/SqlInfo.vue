<template>
  <ShadcnModal v-model="visible" title="Show Content" width="40%">
    <AceEditor :value="content as string" read-only/>

    <template #footer>
      <ShadcnButton type="error" @click="onCancel">
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
  components: { AceEditor },
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
      type: Boolean,
      default: () => false
    },
    content: {
      type: String as PropType<string | null>
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
