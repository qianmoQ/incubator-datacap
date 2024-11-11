<template>
  <ShadcnDrawer v-model="visible" :title="title" width="40%">
    <div class="relative">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit">

        <ShadcnAlert v-if="testInfo.message" type="error" :title="testInfo.message" class="mb-3"/>

        <ShadcnTab v-model="activeTab" :key="'tab-' + configureTabs.length" @on-change="onChangeTab">
          <ShadcnTabItem value="source" :label="$t('source.common.source')" :key="'source-tab'">
            <ShadcnFormItem name="type" :label="$t('source.common.type')" :rules="[{ required: true, message: $t('function.tip.selectPluginHolder') }]">
              <div v-for="key in Object.keys(plugins)" :key="key">
                <ShadcnRadioGroup v-model="formState.type" name="plugin" @on-change="onChangePlugin">
                  <ShadcnDivider orientation="left" class="my-2">
                    <span class="text-gray-300">{{ key }}</span>
                  </ShadcnDivider>
                  <ShadcnRadio v-for="plugin in plugins[key as any]" :key="plugin.name + '_' + plugin.type" :value="plugin.name + '_' + plugin.type">
                    <ShadcnTooltip :content="plugin.description">
                      <ShadcnAvatar square :src="'/static/images/plugin/' + plugin.name + '.png'" :alt="plugin.name"/>
                    </ShadcnTooltip>
                  </ShadcnRadio>
                </ShadcnRadioGroup>
              </div>
            </ShadcnFormItem>
          </ShadcnTabItem>

          <ShadcnTabItem v-for="tab in configureTabs"
                         class="space-y-4"
                         :key="tab"
                         :value="tab"
                         :label="$t(`source.common.${ tab }`)">
            <ShadcnFormItem v-for="configure in pluginTabConfigure"
                            :name="configure.field"
                            :label="$t('source.common.' + configure.field)"
                            :description="configure.description">
              <ShadcnInput v-if="configure.type === 'String'" v-model="configure.value" :disabled="configure.disabled"/>

              <ShadcnNumber v-else-if="configure.type === 'Number'"
                            v-model="configure.value"
                            :disabled="configure.disabled"
                            :max="configure.max"
                            :min="configure.min"/>

              <ShadcnSwitch v-else-if="configure.type === 'Boolean'" v-model="configure.value" :disabled="configure.disabled"/>

              <Upload v-else-if="configure.type === 'File'" multiple :format="['xml']" :on-success="handlerUploadSuccess" :on-remove="handlerUploadRemove"
                      action="/api/v1/source/uploadFile"
                      :headers="{'Authorization': auth?.type + ' ' + auth?.token, 'PluginType': (formState.type as string).split(' ')[0]}">
                <Button icon="ios-cloud-upload-outline">{{ $t('common.upload') }}</Button>
              </Upload>

              <div v-else>
                <ShadcnSpace wrap>
                  <ShadcnButton circle size="small" @click="onPlusConfigure(configure.value)">
                    <template #icon>
                      <ShadcnIcon icon="Plus"/>
                    </template>
                  </ShadcnButton>

                  <ShadcnRow v-for="(element, index) in configure.value" :gutter="10" :key="index">
                    <ShadcnCol span="5">
                      <ShadcnFormItem :label="$t('common.field')">
                        <ShadcnInput v-model="element.field"/>
                      </ShadcnFormItem>
                    </ShadcnCol>

                    <ShadcnCol span="6">
                      <ShadcnFormItem :label="$t('common.value')">
                        <ShadcnInput v-model="element.value"/>
                      </ShadcnFormItem>
                    </ShadcnCol>

                    <ShadcnCol span="1">
                      <ShadcnFormItem>
                        <ShadcnButton circle
                                      type="error"
                                      size="small"
                                      style="margin-top: 18px;"
                                      @click="onMinusConfigure(element, configure.value)">
                          <template #icon>
                            <ShadcnIcon icon="Minus"/>
                          </template>
                        </ShadcnButton>
                      </ShadcnFormItem>
                    </ShadcnCol>
                  </ShadcnRow>
                </ShadcnSpace>
              </div>
            </ShadcnFormItem>
          </ShadcnTabItem>
        </ShadcnTab>

        <div class="flex justify-end">
          <ShadcnSpace>
            <ShadcnButton type="error" @click="onCancel">
              {{ $t('common.cancel') }}
            </ShadcnButton>
            <ShadcnButton ghost
                          :loading="testing"
                          :disabled="testing"
                          @click="onTest()">
              {{ $t('common.test') }}
            </ShadcnButton>
            <ShadcnButton submit :loading="saving" :disabled="!testInfo.connected || saving">
              {{ $t('common.save') }}
            </ShadcnButton>
          </ShadcnSpace>
        </div>
      </ShadcnForm>
    </div>
  </ShadcnDrawer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { SourceModel, SourceRequest } from '@/model/source'
import { cloneDeep, join } from 'lodash'
import SourceService from '@/services/source'
import { TokenUtils } from '@/utils/token'
import { ResponseModel } from '@/model/response'

interface TestInfo
{
  connected: boolean,
  percent: number,
  successful: boolean,
  message?: null | string
}

