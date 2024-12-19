<template>
  <ShadcnModal v-model="visible"
               width="40%"
               :mask-closable="false"
               :title="$t('workflow.text.stop')"
               @on-close="onCancel">
    <ShadcnSpace wrap>
      <ShadcnAlert type="error" :title="$t('workflow.tip.stopAlert1')"/>
      <ShadcnAlert type="error" :title="$t('workflow.tip.stopAlert2')"/>
      <ShadcnAlert :title="$t('workflow.tip.stopAlert3').replace('$VALUE', String(info?.name))"/>
    </ShadcnSpace>

    <ShadcnForm v-model="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="name"
                      :rules="[
                            { required: true, message: $t('workflow.validator.name.required') },
                            { validator: validateMatch }
                      ]">
        <ShadcnInput v-model="formState.name" name="name" :placeholder="$t('workflow.placeholder.name')"/>
      </ShadcnFormItem>

      <div class="flex justify-end">
        <ShadcnSpace>
          <ShadcnButton type="default" @click="onCancel">
            {{ $t('common.cancel') }}
          </ShadcnButton>

          <ShadcnButton submit type="error" :disabled="loading" :loading="loading">
            {{ $t('workflow.text.stop') }}
          </ShadcnButton>
        </ShadcnSpace>
      </div>
    </ShadcnForm>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import WorkflowService from '@/services/workflow'
import { WorkflowModel } from '@/model/workflow'

export default defineComponent({
  name: 'WorkflowStop',
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
      loading: false,
      title: null as string | null,
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
        WorkflowService.stop(this.info.code!)
                       .then(response => {
                         if (response.status) {
                           this.$Message.success({
                             content: this.$t('workflow.tip.stopSuccess').replace('$VALUE', String(this.info?.name)),
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
        return Promise.reject(new Error(this.$t('workflow.validator.name.match').replace('$VALUE', String(this.info?.name))))
      }
      return Promise.resolve(true)
    }
  }
})
</script>
