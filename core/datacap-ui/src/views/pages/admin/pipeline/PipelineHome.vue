<template>
  <ShadcnCard>
    <template #title>
      <div class="ml-2">{{ $t('pipeline.common.list') }}</div>
    </template>

    <template #extra>
      <ShadcnTooltip :content="$t('pipeline.common.create')">
        <ShadcnLink link="/admin/pipeline/info">
          <ShadcnButton circle size="small">
            <ShadcnIcon icon="Plus" size="15"/>
          </ShadcnButton>
        </ShadcnLink>
      </ShadcnTooltip>

    </template>

    <div class="relative">
      <ShadcnAlert closable type="error">{{ $t('pipeline.tip.timeUp') }}</ShadcnAlert>

      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="headers" :data="data">
        <template #executor="{row}">
          <ShadcnTooltip :content="row.executor">
            <ShadcnAvatar size="small" :src="`/static/images/executor/${row.executor.toLowerCase()}.svg`"/>
          </ShadcnTooltip>
        </template>

        <template #from="{row}">
          <ShadcnTooltip :content="row.from.name">
            <ShadcnAvatar size="small" :src="'/static/images/plugin/' + row.from.type.toLowerCase() + '.svg'"/>
          </ShadcnTooltip>
        </template>

        <template #to="{row}">
          <ShadcnTooltip :content="row.to.name">
            <ShadcnAvatar size="small" :src="'/static/images/plugin/' + row.to.type.toLowerCase() + '.svg'"/>
          </ShadcnTooltip>
        </template>

        <template #state="{row}">
          <ShadcnTag class="w-20" :color="Common.getColor(row.state)">
            {{ getText(row.state) }}
          </ShadcnTag>
        </template>

        <template #action="{row}">
          <ShadcnSpace>
            <ShadcnTooltip :content="$t('common.error')">
              <ShadcnButton circle
                            size="small"
                            type="error"
                            :disabled="row.state !== 'FAILURE' && !(row.state == 'STOPPED' && row.message)"
                            @click="visibleShowMessage(true, row)">
                <ShadcnIcon icon="TriangleAlert" size="15"/>
              </ShadcnButton>
            </ShadcnTooltip>

            <ShadcnDropdown trigger="click" position="right">
              <template #trigger>
                <ShadcnButton circle size="small">
                  <ShadcnIcon icon="Cog" :size="15"/>
                </ShadcnButton>
              </template>

              <ShadcnDropdownItem :disabled="row.state !== 'RUNNING'" @on-click="visibleStop(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="CircleStop" size="15"/>
                  <span>{{ $t('pipeline.common.stop') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem @on-click="visibleLogger(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="Rss" size="15"/>
                  <span>{{ $t('pipeline.common.logger') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem @on-click="visibleDelete(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="Delete" size="15"/>
                  <span>{{ $t('pipeline.common.delete') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem @on-click="visibleFlow(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="Flower" size="15"/>
                  <span>{{ $t('pipeline.common.flow') }}</span>
                </div>
              </ShadcnDropdownItem>
            </ShadcnDropdown>
          </ShadcnSpace>
        </template>
      </ShadcnTable>

      <ShadcnPagination v-if="data.length > 0"
                        v-model="pageIndex"
                        class="py-2"
                        show-total
                        show-sizer
                        :page-size="pageSize"
                        :total="dataCount"
                        :sizerOptions="[10, 20, 50]"
                        @on-change="onPageChange"
                        @on-prev="onPrevChange"
                        @on-next="onNextChange"
                        @on-change-size="onSizeChange"/>
    </div>
  </ShadcnCard>

  <MarkdownPreview v-if="dataMessageVisible && dataInfo"
                   :is-visible="dataMessageVisible"
                   :content="dataInfo.message"
                   @close="visibleShowMessage(false, null)"/>

  <PipelineLogger v-if="dataLoggerVisible && dataInfo"
                  :is-visible="dataLoggerVisible"
                  :info="dataInfo"
                  @close="visibleLogger(false, null)"/>

  <PipelineDelete v-if="dataDeleteVisible && dataInfo"
                  :is-visible="dataDeleteVisible"
                  :info="dataInfo"
                  @close="visibleDelete(false, null)"/>

  <PipelineStop v-if="dataStopVisible && dataInfo"
                :is-visible="dataStopVisible"
                :info="dataInfo"
                @close="visibleStop(false, null)"/>

  <PipelineFlow v-if="dataFlowVisible && dataInfo"
                :is-visible="dataFlowVisible"
                :info="dataInfo"
                @close="visibleFlow(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter'
import { useHeaders } from '@/views/pages/admin/pipeline/PipelineUtils'
import PipelineService from '@/services/pipeline'
import Common, { useUtil } from '@/utils/common.ts'
import { PipelineModel } from '@/model/pipeline.ts'
import MarkdownPreview from '@/views/components/markdown/MarkdownView.vue'
import PipelineStop from '@/views/pages/admin/pipeline/PipelineStop.vue'
import PipelineLogger from '@/views/pages/admin/pipeline/PipelineLogger.vue'
import PipelineDelete from '@/views/pages/admin/pipeline/PipelineDelete.vue'
import PipelineFlow from '@/views/pages/admin/pipeline/PipelineFlow.vue'

export default defineComponent({
  name: 'PipelineHome',
  components: { PipelineFlow, PipelineDelete, PipelineLogger, PipelineStop, MarkdownPreview },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const { headers } = useHeaders()
    const { getText } = useUtil()

    return {
      filter,
      headers,
      getText
    }
  },
  computed: {
    Common()
    {
      return Common
    }
  },
  data()
  {
    return {
      loading: false,
      data: [],
      pageIndex: 1,
      pageSize: 10,
      dataCount: 0,
      dataInfo: null as PipelineModel | null,
      dataMessageVisible: false,
      dataLoggerVisible: false,
      dataDeleteVisible: false,
      dataStopVisible: false,
      dataFlowVisible: false
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
      PipelineService.getAll(this.filter)
                     .then((response) => {
                       if (response.status) {
                         this.data = response.data.content
                         this.dataCount = response.data.total
                         this.pageSize = response.data.size
                         this.pageIndex = response.data.page
                       }
                     })
                     .finally(() => this.loading = false)
    },
    fetchData(value: number)
    {
      this.filter.page = value
      this.filter.size = this.pageSize
      this.handleInitialize()
    },
    onPageChange(value: number)
    {
      this.fetchData(value)
    },
    onPrevChange(value: number)
    {
      this.fetchData(value)
    },
    onNextChange(value: number)
    {
      this.fetchData(value)
    },
    onSizeChange(value: number)
    {
      this.pageSize = value
      this.fetchData(this.pageIndex)
    },
    visibleStop(opened: boolean, value: null | PipelineModel)
    {
      this.dataStopVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handleInitialize()
      }
    },
    visibleShowMessage(opened: boolean, value: null | PipelineModel)
    {
      this.dataMessageVisible = opened
      this.dataInfo = value
    },
    visibleLogger(opened: boolean, value: null | PipelineModel)
    {
      this.dataLoggerVisible = opened
      this.dataInfo = value
    },
    visibleDelete(opened: boolean, value: null | PipelineModel)
    {
      this.dataDeleteVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handleInitialize()
      }
    },
    visibleFlow(opened: boolean, value: null | PipelineModel)
    {
      this.dataFlowVisible = opened
      this.dataInfo = value
    }
  }
})
</script>
