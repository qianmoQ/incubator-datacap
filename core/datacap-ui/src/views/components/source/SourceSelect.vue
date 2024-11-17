<template>
  <div>
    <ShadcnSelect v-model="applySource"
                  v-model:options="options"
                  lazy
                  :placeholder="$t('source.tip.selectSource')"
                  :load-data="loadMoreData"
                  @on-change="onChange">
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
      options: [] as SourceModel[],
      loading: false,
      applySource: undefined,
      pageIndex: 1,
      pageTotal: 10,
      dataCount: 0
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
      SourceService.getAll(this.filter)
                   .then((response) => {
                     if (response.status) {
                       this.options = response.data.content.map((item: any) => ({ ...item, label: item.name, value: `${ item.id }:${ item.type }:${ item.code }` }))
                       this.dataCount = response.data.total
                       this.pageTotal = response.data.totalPage
                       this.pageIndex = response.data.page
                       if (this.value) {
                         this.applySource = this.value as any
                       }
                     }
                   })
                   .finally(() => this.loading = false)
    },
    async loadMoreData(callback: (children: any[]) => void)
    {
      if (this.pageIndex < this.pageTotal) {
        this.filter.page = this.pageIndex + 1
        const response = await SourceService.getAll(this.filter)

        if (response.status) {
          const options = response.data.content.map((item: any) => ({ ...item, label: item.name, value: `${ item.id }:${ item.type }:${ item.code }` }))
          this.dataCount = response.data.total
          this.pageTotal = response.data.totalPage
          this.pageIndex = response.data.page
          console.log(options)
          callback(options)
        }
      }
    },
    onChange()
    {
      this.$emit('on-change', this.applySource)
    }
  }
})
</script>
