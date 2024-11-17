<template>
  <ShadcnModal v-model="visible"
               width="60%"
               height="80%"
               :title="$t('report.common.list')"
               @on-close="onCancel">

    <div class="relative w-full h-full">
      <ShadcnSpin v-model="loading"/>

      <div v-if="!loading" class="p-2">
        <FormField type="radio" name="theme">
          <FormItem class="space-y-1">
            <FormMessage/>
            <RadioGroup v-model="report" class="grid w-full grid-cols-4 gap-8 pt-2">
              <FormItem v-for="item of data" :key="item.id">
                <FormLabel class="[&:has([data-state=checked])>div]:border-primary">
                  <FormControl>
                    <RadioGroupItem :value="item.id as unknown as string" class="sr-only"/>
                  </FormControl>
                  <div class="items-center rounded-md border-4 border-muted p-1 hover:border-accent cursor-pointer text-center">
                    <VisualView width="200px" height="100px" :code="item.dataset?.code as string" :configuration="JSON.parse(item.configure as string)"
                                :type="item.type" :query="item.type === 'DATASET' ? JSON.parse(item.query as string) : item.query" :original="item?.source?.id"/>
                  </div>
                  <span class="block w-full p-2 text-center font-normal">{{ item.name }}</span>
                </FormLabel>
              </FormItem>
            </RadioGroup>
          </FormItem>
        </FormField>
        <div v-if="data.length === 0" class="flex w-full items-center">
          {{ $t('common.noData') }}
        </div>
      </div>
    </div>

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
    <template #footer>
      <ShadcnSpace>
        <ShadcnButton type="default" @click="onCancel">
          {{ $t('common.cancel') }}
        </ShadcnButton>

        <ShadcnButton @click="onSubmit()">
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
      report: '',
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
      const node = this.data.find(item => item.id === toNumber(this.report))
      this.$emit('change', node)
      this.onCancel()
    },
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>
