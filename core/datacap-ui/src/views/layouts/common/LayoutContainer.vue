<template>
  <div class="relative">
    <div class="relative" v-if="loadingState" style="height: 100vh;">
      <ShadcnSpin v-model="loadingState" fixed>
        {{ loadingText }}
      </ShadcnSpin>
    </div>

    <div v-show="!loadingState">
      <ShadcnLayout>
        <LayoutHeader/>
      </ShadcnLayout>

      <div class="container my-2 min-h-screen">
        <LayoutBreadcrumb class="mb-2"/>
        <RouterView/>
      </div>

      <LayoutFooter/>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onBeforeMount, onBeforeUnmount, onMounted } from 'vue'
import { TokenUtils } from '@/utils/token'
import { ObjectUtils } from '@/utils/object'
import { HttpUtils } from '@/utils/http'
import CommonUtils from '@/utils/common'
import UserService from '@/services/user'
import LayoutHeader from '@/views/layouts/common/components/LayoutHeader.vue'
import LayoutFooter from '@/views/layouts/common/components/LayoutFooter.vue'
import LayoutBreadcrumb from '@/views/layouts/common/components/LayoutBreadcrumb.vue'
import { provideI18nHandler } from '@/i18n/I18n'

const { loadLocale, loadingState, loadingText } = provideI18nHandler()
let timer: NodeJS.Timer | null = null

onBeforeMount(async () => {
  try {
    const locale = localStorage.getItem('locale') || 'zh-CN'
    await loadLocale(locale)
  }
  catch (error) {
    console.error('Failed to load locale:', error)
  }
})

const handleInitialize = () => {
  console.log('Initializing...')
  const user = TokenUtils.getAuthUser()
  if (ObjectUtils.isNotEmpty(user)) {
    timer = setInterval(() => {
      const runTime = new Date().toLocaleTimeString()
      console.log(`[DataCap] refresh on time ${ runTime }`)
      const client = new HttpUtils().getAxios()
      client.all([UserService.getMenus(), UserService.getInfo()])
            .then(client.spread((fetchMenu, fetchInfo) => {
              if (fetchMenu.status && fetchInfo.status) {
                localStorage.setItem(CommonUtils.menu, JSON.stringify(fetchMenu.data))
                localStorage.setItem(CommonUtils.userEditorConfigure, JSON.stringify(fetchInfo.data.editorConfigure))
              }
            }))
    }, 1000 * 60)
  }
}

onMounted(() => {
  console.log('Component mounted')
  handleInitialize()
})

onBeforeUnmount(() => {
  console.log('Before unmount, clearing timer')
  if (timer) {
    clearInterval(timer as any)
  }
})
</script>
