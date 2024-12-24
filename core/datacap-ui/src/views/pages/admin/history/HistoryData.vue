<template>
  <ShadcnModal v-model="visible" :title="title">
    <template #content>
      <div class="relative max-h-[80%]"
           :class="configuration ? '' : 'h-24'">
        <ShadcnSpin v-if="loading" fixed/>
        <VisualTable v-else-if="configuration" :configuration="configuration as any" :submitted="false"/>
      </div>
    </template>

    <template #footer>
      <ShadcnButton type="error" @click="handlerCancel">
        {{ $t('common.cancel') }}
      </ShadcnButton>
    </template>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { HistoryModel } from '@/model/history'
import AuditService from '@/services/audit'
import { Configuration, ConfigurationRequest } from '@/views/components/visual/Configuration'
import VisualTable from '@/views/components/visual/components/VisualTable.vue'

export default defineComponent({
  name: 'HistoryData',
  components: { VisualTable },
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
      type: Boolean
    },
    info: {
      type: Object as () => HistoryModel | null
    }
  },
  data()
  {
    return {
      title: null as string | null,
      loading: false,
      configuration: null as Configuration | null
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      if (this.info) {
        this.title = `${ this.$t('query.common.historyDataInfo').replace('$VALUE', this.info.id as unknown as string) }`
        this.loading = true
        AuditService.getData(this.info.code as string)
                    .then(response => {
                      if (response.status && response.data?.isSuccessful) {
                        this.configuration = ConfigurationRequest.of(response)
                      }
                      else {
                        this.$Message.error({
                          content: response.data.message,
                          showIcon: true
                        })
                      }
                    })
                    .finally(() => this.loading = false)
      }
    },
    handlerCancel()
    {
      this.visible = false
    }
  }
})
</script>
