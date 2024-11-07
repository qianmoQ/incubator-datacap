<template>
  <ShadcnModal v-model="visible" :title="$t('schedule.common.history')" width="60" @on-close="onCancel">
    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="headers" :data="data" />

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
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { ScheduleModel } from '@/model/schedule'
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { CardContent } from '@/components/ui/card'
import TableCommon from '@/views/components/table/TableCommon.vue'
import { FilterModel } from '@/model/filter'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import { useI18n } from 'vue-i18n'
import { createHistoryHeaders } from '@/views/pages/system/schedule/ScheduleUtils'
import ScheduleService from '@/services/schedule'
import { cn } from '@/lib/utils'

export default defineComponent({
  name: 'ScheduleHistory',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object as () => ScheduleModel | null,
      default: null
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
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const headers = createHistoryHeaders(useI18n())

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
      dataCount: 0
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
      ScheduleService.getScheduleHistory(this.filter, this.info?.id as number)
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
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>