<template>
  <ShadcnDrawer v-model="visible" width="20%" :title="$t('source.common.visibleColumn')">
    <ShadcnCheckboxGroup v-model="value">
      <ShadcnCheckbox v-for="item in columns" :value="item.field">{{ item.field }}</ShadcnCheckbox>
    </ShadcnCheckboxGroup>

    <template #footer>
      <ShadcnButton @click="onCancel">
        {{ $t('common.apply') }}
      </ShadcnButton>
    </template>
  </ShadcnDrawer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'TableColumn',
  props: {
    isVisible: {
      type: Boolean
    },
    columns: {
      type: Array<any>,
      default: () => [] as any[]
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
  data()
  {
    return {
      value: [] as string[]
    }
  },
  created()
  {
    this.value = this.columns.map(item => item.field)
  },
  methods: {
    onCancel()
    {
      this.$emit('change', this.value)
      this.visible = false
    }
  }
})
</script>
