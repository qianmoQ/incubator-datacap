<template>
  <div class="relative min-h-screen">
    <ShadcnSpin v-if="loading" fixed/>
    <ShadcnAlert type="warning">
      {{ $t('common.plugin.systemVersion') }}
      <ShadcnTag class="text-red-400">{{ version }}</ShadcnTag>
    </ShadcnAlert>

    <ShadcnTab v-model="activeTab" v-show="!loading">
      <ShadcnTabItem v-if="metadata" :label="metadata.i18nFormat ? $t(metadata.label) : metadata.label" :value="metadata.key">
        <div class="relative">
          <ShadcnSpace wrap size="15">
            <ShadcnAlert v-if="metadata.description">
              {{ metadata.i18nFormat ? $t(metadata.description) : metadata.description }}
            </ShadcnAlert>

            <ShadcnCard v-for="child in metadata.children" class="w-full">
              <div class="p-3 px-6">
                <div class="flex items-center justify-between">
                  <!-- Plugin -->
                  <div class="flex items-center space-x-4 justify-between">
                    <!-- Logo and Name -->
                    <div class="flex flex-col items-center space-y-2 justify-between">
                      <ShadcnAvatar class="bg-transparent border p-1.5" :src="child.logo" :alt="child.i18nFormat ? $t(child.label) : child.label"/>

                      <ShadcnText type="h6">
                        {{ child.i18nFormat ? $t(child.label) : child.label }}
                      </ShadcnText>
                    </div>

                    <ShadcnSpace class="pl-8" wrap :size="[20, 40]">
                      <!-- Description -->
                      <div class="flex flex-col space-y-4">
                        <ShadcnText class="text-sm text-gray-500" type="small">
                          {{ child.i18nFormat ? $t(child.description) : child.description }}
                        </ShadcnText>

                        <!-- Support Version -->
                        <div class="flex space-x-2 text-sm text-gray-500">
                          <div class="flex items-center space-x-2">
                            {{ $t('common.plugin.list.supportVersion') }} :
                          </div>

                          <ShadcnTag v-for="version in child.supportVersion" type="success" :key="version">
                            {{ version }}
                          </ShadcnTag>
                        </div>

                        <!-- Version -->
                        <div class="flex space-x-2 text-sm text-gray-500">
                          <div class="space-x-1">
                            {{ $t('common.plugin.version') }} :
                            <ShadcnTag>{{ child.version }}</ShadcnTag>
                          </div>

                          <ShadcnDivider v-if="child.installed" type="vertical"/>

                          <div v-if="child.installed" class="space-x-1">
                            {{ $t('common.installVersion') }}:
                            <ShadcnTag color="#00BFFF">{{ child.installVersion }}</ShadcnTag>
                          </div>
                        </div>

                        <!-- Time -->
                        <div class="flex space-x-2 text-sm text-gray-500">
                          <div class="space-x-1">
                            {{ $t('common.releasedTime') }}：
                            <ShadcnTag type="warning">{{ child.released }}</ShadcnTag>
                          </div>

                          <ShadcnDivider v-if="child.installed" type="vertical"/>

                          <div v-if="child.installed" class="space-x-1">
                            {{ $t('common.installTime') }}：
                            <ShadcnTag color="#00BFFF">{{ child.installTime }}</ShadcnTag>
                          </div>
                        </div>

                        <!-- Other -->
                        <div class="flex space-x-2 text-sm text-gray-500">
                          <div class="space-x-1">
                            {{ $t('common.author') }}： {{ child.author }}
                          </div>

                          <ShadcnDivider type="vertical"/>

                          <div class="space-x-1">
                            {{ $t('common.type') }}：
                            <ShadcnTag>{{ child.type }}</ShadcnTag>
                          </div>
                        </div>
                      </div>
                    </ShadcnSpace>
                  </div>

                  <!-- Action -->
                  <div class="flex items-center space-x-2">
                    <ShadcnButton :type="child.installed ? 'danger' : 'primary'" :loading="child.loading" @click="child.installed ? onUninstall(child) : onInstall(child)">
                      <template #icon>
                        <ShadcnIcon :icon="child.installed ? 'Trash' : 'Plus'" size="15"/>
                      </template>
                      {{ child.installed ? $t('common.uninstall') : $t('common.install') }}
                    </ShadcnButton>
                  </div>
                </div>
              </div>
            </ShadcnCard>
          </ShadcnSpace>
        </div>
      </ShadcnTabItem>
    </ShadcnTab>
  </div>
