<template>
  <ShadcnCard>
    <template #title>
      <div class="ml-2">{{ $t('source.common.list') }}</div>
    </template>

    <template #extra>
      <ShadcnButton circle size="small" @click="visibleInfo(true, null)">
        <ShadcnIcon icon="Plus" :size="20"/>
      </ShadcnButton>
    </template>

    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="headers" :data="data">
        <template #type="{row}">
          <ShadcnTooltip :content="row.type">
            <ShadcnAvatar class="cursor-pointer"
                          size="small"
                          :src="'/static/images/plugin/' + row.type.toLowerCase() + '.svg'"
                          :alt="row.type"/>
          </ShadcnTooltip>
        </template>

        <template #public="{row}">
          <ShadcnSwitch v-model="row.public" disabled size="small"/>
        </template>

        <template #version="{row}">
          <ShadcnTag :text="row.version || '-'" size="default" type="primary"/>
        </template>

        <template #available="{row}">
          <ShadcnTooltip v-if="!row.available" :content="row.message">
            <ShadcnIcon icon="CircleX" :size="20" class="cursor-pointer text-red-500"/>
          </ShadcnTooltip>
          <ShadcnIcon v-else icon="CirclePlay" :size="20" class="text-green-500"/>
        </template>

        <template #action="{row}">
          <ShadcnSpace>
            <ShadcnTooltip :content="$t('source.common.modify').replace('$NAME', row.name)">
              <ShadcnButton circle
                            size="small"
                            :disabled="loginUserCode !== row.user.code"
                            @click="visibleInfo(true, row)">
                <ShadcnIcon icon="Pencil" :size="15"/>
              </ShadcnButton>
            </ShadcnTooltip>

            <ShadcnDropdown trigger="click" position="right">
              <template #trigger>
                <ShadcnButton circle size="small">
                  <ShadcnIcon icon="Cog" :size="15"/>
                </ShadcnButton>
              </template>

              <ShadcnDropdownItem :disabled="(loginUserCode !== row.user.code) || !row.available">
                <ShadcnLink :link="`/admin/source/${row?.code}`" target="_blank">
                  <div class="flex items-center space-x-2">
                    <ShadcnIcon icon="Cog" size="15"/>
                    <span>{{ $t('source.common.manager') }}</span>
                  </div>
                </ShadcnLink>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem :disabled="(loginUserCode !== row.user.code)" @on-click="visibleHistory(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="History" size="15"/>
                  <span>{{ $t('source.common.syncHistory') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem :disabled="(loginUserCode !== row.user.code) || !row.available" @on-click="visibleSyncMetadata(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="RefreshCcwDot" size="15"/>
                  <span>{{ $t('source.common.syncMetadata') }}</span>
                </div>
              </ShadcnDropdownItem>

              <ShadcnDropdownItem :disabled="loginUserCode !== row.user.code" @on-click="visibleDelete(true, row)">
                <div class="flex items-center space-x-2">
                  <ShadcnIcon icon="Trash" size="15"/>
                  <span>{{ $t('common.deleteData') }}</span>
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

  <SourceInfo v-if="dataInfoVisible"
              :is-visible="dataInfoVisible"
              :info="dataInfo"
              @close="visibleInfo(false, null)"/>

  <SourceDelete v-if="dataDeleteVisible"
                :is-visible="dataDeleteVisible"
                :info="dataInfo"
                @close="visibleDelete(false, null)"/>

  <SourceMetadata v-if="dataSyncMetadataVisible"
                  :is-visible="dataSyncMetadataVisible"
                  :info="dataInfo"
                  @close="visibleSyncMetadata(false, null)"/>

  <SourceHistory v-if="dataHistoryVisible"
                 :is-visible="dataHistoryVisible"
                 :info="dataInfo"
                 @close="visibleHistory(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Common from '@/utils/common'
import { useHeaders } from '@/views/pages/admin/source/SourceUtils'
import { FilterModel } from '@/model/filter'
import { SourceModel } from '@/model/source'
import SourceService from '@/services/source'
import SourceInfo from '@/views/pages/admin/source/SourceInfo.vue'
import SourceHistory from '@/views/pages/admin/source/SourceHistory.vue'
import SourceDelete from '@/views/pages/admin/source/SourceDelete.vue'
import SourceMetadata from '@/views/pages/admin/source/SourceMetadata.vue'

export default defineComponent({
  name: 'SourceHome',
  components: { SourceMetadata, SourceDelete, SourceHistory, SourceInfo },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const { headers } = useHeaders()
    const loginUserCode = Common.getCurrentUserCode()

    return {
      filter,
      headers,
      loginUserCode
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
      dataInfo: null as SourceModel | null,
      dataDeleteVisible: false,
      dataSyncMetadataVisible: false,
      dataHistoryVisible: false
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
      SourceService.getAll(this.filter)
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
    visibleInfo(opened: boolean, value: null | SourceModel)
    {
      this.dataInfoVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handlerInitialize()
      }
    },
    visibleDelete(opened: boolean, value: null | SourceModel)
    {
      this.dataDeleteVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handlerInitialize()
      }
    },
    visibleSyncMetadata(opened: boolean, value: null | SourceModel)
    {
      this.dataSyncMetadataVisible = opened
      this.dataInfo = value
    },
    visibleHistory(opened: boolean, value: null | SourceModel)
    {
      this.dataHistoryVisible = opened
      this.dataInfo = value
    }
  }
})
</script>
