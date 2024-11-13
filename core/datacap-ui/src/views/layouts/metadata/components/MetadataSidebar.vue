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
          <div class="flex items-center space-x-1">
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
    </div>
  </ShadcnCard>

  <!--          <Tree :data="dataTreeArray" :empty-text="$t('source.tip.selectDatabase')" :load-data="handlerLoadChildData" @on-select-change="handlerSelectNode"-->
  <!--                @on-contextmenu="handlerContextMenu">-->
  <!--            <template #contextMenu>-->
  <!--              <DropdownMenu>-->
  <!--                <DropdownMenuTrigger as-child>-->
  <!--                  <span id="contextMenu"></span>-->
  <!--                </DropdownMenuTrigger>-->
  <!--                <DropdownMenuContent class="-mt-3">-->
  <!--                  <DropdownMenuLabel>{{ $t('common.action') }}</DropdownMenuLabel>-->
  <!--                  <DropdownMenuSeparator/>-->
  <!--                  <DropdownMenuGroup>-->
  <!--                    <DropdownMenuSub>-->
  <!--                      <DropdownMenuSubTrigger class="cursor-pointer">{{ $t('source.common.menuNew') }}</DropdownMenuSubTrigger>-->
  <!--                      <DropdownMenuPortal>-->
  <!--                        <DropdownMenuSubContent>-->
  <!--                          <DropdownMenuItem v-if="dataInfo?.level === StructureEnum.TABLE" class="cursor-pointer" @click="handlerCreateTable(true)">-->
  <!--                            <Table :size="18" class="mr-2"/>-->
  <!--                            {{ $t('source.common.menuNewTable') }}-->
  <!--                          </DropdownMenuItem>-->
  <!--                          <DropdownMenuItem class="cursor-pointer" @click="handlerCreateColumn(true)">-->
  <!--                            <Columns :size="18" class="mr-2"/>-->
  <!--                            {{ $t('source.common.newColumn') }}-->
  <!--                          </DropdownMenuItem>-->
  <!--                        </DropdownMenuSubContent>-->
  <!--                      </DropdownMenuPortal>-->
  <!--                    </DropdownMenuSub>-->
  <!--                  </DropdownMenuGroup>-->
  <!--                  <DropdownMenuGroup v-if="dataInfo?.level === StructureEnum.TABLE">-->
  <!--                    <DropdownMenuSub>-->
  <!--                      <DropdownMenuSubTrigger class="cursor-pointer">{{ $t('source.common.menuExport') }}</DropdownMenuSubTrigger>-->
  <!--                      <DropdownMenuPortal>-->
  <!--                        <DropdownMenuSubContent>-->
  <!--                          <DropdownMenuItem v-if="dataInfo?.level === StructureEnum.TABLE" class="cursor-pointer" @click="handlerExportData(true)">-->
  <!--                            <ArrowUpFromLine :size="18" class="mr-2"/>-->
  <!--                            {{ $t('source.common.exportData') }}-->
  <!--                          </DropdownMenuItem>-->
  <!--                        </DropdownMenuSubContent>-->
  <!--                      </DropdownMenuPortal>-->
  <!--                    </DropdownMenuSub>-->
  <!--                  </DropdownMenuGroup>-->
  <!--                  <DropdownMenuSeparator/>-->
  <!--                  <DropdownMenuItem v-if="dataInfo?.level === StructureEnum.TABLE" class="cursor-pointer" @click="handlerTruncateTable(true)">-->
  <!--                    <Trash :size="18" class="mr-2"/>-->
  <!--                    {{ $t('source.common.truncateTable') }}-->
  <!--                  </DropdownMenuItem>-->
  <!--                  <DropdownMenuItem v-if="dataInfo?.level === StructureEnum.TABLE" class="cursor-pointer" @click="handlerDropTable(true)">-->
  <!--                    <Delete :size="18" class="mr-2"/>-->
  <!--                    {{ $t('source.common.dropTable') }}-->
  <!--                  </DropdownMenuItem>-->
  <!--                  <DropdownMenuItem v-if="dataInfo?.level === StructureEnum.COLUMN" class="cursor-pointer" @click="handlerChangeColumn(true)">-->
  <!--                    <Pencil :size="18" class="mr-2"/>-->
  <!--                    {{ $t('source.common.changeColumn') }}-->
  <!--                  </DropdownMenuItem>-->
  <!--                  <DropdownMenuItem v-if="dataInfo?.level === StructureEnum.COLUMN" class="cursor-pointer" @click="handlerDropColumn(true)">-->
  <!--                    <Delete :size="18" class="mr-2"/>-->
  <!--                    {{ $t('source.common.dropColumn') }}-->
  <!--                  </DropdownMenuItem>-->
  <!--                </DropdownMenuContent>-->
  <!--              </DropdownMenu>-->
  <!--            </template>-->
  <!--          </Tree>-->
  <!--  <TableCreate v-if="tableCreateVisible" :isVisible="tableCreateVisible" :info="dataInfo" @close="handlerCreateTable(false)"/>-->
  <!--  <TableExport v-if="tableExportVisible" :isVisible="tableExportVisible" :info="dataInfo" @close="handlerExportData(false)"/>-->
  <!--  <TableTruncate v-if="tableTruncateVisible" :isVisible="tableTruncateVisible" :info="dataInfo" @close="handlerTruncateTable(false)"/>-->
  <!--  <TableDrop v-if="tableDropVisible" :isVisible="tableDropVisible" :info="dataInfo" @close="handlerDropTable(false)"/>-->
  <!--  <ColumnCreate v-if="columnCreateVisible" :isVisible="columnCreateVisible" :info="dataInfo" @close="handlerCreateColumn(false)"/>-->
  <!--  <ColumnChange v-if="columnChangeVisible" :isVisible="columnChangeVisible" :info="dataInfo" @close="handlerChangeColumn(false)"/>-->
  <!--  <ColumnDrop v-if="columnDropVisible" :isVisible="columnDropVisible" :info="dataInfo" @close="handlerDropColumn(false)"/>-->
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
      columnDropVisible: false
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
    handlerContextMenu(node: StructureModel)
    {
      console.log(node)
      this.dataInfo = node
      // Simulate right-click to trigger right-click menu
      const element = document.getElementById('contextMenu') as HTMLElement
      if (element) {
        element.click()
      }
    },
    handlerCreateTable(opened: boolean)
    {
      this.tableCreateVisible = opened
    },
    handlerCreateColumn(opened: boolean)
    {
      this.columnCreateVisible = opened
    },
    handlerExportData(opened: boolean)
    {
      this.tableExportVisible = opened
    },
    handlerTruncateTable(opened: boolean)
    {
      this.tableTruncateVisible = opened
    },
    handlerDropTable(opened: boolean)
    {
      this.tableDropVisible = opened
    },
    handlerChangeColumn(opened: boolean)
    {
      this.columnChangeVisible = opened
    },
    handlerDropColumn(opened: boolean)
    {
      this.columnDropVisible = opened
    },
    getColumnIcon(type: string)
    {
      if (type === 'PRI') {
        return 'key'
      }
      else if (type === 'MUL') {
        return 'repeat'
      }
      else if (type === 'UNI') {
        return 'circle'
      }
      else {
        return 'columns'
      }
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
