<template>
  <ShadcnModal v-model="visible"
               height="410"
               width="40%"
               :title="$t('source.common.previewDML')"
               @on-close="onCancel">
    <div class="relative h-full">
      <ShadcnSpin v-model="loading" fixed/>

      <AceEditor v-if="!loading && contentDML" :value="contentDML" :read-only="true"/>
    </div>

    <template #footer>
      <ShadcnSpace>
        <ShadcnButton type="default" @click="onCancel">
          {{ $t('common.cancel') }}
        </ShadcnButton>

        <ShadcnButton type="primary" :disabled="submitting" :loading="submitting" @click="onSubmit">
          {{ $t('common.submit') }}
        </ShadcnButton>
      </ShadcnSpace>
    </template>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AceEditor from '@/views/components/editor/AceEditor.vue'
import TableService from '@/services/table'
import { SqlColumn, SqlType, TableFilter, TableFilterRequest } from '@/model/table'

export default defineComponent({
  name: 'TableRowDelete',
  components: { AceEditor },
  props: {
    isVisible: {
      type: Boolean
    },
    columns: {
      type: Array<SqlColumn>
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
      loading: false,
      submitting: false,
      contentDML: null as string | null,
      configure: null as unknown as TableFilter,
      code: null as string | null
    }
  },
  created()
  {
    const code = String(this.$route?.params.table)
    if (code) {
      this.code = code
    }
    this.handleInitialize()
  },
  methods: {
    handleInitialize()
    {
      this.configure = TableFilterRequest.of()
      this.loading = true
      const originalColumns = Array<SqlColumn>()
      this.columns?.forEach((item: any) => originalColumns.push({ original: item }))
      this.configure.columns = originalColumns
      this.configure.type = SqlType.DELETE
      this.configure.preview = true
      TableService.putData(String(this.code), this.configure)
                  .then(response => {
                    if (response.status && response.data && response.data.isSuccessful) {
                      this.contentDML = response.data.content
                    }
                    else {
                      this.$Message.error({
                        content: response.message,
                        showIcon: true
                      })
                    }
                  })
                  .finally(() => this.loading = false)
    },
    onSubmit()
    {
      this.submitting = true
      this.configure.preview = false
      TableService.putData(this.code as string, this.configure)
                  .then(response => {
                    if (response.status && response.data && response.data.isSuccessful) {
                      this.$Message.success({
                        content: this.$t('source.tip.deleteSuccess'),
                        showIcon: true
                      })

                      this.onCancel()
                    }
                    else {
                      this.$Message.error({
                        content: response.data.message,
                        showIcon: true
                      })
                    }
                  })
                  .finally(() => this.submitting = false)
    },
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>
