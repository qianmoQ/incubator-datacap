<template>
  <ShadcnCard :border="false">
    <ShadcnSelect v-model="selectDatabase" @on-change="onChangeDatabase">
      <template #options>
        <ShadcnSelectOption v-for="item in databaseArray" :label="item.title" :value="item.code"/>
      </template>
    </ShadcnSelect>

    <div class="relative h-screen overflow-x-auto overflow-y-auto">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTree v-model="databaseModel"
                  :data="dataTreeArray"
                  :loadData="onLoadData"
                  @on-node-click="onNodeClick">
        <template #label="{ node }">
          <div class="flex items-center space-x-1" @contextmenu.prevent="visibleContextMenu($event, node)">
            <ShadcnIcon class="text-xs font-semibold text-gray-500" size="16" :icon="node.level === 2 ? 'Table' : 'Columns'"/>
            <span class="text-xs font-normal text-gray-900">
              {{ node.title }}
            </span>
            <span v-if="node.level === 3" class="text-xs font-normal text-gray-500 ml-1">
              {{ getColumnTitle(String(node.type), String(node.extra), String(node.isKey), String(node.defaultValue)) }}
            </span>
          </div>
        </template>
      </ShadcnTree>

      <ShadcnContextMenu v-if="contextmenu.visible" v-model="contextmenu.visible" :position="contextmenu.position">
        <ShadcnContextMenuSub :label="$t('source.common.menuNew')">
          <ShadcnContextMenuItem v-if="dataInfo?.level === StructureEnum.TABLE" @click="visibleCreateTable(true)">
            <div class="flex items-center space-x-1">
              <ShadcnIcon icon="Table" size="15"/>
              <span>{{ $t('source.common.menuNewTable') }}</span>
            </div>
          </ShadcnContextMenuItem>

          <ShadcnContextMenuItem @click="visibleCreateColumn(true)">
            <div class="flex items-center space-x-1">
              <ShadcnIcon icon="Columns" size="15"/>
              <span>{{ $t('source.common.newColumn') }}</span>
            </div>
          </ShadcnContextMenuItem>
        </ShadcnContextMenuSub>

        <ShadcnContextMenuSub v-if="dataInfo?.level === StructureEnum.TABLE" :label="$t('source.common.menuExport')">
          <ShadcnContextMenuItem @click="visibleExportData(true)">
            <div class="flex items-center space-x-1">
              <ShadcnIcon icon="ArrowUpFromLine" size="15"/>
              <span>{{ $t('source.common.exportData') }}</span>
            </div>
          </ShadcnContextMenuItem>
        </ShadcnContextMenuSub>

        <ShadcnContextMenuItem v-if="dataInfo?.level === StructureEnum.TABLE" @click="visibleTruncateTable(true)">
          <div class="flex items-center space-x-1">
            <ShadcnIcon icon="Trash" size="15"/>
            <span>{{ $t('source.common.truncateTable') }}</span>
          </div>
        </ShadcnContextMenuItem>

        <ShadcnContextMenuItem v-if="dataInfo?.level === StructureEnum.TABLE" @click="visibleDropTable(true)">
          <div class="flex items-center space-x-1">
            <ShadcnIcon icon="Delete" size="15"/>
            <span>{{ $t('source.common.dropTable') }}</span>
          </div>
        </ShadcnContextMenuItem>

        <ShadcnContextMenuItem v-if="dataInfo?.level === StructureEnum.COLUMN" @click="visibleChangeColumn(true)">
          <div class="flex items-center space-x-1">
            <ShadcnIcon icon="Pencil" size="15"/>
            <span>{{ $t('source.common.changeColumn') }}</span>
          </div>
        </ShadcnContextMenuItem>

        <ShadcnContextMenuItem v-if="dataInfo?.level === StructureEnum.COLUMN" @click="visibleDropColumn(true)">
          <div class="flex items-center space-x-1">
            <ShadcnIcon icon="Delete" size="15"/>
            <span>{{ $t('source.common.dropColumn') }}</span>
          </div>
        </ShadcnContextMenuItem>
      </ShadcnContextMenu>
    </div>
  </ShadcnCard>

  <TableCreate v-if="tableCreateVisible"
               :is-visible="tableCreateVisible"
               :info="dataInfo as any"
               @close="visibleCreateTable(false)"/>

  <ColumnCreate v-if="columnCreateVisible"
                :is-visible="columnCreateVisible"
                :info="dataInfo as any"
                @close="visibleCreateColumn(false)"/>

  <TableExport v-if="tableExportVisible"
               :isVisible="tableExportVisible"
               :info="dataInfo as any"
               @close="visibleExportData(false)"/>

  <TableTruncate v-if="tableTruncateVisible"
                 :is-visible="tableTruncateVisible"
                 :info="dataInfo as any"
                 @close="visibleTruncateTable(false)"/>

  <TableDrop v-if="tableDropVisible"
             :is-visible="tableDropVisible"
             :info="dataInfo as any"
             @close="visibleDropTable(false)"/>

  <ColumnChange v-if="columnChangeVisible"
                :is-visible="columnChangeVisible"
                :info="dataInfo as any"
                @close="visibleChangeColumn(false)"/>

  <ColumnDrop v-if="columnDropVisible"
              :is-visible="columnDropVisible"
              :info="dataInfo as any"
              @close="visibleDropColumn(false)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import DatabaseService from '@/services/database.ts'
