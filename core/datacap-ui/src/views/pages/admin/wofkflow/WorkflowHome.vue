<template>
  <ShadcnCard>
    <template #title>
      <div class="ml-2">{{ $t('workflow.text.list') }}</div>
    </template>

    <template #extra>
      <ShadcnTooltip :content="$t('workflow.text.create')">
        <ShadcnLink link="/admin/workflow/info">
          <ShadcnButton circle size="small">
            <ShadcnIcon icon="Plus" size="15"/>
          </ShadcnButton>
        </ShadcnLink>
      </ShadcnTooltip>

    </template>

    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="headers" :data="data">
        <template #executor="{row}">
          <ShadcnTooltip :content="row.executor">
            <ShadcnAvatar size="small" :src="`/static/images/executor/${row.executor.replace('Executor', '').toLowerCase()}.svg`"/>
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

              <ShadcnDropdownItem :disabled="row.state === 'RUNNING'">
                <template v-if="row.state !== 'RUNNING'">
                  <RouterLink :to="`/admin/workflow/info/${row.code}`" target="_blank" class="flex items-center">
                    <ShadcnIcon icon="Info" size="15"/>
                    <span class="ml-2">{{ $t('workflow.text.modify') }}</span>
                  </RouterLink>
                </template>
                <div v-else class="flex items-center">
                  <ShadcnIcon icon="Info" size="15"/>
                  <span class="ml-2">{{ $t('workflow.text.modify') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem :disabled="row.state !== 'RUNNING'" @on-click="visibleStop(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="CircleStop" size="15"/>
                  <span>{{ $t('workflow.text.stop') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem :disabled="row.state === 'RUNNING'" @on-click="visibleRestart(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="CirclePlay" size="15"/>
                  <span>{{ $t('workflow.text.restart') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem @on-click="visibleLogger(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="Rss" size="15"/>
                  <span>{{ $t('workflow.text.logger') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem :disabled="row.state === 'RUNNING'" @on-click="visibleDelete(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="Delete" size="15"/>
                  <span>{{ $t('workflow.text.delete') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem @on-click="visibleFlow(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="Flower" size="15"/>
                  <span>{{ $t('workflow.text.flow') }}</span>
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

  <WorkflowFlow v-if="dataFlowVisible && dataInfo"
                :is-visible="dataFlowVisible"
                :code="dataInfo.code"
                @close="visibleFlow(false, null)"/>

  <WorkflowDelete v-if="dataDeleteVisible && dataInfo"
                  :is-visible="dataDeleteVisible"
                  :info="dataInfo"
                  @close="visibleDelete(false, null)"/>

  <WorkflowStop v-if="dataStopVisible && dataInfo"
                :is-visible="dataStopVisible"
                :info="dataInfo"
                @close="visibleStop(false, null)"/>

  <WorkflowLogger v-if="dataLoggerVisible && dataInfo"
                  :is-visible="dataLoggerVisible"
                  :info="dataInfo"
                  @close="visibleLogger(false, null)"/>

  <WorkflowRestart v-if="dataRestartVisible && dataInfo"
                   :is-visible="dataRestartVisible"
                   :info="dataInfo"
                   @close="visibleRestart(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter'
import { useHeaders } from './Utils'
import WorkflowService from '@/services/workflow'
import Common, { useUtil } from '@/utils/common.ts'
import MarkdownPreview from '@/views/components/markdown/MarkdownView.vue'
import { WorkflowModel } from '@/model/workflow.ts'
import WorkflowFlow from '@/views/pages/admin/wofkflow/WorkflowFlow.vue'
import WorkflowDelete from '@/views/pages/admin/wofkflow/WorkflowDelete.vue'
import WorkflowStop from '@/views/pages/admin/wofkflow/WorkflowStop.vue'
import WorkflowLogger from '@/views/pages/admin/wofkflow/WorkflowLogger.vue'
import WorkflowRestart from '@/views/pages/admin/wofkflow/WorkflowRestart.vue'

export default defineComponent({
  name: 'PipelineHome',
  components: { WorkflowRestart, WorkflowLogger, WorkflowStop, WorkflowDelete, WorkflowFlow, MarkdownPreview },
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
      dataInfo: null as WorkflowModel | null,
      dataMessageVisible: false,
      dataLoggerVisible: false,
      dataDeleteVisible: false,
      dataStopVisible: false,
      dataFlowVisible: false,
      dataRestartVisible: false
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
      WorkflowService.getAll(this.filter)
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
    visibleStop(opened: boolean, value: null | WorkflowModel)
    {
      this.dataStopVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handleInitialize()
      }
    },
    visibleShowMessage(opened: boolean, value: null | WorkflowModel)
    {
      this.dataMessageVisible = opened
      this.dataInfo = value
    },
    visibleLogger(opened: boolean, value: null | WorkflowModel)
    {
      this.dataLoggerVisible = opened
      this.dataInfo = value
    },
    visibleDelete(opened: boolean, value: null | WorkflowModel)
    {
      this.dataDeleteVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handleInitialize()
      }
    },
    visibleFlow(opened: boolean, value: null | WorkflowModel)
    {
      this.dataFlowVisible = opened
      this.dataInfo = value
    },
    visibleRestart(opened: boolean, value: null | WorkflowModel)
    {
      this.dataRestartVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handleInitialize()
      }
    }
  }
})
</script>
