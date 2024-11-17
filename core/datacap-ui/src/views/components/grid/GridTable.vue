<template>
  <ShadcnCard>
    <template #title>
      <ShadcnSpace>
        <ShadcnButton>
          <RouterLink :to="`/admin/dataset/info/source/${configure.code}`" target="_blank">
              <span class="flex items-center">
                <ShadcnIcon icon="Plus" size="15"/>
                <span>{{ $t('common.dataset') }}</span>
              </span>
          </RouterLink>
        </ShadcnButton>

        <ShadcnButton type="default" @click="visualVisible = true">
          <ShadcnIcon icon="BarChart" :size="15"/>
          <span>{{ $t('dataset.common.visual') }}</span>
        </ShadcnButton>

        <ShadcnTooltip class="mt-1" :content="$t('query.tip.pageShow')">
          <ShadcnSwitch v-model="isPage" @on-change="onChange"/>
        </ShadcnTooltip>

        <ShadcnTooltip :content="$t('query.tip.smallTips')">
          <ShadcnButton circle type="default">
            <ShadcnIcon icon="CircleHelp" :size="15"/>
          </ShadcnButton>
        </ShadcnTooltip>
      </ShadcnSpace>
    </template>

    <ag-grid-vue v-if="type === 'table'"
                 class="ag-theme-datacap"
                 :key="timestamp"
                 :style="{width: configure.width + 'px', height: configure.height + 'px', 'margin-top': '2px'}"
                 :pagination="isPage"
                 :columnDefs="columnDefs"
                 :rowData="configure.columns"
                 :gridOptions="gridOptions as any"/>
  </ShadcnCard>

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
    this.handleInitialize()
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
    handleInitialize()
    {
      if (this.configure) {
        this.updateData(this.configure)
        this.configure.headers!.forEach((header: string) => {
          const columnDef: GridColumn = { headerName: header, field: header }
          this.columnDefs.push(columnDef)
        })
      }
    },
    onChange(value: boolean)
    {
      this.timestamp = ObjectUtils.getTimestamp()
      this.isPage = value
    }
  }
})
</script>
