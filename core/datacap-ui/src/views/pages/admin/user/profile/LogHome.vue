<template>
  <ShadcnCard :title="$t('user.common.log')"
              :description="$t('user.tip.log')"
              :border="false">
    <ShadcnDivider class="my-2"/>
    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnTable size="small" :columns="headers" :data="data">
        <template #state="{row}">
          <ShadcnBadge :text="row.state" :type="row.state === 'SUCCESS' ? 'success' : 'error'"/>
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
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { FilterModel } from '@/model/filter'
import { useI18n } from 'vue-i18n'
import { createHeaders } from './ProfileUtils'
import UserService from '@/services/user'
import Common from '@/utils/common'

export default defineComponent({
  name: 'LogHome',
  computed: {
    Common()
    {
      return Common
    }
  },
  setup()
  {
    const filter: FilterModel = new FilterModel()
    const headers = createHeaders(useI18n())

    return {
      filter,
      headers
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  data()
  {
    return {
      loading: false,
      pageIndex: 1,
      pageSize: 10,
      dataCount: 0,
      data: []
    }
  },
  methods: {
    handlerInitialize()
    {
      this.loading = true
      UserService.getLogs(this.filter)
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
    }
  }
})
</script>