<template>
  <div class="relative h-screen">
    <ShadcnSpin v-model="loading" fixed/>

    <ErDiagram v-if="!loading" :options="options"/>
  </div>
</template>
<script lang="ts">
import { defineComponent, watch } from 'vue'
import ColumnService from '@/services/column.ts'

import ErDiagram from '@/views/components/diagram/ErDiagram.vue'
import { ErDiagramOptions } from '@/views/components/diagram/ErDiagramOptions.ts'

export default defineComponent({
  name: 'SourceTableErDiagram',
  components: { ErDiagram },
  data()
  {
    return {
      loading: false,
      options: null as unknown as ErDiagramOptions
    }
  },
  created()
  {
    this.handleInitialize()
    this.watchChange()
  },
  methods: {
    handleInitialize()
    {
      const code = this.$route?.params.table as string
      if (code) {
        this.loading = true
        ColumnService.getAllByTable(code)
                     .then(response => {
                       if (response.status && response.data?.length > 0) {
                         const table = response.data[0]
                         this.options = new ErDiagramOptions()
                         this.options.table = { id: table.id, name: table.name }
                         this.options.columns = response.data
                       }
                     })
                     .finally(() => this.loading = false)
      }
    },
    watchChange()
    {
      watch(
          () => this.$route?.params.table,
          () => this.handleInitialize()
      )
    }
  }
})
</script>
