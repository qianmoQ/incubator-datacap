<template>
  <ShadcnModal v-model="visible"
               width="60%"
               height="60%"
               :title="title"
               @on-close="onCancel">
    <ShadcnSpin v-model="loading"/>

    <div v-for="(log, index) in logs" :key="index" style="white-space: nowrap;">
      <div v-html="log" style="margin-bottom: 5px; font-size: 16px"/>
    </div>

    <template #footer>
      <ShadcnButton type="default" @click="onCancel">
        {{ $t('common.cancel') }}
      </ShadcnButton>
    </template>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { PipelineModel } from '@/model/pipeline.ts'
import PipelineService from '@/services/pipeline'
import { AnsiUp } from 'ansi_up'

export default defineComponent({
  name: 'PipelineLogger',
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
      type: Object as () => PipelineModel | null
    }
  },
  data()
  {
    return {
      title: null as string | null,
      loading: false,
      logs: Array<string>()
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
        this.title = `${ this.$t('pipeline.common.loggerInfo').replace('$VALUE', String(this.info.name)) }`
        this.loading = true
        PipelineService.getLogger(this.info.code)
                       .then(response => {
                         if (response.status) {
                           const ansi = new AnsiUp()
                           const array = response.data
                           for (const i in array) {
                             this.logs.push(ansi.ansi_to_html(array[i]))
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
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>
