<template>
  <ShadcnModal v-model="visible"
               width="60%"
               height="60%"
               :title="$t('workflow.text.logger')"
               @on-close="onCancel">
    <ShadcnSpin v-model="loading" fixed/>

    <ShadcnLogger v-if="!loading"
                  height="380"
                  toolbar
                  :items="logs"
                  :custom-patterns="customPatterns"/>

    <template #footer>
      <ShadcnButton type="default" @click="onCancel">
        {{ $t('common.cancel') }}
      </ShadcnButton>
    </template>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { WorkflowModel } from '@/model/workflow'
import WorkflowService from '@/services/workflow'

export default defineComponent({
  name: 'WorkflowLogger',
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
    info: {
      type: Object as () => WorkflowModel | null
    }
  },
  data()
  {
    return {
      title: null as string | null,
      loading: false,
      logs: Array<string>(),
      customPatterns: {
        timestamp: [/^(\d{4}-\d{2}-\d{2}\s+\d{2}:\d{2}:\d{2},\d{3})/],
        level: [/\b(INFO|ERROR|WARN|DEBUG)\b/],
        thread: [/\[(pool-\d+-thread-\d+)\]/],
        file: [/\[([^[\]]+\.java:\d+)\]/]
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
      if (this.info) {
        this.loading = true
        WorkflowService.getLogger(this.info.code)
                       .then(response => {
                         if (response.status) {
                           this.logs = response.data
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
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>
