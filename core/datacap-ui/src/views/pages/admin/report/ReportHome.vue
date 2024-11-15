<template>
  <ShadcnCard>
    <template #title>
      <div class="ml-2 font-normal text-sm">{{ $t('report.common.list') }}</div>
    </template>

    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="headers" :data="data">
        <template #realtime="{ row }">
          <ShadcnSwitch v-model="row.realtime" size="small" :disabled="row.realtime"/>
        </template>

        <template #source="{ row }">
          <ShadcnTooltip v-if="row.source" :content="row.source?.type">
            <ShadcnAvatar size="small" :src="'/static/images/plugin/' + row.source?.type + '.png'" :alt="row.source?.type"/>
          </ShadcnTooltip>

          <ShadcnTooltip v-else :content="row.dataset?.name">
            <ShadcnTag>{{ $t('common.dataset') }}</ShadcnTag>
          </ShadcnTooltip>
        </template>

        <template #action="{ row }">
          <ShadcnSpace>
            <ShadcnTooltip :content="$t('report.common.view').replace('$VALUE', row.name)">
              <ShadcnButton circle size="small" @click="visibleView(true, row)">
                <ShadcnIcon icon="Eye" size="15"/>
              </ShadcnButton>
            </ShadcnTooltip>

            <ShadcnDropdown trigger="click">
              <template #trigger>
                <ShadcnButton circle size="small">
                  <ShadcnIcon icon="Cog" size="15"/>
                </ShadcnButton>
              </template>

              <ShadcnDropdownItem :disabled="row.type === 'QUERY'">
                <ShadcnLink :link="`/admin/dataset/adhoc/${row.dataset?.code}/${row.id}`" target="_blank">
                  <div class="flex items-center space-x-2">
                    <ShadcnIcon icon="Pencil" size="15"/>
                    <span>{{ $t('report.common.modify') }}</span>
                  </div>
                </ShadcnLink>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem @on-click="visibleDelete(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="Delete" size="15"/>
                  <span>{{ $t('report.common.delete') }}</span>
                </div>
              </ShadcnDropdownItem>
            </ShadcnDropdown>
          </ShadcnSpace>
        </template>
      </ShadcnTable>

      <ShadcnPagination v-if="data?.length > 0"
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

  <ReportView v-if="dataViewVisible"
              :is-visible="dataViewVisible"
              :info="dataInfo"
              @close="visibleView(false, null)"/>

  <ReportDelete v-if="dataDeleteVisible"
                :is-visible="dataDeleteVisible"
                :info="dataInfo"
                @close="visibleDelete(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter'
import { useI18n } from 'vue-i18n'
import { createHeaders } from '@/views/pages/admin/report/ReportUtils'
import ReportService from '@/services/report'
import { ReportModel } from '@/model/report'
import ReportView from '@/views/pages/admin/report/ReportView.vue'
import ReportDelete from '@/views/pages/admin/report/ReportDelete.vue'

export default defineComponent({
  name: 'ReportHome',
  components: { ReportDelete, ReportView },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const headers = createHeaders(useI18n())
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
      dataInfo: null as ReportModel | null,
      dataViewVisible: false,
      dataDeleteVisible: false
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
      ReportService.getAll(this.filter)
                   .then(response => {
                     if (response.status) {
                       this.data = response.data.content
                       this.dataCount = response.data.total
                       this.pageSize = response.data.size
                       this.pageIndex = response.data.page
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
    visibleView(opened: boolean, value: ReportModel | null)
    {
      this.dataViewVisible = opened
      this.dataInfo = value
    },
    visibleDelete(opened: boolean, data: ReportModel | null)
    {
      this.dataDeleteVisible = opened
      this.dataInfo = data
      if (!opened) {
        this.handleInitialize()
      }
    }
  }
})
</script>
