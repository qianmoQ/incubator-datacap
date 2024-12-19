<template>
  <ShadcnModal v-model="visible"
               width="60%"
               :title="$t('workflow.text.configure')"
               @on-close="onCancel">
    <ShadcnSpin v-if="loading" fixed/>

    <ShadcnWorkflowView v-if="!loading && configuration"
                        :nodes="configuration.nodes"
                        :canvas="{ height: 500 }"
                        :data="data"/>

    <template #footer>
      <ShadcnButton type="default" @click="onCancel">
        {{ $t('common.cancel') }}
      </ShadcnButton>
    </template>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ConfigurationService from '@/services/configure.ts'
import WorkflowService from '@/services/workflow.ts'
import HttpUtils from '@/utils/http.ts'

export interface Configuration
{
  categories: any[]
  nodes: any[]
}

export default defineComponent({
  name: 'WorkflowFlow',
  computed: {
    visible: {
      get(): boolean
      {
        return this.isVisible
      },
      set(value: boolean)
      {
        this.$emit('close', value)
      }
    }
  },
  props: {
    isVisible: {
      type: Boolean
    },
    code: {
      type: String
    }
  },
  data()
  {
    return {
      loading: false,
      configuration: null as Configuration | null,
      data: null
    }
  },
  created()
  {
    HttpUtils.all([ConfigurationService.getExecutor(), WorkflowService.getByCode(this.code)])
             .then(HttpUtils.spread((...responses) => {
               const [executor, workflow] = responses

               if (executor.status && executor.data) {
                 this.configuration = executor.data
               }

               if (workflow.status && workflow.data) {
                 this.data = workflow.data.configure
               }
               else {
                 this.$Message.error({ content: workflow.message, showIcon: true })
               }
             }))
             .catch(error => {
               this.$Message.error({
                 content: error.message || 'Failed to initialize workflow',
                 showIcon: true
               })
             })
             .finally(() => {
               this.loading = false
             })
  },
  methods: {
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>
