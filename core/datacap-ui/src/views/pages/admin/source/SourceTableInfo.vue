<template>
  <div class="relative min-h-screen">
    <ShadcnSpin v-if="loading" fixed/>

    <div v-if="dataInfo">
      <ShadcnRow class="space-y-4">
        <ShadcnCol span="4">
          <div class="flex items-center space-x-2">
            <ShadcnIcon icon="Database"/>
            <span>{{ dataInfo.database?.name }}</span>
          </div>
        </ShadcnCol>

        <ShadcnCol span="4">
          <div class="flex items-center space-x-2">
            <ShadcnIcon icon="Table"/>
            <span>{{ dataInfo.name }}</span>
          </div>
        </ShadcnCol>

        <ShadcnCol span="4">
          <ShadcnTooltip arrow :content="$t('common.createTime')">
            <div class="flex items-center space-x-2">
              <ShadcnIcon icon="Clock"/>
              <span>{{ dataInfo?.inCreateTime === 'null' ? $t('source.common.notSpecified') : dataInfo.inCreateTime }}</span>
            </div>
          </ShadcnTooltip>
        </ShadcnCol>

        <ShadcnCol span="4">
          <ShadcnTooltip arrow :content="$t('common.updateTime')">
            <div class="flex items-center space-x-2">
              <ShadcnIcon icon="Clock"/>
              <span>{{ dataInfo?.inUpdateTime === 'null' ? $t('source.common.notUpdated') : dataInfo.inUpdateTime }}</span>
            </div>
          </ShadcnTooltip>
        </ShadcnCol>

        <ShadcnCol span="4">
          <ShadcnTooltip arrow :content="$t('source.common.engine')">
            <div class="flex items-center space-x-2">
              <ShadcnIcon icon="CalendarHeart"/>
              <span>{{ dataInfo.engine === 'null' ? $t('source.common.notSpecifiedEngine') : dataInfo.engine }}</span>
            </div>
          </ShadcnTooltip>
        </ShadcnCol>

        <ShadcnCol span="4">
          <ShadcnTooltip arrow :content="$t('source.common.collation')">
            <div class="flex items-center space-x-2">
              <ShadcnIcon icon="ArrowUpDown"/>
              <span>{{ dataInfo.collation === 'null' ? $t('source.common.notSpecifiedCollation') : dataInfo.collation }}</span>
            </div>
          </ShadcnTooltip>
        </ShadcnCol>

        <ShadcnCol span="4">
          <ShadcnTooltip arrow :content="$t('source.common.totalRows')">
            <div class="flex items-center space-x-2">
              <ShadcnIcon icon="TableCellsMerge"/>
              <span>{{ dataInfo.rows }}</span>
            </div>
          </ShadcnTooltip>
        </ShadcnCol>

        <ShadcnCol span="4">
          <ShadcnTooltip arrow :content="$t('source.common.format')">
            <div class="flex items-center space-x-2">
              <ShadcnIcon icon="RemoveFormatting"/>
              <span>{{ dataInfo.format === 'null' ? $t('source.common.notSpecifiedFormat') : dataInfo.format }}</span>
            </div>
          </ShadcnTooltip>
        </ShadcnCol>

        <ShadcnCol span="4">
          <ShadcnTooltip arrow :content="$t('source.common.avgRowLength')">
            <div class="flex items-center space-x-2">
              <ShadcnIcon icon="ArrowUp10"/>
              <span>{{ dataInfo.avgRowLength === 'null' ? 0 : dataInfo.avgRowLength }}</span>
            </div>
          </ShadcnTooltip>
        </ShadcnCol>

        <ShadcnCol span="4">
          <ShadcnTooltip arrow :content="$t('source.common.dataSize')">
            <div class="flex items-center space-x-2">
              <ShadcnIcon icon="ArrowUpDown"/>
              <span>{{ dataInfo.dataLength === 'null' ? 0 : dataInfo.dataLength }}</span>
            </div>
          </ShadcnTooltip>
        </ShadcnCol>

        <ShadcnCol span="4">
          <ShadcnTooltip arrow :content="$t('source.common.indexSize')">
            <div class="flex items-center space-x-2">
              <ShadcnIcon icon="Search"/>
              <span>{{ dataInfo.indexLength === 'null' ? $t('source.common.notSpecifiedIndex') : dataInfo.indexLength }}</span>
            </div>
          </ShadcnTooltip>
        </ShadcnCol>

        <ShadcnCol span="4">
          <div class="flex items-center space-x-4 justify-between">
            <ShadcnTooltip arrow :content="$t('source.common.autoIncrement')">
              <div class="flex items-center space-x-2">
                <ShadcnIcon icon="ArrowUpDown"/>
                <span>{{ dataInfo.autoIncrement === 'null' ? $t('source.common.notSpecifiedPrimaryKey') : dataInfo.autoIncrement }}</span>
              </div>
            </ShadcnTooltip>
            <div>
              <ShadcnTooltip arrow :content="$t('source.common.resetAutoIncrement')">
                <ShadcnButton circle size="small" :disabled="dataInfo.autoIncrement === 'null'" @click="autoIncrement = true">
                  <template #icon>
                    <ShadcnIcon icon="Cog" :size="16"/>
                  </template>
                </ShadcnButton>
              </ShadcnTooltip>
            </div>
          </div>
        </ShadcnCol>

        <ShadcnCol span="12">
          <ShadcnDivider class="mt-4 mb-4"/>

          <ShadcnText class="mb-2">{{ $t('source.common.comment') }}</ShadcnText>

          <ShadcnInput v-model="dataInfo.comment" type="textarea"/>
        </ShadcnCol>
      </ShadcnRow>
    </div>
  </div>

  <TableAutoIncrement v-if="autoIncrement"
                      :visible="autoIncrement"
                      :info="dataInfo"
                      @close="autoIncrement = false"/>
</template>
<script lang="ts">
import { defineComponent, watch } from 'vue'
import TableService from '@/services/table'
import { TableModel } from '@/model/table'
import TableAutoIncrement from '@/views/pages/admin/source/components/TableAutoIncrement.vue'

export default defineComponent({
  name: 'SourceTableInfo',
  components: { TableAutoIncrement },
  created()
  {
    this.handleInitialize()
    this.watchChange()
  },
  data()
  {
    return {
      loading: false,
      submitting: false,
      autoIncrement: false,
      dataInfo: null as TableModel | null
    }
  },
  methods: {
    handleInitialize()
    {
      const code = String(this.$route?.params.table)
      if (code) {
        this.loading = true
        TableService.getByCode(code)
                    .then(response => {
                      if (response.status) {
                        this.dataInfo = response.data
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
