<template>
  <ShadcnModal v-model="visible"
               width="60%"
               :title="$t('source.common.syncHistory')"
               @on-close="onCancel">

    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="headers" :data="data">
        <template #elapsed="{ row }">
          {{ (getTime(row.updateTime) - getTime(row.createTime)) / 1000 }}
        </template>

        <template #state="{ row }">
          <ShadcnTooltip v-if="row?.state === 'FAILURE'" :content="row.message">
            <ShadcnBadge :color="Common.getColor(row?.state)" :text="getStateText(row.state)"/>
          </ShadcnTooltip>
          <ShadcnBadge v-else :color="Common.getColor(row?.state)" :text="getStateText(row.state)"/>
        </template>

        <template #result="{ row }">
          <ShadcnTooltip>
            <template #content>
              <MdPreview :modelValue="toMarkdown(row.info)" style="padding: 0"/>
            </template>

            <ShadcnButton circle size="small">
              <template #icon>
                <ShadcnIcon icon="Eye" :size="20"/>
              </template>
            </ShadcnButton>
          </ShadcnTooltip>
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

    <template #footer>
      <ShadcnButton type="error" @click="onCancel">
        {{ $t('common.cancel') }}
      </ShadcnButton>
    </template>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { SourceModel } from '@/model/source'
import SourceService from '@/services/source'
import { FilterModel } from '@/model/filter'
import { useHeaders } from '@/views/pages/admin/source/SourceUtils'
import Common, { useUtil } from '@/utils/common'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

export default defineComponent({
  name: 'SourceHistory',
  components: {
    MdPreview
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
    },
    Common()
    {
      return Common
    }
  },
  props: {
    isVisible: {
      type: Boolean
    },
    info: {
      type: Object as () => SourceModel | null
    }
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const { historyHeaders: headers } = useHeaders()
    const { getText } = useUtil()

    return {
      filter,
      headers,
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
      SourceService.getHistory(this.info?.code as string, this.filter)
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
    getTime(time: any)
    {
      return time ? new Date(time).getTime() : 0
    },
    getStateText(origin: string): string
    {
      return this.getText(origin)
    },
    toMarkdown(content: string)
    {
      return '```json\n' + JSON.stringify(content, null, 4) + '\n```'
    }
  }
})
</script>
