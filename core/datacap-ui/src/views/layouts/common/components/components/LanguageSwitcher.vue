<template>
  <ShadcnSelect v-model="language"
                :placeholder="$t('region.common.selectLanguage')"
                style="width: 120px;"
                @on-change="changeLanguage">
    <template #options>
      <ShadcnSelectGroup :label="$t('region.common.asia.default')">
        <ShadcnSelectOption value="language_zh_cn"
                            :label="$t('region.common.asia.chineseSimple')"
                            :selected="language === 'language_zh_cn'"/>
      </ShadcnSelectGroup>

      <ShadcnSelectGroup :label="$t('region.common.northAmerica.default')">
        <ShadcnSelectOption value="language_en"
                            :label="$t('region.common.northAmerica.english')"
                            :selected="language === 'language_en'"/>
      </ShadcnSelectGroup>
    </template>
  </ShadcnSelect>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useI18n } from 'vue-i18n'

export default defineComponent({
  name: 'LanguageSwitcher',
  setup()
  {
    const { locale } = useI18n()

    const changeLanguage = (language: any) => {
      const prefix = 'language_'

      language = language.value

      if (language.startsWith(prefix)) {
        locale.value = language.substring(prefix.length)
      }
      else {
        locale.value = language
      }
    }

    return {
      changeLanguage
    }
  },
  data()
  {
    return {
      language: 'language_zh_cn'
    }
  }
})
</script>
