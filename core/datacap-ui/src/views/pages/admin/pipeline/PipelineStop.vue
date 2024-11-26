<template>
  <ShadcnModal v-model="visible"
               width="40%"
               :title="title"
               @on-close="onCancel">
    <ShadcnSpace wrap>
      <ShadcnAlert type="error" :title="$t('pipeline.tip.stopAlert1')"/>
      <ShadcnAlert type="error" :title="$t('pipeline.tip.stopAlert2')"/>
      <ShadcnAlert :title="$t('pipeline.tip.stopAlert3').replace('$VALUE', String(info?.name))"/>
    </ShadcnSpace>

    <ShadcnForm v-model="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="name"
                      :rules="[
                            { required: true, message: $t('pipeline.validator.name.required') },
                            { validator: validateMatch }
                      ]">
        <ShadcnInput v-model="formState.name" name="name" :placeholder="$t('pipeline.placeholder.name')"/>
      </ShadcnFormItem>

      <div class="flex justify-end">
        <ShadcnSpace>
          <ShadcnButton type="default" @click="onCancel">
            {{ $t('common.cancel') }}
          </ShadcnButton>

          <ShadcnButton submit type="error" :disabled="loading" :loading="loading">
            {{ $t('pipeline.common.stop') }}
          </ShadcnButton>
        </ShadcnSpace>
      </div>
    </ShadcnForm>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import PipelineService from '@/services/pipeline'
import { PipelineModel } from '@/model/pipeline.ts'

export default defineComponent({
  name: 'PipelineStop',
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
      loading: false,
      title: null as string | null,
      formState: {
        name: ''
      }
    }
  },
  created()
  {
    if (this.info) {
      this.title = this.$t('pipeline.common.stopInfo').replace('$VALUE', String(this.info.name))
    }
  },
  methods: {
    onSubmit()
    {
      if (this.info) {
        this.loading = true
        PipelineService.stop(this.info.code!)
                       .then(response => {
                         if (response.status) {
                           this.$Message.success({
                             content: this.$t('pipeline.tip.stopSuccess').replace('$VALUE', String(this.info?.name)),
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
        return Promise.reject(new Error(this.$t('pipeline.validator.name.match').replace('$VALUE', String(this.info?.name))))
      }
      return Promise.resolve(true)
    }
  }
})
</script>
