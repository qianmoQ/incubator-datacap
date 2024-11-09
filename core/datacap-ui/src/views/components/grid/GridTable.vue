<template>
  <ShadcnCard>
    <template #title>
      <div class="ml-2 font-normal text-sm">{{ $t('role.common.list') }}</div>
    </template>

  </ShadcnCard>


  <div>
    <Card class="p-0" style="border-radius: 0;">
      <CardHeader class="p-0">
        <CardTitle class="pt-1 pl-3 space-x-2">
          <Button size="sm" class="h-7 space-x-1">
            <RouterLink :to="`/admin/dataset/info/source/${configure.code}`" target="_blank">
              <span class="flex items-center">
                <Plus :size="18"/> {{ $t('common.dataset') }}
              </span>
            </RouterLink>
          </Button>
          <Button size="sm" variant="outline" class="h-7 space-x-1" @click="visualVisible = true">
            <BarChart :size="18"/>
            <span>{{ $t('dataset.common.visual') }}</span>
          </Button>
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger>
                <Switch :default-checked="isPage" @update:checked="handlerChange"/>
              </TooltipTrigger>
              <TooltipContent>{{ $t('query.tip.pageShow') }}</TooltipContent>
            </Tooltip>
          </TooltipProvider>
          <HoverCard>
            <HoverCardTrigger as-child>
              <Button size="icon" class="h-7 w-7" variant="outline">
                <CircleHelp :size="18"/>
              </Button>
            </HoverCardTrigger>
            <HoverCardContent class="w-80">{{ $t('query.tip.smallTips') }}</HoverCardContent>
          </HoverCard>
        </CardTitle>
        <CardContent class="p-0" style="margin-top: -5px;">
          <ag-grid-vue v-if="type === 'table'" :key="timestamp" :style="{width: configure.width + 'px', height: configure.height + 'px', 'margin-top': '2px'}" :pagination="isPage"
                       class="ag-theme-datacap" :columnDefs="columnDefs" :rowData="configure.columns" :gridOptions="gridOptions as any"/>
        </CardContent>
      </CardHeader>
    </Card>
  </div>
  <GridVisual :is-visible="visualVisible" :configure="configure" @close="visualVisible = $event"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { AgGridVue } from 'ag-grid-vue3'
import 'ag-grid-community/styles/ag-grid.css'
import './ag-theme-datacap.css'
import { useI18n } from 'vue-i18n'
import { mapActions } from 'vuex'
import { GridConfigure } from '@/views/components/grid/GridConfigure'
import GridOptions from '@/views/components/grid/GridOptions'
import { GridColumn } from '@/views/components/grid/GridColumn'
import { ObjectUtils } from '@/utils/object'
import GridVisual from '@/views/components/grid/GridVisual.vue'

export default defineComponent({
  name: 'GridTable',
  components: { GridVisual, AgGridVue },
  props: {
    configure: {
      type: Object as () => GridConfigure,
      default: () => null
    }
  },
  setup()
  {
    const gridOptions = GridOptions.createDefaultOptions(useI18n())
    const timestamp = ObjectUtils.getTimestamp()

    return {
      gridOptions,
      timestamp
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  data()
  {
    return {
      columnDrawerVisible: false,
      visibleColumns: [],
      columnDefs: [] as GridColumn[],
      isPage: true,
      type: 'table',
      visualVisible: false
    }
  },
  methods: {
    ...mapActions(['updateData']),
    handlerInitialize()
    {
      if (this.configure) {
        this.updateData(this.configure)
        this.configure.headers!.forEach((header: string) => {
          const columnDef: GridColumn = { headerName: header, field: header }
          this.columnDefs.push(columnDef)
        })
      }
    },
    handlerApplyColumn()
    {
      this.columnDefs = this.visibleColumns
      this.columnDrawerVisible = false
    },
    handlerChange(value: boolean)
    {
      this.timestamp = ObjectUtils.getTimestamp()
      this.isPage = value
    }
  }
})
</script>
