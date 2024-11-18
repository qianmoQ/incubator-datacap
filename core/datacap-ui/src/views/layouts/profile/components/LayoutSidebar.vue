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
import { computed, defineComponent } from 'vue'
import { useI18n } from 'vue-i18n'

export default defineComponent({
  name: 'LayoutSidebar',
  setup()
  {
    const { t } = useI18n()

    const items = computed(() => [
      { title: t('user.common.profile'), href: '/admin/user/profile' },
      { title: t('user.common.info'), href: '/admin/user/info' },
      { title: t('user.common.log'), href: '/admin/user/log' },
      { title: t('user.common.editor'), href: '/admin/user/editor' },
      { title: t('user.common.assistant'), href: '/admin/user/assistant' },
      { title: t('user.common.modifyPassword'), href: '/admin/user/password' },
      { title: t('user.common.modifyUsername'), href: '/admin/user/username' }
    ])

    return {
      items
    }
  }
})
</script>
