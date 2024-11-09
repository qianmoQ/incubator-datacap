<template>
  <ShadcnRow :gutter="10">
    <ShadcnCol span="2">
      <ShadcnCard :loading="loading">
        <template #title>
          <div class="text-center">
            {{ $t('user.common.sourceCount') }}
          </div>
        </template>
        <div class="flex flex-col items-center justify-center py-8">
          <div class="text-2xl font-bold">
            {{ summary.sourceCount }}
          </div>
          <div class="text-xs text-muted-foreground mt-2">
            {{ $t('user.tip.sourceCountTip') }}
          </div>
        </div>
      </ShadcnCard>
    </ShadcnCol>

    <ShadcnCol span="2">
      <ShadcnCard :loading="loading">
        <template #title>
          <div class="text-center">
            {{ $t('user.common.queryCount') }}
          </div>
        </template>
        <div class="flex flex-col items-center justify-center py-8">
          <div class="text-2xl font-bold">
            {{ summary.queryCount }}
          </div>
          <div class="text-xs text-muted-foreground mt-2">
            {{ $t('user.tip.queryCountTip') }}
          </div>
        </div>
      </ShadcnCard>
    </ShadcnCol>
  </ShadcnRow>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import { HttpUtils } from '@/utils/http'
import UserService from '@/services/user'

export default defineComponent({
  name: 'DashboardHome',
  data()
  {
    return {
      loading: false,
      summary: {
        sourceCount: 0,
        queryCount: 0
      }
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
      const axios = new HttpUtils().getAxios()
      axios.all([UserService.getSourceCount(), UserService.getQueryCount()])
           .then(axios.spread((source, query) => {
             if (source.status) {
               this.summary.sourceCount = source.data
             }
             if (query.status) {
               this.summary.queryCount = query.data
             }
           }))
           .finally(() => this.loading = false)
    }
  }
})
</script>
