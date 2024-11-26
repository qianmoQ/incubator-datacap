<template>
  <ShadcnModal v-model="visible" :title="`[ ${ info?.name } ] ${ $t('dataset.common.syncData') }`" @on-close="onCancel">
    <ShadcnAlert type="error" :title="$t('dataset.tip.syncData')"/>

    <template #footer>
      <ShadcnSpace>
        <ShadcnButton type="default" @click="onCancel">{{ $t('common.cancel') }}</ShadcnButton>

        <ShadcnButton :disabled="loading" :loading="loading" @click="onSubmit">
          {{ $t('dataset.common.syncData') }}
        </ShadcnButton>
      </ShadcnSpace>
    </template>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import DatasetService from '@/services/dataset'
import { DatasetModel } from '@/model/dataset'

export default defineComponent({
  name: 'DatasetSync',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
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
    onSubmit()
    {
      if (this.info) {
        this.loading = true
        DatasetService.syncData(this.info.code)
                      .then(response => {
                        if (response.status) {
                          this.$Message.success({
                            content: `${ this.$t('dataset.common.syncData') } [ ${ this.info?.name } ] ${ this.$t('common.successfully') }`,
                            showIcon: true
                          })

                          this.onCancel()
                        }
                        else {
                          this.$Message.error({
                            content: `${ this.$t('dataset.common.syncData') } [ ${ this.info?.name } ] ${ this.$t('common.fail') }`,
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