<template>
  <ShadcnCard>
    <template #title>
      <div class="ml-2 font-normal text-sm">{{ $t('source.common.list') }}</div>
    </template>

    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="headers" :data="data">
        <template #source="{ row }">
          <span>{{ row?.source?.name }}</span>
        </template>

        <template #type="{row}">
          <ShadcnTooltip :content="row?.source?.type">
            <ShadcnAvatar size="small"
                          :src="'/static/images/plugin/' + row?.source?.type + '.png'"
                          :alt="row?.source?.type" class="cursor-pointer"/>
          </ShadcnTooltip>
        </template>

        <template #mode="{ row }">
          <ShadcnTag v-if="row.mode === 'ADHOC'">{{ $t('common.adhoc') }}</ShadcnTag>
          <ShadcnTag v-else-if="row.mode === 'HISTORY'">{{ $t('common.history') }}</ShadcnTag>
          <ShadcnTag v-else-if="row.mode === 'REPORT'">{{ $t('common.report') }}</ShadcnTag>
          <ShadcnTag v-else-if="row.mode === 'SNIPPET'">{{ $t('common.snippet') }}</ShadcnTag>
          <ShadcnTag v-else-if="row.mode === 'DATASET'">{{ $t('common.dataset') }}</ShadcnTag>
          <ShadcnTag v-else>{{ row.mode }}</ShadcnTag>
        </template>

        <template #state="{ row }">
          <ShadcnTag :type="row.state === 'SUCCESS' ? 'success' : 'error'">{{ row.state }}</ShadcnTag>
        </template>

        <template #action="{row}">
          <ShadcnSpace>
            <ShadcnTooltip :content="$t('common.error')">
              <ShadcnButton color="#ed4014"
                            circle
                            size="small"
                            :disabled="row.state === 'SUCCESS'"
                            @click="handlerShowContent(true, row?.message)">
                <ShadcnIcon icon="TriangleAlert" size="15"/>
              </ShadcnButton>
            </ShadcnTooltip>
            <ShadcnDropdown position="right">
              <template #trigger>
                <ShadcnButton circle size="small">
                  <ShadcnIcon icon="EllipsisVertical" size="15"/>
                </ShadcnButton>
              </template>

              <ShadcnDropdownItem>
                <ShadcnLink :link="`/admin/query/history/${row?.code}`" target="_blank">
                  <div class="flex items-center space-x-2">
                    <ShadcnIcon icon="Quote" size="15"/>
                    <span>{{ $t('query.common.quoteRecord') }}</span>
                  </div>
                </ShadcnLink>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem>
                <div class="flex items-center space-x-2" @click="handlerShowContent(true, row?.content)">
                  <ShadcnIcon icon="SquareChevronRight" size="15"/>
                  <span>{{ $t('query.common.showSql') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem>
                <div class="flex items-center space-x-2" @click="handlerShowData(true, row)">
                  <ShadcnIcon icon="Table" size="15"/>
                  <span>{{ $t('query.common.historyData') }}</span>
                </div>
              </ShadcnDropdownItem>
            </ShadcnDropdown>
          </ShadcnSpace>
        </template>
      </ShadcnTable>

      <ShadcnPagination v-model="pageIndex"
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

  <SqlInfo v-if="contentVisible && content"
           :is-visible="contentVisible"
           :content="content"
           @close="handlerShowContent(false, null)"/>

  <HistoryData v-if="dataVisible && dataInfo"
               :is-visible="dataVisible"
               :info="dataInfo"
               @close="handlerShowData(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter.ts'
import AuditService from '@/services/audit'
import { useHeaders } from '@/views/pages/admin/history/HistoryUtils'
import SqlInfo from '@/views/components/sql/SqlInfo.vue'
import { HistoryModel } from '@/model/history'
import HistoryData from '@/views/pages/admin/history/HistoryData.vue'

export default defineComponent({
  name: 'HistoryHome',
  components: {
    HistoryData,
    SqlInfo
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const { headers } = useHeaders()

    return {
      filter,
      headers
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
      contentVisible: false,
      dataInfo: null as HistoryModel | null,
      dataVisible: false,
      content: null as string | null
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
      AuditService.getAll(this.filter)
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
      this.handlerInitialize()
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
    handlerShowContent(opened: boolean, value: string | null)
    {
      this.contentVisible = opened
      this.content = value
    },
    handlerShowData(opened: boolean, value: HistoryModel | null)
    {
      this.dataVisible = opened
      this.dataInfo = value
    }
  }
})
</script>