import { StructureEnum, StructureModel } from '@/model/structure.ts'
import TableService from '@/services/table.ts'
import ColumnService from '@/services/column.ts'
import ColumnCreate from '@/views/pages/admin/source/components/ColumnCreate.vue'
import ColumnDrop from '@/views/pages/admin/source/components/ColumnDrop.vue'
import TableExport from '@/views/pages/admin/source/components/TableExport.vue'
import ColumnChange from '@/views/pages/admin/source/components/ColumnChange.vue'
import TableTruncate from '@/views/pages/admin/source/components/TableTruncate.vue'
import TableDrop from '@/views/pages/admin/source/components/TableDrop.vue'
import TableCreate from '@/views/pages/admin/source/components/TableCreate.vue'

export default defineComponent({
  name: 'MetadataSidebar',
  computed: {
    StructureEnum()
    {
      return StructureEnum
    }
  },
  components: { TableCreate, TableDrop, TableTruncate, ColumnChange, TableExport, ColumnDrop, ColumnCreate },
  data()
  {
    return {
      loading: false,
      selectDatabase: undefined,
      databaseModel: '',
      originalSource: null as string | null,
      originalDatabase: null as string | null,
      originalTable: null as string | null,
      selectNode: null as StructureModel | null,
      databaseArray: Array<StructureModel>(),
      dataTreeArray: [] as any[],
      dataInfo: null as StructureModel | null,
      tableCreateVisible: false,
      tableExportVisible: false,
      tableTruncateVisible: false,
      tableDropVisible: false,
      columnCreateVisible: false,
      columnChangeVisible: false,
      columnDropVisible: false,
      contextmenu: {
        visible: false,
        position: { x: 0, y: 0 }
      }
    }
  },
  created()
  {
    this.handleInitialize()
  },
  methods: {
    handleInitialize()
    {
      const source = this.$route.params?.source as string
      const database = this.$route.params?.database as string
      if (source) {
        this.originalSource = source
        this.loading = true
        DatabaseService.getAllBySource(source)
                       .then(response => {
                         if (response.status) {
                           response.data
                                   .forEach((item: { name: null; catalog: null; code: undefined }) => {
                                     const structure: StructureModel = {
                                       title: item.name,
                                       catalog: item.catalog,
                                       code: item.code
                                     }
                                     this.databaseArray.push(structure)
                                   })
                           if (database) {
                             this.originalDatabase = database
                             this.selectDatabase = database as any
                             this.onChangeDatabase()
                           }
                         }
                         else {
                           this.$Message.error({
                             content: response.message,
                             showIcon: true
                           })
                         }
                       })
                       .finally(() => this.loading = false)
      }
    },
    onChangeDatabase()
    {
      this.loading = true
      this.dataTreeArray = []
      TableService.getAllByDatabase(this.selectDatabase as any)
                  .then(response => {
                    if (response.status) {
                      response.data.forEach((item: any) => {
                        const structure = {
                          title: item.name, database: item.database.name, databaseId: item.database.id, catalog: item.catalog, value: item.code, type: item.type,
                          level: StructureEnum.TABLE, engine: item.engine, comment: item.comment, origin: item, contextmenu: true, children: [], isLeaf: false
                        }
                        this.dataTreeArray.push(structure)
                      })
                    }
                    else {
                      this.$Message.error({
                        content: response.message,
                        showIcon: true
                      })
                    }
                  })
                  .finally(() => {
                    this.loading = false
                    const table = String(this.$route.params?.table)
                    if (table) {
                      const node = this.dataTreeArray.find(item => item.code === table)
                      if (node) {
                        node.selected = true
                        this.handlerSelectNode([node])
                      }
                    }
                    else {
                      this.$router.push(`/admin/source/${ this.originalSource }/d/${ this.selectDatabase }`)
                    }
                  })
    },
    onNodeClick(node: any)
    {
      const type = this.$route.meta.type
      this.$router.push(`/admin/source/${ this.originalSource }/d/${ this.selectDatabase }/t/${ type ? type : 'info' }/${ node.value }`)
    },
    onLoadData(item: StructureModel, callback: any)
    {
      const dataChildArray = [] as StructureModel[]
      if (item.level === StructureEnum.COLUMN) {
        callback(dataChildArray)
        return
      }
      ColumnService.getAllByTable(String(item.value))
                   .then(response => {
                     if (response.status) {
                       response.data.forEach((item: any) => {
                         dataChildArray.push({
                           title: item.name, database: item.table.database.name, databaseId: item.table.database.id, table: item.table.name,
                           tableId: item.table.code, catalog: item.catalog, value: item.code, level: StructureEnum.COLUMN, type: item.type, extra: item.extra,
                           dataType: item.dataType, engine: item.engine, isKey: item.isKey, defaultValue: item.defaultValue, children: [], isLeaf: false
                         })
                       })
                     }
                   })
                   .finally(() => callback(dataChildArray))
    },
    visibleCreateTable(opened: boolean)
    {
      this.tableCreateVisible = opened
    },
    visibleCreateColumn(opened: boolean)
    {
      this.columnCreateVisible = opened
    },
    visibleExportData(opened: boolean)
    {
      this.tableExportVisible = opened
    },
    visibleTruncateTable(opened: boolean)
    {
      this.tableTruncateVisible = opened
    },
    visibleDropTable(opened: boolean)
    {
      this.tableDropVisible = opened
    },
    visibleChangeColumn(opened: boolean)
    {
      this.columnChangeVisible = opened
    },
    visibleDropColumn(opened: boolean)
    {
      this.columnDropVisible = opened
    },
    visibleContextMenu(event: any, node: any)
    {
      this.contextmenu.position = {
        x: event.clientX,
        y: event.clientY
      }
      this.dataInfo = node
      this.contextmenu.visible = true
    },
    getColumnTitle(dataType: string, extra: string, isKey: string, defaultValue: string)
    {
      let title = dataType
      if (isKey === 'PRI') {
        if (extra) {
          title = `${ title }\u00A0(${ extra.replace('_', '\u00A0') })`
        }
        else {
          title = `${ title }`
        }
      }
      if (defaultValue && defaultValue !== 'null') {
        title = `${ title }\u00A0=\u00A0${ defaultValue }`
      }
      return title
    }
  }
})
</script>
