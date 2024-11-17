<template>
  <ShadcnCard :title="$t('dashboard.common.list')">
    <template #extra>
      <ShadcnTooltip :content="$t('dashboard.common.create')">
        <ShadcnLink link="/admin/dashboard/info">
          <ShadcnButton circle size="small">
            <ShadcnIcon icon="Plus" size="15"/>
          </ShadcnButton>
        </ShadcnLink>
      </ShadcnTooltip>
    </template>

    <template #content>
      <div class="mb-3 min-h-screen">
        <Loader2 v-if="loading" class="w-full justify-center animate-spin"/>
        <div v-else class="hidden flex-col md:flex">
          <div class="flex-1 space-y-4 pt-6">
            <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-6">
              <DataCapCard v-for="item in data">
                <template #title>
                  <div class="flex space-x-1">
                    <div>
                      <RouterLink :to="`/admin/dashboard/preview/${item.code}`" target="_blank">{{ item.name }}</RouterLink>
                    </div>
                    <div>
                      <Tooltip :content="item.description">
                        <Info :size="18" class="cursor-pointer"/>
                      </Tooltip>
                    </div>
                  </div>
                </template>
                <template #extra>
                  <DropdownMenu class="justify-items-end">
                    <DropdownMenuTrigger>
                      <Cog :size="20"/>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent>
                      <DropdownMenuItem class="cursor-pointer">
                        <RouterLink :to="`/admin/dashboard/info/${item.code}`" target="_blank" class="flex items-center">
                          <Pencil :size="15" class="mr-1"/>
                          {{ $t('dashboard.common.modify') }}
                        </RouterLink>
                      </DropdownMenuItem>
                      <DropdownMenuItem class="cursor-pointer" @click="handlerDelete(true, item)">
                        <Trash :size="15" class="mr-1"/>
                        {{ $t('dashboard.common.delete') }}
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </template>
                <div class="shadow-blackA7 w-full overflow-hidden rounded-md">
                  <AspectRatio :ratio="16 / 11">
                    <img class="h-full w-full object-cover" :src="`${item.avatar?.path ? item.avatar.path : '/static/images/dashboard.png'}`" :alt="item.name"/>
                  </AspectRatio>
                </div>
                <template #footer>
                  <p class="text-xs text-muted-foreground text-right">{{ item.createTime }}</p>
                </template>
              </DataCapCard>
            </div>
            <div v-if="data.length === 0" class="text-center">
              {{ $t('common.noData') }}
            </div>
            <div>
              <Pagination v-if="pagination && !loading && data.length > 0" :pagination="pagination" @changePage="handlerChangePage"/>
            </div>
          </div>
        </div>
      </div>
    </template>
  </ShadcnCard>
  <!--    <DashboardDelete v-if="deleteVisible" :is-visible="deleteVisible" :data="dataInfo" @close="handlerDelete(false, null)"></DashboardDelete>-->
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import DashboardService from '@/services/dashboard'
import { FilterModel } from '@/model/filter'
import { PaginationModel, PaginationRequest } from '@/model/pagination'
import { DashboardModel } from '@/model/dashboard'

export default defineComponent({
  name: 'DashboardHome',
  setup()
  {
    const filter: FilterModel = new FilterModel()
    filter.size = 30

    return {
      filter
    }
  },
  data()
  {
    return {
      loading: false,
      deleteVisible: false,
      data: [] as DashboardModel[],
      pagination: {} as PaginationModel,
      dataInfo: null as DashboardModel | null
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
      DashboardService.getAll(this.filter)
                      .then(response => {
                        if (response.status) {
                          this.data = response.data.content
                          this.pagination = PaginationRequest.of(response.data)
                        }
                      })
                      .finally(() => this.loading = false)
    },
    handlerDelete(opened: boolean, data: DashboardModel | null)
    {
      this.deleteVisible = opened
      this.dataInfo = data
      if (!opened) {
        this.handlerInitialize()
      }
    },
    handlerChangePage(value: PaginationModel)
    {
      this.filter.page = value.currentPage
      this.filter.size = value.pageSize
      this.handlerInitialize()
    }
  }
})
</script>
