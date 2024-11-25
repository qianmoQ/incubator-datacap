<template>
  <ShadcnRow gutter="8">
    <ShadcnCol span="2">
      <ShadcnSpace wrap>
        <ShadcnCard class="w-full h-full">
          <template #title>
            <span class="text-sm font-semibold">{{ $t('dataset.common.columnModeMetric') }}</span>
          </template>

          <div class="relative p-1" style="min-height: 200px;">
            <ShadcnSpin v-model="initialize" fixed/>

            <div v-if="!initialize">
              <Draggable item-key="id"
                         :group="{ name: 'metrics', pull: 'clone', put: false }"
                         :list="originalMetrics"
                         :clone="onClone"
                         @start="visibleHighlight(true, ColumnType.METRIC)"
                         @end="visibleHighlight(false, ColumnType.METRIC)">
                <template #item="{ element }">
                  <ShadcnTag class="my-1 mx-0.5 cursor-pointer" :text="element.aliasName ? element.aliasName : element.name" @dblclick="onClone(element)"/>
                </template>
              </Draggable>
            </div>
          </div>
        </ShadcnCard>

        <ShadcnCard class="w-full">
          <template #title>
            <span class="text-sm font-semibold">{{ $t('dataset.common.columnModeDimension') }}</span>
          </template>

          <div class="relative p-1" style="min-height: 200px;">
            <ShadcnSpin v-model="initialize" fixed/>

            <div v-if="!initialize">
              <Draggable item-key="id"
                         :group="{ name: 'dimensions', pull: 'clone', put: false }"
                         :list="originalDimensions"
                         :clone="onClone"
                         @start="visibleHighlight(true, ColumnType.DIMENSION)"
                         @end="visibleHighlight(false, ColumnType.DIMENSION)">
                <template #item="{ element }">
                  <ShadcnTag class="my-1 mx-0.5 cursor-pointer" :text="element.aliasName ? element.aliasName : element.name" @dblclick="onClone(element)"/>
                </template>
              </Draggable>
            </div>
          </div>
        </ShadcnCard>
      </ShadcnSpace>
    </ShadcnCol>

    <ShadcnCol span="10">
      <div class="relative">
        <ShadcnSpin v-model="loading" fixed/>

        <div class="flex items-center space-x-2 text-sm">
          <div>{{ $t('dataset.common.columnModeMetric') }}:</div>

          <div :class="cn('w-full flex-1 p-1',
                      (highlight.active && highlight.type === ColumnType.METRIC) && 'border-2 border-green-400 rounded-sm min-h-8'
               )">
            <Draggable group="metrics"
                       item-key="id"
                       :list="metrics"
                       class="flex flex-wrap gap-2">
              <template #item="{ element, index }">
                <ShadcnTag class="inline-flex items-center whitespace-nowrap">
                  <span class="flex items-center">
                    <DatasetColumnMetric :element="element"/>
                  </span>

                  <span class="ml-2 flex items-center space-x-1">
                    <ShadcnTooltip :content="$t('common.configure')">
                      <ShadcnIcon class="cursor-pointer hover:text-primary"
                                  icon="Cog"
                                  size="15"
                                  @click="onColumnConfigure(true, element, ColumnType.METRIC)"/>
                    </ShadcnTooltip>

                    <ShadcnTooltip :content="$t('common.remove')">
                      <ShadcnIcon class="cursor-pointer text-red-400 hover:text-red-500"
                                  icon="Trash"
                                  size="15"
                                  @click="onRemove(index, metrics)"/>
                    </ShadcnTooltip>
                  </span>
                </ShadcnTag>
              </template>
            </Draggable>
          </div>
        </div>

        <ShadcnDivider class="my-2"/>

        <div class="flex items-center space-x-2 text-sm">
          <div>{{ $t('dataset.common.columnModeDimension') }}:</div>

          <div :class="cn('w-full flex-1 p-1',
                       (highlight.active && highlight.type === ColumnType.DIMENSION) && 'border-2 border-blue-400 rounded-sm min-h-8')
               ">
            <Draggable group="dimensions"
                       item-key="id"
                       :list="dimensions"
                       class="flex flex-wrap gap-2">
              <template #item="{ element, index}">
                <ShadcnTag class="inline-flex items-center whitespace-nowrap">
                  <span class="flex items-center">
                      {{ element.aliasName ? element.aliasName : element.name }}
                  </span>

                  <span class="ml-2 flex items-center space-x-1">
                    <ShadcnTooltip :content="$t('common.configure')">
                      <ShadcnIcon class="cursor-pointer hover:text-primary"
                                  icon="Cog"
                                  size="15"
                                  @click="onColumnConfigure(true, element, ColumnType.DIMENSION)"/>
                    </ShadcnTooltip>

                    <ShadcnTooltip :content="$t('common.remove')">
                      <ShadcnIcon class="cursor-pointer text-red-400 hover:text-red-500"
                                  icon="Trash"
                                  size="15"
                                  @click="onRemove(index, dimensions)"/>
                    </ShadcnTooltip>
                  </span>
                </ShadcnTag>
              </template>
            </Draggable>
          </div>
        </div>

        <ShadcnDivider class="my-2"/>

        <div class="flex items-center space-x-2 text-sm">
          <div>{{ $t('dataset.common.columnModeFilter') }}:</div>

          <div :class="cn('w-full flex-1 p-1',
                      (highlight.active && highlight.type === ColumnType.DIMENSION) && 'border-2 border-yellow-400 rounded-sm min-h-8')
               ">
            <Draggable group="dimensions"
                       item-key="id"
                       :list="filters"
                       class="flex flex-wrap gap-2">
              <template #item="{ element, index}">
                <ShadcnTag class="inline-flex items-center whitespace-nowrap">
                  <span class="flex items-center">
                      {{ element.aliasName ? element.aliasName : element.name }}
                  </span>

                  <span class="ml-2 flex items-center space-x-1">
                    <ShadcnTooltip :content="$t('common.configure')">
                      <ShadcnIcon class="cursor-pointer hover:text-primary"
                                  icon="Cog"
                                  size="15"
                                  @click="onColumnConfigure(true, element, ColumnType.FILTER)"/>
                    </ShadcnTooltip>

                    <ShadcnTooltip :content="$t('common.remove')">
                      <ShadcnIcon class="cursor-pointer text-red-400 hover:text-red-500"
                                  icon="Trash"
                                  size="15"
                                  @click="onRemove(index, filters)"/>
                    </ShadcnTooltip>
                  </span>
                </ShadcnTag>
              </template>
            </Draggable>
          </div>
        </div>

        <ShadcnDivider class="my-2"/>

        <div class="flex justify-between items-center">
          <div class="flex items-center space-x-4">
            <span>{{ $t('dataset.common.showPageSize') }}</span>
            <ShadcnNumber v-model="configure.limit" min="1"/>
          </div>

          <div class="flex items-center space-x-4 text-sm">
            <ShadcnButton :disabled="loading" :loading="loading" @click="onApplyAdhoc">
              <ShadcnIcon icon="CirclePlay"/>
            </ShadcnButton>

            <ShadcnButton type="default" :disabled="!showSql.content || loading" @click="visibleShowSql(true)">
              <template #icon>
                <ShadcnIcon icon="Eye"/>
              </template>
            </ShadcnButton>

            <ShadcnButton @click="publishVisible = true">
              {{ $t('common.publish') }}
            </ShadcnButton>
          </div>
        </div>

        <ShadcnDivider class="my-2"/>
      </div>

      <VisualEditor :loading="loading" :configuration="configuration as any" @commitOptions="visibleCommitOptions"/>
    </ShadcnCol>
  </ShadcnRow>

  <SqlInfo v-if="showSql.visible"
           :is-visible="showSql.visible"
           :content="showSql.content"
           @close="visibleShowSql(false)"/>

  <DatasetColumnConfigure v-if="columnContent.visible"
                          :is-visible="columnContent.visible"
                          :column-type="columnContent.type"
                          :content="columnContent.content"
                          :configure="columnContent.configure"
                          @close="onColumnConfigure(false, null, null)"
                          @commit="onCommitColumnConfigure"/>

  <DatasetReport v-if="publishVisible"
                 :info="dataInfo as any"
                 :code="reportCode"
                 :dimension="originalDimensions[0] as any"
                 :visible="publishVisible"
                 :commit-options="commitOptions as any"
                 :configure="configure as any"
                 @close="visiblePublish(false)"/>
