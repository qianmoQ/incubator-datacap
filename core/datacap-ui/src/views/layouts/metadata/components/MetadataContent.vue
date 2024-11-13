<template>
  <ShadcnTab v-model="selectTab">
    <ShadcnTabItem value="info" :disabled="!originalTable" @on-click="onChange">
      <template #label>
        <div class="flex items-center space-x-1">
          <ShadcnIcon icon="Info"/>
          <span>{{ $t('source.common.info') }}</span>
        </div>
      </template>

      <RouterView/>
    </ShadcnTabItem>

    <ShadcnTabItem value="structure" :disabled="!originalTable" @on-click="onChange">
      <template #label>
        <div class="flex space-x-2">
          <ShadcnIcon icon="LayoutPanelTop"/>
          <span>{{ $t('source.common.structure') }}</span>
        </div>
      </template>
    </ShadcnTabItem>
  </ShadcnTab>

  <!--  <Tabs v-model="selectTab as string" :default-value="selectTab as string" class="w-full">-->
  <!--    <DataCapCard>-->
  <!--      <template #title>-->
  <!--        <TabsList class="rounded-none">-->
  <!--          <TabsTrigger value="data" class="cursor-pointer" :disabled="!originalTable" @click="handlerChange">-->
  <!--            <div class="flex space-x-2">-->
  <!--              <Table :size="18"/>-->
  <!--              <span>{{ $t('source.common.tableData') }}</span>-->
  <!--            </div>-->
  <!--          </TabsTrigger>-->
  <!--          <TabsTrigger value="statement" class="cursor-pointer" :disabled="!originalTable" @click="handlerChange">-->
  <!--            <div class="flex space-x-2">-->
  <!--              <SatelliteDish :size="18"/>-->
  <!--              <span>{{ $t('source.common.statement') }}</span>-->
  <!--            </div>-->
  <!--          </TabsTrigger>-->
  <!--          <TabsTrigger value="erDiagram" class="cursor-pointer" :disabled="!originalTable" @click="handlerChange">-->
  <!--            <div class="flex space-x-2">-->
  <!--              <Wind :size="18"/>-->
  <!--              <span>{{ $t('source.common.erDiagram') }}</span>-->
  <!--            </div>-->
  <!--          </TabsTrigger>-->
  <!--        </TabsList>-->
  <!--      </template>-->
  <!--      <template #content>-->
  <!--        <TabsContent :value="selectTab as string">-->
  <!--          <div class="h-[695px] overflow-x-auto overflow-y-auto">-->
  <!--            <RouterView/>-->
  <!--          </div>-->
  <!--        </TabsContent>-->
  <!--      </template>-->
  <!--    </DataCapCard>-->
  <!--  </Tabs>-->
</template>

<script lang="ts">
import { defineComponent, watch } from 'vue'

export default defineComponent({
  name: 'MetadataContent',
  data()
  {
    return {
      selectTab: null as string | null,
      originalSource: null as string | null,
      originalDatabase: null as string | null,
      originalTable: null as string | null
    }
  },
  created()
  {
    this.handleInitialize()
    this.watchChange()
  },
  methods: {
    handleInitialize()
    {
      const source = String(this.$route.params?.source)
      const database = String(this.$route.params?.database)
      const table = String(this.$route.params?.table)
      const type = String(this.$route.meta.type)
      this.originalSource = source
      this.originalDatabase = database
      this.originalTable = table
      this.selectTab = type
    },
    onChange()
    {
      this.$router.push(`/admin/source/${ this.originalSource }/d/${ this.originalDatabase }/t/${ this.selectTab }/${ this.originalTable }`)
    },
    watchChange()
    {
      watch(
          () => this.$route?.params.table,
          () => this.handleInitialize()
      )
    }
  }
})
</script>
