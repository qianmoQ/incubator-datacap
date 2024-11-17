<template>
  <ShadcnModal v-model="visible" :title="$t('dataset.common.rebuild') + ' [ ' + data?.name + ' ]'" @on-close="onCancel">
    <ShadcnAlert :title="$t('dataset.tip.rebuildProgress')"/>

    <template #footer>
      <ShadcnButton :disabled="loading" :loading="loading" @click="onRebuild">
        {{ $t('dataset.common.rebuild') }}
      </ShadcnButton>
    </template>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import DatasetService from '@/services/dataset'
import { DatasetModel } from '@/model/dataset'

export default defineComponent({
  name: 'DatasetRebuild',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: Object as () => DatasetModel | null
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
      loading: false
    }
  },
  methods: {
    onRebuild()
    {
      if (this.data) {
        this.loading = true
        DatasetService.rebuild(this.data.id)
                      .then((response) => {
                        if (response.status) {
                          this.$Message.success({
                            content: `${ this.$t('dataset.common.rebuild') } [ ${ this.data?.name } ] ${ this.$t('common.successfully') }`,
                            showIcon: true
                          })

                          this.onCancel()
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