</template>

<script lang="ts">
import Draggable from 'vuedraggable'
import DatasetService from '@/services/dataset'
import { Type } from '@/views/components/visual/Type'
import { Type as ColumnType } from './Type'
import ReportService from '@/services/report'
import { cloneDeep } from 'lodash'
import { Configuration } from '@/views/components/visual/Configuration'
import VisualEditor from '@/views/components/visual/VisualEditor.vue'
import DatasetColumnMetric from '@/views/pages/admin/dataset/components/adhoc/DatasetColumnMetric.vue'
import DatasetColumnConfigure from '@/views/pages/admin/dataset/components/adhoc/DatasetColumnConfigure.vue'
import { defineComponent } from 'vue'
import SqlInfo from '@/views/components/sql/SqlInfo.vue'
import { cn } from '@/lib/utils.ts'
import DatasetReport from '@/views/pages/admin/dataset/components/DatasetReport.vue'

export default defineComponent({
  name: 'DatasetAdhoc',
  computed: {
    Type()
    {
      return Type
    },
    ColumnType()
    {
      return ColumnType
    }
  },
  components: { DatasetReport, SqlInfo, DatasetColumnMetric, DatasetColumnConfigure, Draggable, VisualEditor },
  setup()
  {
    return {
      cn
    }
  },
  data()
  {
    return {
      loading: false,
      code: null as string | null,
      reportCode: null as string | null,
      originalMetrics: [],
      originalDimensions: [],
      originalData: [],
      metrics: [],
      dimensions: [],
      filters: [],
      configure: {
        columns: [] as any[],
        limit: 1000
      },
      configuration: null as Configuration | null,
      showSql: {
        visible: false,
        content: null as string | null
      },
      columnContent: {
        visible: false,
        type: null as ColumnType | null,
        content: [] as never[],
        configure: null as any | null
      },
      commitOptions: null,
      publishVisible: false,
      initialize: false,
      highlight: {
        active: false,
        type: 'METRIC'
      },
      dataInfo: { name: null as string | null, description: null as string | null }
    }
  },
  created()
  {
    this.configuration = new Configuration()
    this.handleInitialize()
  },
  methods: {
    handleInitialize()
    {
      setTimeout(() => {
        this.initialize = true
        const code = this.$route.params.code as string
        this.code = code as string
        const id = this.$route.params.id
        this.reportCode = id
        DatasetService.getColumnsByCode(this.code)
                      .then(response => {
                        if (response.status) {
                          this.originalData = response.data
                          this.originalMetrics = response.data.filter((item: { mode: string; }) => item.mode === 'METRIC')
                          this.originalDimensions = response.data.filter((item: { mode: string; }) => item.mode === 'DIMENSION')
                          if (id) {
                            ReportService.getByCode(id)
                                         .then(response => {
                                           if (response.status) {
                                             this.dataInfo.name = response.data.name
                                             this.dataInfo.description = response.data.description
                                             const query = JSON.parse(response.data.query)
                                             this.mergeColumns(query.columns, this.metrics, ColumnType.METRIC)
                                             this.mergeColumns(query.columns, this.dimensions, ColumnType.DIMENSION)
                                             this.mergeColumns(query.columns, this.filters, ColumnType.FILTER)
                                             this.configure.columns = query.columns
                                             this.configure.limit = query.limit
                                             this.configuration = JSON.parse(response.data.configure)
                                             this.onApplyAdhoc()
                                           }
                                         })
                          }
                        }
                        else {
                          this.$Message.error({
                            content: response.message,
                            showIcon: true
                          })
                        }
                      })
                      .finally(() => this.initialize = false)
      }, 0)
    },
    onApplyAdhoc()
    {
      // Set the mode to: FILTER
      this.filters.forEach((item: { mode: ColumnType; }) => item.mode = ColumnType.FILTER)
      this.configure.columns = [...this.splitColumns(this.metrics), ...this.splitColumns(this.dimensions), ...this.splitColumns(this.filters)]
      this.onAdhoc()
    },
    onAdhoc()
    {
      this.loading = true
      DatasetService.adhoc(this.code as string, this.configure)
                    .then(response => {
                      if (response.status) {
                        if (this.configuration) {
                          if (response.data.isSuccessful) {
                            this.configuration.headers = response.data.headers
                            this.configuration.columns = response.data.columns
                            this.showSql.content = response.data.content
                            this.configuration.message = null
                          }
                          else {
                            this.configuration.headers = []
                            this.configuration.columns = []
                            this.configuration.message = response.data.message
                          }
                        }
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
    onClone(value: any)
    {
      return cloneDeep(value)
    },
    onRemove(index: number, array: any[])
    {
      array.splice(index, 1)
      this.onApplyAdhoc()
    },
    onCommit(value: any)
    {
      if (this.configuration) {
        this.configuration.chartConfigure = value
      }
    },
    onColumnConfigure(opened: boolean, record: any, type: ColumnType | null)
    {
      this.columnContent.visible = opened
      this.columnContent.type = type
      if (record) {
        this.columnContent.content = this.originalData.find((item: { id: number }) => item.id === record.id) as unknown as never[]
        const foundIndex = this.configure.columns.findIndex((item: { id: unknown; }) => item.id === record.id)
        if (foundIndex !== -1) {
          const column = this.configure.columns[foundIndex]
          column.type = record.type
          this.columnContent.configure = column
        }
        else {
          this.columnContent.configure = { id: record.id, type: record.type }
        }
      }
      else {
        this.columnContent.configure = null
      }
    },
    onCommitColumnConfigure(value: any)
    {
      const clonedValue = cloneDeep(value)
      if (clonedValue.mode === ColumnType.METRIC) {
        this.replaceColumn(this.metrics, clonedValue)
      }
      else if (clonedValue.mode === ColumnType.DIMENSION) {
        this.replaceColumn(this.dimensions, clonedValue)
      }
      else if (clonedValue.mode === ColumnType.FILTER) {
        this.replaceColumn(this.filters, clonedValue)
      }
      this.onApplyAdhoc()
    },
    visibleShowSql(opened: boolean)
    {
      this.showSql.visible = opened
    },
    visibleCommitOptions(value: any)
    {
      this.commitOptions = value
    },
    visibleHighlight(opened: boolean, type: any)
    {
      this.highlight.active = opened
      this.highlight.type = type
    },
    visiblePublish(opened: boolean)
    {
      this.publishVisible = opened
    },
    mergeColumns(originalColumns: any[], array: any[], type?: ColumnType)
    {
      originalColumns.filter((item: { mode: ColumnType; id: number; }) => {
        const column = this.originalData.filter((value: { id: number; }) => value.id === item.id)[0]
        if (type) {
          if (item.mode === type) {
            Object.assign(column, item)
            array.push(column)
          }
        }
        else {
          Object.assign(column, item)
          array.push(column)
        }
      })
    },
    replaceColumn(originalColumns: any[], originalValue: any)
    {
      const index = originalColumns.findIndex((item: { id: number; }) => item.id === originalValue.id)
      if (index !== -1) {
        const cloneValue = cloneDeep(originalValue)
        originalColumns[index] = Object.assign(originalValue, originalColumns[index], cloneValue)
      }
    },
    splitColumns(original: any[]): any[]
    {
      const array: any[] = []
      original.forEach((item: { id: number; mode: ColumnType; alias: string; expression: string; name: string; function: string; value: string; order: string; }) => array.push(
          {
            id: item.id,
            mode: item.mode,
            alias: item.alias,
            expression: item.expression,
            name: item.name,
            function: item.function,
            value: item.value,
            order: item.order
          }))
      return array
    }
  }
})
</script>
