<template>
  <div class="relative h-full w-full" :style="{ width: width, height: height }">
    <ShadcnSpin v-model="loading" fixed/>

    <div v-if="localConfiguration && !loading">
      <div v-if="localConfiguration.message" class="p-4">
        <ShadcnAlert type="error" :title="localConfiguration.message"/>
      </div>

      <div v-else-if="hasError && message" class="flex items-center justify-center absolute inset-0">
        <ShadcnAlert type="error" show-icon :title="message"/>
      </div>

      <div v-else>
        <VisualTable v-if="configuration?.type === Type.TABLE"
                     :configuration="localConfiguration as any"
                     :submitted="false"
                     :width="width"
                     :height="height"/>

        <VisualLine v-else-if="configuration?.type === Type.LINE"
                    :configuration="localConfiguration as any"
                    :submitted="false"
                    :width="width"
                    :height="height"/>

        <VisualBar v-else-if="configuration?.type === Type.BAR"
                   :configuration="localConfiguration as any"
                   :submitted="false"
                   :width="width"
                   :height="height"/>

        <VisualArea v-else-if="configuration?.type === Type.AREA"
                    :configuration="localConfiguration as any"
                    :submitted="false"
                    :width="width"
                    :height="height"/>

        <VisualPie v-else-if="configuration?.type === Type.PIE"
                   :configuration="localConfiguration as any"
                   :submitted="false"
                   :width="width"
                   :height="height"/>

        <VisualHistogram v-else-if="configuration?.type === Type.HISTOGRAM"
                         :configuration="localConfiguration as any"
                         :submitted="false"
                         :width="width"
                         :height="height"/>

        <VisualWordCloud v-else-if="configuration?.type === Type.WORDCLOUD"
                         :configuration="localConfiguration as any"
                         :submitted="false"
                         :width="width"
                         :height="height"/>

        <VisualScatter v-else-if="configuration?.type === Type.SCATTER"
                       :configuration="localConfiguration as any"
                       :submitted="false"
                       :width="width"
                       :height="height"/>

        <VisualRadar v-else-if="configuration?.type === Type.RADAR"
                     :configuration="localConfiguration as any"
                     :submitted="false"
                     :width="width"
                     :height="height"/>

        <VisualFunnel v-else-if="configuration?.type === Type.FUNNEL"
                      :configuration="localConfiguration as any"
                      :submitted="false"
                      :width="width"
                      :height="height"/>

        <VisualGauge v-else-if="configuration?.type === Type.GAUGE"
                     :configuration="localConfiguration as any"
                     :submitted="false"
                     :width="width"
                     :height="height"/>

        <VisualRose v-else-if="configuration?.type === Type.ROSE"
                    :configuration="localConfiguration as any"
                    :submitted="false"
                    :width="width"
                    :height="height"/>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { cloneDeep } from 'lodash'
import { Type } from '@/views/components/visual/Type'
import VisualWordCloud from '@/views/components/visual/components/VisualWordCloud.vue'
import VisualHistogram from '@/views/components/visual/components/VisualHistogram.vue'
import VisualPie from '@/views/components/visual/components/VisualPie.vue'
import VisualArea from '@/views/components/visual/components/VisualArea.vue'
import { Configuration } from './Configuration'
import VisualBar from '@/views/components/visual/components/VisualBar.vue'
import VisualLine from '@/views/components/visual/components/VisualLine.vue'
import VisualTable from '@/views/components/visual/components/VisualTable.vue'
import DatasetService from '@/services/dataset'
import VisualRadar from '@/views/components/visual/components/VisualRadar.vue'
import VisualScatter from '@/views/components/visual/components/VisualScatter.vue'
import VisualFunnel from '@/views/components/visual/components/VisualFunnel.vue'
import VisualGauge from '@/views/components/visual/components/VisualGauge.vue'
import VisualRose from '@/views/components/visual/components/VisualRose.vue'
import ExecuteService from '@/services/execute.ts'
import { ExecuteModel } from '@/model/execute.ts'

export default defineComponent({
  name: 'VisualView',
  computed: {
    Type()
    {
      return Type
    }
  },
  components: {
    VisualRose, VisualGauge, VisualFunnel, VisualScatter, VisualRadar,
    VisualWordCloud, VisualHistogram, VisualPie, VisualArea, VisualBar, VisualLine, VisualTable
  },
  props: {
    configuration: {
      type: Object as () => Configuration | null
    },
    query: {
      type: Object
    },
    code: {
      type: String
    },
    type: {
      type: String
    },
    width: {
      type: String,
      default: () => '100%'
    },
    height: {
      type: String,
      default: () => '400px'
    },
    original: {
      type: String
    }
  },
  data()
  {
    return {
      loading: false,
      hasError: false,
      message: null as string | null,
      localConfiguration: null as Configuration | null
    }
  },
  created()
  {
    this.handleInitialize()
  },
  methods: {
    handleInitialize()
    {
      this.localConfiguration = cloneDeep(this.configuration) as Configuration
      setTimeout(() => {
        this.loading = true
        if (this.type === 'QUERY') {
          const configure: ExecuteModel = { name: this.original as any, content: this.query as any, mode: 'REPORT', format: 'JsonConvert' }
          ExecuteService.execute(configure, null)
                        .then(response => {
                          if (response.status && response.data.isSuccessful) {
                            this.formatRaw(response)
                            this.message = null
                          }
                          else {
                            this.hasError = response.message
                            this.message = response.message
                          }
                        })
                        .finally(() => this.loading = false)
        }
        else {
          DatasetService.adhoc(this.code!, this.query)
                        .then(response => {
                          if (response.status) {
                            this.formatRaw(response)
                          }
                          else {
                            this.$Message.error({
                              content: response.message,
                              showIcon: true
                            })
                          }
                        })
                        .finally(() => this.loading = false)
        }
      })
    },
    formatRaw(response: any)
    {
      if (this.localConfiguration) {
        if (response.data.isSuccessful) {
          this.localConfiguration.headers = response.data.headers
          this.localConfiguration.columns = response.data.columns
          this.localConfiguration.message = null
        }
        else {
          this.localConfiguration.headers = []
          this.localConfiguration.columns = []
          this.localConfiguration.message = response.data.message
        }
      }
    }
  }
})
</script>