</template>

<script setup lang="ts">
import { getCurrentInstance, onBeforeMount, ref, watch } from 'vue'
import { useI18nHandler } from '@/i18n/I18n'
import { PackageUtils } from '@/utils/package.ts'
import PluginService from '@/services/plugin.ts'

interface MetadataItem
{
  key: string
  label: string
  description: string
  logo: string
  type: string
  released: string
  i18nFormat: boolean
  version: string
  supportVersion: string[]
  author: string
  installed: boolean
  installTime: string
  installVersion: string
  url: string
  loading: boolean
}

interface Metadata
{
  key: string
  label: string
  description: string
  i18nFormat: boolean
  children: MetadataItem[]
}

const metadataUrl = ref('https://cdn.north.devlive.org/applications/datacap/metadata/metadata.json')
const { proxy } = getCurrentInstance()!
// @ts-ignore
const { loadingState } = useI18nHandler()
const loading = ref(false)
const metadata = ref<Metadata>(null)
const activeTab = ref('plugin')
const version = ref(PackageUtils.get('version'))

watch(loadingState, async (newVal) => {
  if (!newVal && !metadata.value) {
    await loadMetadata()
  }
})

const loadMetadata = async () => {
  loading.value = true
  try {
    const response = await fetch(metadataUrl.value)

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${ response.status }`)
    }

    const data = await response.json()
    metadata.value = data

    const installResponse = await PluginService.getPlugins()
    if (!installResponse.status) {
      // @ts-ignore
      proxy?.$Message.error({ content: response.message, showIcon: true })
      return
    }

    // 绑定安装信息
    // Bind installation information
    metadata.value.children.map(item => {
      installResponse.data.some((installedPlugin: { name: string, loadTime: string, version: string }) => {
        if (installedPlugin.name === item.label) {
          item.installed = true
          item.installTime = installedPlugin.loadTime
          item.installVersion = installedPlugin.version
        }
      })
    })
  }
  catch (error) {
    if (error instanceof TypeError) {
      // @ts-ignore
      proxy?.$Message.error({ content: proxy?.$t('common.tip.pageNotNetwork'), showIcon: true })
    }
    else {
      // @ts-ignore
      proxy?.$Message.error({ content: `${ proxy?.$t('common.pageNotFoundTip') }: ${ error.message }`, showIcon: true })
    }
  }
  finally {
    loading.value = false
  }
}

// 安装插件
// Install plugin
const onInstall = async (item: MetadataItem) => {
  try {
    item.loading = true
    const installResponse = await PluginService.install({ name: item.key, url: item.url })
    if (installResponse.status) {
      // @ts-ignore
      proxy?.$Message.success({ content: proxy?.$t('common.installSuccess'), showIcon: true })
    }
    else {
      // @ts-ignore
      proxy?.$Message.error({ content: installResponse.message, showIcon: true })
    }

    await loadMetadata()
  }
  finally {
    item.loading = false
  }
}

// 卸载插件
// Uninstall plugin
const onUninstall = async (item: MetadataItem) => {
  try {
    item.loading = true
    const unInstallResponse = await PluginService.uninstall(item.label)
    if (unInstallResponse.status) {
      // @ts-ignore
      proxy?.$Message.success({ content: proxy?.$t('common.uninstallSuccess'), showIcon: true })
    }
    else {
      // @ts-ignore
      proxy?.$Message.error({ content: unInstallResponse.message, showIcon: true })
    }

    await loadMetadata()
  }
  finally {
    item.loading = false
  }
}

onBeforeMount(() => {
  if (!loadingState.value) {
    loadMetadata()
  }
})
</script>
