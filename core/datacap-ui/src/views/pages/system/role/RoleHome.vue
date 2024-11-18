<template>
  <ShadcnCard>
    <template #title>
      <div class="ml-2 font-normal text-sm">{{ $t('role.common.list') }}</div>
    </template>

    <template #extra>
      <ShadcnTooltip :content="$t('role.common.create')">
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
        <template #action="{ row }">
          <ShadcnSpace>
            <ShadcnTooltip :content="$t('common.editData')">
              <ShadcnButton size="small" circle @click="handlerChangeInfo(true, row)">
                <ShadcnIcon icon="Pencil" size="15"/>
              </ShadcnButton>
            </ShadcnTooltip>

            <ShadcnTooltip :content="$t('role.common.assignMenu').replace('$NAME', row?.name)">
              <ShadcnButton size="small" circle @click="handlerAssignMenu(true, row)">
                <ShadcnIcon icon="Menu" size="15"/>
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

  <RoleInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :info="dataInfo" @close="handlerChangeInfo(false, null)"/>
  <RoleMenu v-if="dataAllocationVisible" :is-visible="dataAllocationVisible" :info="dataInfo" @close="handlerAssignMenu(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter'
import { useHeaders } from '@/views/pages/system/role/RoleUtils'
import RoleService from '@/services/role'
import { RoleModel } from '@/model/role'
import RoleInfo from '@/views/pages/system/role/RoleInfo.vue'
import RoleMenu from '@/views/pages/system/role/RoleMenu.vue'

export default defineComponent({
  name: 'RoleHome',
  components: { RoleMenu, RoleInfo },
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
      dataInfoVisible: false,
      dataAllocationVisible: false,
      data: [],
      pageIndex: 1,
      pageSize: 10,
      dataCount: 0,
      dataInfo: null as RoleModel | null
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
      RoleService.getAll(this.filter)
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
    handlerChangeInfo(isOpen: boolean, dataInfo: any)
    {
      this.dataInfoVisible = isOpen
      this.dataInfo = dataInfo
      if (!isOpen) {
        this.handlerInitialize()
      }
    },
    handlerAssignMenu(opened: boolean, dataInfo: RoleModel | null)
    {
      this.dataAllocationVisible = opened
      this.dataInfo = dataInfo
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
    }
  }
})
</script>
