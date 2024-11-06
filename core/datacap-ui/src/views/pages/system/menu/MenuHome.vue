<template>
  <ShadcnCard>
    <template #title>
      <div class="ml-2 font-normal text-sm">{{ $t('menu.common.list') }}</div>
    </template>

    <template #extra>
      <ShadcnTooltip :content="$t('menu.common.create')">
        <ShadcnButton size="small" circle @click="handlerChangeInfo(true, null)">
          <template #icon>
            <ShadcnIcon icon="Plus"/>
          </template>
        </ShadcnButton>
      </ShadcnTooltip>
    </template>

    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="headers" :data="data">
        <template #active="{ row }">
          <ShadcnSwitch v-model="row.active" :disabled="row.active"/>
        </template>

        <template #action="{ row }">
          <ShadcnSpace>
            <ShadcnTooltip :content="$t('common.editData')">
              <ShadcnButton size="small" circle @click="handlerChangeInfo(true, row)">
                <ShadcnIcon icon="Pencil" size="15"/>
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

  <MenuInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :info="dataInfo" @close="handlerChangeInfo(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter'
import { useI18n } from 'vue-i18n'
import { createHeaders } from '@/views/pages/system/menu/MenuUtils'
import { MenuModel } from '@/model/menu'
import MenuService from '@/services/menu'
import MenuInfo from '@/views/pages/system/menu/MenuInfo.vue'

export default defineComponent({
  name: 'MenuHome',
  components: { MenuInfo },
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
      dataInfoVisible: false,
      data: [],
      pageIndex: 1,
      pageSize: 10,
      dataCount: 0,
      dataInfo: null as MenuModel | null
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
      MenuService.getAll(this.filter)
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
      this.dataInfoVisible = isOpen
      this.dataInfo = dataInfo
      if (!isOpen) {
        this.handlerInitialize()
      }
    }
  }
})
</script>
