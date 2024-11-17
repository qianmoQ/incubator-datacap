<template>
  <ShadcnModal v-model="visible"
               width="60%"
               height="60%"
               :title="title"
               @on-close="onCancel">
    <div class="relative w-full h-full justify-center items-center">
      <div v-if="info">
        <VisualView class="h-full"
                    :code="info.dataset?.code"
                    :type="info.type"
                    :configuration="JSON.parse(info.configure as string)"
                    :query="info.type === 'DATASET' ? JSON.parse(info.query as string) : info.query"
                    :original="info?.source?.id"/>
      </div>
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
import { ReportModel } from '@/model/report'
import VisualView from '@/views/components/visual/VisualView.vue'

export default defineComponent({
  name: 'ReportView',
  components: { VisualView },
  props: {
    isVisible: {
      type: Boolean
    },
    info: {
      type: Object as () => ReportModel | null
    }
  },
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
  data()
  {
    return {
      title: null as string | null
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
        this.title = `${ this.$t('report.common.view').replace('$VALUE', String(this.info.name)) }`
      }
    },
    onCancel()
    {
      this.visible = false
    }
  }
})
</script>
