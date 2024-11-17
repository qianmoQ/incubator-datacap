<template>
  <ShadcnModal v-model="visible" :title="`[ ${ info?.name } ] ${ $t('dataset.common.clearData') }`" @on-close="onCancel">
    <ShadcnAlert :title="$t('dataset.tip.clearData')"/>

    <ShadcnRow class="mt-2.5" gutter="8">
      <ShadcnCol span="6">
        <ShadcnCard :title="$t('dataset.common.totalRows')">
          <div class="mt-3 flex items-center p-4 text-center">{{ info?.totalRows }}</div>
        </ShadcnCard>
      </ShadcnCol>

      <ShadcnCol span="6">
        <ShadcnCard :title="$t('dataset.common.totalSize')">
          <div class="mt-3 flex items-center p-4 text-center">{{ info?.totalSize }}</div>
        </ShadcnCard>
      </ShadcnCol>
    </ShadcnRow>

    <template #footer>
      <ShadcnSpace>
        <ShadcnButton type="default" @click="onCancel">{{ $t('common.cancel') }}</ShadcnButton>

        <ShadcnButton type="error"
                      :disabled="loading"
                      :loading="loading"
                      @click="onSubmit">
          {{ $t('dataset.common.clearData') }}
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
  name: 'DatasetClear',
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
        DatasetService.clearData(this.info.code)
                      .then((response: { status: boolean; }) => {
                        if (response.status) {
                          this.$Message.success({
                            content: `${ this.$t('dataset.common.clearData') } [ ${ this.info?.name } ] ${ this.$t('common.successfully') }`,
                            showIcon: true
                          })

                          this.onCancel()
                        }
                        else {
                          this.$Message.error({
                            content: `${ this.$t('dataset.common.clearData') } [ ${ this.info?.name } ] ${ this.$t('common.fail') }`,
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
