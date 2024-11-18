<template>
  <ShadcnModal v-model="visible"
               width="60%"
               :title="`[ ${info?.name} ] ${$t('dataset.common.history')}`"
               @on-close="onCancel">
    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="historyHeaders" :data="data">
        <template #state="{ row }">
          <ShadcnHoverCard v-if="row?.state === 'FAILURE'">
            <ShadcnTag :color="Common.getColor(row?.state)">
              {{ getStateText(row?.state) }}
            </ShadcnTag>

            <template #content>
              <div class="p-2 w-full overflow-x-auto">
                {{ row?.message }}
              </div>
            </template>
          </ShadcnHoverCard>

          <ShadcnTag v-else :color="Common.getColor(row?.state)">
            <span>{{ getStateText(row?.state) }}</span>
          </ShadcnTag>
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
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter'
import { useDatasetHeaders } from './DatasetUtils'
import DatasetService from '@/services/dataset'
import { DatasetModel } from '@/model/dataset'
import Common, { useUtil } from '@/utils/common'

export default defineComponent({
  name: 'DatasetHistory',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object as () => DatasetModel | null
    }
  },
  computed: {
    Common()
    {
      return Common
    },
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
    const { historyHeaders } = useDatasetHeaders()
    const { getText } = useUtil()

    return {
      filter,
      historyHeaders,
      getText
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
    this.handleInitialize()
  },
  methods: {
    handleInitialize()
    {
      this.loading = true
      DatasetService.getHistory(this.info?.code as string, this.filter)
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
    onCancel()
    {
      this.visible = false
    },
    getStateText(origin: string): string
    {
      return this.getText(origin)
    }
  }
})
</script>
