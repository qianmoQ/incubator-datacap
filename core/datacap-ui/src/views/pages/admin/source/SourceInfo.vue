<template>
  <ShadcnDrawer v-model="visible" :title="title" width="40%">
    <div class="relative min-h-screen">
      <ShadcnSpin v-if="loading" fixed/>

      <ShadcnForm v-model="formState" v-if="formState" @on-submit="onSubmit">

        <ShadcnAlert v-if="testInfo.message" type="error" :title="testInfo.message" class="mb-3"/>

        <ShadcnTab v-model="activeTab" :key="`tab-${configureTabs.length}`" @on-change="onChangeTab">
          <ShadcnTabItem value="source" :label="$t('source.common.source')" :key="'source-tab'">
            <ShadcnFormItem name="type" :label="$t('source.common.type')" :rules="[{ required: true, message: $t('function.tip.selectPluginHolder') }]">
              <ShadcnToggleGroup v-model="formState.type" class="space-x-2" name="plugin">
                <ShadcnToggle v-for="plugin in plugins" class="p-1"
                              :key="plugin.name"
                              :value="plugin.name">
                  <ShadcnTooltip :content="plugin.name" class="p-1">
                    <img class="h-16 w-16 object-contain" :src="'/static/images/plugin/' + plugin.name + '.png'" :alt="plugin.name">
                  </ShadcnTooltip>
                </ShadcnToggle>
              </ShadcnToggleGroup>
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
                          :disabled="testing || !formState.type"
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
import { cloneDeep, pick } from 'lodash'
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
    return { auth }
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
      applyConfigure: null as unknown as any,
      originalSchema: null as unknown as any
    }
  },
  created()
  {
    this.handleInitialize()
  },
  watch: {
    'formState.type': {
      handler(newValue: any)
      {
        if (!newValue) {
          return
        }

        if (newValue instanceof Array) {
          this.formState.type = newValue[0]
        }

        try {
          this.activeTab = 'source'

          // 找到匹配的插件
          // Find matching plugin
          const applyPlugin = this.plugins.find((plugin: { name: string }) =>
              plugin.name === this.formState.type
          ) as any

          // 判断是否是修改模式且类型匹配
          // Determine whether it is a modification mode and whether the type matches
          const isEditingMatchingType = this.info && this.info.type === this.formState.type

          if (isEditingMatchingType && this.originalSchema) {
            // 如果是修改模式且类型匹配，使用修改数据的配置
            // If it is a modification mode and the type matches, use the modified data's configuration
            this.applyConfigure = cloneDeep(this.originalSchema)
            this.pluginConfigure = this.applyConfigure?.configures
          }
          else {
            // 如果是新建或者类型不匹配，使用当前插件的配置
            // If it is a new one or the type does not match, use the current plugin's configuration
            this.applyConfigure = cloneDeep(applyPlugin?.configure)
            this.pluginConfigure = this.applyConfigure?.configures
          }

          // 重置配置标签页
          // Reset configuration tab
          this.$nextTick(() => {
            this.resetConfigureTab()

            if (this.configureTabs.length > 0) {
              // 更新当前标签页的配置
              // Update the configuration of the current tab
              this.updatePluginTabConfigure('source')
            }
          })
        }
        catch (error) {
          console.error('Plugin change error:', error)
        }
      },
      immediate: false
    }
  },
  methods: {
    async handleInitialize()
    {
      try {
        this.loading = true
        this.testInfo = { connected: false, percent: 0, successful: false }
        this.title = `${ this.$t('source.common.create') }`

        // 获取插件列表的 Promise
        // Get plugins promise
        const getPluginsPromise = SourceService.getPlugins()

        if (this.info) {
          this.title = `${ this.$t('source.common.modify').replace('$NAME', String(this.info.name)) }`

          // 同时执行获取插件列表和源代码信息的请求
          // Simultaneously execute requests to get plugin list and source code
          const [pluginsResponse, sourceResponse] = await Promise.all([
            getPluginsPromise,
            SourceService.getByCode(this.info.code)
          ])

          // 处理插件列表响应
          // Handle plugin list response
          if (pluginsResponse.status) {
            this.plugins = pluginsResponse.data
          }

          // 处理源代码信息响应
          // Handle source code response
          if (sourceResponse.status) {
            this.formState = cloneDeep(sourceResponse.data)
            this.applyConfigure = sourceResponse.data?.schema
            this.pluginConfigure = this.applyConfigure?.configures
            this.originalSchema = cloneDeep(sourceResponse.data?.schema)

            if (this.pluginConfigure) {
              this.resetConfigureTab()

              // 初始化时设置默认标签页的配置
              // Set default tab config
              this.updatePluginTabConfigure('source')
            }
          }
        }
        else {
          // 如果没有 info，只需要获取插件列表
          // If there is no info, only get plugin list
          const pluginsResponse = await getPluginsPromise
          if (pluginsResponse.status) {
            this.plugins = pluginsResponse.data
          }
          this.formState = SourceRequest.of()
        }
      }
      catch (error) {
        console.error('Failed to initialize:', error)
        this.$Message.error({
          content: 'Failed to initialize the form',
          showIcon: true
        })
      }
      finally {
        this.loading = false
      }
    },
    onSubmit()
    {
      this.saving = true
      const { configures } = this.applyConfigure
      const configure = {
        code: this.formState.code,
        name: this.formState.type,
        configure: {
          configures: configures.map((item: any) =>
              pick(item, ['field', 'required', 'type', 'min', 'max', 'message', 'value']))
        },
        type: this.formState.type,
        version: this.formState.version
      }

      SourceService.saveOrUpdate(configure as any)
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

      const { configures } = this.applyConfigure
      const configure = {
        type: this.formState.type,
        configure: {
          configures: configures.map((item: any) =>
              pick(item, ['field', 'required', 'type', 'min', 'max', 'message', 'value']))
        }
      }

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
                       this.formState.version = response.data?.columns[0]?.version
                           ? response.data?.columns[0]?.version
                           : response.data?.columns[0]?.result
                     }
                     else {
                       this.testInfo.message = response.message
                       this.testInfo.connected = false
                       this.testInfo.successful = false
                     }
                   })
                   .finally(() => this.testing = false)
    },
    updatePluginTabConfigure(tabValue: string)
    {
      if (tabValue !== 'source' && this.pluginConfigure) {
        this.pluginTabConfigure = this.pluginConfigure.filter(
            (field: { group: string }) => field.group === tabValue
        )
      }
    },
    onChangeTab(value: any)
    {
      this.updatePluginTabConfigure(value)
    },
    handlerUploadSuccess(response: ResponseModel)
    {
      if (response.status) {
        const configure = this.applyConfigure.configures.filter(
            (configure: { field: string }) => configure.field === 'file'
        )
        configure[0].value.push(response.data)
      }
    },
    handlerUploadRemove(file: any)
    {
      const configure = this.applyConfigure.configures.filter(
          (configure: { field: string }) => configure.field === 'file'
      )
      configure[0].value = configure[0].value.filter(
          (value: string) => !value.endsWith(file.name)
      )
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
