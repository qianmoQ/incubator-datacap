<template>
  <div class="relative">
    <div class="relative" v-if="loadingState" style="height: 100vh;">
      <ShadcnSpin v-model="loadingState" fixed>
        {{ loadingText }}
      </ShadcnSpin>
    </div>

    <div v-show="!loadingState">
      <slot></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onBeforeMount } from 'vue'
import { provideI18nHandler } from '@/i18n/I18n'

const { loadLocale, loadingState, loadingText } = provideI18nHandler()

onBeforeMount(async () => {
  try {
    const locale = localStorage.getItem('locale') || 'zh_cn'
    await loadLocale(locale)
  }
  catch (error) {
    console.error('Failed to load locale:', error)
  }
})
</script>

