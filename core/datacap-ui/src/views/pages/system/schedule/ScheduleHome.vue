<template>
  <ShadcnCard>
    <template #title>
      <div class="ml-2 font-normal text-sm">{{ $t('schedule.common.list') }}</div>
    </template>

    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="headers" :data="data">
        <template #active="{ row }">
          <ShadcnSwitch v-model="row.active" size="small" :disabled="row.active"/>
        </template>

        <template #system="{ row }">
          <ShadcnSwitch v-model="row.system" size="small" :disabled="row.system"/>
        </template>

        <template #action="{ row }">
          <ShadcnSpace>
            <ShadcnTooltip :content="$t('schedule.common.history')">
              <ShadcnButton size="small" circle @click="handlerChangeInfo(true, row)">
                <ShadcnIcon icon="History" size="15"/>
              </ShadcnButton>
            </ShadcnTooltip>
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

  <ScheduleHistory v-if="dataHistoryVisible"
                   :is-visible="dataHistoryVisible"
                   :info="dataInfo"
                   @close="handlerChangeInfo(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter'
import { useHeaders } from './ScheduleUtils'
import ScheduleService from '@/services/schedule'
import ScheduleHistory from '@/views/pages/system/schedule/ScheduleHistory.vue'
import { ScheduleModel } from '@/model/schedule'

export default defineComponent({
  name: 'ScheduleHome',
  components: { ScheduleHistory },
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
      dataHistoryVisible: false,
      data: [],
      pageIndex: 1,
      pageSize: 10,
      dataCount: 0,
      dataInfo: null as ScheduleModel | null
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
      ScheduleService.getAll(this.filter)
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
    handlerChangeInfo(isOpen: boolean, dataInfo: any)
    {
      this.dataHistoryVisible = isOpen
      this.dataInfo = dataInfo
    }
  }
})
</script>
