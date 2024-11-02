<template>
  <div>
    <ShadcnLayout>
      <LayoutHeader/>
    </ShadcnLayout>

    <div class="container my-2 min-h-screen">
      <LayoutBreadcrumb class="mb-2"/>
      <RouterView/>
    </div>

    <LayoutFooter/>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import { TokenUtils } from '@/utils/token'
import { ObjectUtils } from '@/utils/object'
import { HttpUtils } from '@/utils/http'
import CommonUtils from '@/utils/common'

import UserService from '@/services/user'

import LayoutHeader from '@/views/layouts/common/components/LayoutHeader.vue'
import LayoutFooter from '@/views/layouts/common/components/LayoutFooter.vue'
import LayoutBreadcrumb from '@/views/layouts/common/components/LayoutBreadcrumb.vue'

export default defineComponent({
  name: 'LayoutContainer',
  components: {
    LayoutBreadcrumb,
    LayoutHeader,
    LayoutFooter
  },
  beforeUnmount()
  {
    clearInterval(this.timer)
  },
  data()
  {
    return {
      timer: null as any
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      const user = TokenUtils.getAuthUser()
      if (ObjectUtils.isNotEmpty(user)) {
        this.timer = setInterval(() => {
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
  }
})
</script>
