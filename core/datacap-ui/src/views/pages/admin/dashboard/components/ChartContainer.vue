<template>
  <ShadcnModal v-model="visible"
               width="60%"
               height="80%"
               :title="$t('report.common.list')"
               @on-close="onCancel">

    <div class="relative w-full h-full">
      <ShadcnSpin v-model="loading" fixed/>

      <div v-if="!loading" class="p-2">
        <ShadcnToggleGroup v-model="report" multiple>
          <ShadcnRow gutter="8">
            <ShadcnCol v-for="item of data" span="4">
              <ShadcnToggle :key="item.id" class="px-1 py-1" :value="item.id">
                <ShadcnCard :title="item.name">
                  <template #extra>
                    <ShadcnTooltip v-if="item.description" :content="item.description">
                      <ShadcnIcon icon="Info" size="20"/>
                    </ShadcnTooltip>
                  </template>

                  <VisualView width="300px" height="250px"
                              :code="item.dataset?.code as string"
                              :configuration="JSON.parse(item.configure as string)"
                              :type="item.type"
                              :query="item.type === 'DATASET' ? JSON.parse(item.query as string) : item.query"
                              :original="item?.source?.id"/>
                </ShadcnCard>
              </ShadcnToggle>
            </ShadcnCol>
          </ShadcnRow>
        </ShadcnToggleGroup>

        <div v-if="data.length === 0" class="flex w-full items-center">
          {{ $t('common.noData') }}
        </div>
      </div>

      <ShadcnPagination v-if="data.length > 0"
                        v-model="pageIndex"
                        class="py-2 mt-2"
                        show-total
                        show-sizer
                        :page-size="pageSize"
                        :total="dataCount"
                        :sizerOptions="[12, 24, 36]"
                        @on-change="onPageChange"
                        @on-prev="onPrevChange"
                        @on-next="onNextChange"
                        @on-change-size="onSizeChange"/>
    </div>

    <template #footer>
      <ShadcnSpace>
        <ShadcnButton type="default" @click="onCancel">
          {{ $t('common.cancel') }}
        </ShadcnButton>

        <ShadcnButton :disabled="!report" @click="onSubmit()">
          {{ $t('common.save') }}
        </ShadcnButton>
      </ShadcnSpace>
    </template>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ReportService from '@/services/report.ts'
import { FilterModel } from '@/model/filter.ts'
import { ReportModel } from '@/model/report.ts'
import VisualView from '@/views/components/visual/VisualView.vue'
import { toNumber } from 'lodash'

export default defineComponent({
  name: 'ChartContainer',
  components: { VisualView },
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
    }
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    filter.size = 12

    return {
      filter
    }
  },
  created()
  {
    this.handleInitialize()
  },
  data()
  {
    return {
      loading: false,
      data: [] as ReportModel[],
      report: [] as any[],
      pageIndex: 1,
      pageSize: 10,
      dataCount: 0
    }
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
    onSubmit()
    {
      const nodes = this.data.filter(item =>
          this.report.some((reportId: number) => toNumber(item.id) === toNumber(reportId))
      )
      this.$emit('change', nodes)

      this.onCancel()
    },
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>
