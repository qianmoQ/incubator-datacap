<template>
  <ShadcnModal v-model="visible" :title="$t('common.content')" @on-close="onCancel">
    <MdPreview v-if="content" :modelValue="content" style="padding: 0"/>

    <template #footer>
      <ShadcnButton type="default" @click="onCancel">
        {{ $t('common.cancel') }}
      </ShadcnButton>
    </template>
  </ShadcnModal>
</template>
<script lang="ts">
import { defineComponent } from 'vue'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

export default defineComponent({
  name: 'MarkdownPreview',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    content: {
      type: String,
      default: () => ''
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
  },
  components: { MdPreview },
  methods: {
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>
