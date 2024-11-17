<template>
  <ShadcnDrawer v-model="visible" :title="title" width="40%">
    <ShadcnSpin v-if="loading" fixed/>

    <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="name"
                      :label="$t('common.name')"
                      :rules="[
                          { required: true, message: $t('common.name') }
                      ]">
        <ShadcnInput v-model="formState.name" name="name"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="plugin"
                      :label="$t('common.plugin')"
                      :rules="[
                          { required: true, message: $t('common.plugin') }
                      ]">
        <ShadcnSelect v-model="formState.plugin" :options="plugins" multiple name="plugin"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="content"
                      :label="$t('common.content')"
                      :rules="[
                          { required: true, message: $t('common.content') }
                      ]">
        <ShadcnInput v-model="formState.content" type="textarea" name="content"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="description"
                      :label="$t('common.description')"
                      :rules="[
                          { required: true, message: $t('common.description') }
                      ]">
        <ShadcnInput v-model="formState.description" type="textarea" name="description"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="type"
                      :label="$t('common.type')"
                      :rules="[
                          { required: true, message: $t('common.type') }
                      ]">
        <ShadcnSelect v-model="formState.type"
                      name="type"
                      :placeholder="$t('function.tip.selectTypeHolder')"
                      :options="types"/>
      </ShadcnFormItem>

      <div class="flex justify-end">
        <ShadcnButton submit :loading="saving" :disabled="saving">
          {{ $t('common.save') }}
        </ShadcnButton>
      </div>
    </ShadcnForm>
  </ShadcnDrawer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useI18n } from 'vue-i18n'
import { createDefaultType } from '@/views/pages/system/function/FunctionUtils'
import { FunctionModel } from '@/model/function'
import FunctionService from '@/services/function'
import SourceService from '@/services/source'
import { cloneDeep, omit } from 'lodash'

export default defineComponent({
  name: 'FunctionInfo',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    },
    info: {
      type: Object as () => FunctionModel | null
    }
  },
  setup()
  {
    const types = createDefaultType(useI18n())
    return {
      types
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
      formState: null as unknown as FunctionModel,
      loading: false,
      saving: false,
      title: null as string | null,
      plugins: [] as any[]
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.title = `${ this.$t('function.common.create') }`
      if (this.info) {
        this.formState = cloneDeep(omit(this.info, ['createTime', 'updateTime']))
        this.title = `${ this.$t('function.common.modify').replace('$NAME', this.info.name as string) }`
      }
      else {
        this.formState = {
          name: undefined,
          plugin: undefined,
          content: undefined,
          description: undefined,
          type: undefined,
          example: undefined
        }
      }

      this.loading = true
      SourceService.getPlugins()
                   .then(response => {
                     if (response.status) {
                       const result = Array.from(
                           new Set(
                               (Object.values(response.data)
                                      .reduce((acc: any, curr) => acc.concat(curr), []) as any[])
                                   .map((value: { name: string }) => ({ label: value.name, value: value.name }))
                           )
                       )

                       this.plugins = result
                     }
                   })
                   .finally(() => this.loading = false)
    },
    onSubmit()
    {
      this.saving = true
      FunctionService.saveOrUpdate(this.formState)
                     .then((response) => {
                       if (response.status) {
                         this.visible = false
                         this.$Message.success({
                           content: 'Save successfully',
                           showIcon: true
                         })
                       }
                       else {
                         this.$Message.error({
                           content: response.message,
                           showIcon: true
                         })
                       }
                     })
                     .finally(() => this.saving = false)
    },
    onCancel()
    {
      this.$emit('close', false)
    }
  }
})
</script>
