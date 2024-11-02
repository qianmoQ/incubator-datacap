<template>
  <ShadcnCard>
    <template #title>
      <div class="ml-2">{{ $t('source.common.list') }}</div>
    </template>

    <template #extra>
      <ShadcnButton circle size="small" @click="handlerInfo(true, null)">
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
                          :src="'/static/images/plugin/' + row.type + '.png'"
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
        </template>
      </ShadcnTable>

      <ShadcnPagination v-model="pageIndex"
                        :page-size="pageSize"
                        :total="total"
                        class="py-2"
                        @on-change="handlerChangePage"
                        @on-prev="handlerChangePage"
                        @on-next="handlerChangePage"/>
    </div>
  </ShadcnCard>

  <div class="w-full">
    <SourceInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :info="dataInfo" @close="handlerInfo(false, null)"/>
    <SourceDelete v-if="dataDeleteVisible" :is-visible="dataDeleteVisible" :info="dataInfo" @close="handlerDelete(false, null)"/>
    <SourceMetadata v-if="dataSyncMetadataVisible" :is-visible="dataSyncMetadataVisible" :info="dataInfo" @close="handlerSyncMetadata(false, null)"/>
    <SourceHistory v-if="dataHistoryVisible" :is-visible="dataHistoryVisible" :info="dataInfo" @close="handlerHistory(false, null)"/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useI18n } from 'vue-i18n'
import Common from '@/utils/common'
import { createHeaders } from '@/views/pages/admin/source/SourceUtils'
import { FilterModel } from '@/model/filter'
import { SourceModel } from '@/model/source'
import SourceService from '@/services/source'
import SourceInfo from '@/views/pages/admin/source/SourceInfo.vue'
import SourceDelete from '@/views/pages/admin/source/SourceDelete.vue'
import SourceMetadata from '@/views/pages/admin/source/SourceMetadata.vue'
import SourceHistory from '@/views/pages/admin/source/SourceHistory.vue'

export default defineComponent({
  name: 'SourceHome',
  components: {
    SourceHistory,
    SourceMetadata,
    SourceDelete,
    SourceInfo
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const headers = createHeaders(useI18n())
    const loginUserId = Common.getCurrentUserId()

    return {
      filter,
      headers,
      loginUserId
    }
  },
  data()
  {
    return {
      loading: false,
      data: [],
      pageIndex: 0,
      pageSize: 10,
      total: 0,
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
                       this.pageIndex = response.data.page + 1
                       this.pageSize = response.data.size
                       this.total = response.data.total
                     }
                   })
                   .finally(() => this.loading = false)
    },
    handlerChangePage(value: number)
    {
      console.log(value)
      this.filter.size = this.pageSize
      this.handlerInitialize()
    },
    handlerInfo(opened: boolean, value: null | SourceModel)
    {
      this.dataInfoVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handlerInitialize()
      }
    },
    handlerDelete(opened: boolean, value: null | SourceModel)
    {
      this.dataDeleteVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handlerInitialize()
      }
    },
    handlerSyncMetadata(opened: boolean, value: null | SourceModel)
    {
      this.dataSyncMetadataVisible = opened
      this.dataInfo = value
    },
    handlerHistory(opened: boolean, value: null | SourceModel)
    {
      this.dataHistoryVisible = opened
      this.dataInfo = value
    }
  }
})
</script>
