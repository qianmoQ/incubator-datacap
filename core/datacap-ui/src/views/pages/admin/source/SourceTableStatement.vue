<template>
  <div class="relative min-h-screen">
    <ShadcnSpin v-model="loading" fixed/>

    <AceEditor v-if="!loading" :value="statement as string" :read-only="true" :height="'100vh'"/>
  </div>
</template>

<script lang="ts">
import { defineComponent, watch } from 'vue'
import { SqlType, TableFilter, TableFilterRequest } from '@/model/table.ts'
import AceEditor from '@/views/components/editor/AceEditor.vue'
import TableService from '@/services/table.ts'

export default defineComponent({
  name: 'SourceTableStatement',
  components: { AceEditor },
  data()
  {
    return {
      loading: false,
      statement: null as string | null,
      formState: null as unknown as TableFilter
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
      const code = String(this.$route?.params.table)
      if (code) {
        this.formState = TableFilterRequest.of()
        this.formState.type = SqlType.SHOW
        this.loading = true
        TableService.getData(code, this.formState)
                    .then(response => {
                      if (response.status) {
                        const content = response.data?.columns[0]
                        if (content.length > 1) {
                          this.statement = content[1]
                        }
                        else {
                          this.statement = content[0]
                        }
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
