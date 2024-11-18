<template>
  <ShadcnDrawer v-model="visible"
                :title="$t('function.common.import')"
                width="40%"
                @on-close="onCancel">
    <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit">
      <ShadcnTab v-model="formState.mode">
        <ShadcnTabItem :label="$t('common.content')" :value="FunctionMode.txt">
          <ShadcnFormItem name="content" :label="$t('common.content')">
            <ShadcnInput v-model="formState.content"
                         name="content"
                         type="textarea"
                         :placeholder="$t('common.content')"/>
          </ShadcnFormItem>
        </ShadcnTabItem>

        <ShadcnTabItem class="space-y-2" :label="$t('function.common.importFromUrl')" :value="FunctionMode.url">
          <ShadcnSpace wrap>
            <ShadcnAlert type="warning" show-icon :title="$t('function.common.keyword')">
              (http|https)://datacap.devlive.org/resources/functions/plugin/keywords.txt
            </ShadcnAlert>
            <ShadcnAlert type="warning" show-icon :title="$t('function.common.operator')">
              (http|https)://datacap.devlive.org/resources/functions/plugin/operators.txt
            </ShadcnAlert>
            <ShadcnAlert type="warning" show-icon :title="$t('function.common.function')">
              (http|https)://datacap.devlive.org/resources/functions/plugin/functions.txt
            </ShadcnAlert>
          </ShadcnSpace>

          <ShadcnFormItem name="url" :label="$t('common.url')">
            <ShadcnInput v-model="formState.content" name="content"/>
          </ShadcnFormItem>
        </ShadcnTabItem>
      </ShadcnTab>

      <ShadcnFormItem name="plugin"
                      :label="$t('common.plugin')"
                      :rules="[
                          { required: true, message: $t('common.plugin') }
                      ]">
        <ShadcnSelect v-model="formState.plugin" :options="plugins" multiple name="plugin"/>
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
    </ShadcnForm>

    <div class="flex justify-end mt-2">
      <ShadcnSpace>
        <ShadcnButton type="error"
                      :loading="loading"
                      :disabled="loading"
                      @click="onCancel()">
          {{ $t('common.cancel') }}
        </ShadcnButton>
        <ShadcnButton :loading="loading" :disabled="loading" @click="onSubmit()">
          {{ $t('function.common.import') }}
        </ShadcnButton>
      </ShadcnSpace>
    </div>
  </ShadcnDrawer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useHeaders } from '@/views/pages/system/function/FunctionUtils'
import { FunctionImportModel, FunctionMode } from '@/model/function'
import FunctionService from '@/services/function'
import SourceService from '@/services/source'

export default defineComponent({
  name: 'FunctionImport',
  props: {
    isVisible: {
      type: Boolean,
      default: () => false
    }
  },
  setup()
  {
    const { typeHeaders: types } = useHeaders()

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
    },
    FunctionMode()
    {
      return FunctionMode
    }
  },
  data()
  {
    return {
      formState: null as unknown as FunctionImportModel,
      loading: false,
      plugins: []
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.formState = {
        mode: FunctionMode.txt,
        plugin: undefined,
        content: undefined,
        type: undefined
      }
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
    },
    onSubmit()
    {
      this.loading = true
      FunctionService.import(this.formState)
                     .then((response) => {
                       if (response.status) {
                         this.$Message.success({
                           content: 'Create successfully',
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
    },
    onCancel()
    {
      this.$emit('close', false)
    }
  }
})
</script>
