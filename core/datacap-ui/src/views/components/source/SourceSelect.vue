<template>
  <div>
    <ShadcnSelect v-model="applySource" :placeholder="$t('source.tip.selectSource')" @on-change="onChange">
      <template #options>
        <ShadcnSelectOption v-for="item in items"
                            :label="item.name"
                            :value="`${item.id}:${item.type}:${item.code}`"
                            :disabled="!item.available">
        </ShadcnSelectOption>
      </template>
    </ShadcnSelect>
  </div>
</template>

<script lang="ts">
import SourceService from '@/services/source'
import { defineComponent } from 'vue'
import { SourceModel } from '@/model/source'

import { FilterModel } from '@/model/filter.ts'

export default defineComponent({
  name: 'SourceSelect',
  props: {
    value: {
      type: Object as () => String | undefined
    }
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    return {
      filter
    }
  },
  data()
  {
    return {
      items: [] as SourceModel[],
      loading: false,
      applySource: undefined
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      SourceService.getAll(this.filter)
                   .then((response) => {
                     if (response.status) {
                       this.items = response.data.content
                       if (this.value) {
                         this.applySource = this.value as any
                       }
                     }
                   })
                   .finally(() => this.loading = false)
    },
    onChange()
    {
      this.$emit('on-change', this.applySource)
    }
  }
})
</script>
