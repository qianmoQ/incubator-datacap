<template>
  <ShadcnCard>
    <template #title>
      <div class="ml-2 font-normal text-sm">{{ $t('template.common.list') }}</div>
    </template>

    <template #extra>
      <ShadcnButton size="small" circle @click="handlerInfo(true, null)">
        <template #icon>
          <ShadcnIcon icon="Plus"/>
        </template>
      </ShadcnButton>
    </template>

    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="headers" :data="data">
        <template #plugin="{ row }">
          <ShadcnAvatar v-for="item in row?.plugin.split(',')"
                        size="small"
                        :src="'/static/images/plugin/' + item + '.png'"
                        :alt="item"/>
        </template>

        <template #system="{ row }">
          <ShadcnSwitch v-model="row.system" size="small" :disabled="row.system"/>
        </template>

        <template #action="{ row }">
          <ShadcnSpace>
            <ShadcnTooltip :content="$t('common.editData')">
              <ShadcnButton size="small" circle @click="handlerInfo(true, row)">
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

  <TemplateInfo v-if="dataInfoVisible" :is-visible="dataInfoVisible" :info="dataInfo" @close="handlerInfo(false, null)"/>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter'
import { createHeaders } from '@/views/pages/system/template/TemplateUtils'
import { useI18n } from 'vue-i18n'
import TemplateService from '@/services/template'
import { TemplateModel } from '@/model/template'
import TemplateInfo from '@/views/pages/system/template/TemplateInfo.vue'

export default defineComponent({
  name: 'TemplateHome',
  components: { TemplateInfo },
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
      data: [] as TemplateModel[],
      pageIndex: 1,
      pageSize: 10,
      dataCount: 0,
      dataInfo: null as TemplateModel | null
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
      TemplateService.getAll(this.filter)
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
    handlerInfo(opened: boolean, value: null | TemplateModel)
    {
      this.dataInfoVisible = opened
      this.dataInfo = value
      if (!opened) {
        this.handlerInitialize()
      }
    }
  }
})
</script>