export default defineComponent({
  name: 'SourceInfo',
  setup()
  {
    const auth = TokenUtils.getAuthUser()

    return {
      auth
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
      formState: null as unknown as SourceModel,
      loading: false,
      saving: false,
      title: null as string | null,
      testing: false,
      testInfo: null as unknown as TestInfo,
      configureTabs: [] as any[],
      activeTab: 'source',
      plugins: [] as any[],
      pluginConfigure: null as unknown as any,
      pluginTabConfigure: null as unknown as any,
      applyConfigure: null as unknown as any
    }
  },
  created()
  {
    this.handleInitialize()
  },
  methods: {
    handleInitialize()
    {
      this.loading = true
      this.testInfo = { connected: false, percent: 0, successful: false }
      this.title = `${ this.$t('source.common.create') }`
      if (this.info) {
        this.title = `${ this.$t('source.common.modify').replace('$NAME', this.info.name as string) }`
        SourceService.getById(this.info.id as number)
                     .then(response => {
                       if (response.status) {
                         this.formState = cloneDeep(response.data)
                         this.formState.type = this.formState.type + '_' + this.formState.protocol
                         this.applyConfigure = response.data?.schema
                         this.pluginConfigure = this.applyConfigure?.configures
                         if (this.pluginConfigure) {
                           this.resetConfigureTab()
                         }
                       }
                     })
      }
      else {
        this.formState = SourceRequest.of()
      }

      SourceService.getPlugins()
                   .then(response => {
                     if (response.status) {
                       this.plugins = response.data
                     }
                   })
                   .finally(() => this.loading = false)
    },
    onSubmit()
    {
      this.saving = true
      const temp = (cloneDeep(this.formState.type) as string).split('_')
      let type = temp[1]
      let name = temp[0]
      if (temp.length === 3) {
        type = temp[2]
        name = join([temp[0], temp[1]], ' ')
      }
      const configure = {
        id: this.info?.id as number,
        type: type,
        name: name,
        configure: this.applyConfigure,
        version: this.formState.version
      }
      SourceService.saveOrUpdate(configure)
                   .then((response) => {
                     if (response.status) {
                       this.$Message.success({
                         content: 'Create successful',
                         showIcon: true
                       })
                       this.onCancel()
                     }
                   })
                   .finally(() => this.saving = false)
    },
    onCancel()
    {
      this.visible = false
    },
    onTest()
    {
      this.testing = true
      const temp = (cloneDeep(this.formState.type) as string).split('_')
      const type = temp[1]
      const name = temp[0]
      const configure = { type: type, name: name, configure: this.applyConfigure }
      SourceService.testConnection(configure)
                   .then((response) => {
                     this.testInfo.percent = 100
                     if (response.status) {
                       this.$Message.success({
                         content: 'Test successful',
                         showIcon: true
                       })
                       this.testInfo.connected = true
                       this.testInfo.successful = true
                       this.testInfo.message = null
                       this.formState.version = response.data?.columns[0]?.version ? response.data?.columns[0]?.version : response.data?.columns[0]?.result
                     }
                     else {
                       this.testInfo.message = response.message
                       this.testInfo.connected = false
                       this.testInfo.successful = false
                     }
                   })
                   .finally(() => this.testing = false)
    },
    onChangePlugin(value: string)
    {
      if (!value) {
        return
      }

      const currentValue = this.formState.type

      const pluginAndType = value.split('_')
      if (!pluginAndType || pluginAndType.length < 2) {
        return
      }

      this.activeTab = 'source'

      try {
        const applyPlugins: [] = this.plugins[pluginAndType[1] as any]
        const applyPlugin = applyPlugins.filter(plugin => plugin['name'] === pluginAndType[0])[0] as any
        this.applyConfigure = cloneDeep(applyPlugin?.configure)
        this.pluginConfigure = this.applyConfigure?.configures

        this.$nextTick(() => {
          this.resetConfigureTab()
          this.formState.type = value
          this.$nextTick(() => {
            this.activeTab = 'source'
          })
        })
      }
      catch (error) {
        console.error('Plugin change error:', error)
        this.formState.type = currentValue
      }
    },
    onChangeTab(value: any)
    {
      if (value !== 'source') {
        this.pluginTabConfigure = this.pluginConfigure.filter((field: { group: string }) => field.group === value)
      }
    },
    handlerUploadSuccess(response: ResponseModel)
    {
      if (response.status) {
        const configure = this.applyConfigure.configures.filter((configure: { field: string }) => configure.field === 'file')
        configure[0].value.push(response.data)
      }
    },
    handlerUploadRemove(file: any)
    {
      const configure = this.applyConfigure.configures.filter((configure: { field: string }) => configure.field === 'file')
      configure[0].value = configure[0].value.filter((value: string) => !value.endsWith(file.name))
    },
    onPlusConfigure(array: Array<any>)
    {
      if (!array) {
        array = new Array<any>()
      }
      const configure = { field: '', value: '' }
      array.push(configure)
    },
    onMinusConfigure(configure: any, array: Array<any>)
    {
      const index = array.indexOf(configure)
      if (index !== -1) {
        array.splice(index, 1)
      }
    },
    resetConfigureTab()
    {
      if (!this.pluginConfigure || !Array.isArray(this.pluginConfigure)) {
        this.configureTabs = []
        return
      }

      const validGroups = this.pluginConfigure
                              .map((v: { group: string }) => v.group)
                              .filter(group => group && typeof group === 'string')

      this.configureTabs = [...new Set(validGroups)]
    }
  }
})
</script>
