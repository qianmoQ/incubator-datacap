<template>
  <ShadcnCard>
    <template #title>
      <div class="ml-2 font-normal text-sm">
        {{ $t('snippet.common.list') }}
      </div>
    </template>

    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="headers" :data="data">
        <template #username="{ row }">
          <ShadcnAvatar size="small" :src="row.user.avatarConfigure?.path" :alt="row.user.username"/>
        </template>

        <template #action="{ row }">
          <ShadcnSpace>
            <ShadcnTooltip :content="$t('snippet.common.modify').replace('$VALUE', row.name)">
              <ShadcnButton size="small" circle @click="visibleInfo(true, row)">
                <ShadcnIcon icon="Pencil" size="15"/>
              </ShadcnButton>
            </ShadcnTooltip>

            <ShadcnDropdown trigger="click">
              <template #trigger>
                <ShadcnButton size="small" circle>
                  <ShadcnIcon icon="Cog" size="15"/>
                </ShadcnButton>
              </template>

              <ShadcnDropdownItem>
                <ShadcnLink :link="`/admin/query/snippet/${row?.code}`" target="_blank">
                  <div class="flex items-center space-x-2">
                    <ShadcnIcon icon="Quote" size="15"/>
                    <span>{{ $t('query.common.quoteRecord') }}</span>
                  </div>
                </ShadcnLink>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem @on-click="visibleContent(true, row?.context)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="SquareChevronRight" size="15"/>
                  <span>{{ $t('query.common.showSql') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem @on-click="visibleDelete(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="Delete" size="15"/>
                  <span>{{ $t('snippet.common.delete') }}</span>
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

  <SnippetInfo v-if="dataInfoVisible"
               :is-visible="dataInfoVisible"
               :info="dataInfo"
               @close="visibleInfo(false, null)"/>

  <SnippetDelete v-if="dataDeleteVisible"
                 :is-visible="dataDeleteVisible"
                 :info="dataInfo"
                 @close="visibleDelete(false, null)"/>

  <SqlInfo v-if="contentVisible && content"
           :is-visible="contentVisible"
           :content="content"
           @close="visibleContent(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter.ts'
import { useHeaders } from '@/views/pages/admin/snippet/SnippetUtils'
import SnippetService from '@/services/snippet'
import { SnippetModel } from '@/model/snippet'
import SnippetInfo from '@/views/pages/admin/snippet/SnippetInfo.vue'
import SqlInfo from '@/views/components/sql/SqlInfo.vue'
import SnippetDelete from '@/views/pages/admin/snippet/SnippetDelete.vue'

export default defineComponent({
  name: 'SnippetHome',
  components: { SnippetDelete, SqlInfo, SnippetInfo },
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
      dataInfoVisible: false,
      dataInfo: null as null | SnippetModel,
      contentVisible: false,
      content: null as string | null,
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
      SnippetService.getAll(this.filter)
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
    visibleInfo(opened: boolean, value: null | SnippetModel)
    {
      this.dataInfoVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handleInitialize()
      }
    },
    visibleContent(opened: boolean, value: string | null)
    {
      this.contentVisible = opened
      this.content = value
    },
    visibleDelete(opened: boolean, data: null | SnippetModel)
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
