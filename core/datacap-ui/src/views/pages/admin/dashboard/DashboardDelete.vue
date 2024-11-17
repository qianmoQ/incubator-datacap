<template>
  <ShadcnModal v-model="visible" :title=" $t('dashboard.common.delete') + ' [ ' + data?.name + ' ]'" @on-close="onCancel">
    <ShadcnSpace wrap>
      <ShadcnAlert type="error" :title="$t('dashboard.tip.deleteTip1')"/>

      <ShadcnAlert type="error" :title="$t('dashboard.tip.deleteTip2')"/>

      <ShadcnAlert type="error" :title="$t('dashboard.tip.deleteTip3').replace('$NAME', data?.name as string)"/>
    </ShadcnSpace>

    <ShadcnForm v-model="formState" @on-error="console.log($event)" @on-submit="onSubmit">
      <ShadcnFormItem name="name"
                      :rules="[
                            { required: true, message: $t('dashboard.validator.name.required') },
                            { validator: validateMatch }
                      ]">
        <ShadcnInput v-model="formState.name" name="name" :placeholder="$t('dashboard.placeholder.name')"/>
      </ShadcnFormItem>

      <div class="flex justify-end">
        <ShadcnSpace>
          <ShadcnButton type="default" @click="onCancel">
            {{ $t('common.cancel') }}
          </ShadcnButton>

          <ShadcnButton submit type="error" :loading="loading">
            {{ $t('dashboard.common.delete') }}
          </ShadcnButton>
        </ShadcnSpace>
      </div>
    </ShadcnForm>
  </ShadcnModal>
</template>
<script lang="ts">
import { defineComponent } from 'vue'
import DashboardService from '@/services/dashboard'
import { DashboardModel } from '@/model/dashboard'

export default defineComponent({
  name: 'DashboardDelete',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    data: {
      type: Object as () => DashboardModel | null,
      default: null
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
      loading: false,
      formState: {
        name: ''
      }
    }
  },
  methods: {
    onSubmit()
    {
      if (this.data) {
        this.loading = true
        DashboardService.deleteById(Number(this.data.id))
                        .then((response) => {
                          if (response.status) {
                            this.$Message.success({
                              content: `${ this.$t('dashboard.common.delete') } [ ${ this.data?.name } ] ${ this.$t('common.successfully') }`,
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
    },
    validateMatch(value: string)
    {
      if (value !== String(this.data?.name)) {
        return Promise.reject(new Error(this.$t('dashboard.validator.name.match').replace('$VALUE', String(this.data?.name))))
      }
      return Promise.resolve(true)
    }
  }
})
</script>
