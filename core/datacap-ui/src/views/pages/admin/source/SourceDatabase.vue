<template>
  <div class="relative min-h-screen">
    <ShadcnSpin v-if="loading" fixed/>

    <div v-if="dataInfo">
      <ShadcnRow class="space-y-4">
        <ShadcnCol span="4">
          <div class="flex items-center space-x-2">
            <ShadcnIcon icon="Database"/>
            <span>{{ dataInfo.name }}</span>
          </div>
        </ShadcnCol>

        <ShadcnCol span="4">
          <ShadcnTooltip arrow :content="$t('common.createTime')">
            <div class="flex items-center space-x-2">
              <ShadcnIcon icon="Clock"/>
              <span>{{ dataInfo?.createTime === 'null' ? $t('source.common.notSpecified') : dataInfo.createTime }}</span>
            </div>
          </ShadcnTooltip>
        </ShadcnCol>

        <ShadcnCol span="4">
          <ShadcnTooltip arrow :content="$t('common.updateTime')">
            <div class="flex items-center space-x-2">
              <ShadcnIcon icon="Clock"/>
              <span>{{ dataInfo?.updateTime === 'null' ? $t('source.common.notUpdated') : dataInfo.updateTime }}</span>
            </div>
          </ShadcnTooltip>
        </ShadcnCol>
      </ShadcnRow>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, watch } from 'vue'
import DatabaseService from '@/services/database.ts'
import { DatabaseModel } from '@/model/database.ts'

export default defineComponent({
  name: 'SourceDatabase',
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
      dataInfo: null as DatabaseModel | null
    }
  },
  methods: {
    handleInitialize()
    {
      const code = String(this.$route?.params.database)
      console.log(code)
      if (code) {
        this.loading = true
        DatabaseService.getByCode(code)
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
          () => this.$route?.params.database,
          () => this.handleInitialize()
      )
    }
  }
})
</script>
