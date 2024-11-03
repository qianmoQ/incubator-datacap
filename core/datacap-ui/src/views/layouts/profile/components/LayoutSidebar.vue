<template>
  <ShadcnMenu>
    <ShadcnMenuItem v-for="item in items"
                    :name="item.title"
                    :to="item.href"
                    :active="$route.path === `${item.href}`">
      {{ item.title }}
    </ShadcnMenuItem>
  </ShadcnMenu>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'LayoutSidebar',
  data()
  {
    return {
      items: [] as any[]
    }
  },
  created()
  {
    this.handlerInitialize()
  },
  methods: {
    handlerInitialize()
    {
      this.items = [
        { title: this.$t('user.common.profile'), href: '/admin/user/profile' },
        { title: this.$t('user.common.info'), href: '/admin/user/info' },
        { title: this.$t('user.common.log'), href: '/admin/user/log' },
        { title: this.$t('user.common.editor'), href: '/admin/user/editor' },
        { title: this.$t('user.common.assistant'), href: '/admin/user/assistant' },
        { title: this.$t('user.common.modifyPassword'), href: '/admin/user/password' },
        { title: this.$t('user.common.modifyUsername'), href: '/admin/user/username' }
      ]
    }
  }
})
</script>
