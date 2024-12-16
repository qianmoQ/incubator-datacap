<template>
  <ShadcnSelect v-model="language"
                :placeholder="$t('region.common.selectLanguage')"
                style="width: 120px;"
                @on-change="changeLanguage">
    <template #options>
      <ShadcnSelectGroup :label="$t('region.common.asia.default')">
        <ShadcnSelectOption value="language_zh-cn"
                            :label="$t('region.common.asia.chineseSimple')"
                            :selected="language === 'language_zh-cn'"/>
      </ShadcnSelectGroup>

      <ShadcnSelectGroup :label="$t('region.common.northAmerica.default')">
        <ShadcnSelectOption value="language_en"
                            :label="$t('region.common.northAmerica.english')"
                            :selected="language === 'language_en'"/>
      </ShadcnSelectGroup>
    </template>
  </ShadcnSelect>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
// @ts-ignore
import { setLocale } from 'view-shadcn-ui'
import { useI18nHandler } from '@/i18n/I18n'

// @ts-ignore
const { loadLocale } = useI18nHandler()
const language = ref('language_zh-cn')

const changeLanguage = async (event: any) => {
  const prefix = 'language_'
  const lang = event.value

  if (lang.startsWith(prefix)) {
    const locale = lang.substring(prefix.length)
    await loadLocale(locale)
    await setLocale(locale)
    language.value = lang
  }
}

onMounted(() => {
  const currentLocale = localStorage.getItem('locale')
  if (currentLocale) {
    language.value = `language_${ currentLocale }`
  }
})
</script>
