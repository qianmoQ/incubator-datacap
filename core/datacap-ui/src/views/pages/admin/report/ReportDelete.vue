<template>
  <ShadcnModal v-model="visible" :title="title" @on-close="onCancel">
    <ShadcnSpace wrap>
      <ShadcnAlert type="error" :title="$t('report.tip.deleteAlert1')" />
      <ShadcnAlert type="error" :title="$t('report.tip.deleteAlert2')" />
      <ShadcnAlert type="info" :title="$t('report.tip.deleteAlert3').replace('$VALUE', String(info?.name))" />
    </ShadcnSpace>

    <ShadcnForm v-model="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="name"
                      :rules="[
                            { required: true, message: $t('report.validator.name.required') },
                            { validator: validateMatch }
                      ]">
        <ShadcnInput v-model="formState.name" name="name" :placeholder="$t('report.placeholder.name')"/>
      </ShadcnFormItem>

      <div class="flex justify-end">
        <ShadcnSpace>
          <ShadcnButton type="default" @click="onCancel">
            {{ $t('common.cancel') }}
          </ShadcnButton>

          <ShadcnButton submit type="error" :loading="loading" :disabled="loading">
            {{ title }}
          </ShadcnButton>
        </ShadcnSpace>
      </div>
    </ShadcnForm>
  </ShadcnModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ReportService from '@/services/report'
import { ReportModel } from '@/model/report'

export default defineComponent({
  name: 'ReportDelete',
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
      type: Object as () => ReportModel | null
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
  created()
  {
    this.handleInitialize()
  },
  methods: {
    handleInitialize()
    {
      if (this.info) {
        this.title = `${ this.$t('report.common.deleteInfo').replace('$VALUE', String(this.info.name)) }`
      }
    },
    onSubmit()
    {
      if (this.info) {
        this.loading = true
        ReportService.deleteById(this.info.id as number)
                     .then((response) => {
                       if (response.status) {
                         this.$Message.success({
                           content: this.$t('report.tip.deleteSuccess').replace('$VALUE', String(this.info?.name)),
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
        return Promise.reject(new Error(this.$t('report.validator.name.match').replace('$VALUE', String(this.info?.name))))
      }
      return Promise.resolve(true)
    }
  }
})
</script>