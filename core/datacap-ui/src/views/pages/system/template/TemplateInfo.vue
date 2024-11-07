<template>
  <ShadcnDrawer v-model="visible" :title="title" width="40%">
    <ShadcnSpin v-if="loading" fixed/>

    <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit">
      <ShadcnFormItem name="name"
                      :label="$t('common.name')"
                      :rules="[
                          { required: true, message: $t('common.name') }
                      ]">
        <ShadcnInput v-model="formState.name" name="name" :disabled="formState.system"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="plugin"
                      :label="$t('common.plugin')"
                      :rules="[
                          { required: true, message: $t('function.tip.selectPluginHolder') },
                      ]">
        <ShadcnCheckboxGroup v-model="formState.plugin" name="plugin">
          <ShadcnCheckbox v-for="item in plugins" :value="item.name">{{ item.name }} ({{ item.type }})</ShadcnCheckbox>
        </ShadcnCheckboxGroup>
      </ShadcnFormItem>

      <ShadcnFormItem name="description" :label="$t('common.description')">
        <ShadcnInput v-model="formState.description" name="description" type="textarea"/>
      </ShadcnFormItem>

      <ShadcnFormItem name="content"
                      :label="$t('common.content')"
                      :rules="[{ required: true, message: $t('common.content') }]">
        <AceEditor :value="formState.content" name="content" @update:value="setContent"/>
      </ShadcnFormItem>

      <div class="flex justify-end">
        <ShadcnSpace>
          <ShadcnButton type="error"
                        :loading="saving"
                        :disabled="saving"
                        @click="onCancel()">
            {{ $t('common.cancel') }}
          </ShadcnButton>
          <ShadcnButton submit :loading="saving" :disabled="saving">
            {{ $t('common.save') }}
          </ShadcnButton>
        </ShadcnSpace>
      </div>
    </ShadcnForm>
  </ShadcnDrawer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { TemplateModel, TemplateRequest } from '@/model/template'
import SourceService from '@/services/source'
import TemplateService from '@/services/template'
import { cloneDeep } from 'lodash'
import AceEditor from '@/views/components/editor/AceEditor.vue'

export default defineComponent({
  name: 'TemplateInfo',
  components: { AceEditor },
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
      type: Object as () => TemplateModel | null
    }
  },
  data()
  {
    return {
      formState: null as unknown as TemplateModel,
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
      this.title = `${ this.$t('template.common.create') }`
      if (this.info) {
        this.formState = cloneDeep(this.info)
        this.title = `${ this.$t('template.common.modify').replace('$NAME', this.info.name as string) }`
      }
      else {
        this.formState = TemplateRequest.of()
      }

      this.loading = true
      SourceService.getPlugins()
                   .then(response => {
                     if (response.status) {
                       this.plugins = Object.values(response.data).reduce((acc, curr) => (acc as any).concat(curr), []) as any[]
                     }
                   })
                   .finally(() => this.loading = false)
    },
    setContent(value: string)
    {
      this.formState.content = value
    },
    onSubmit()
    {
      this.saving = true
      TemplateService.saveOrUpdate(this.formState)
                     .then((response) => {
                       if (response.status) {
                         this.$Message.success({
                           content: 'Create successful',
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
                     .finally(() => this.saving = false)
    },
    onCancel()
    {
      this.$emit('close', false)
    }
  }
})
</script>
