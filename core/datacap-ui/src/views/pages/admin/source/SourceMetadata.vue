<template>
  <ShadcnModal v-model="visible" :title="$t('source.common.syncMetadata')" @on-close="onCancel">
    <ShadcnSpace wrap>
      <ShadcnAlert type="error" :title="$t('source.tip.syncMetadata1')"/>
      <ShadcnAlert type="error" :title="$t('source.tip.syncMetadata2')"/>
      <ShadcnAlert :title="$t('source.tip.syncMetadata3').replace('$NAME', String(info?.name))"/>
    </ShadcnSpace>

    <ShadcnForm v-model="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="name"
                      :rules="[
                            { required: true, message: $t('source.validator.name.required') },
                            { validator: validateMatch }
                      ]">
        <ShadcnInput v-model="formState.name" name="name" :placeholder="$t('source.placeholder.name')"/>
      </ShadcnFormItem>

      <div class="flex justify-end">
        <ShadcnSpace>
          <ShadcnButton type="default" @click="onCancel">
            {{ $t('common.cancel') }}
          </ShadcnButton>

          <ShadcnButton submit type="error" :loading="loading">
            {{ $t('source.common.syncMetadata') }}
          </ShadcnButton>
        </ShadcnSpace>
      </div>
    </ShadcnForm>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { SourceModel } from '@/model/source'
import SourceService from '@/services/source'

export default defineComponent({
  name: 'SourceMetadata',
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
      type: Object as () => SourceModel | null
    }
  },
  data()
  {
    return {
      title: null as string | null,
      loading: false,
      formState: {
        name: ''
      }
    }
  },
  methods: {
    onSubmit()
    {
      if (this.info) {
        this.loading = true
        SourceService.syncMetadata(Number(this.info.id))
                     .then((response) => {
                       if (response.status) {
                         this.$Message.success({
                           content: this.$t('source.tip.syncMetadata4').replace('$NAME', String(this.info?.name)),
                           showIcon: true
                         })
                         this.onCancel()
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
    },
    validateMatch(value: string)
    {
      if (value !== String(this.info?.name)) {
        return Promise.reject(new Error(this.$t('source.validator.name.match').replace('$VALUE', String(this.info?.name))))
      }
      return Promise.resolve(true)
    }
  }
})
</script>
