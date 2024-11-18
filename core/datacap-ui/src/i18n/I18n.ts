import { createI18n } from 'vue-i18n'
import { inject, provide, ref } from 'vue'
import I18nService from '@/services/i18n'

const language = (navigator.language || 'en').toLocaleLowerCase()

const i18n = createI18n({
    fallbackLocale: 'zh_cn',
    globalInjection: true,
    legacy: false,
    silentFallbackWarn: true,
    fallbackWarn: false,
    silentTranslationWarn: true,
    missingWarn: false,
    locale: localStorage.getItem('locale') || language.split('-')[0] || 'zh_cn',
    messages: {}
})

export const I18N_KEY = Symbol('i18n-handler')

export const loadingState = ref(false)
export const loadingText = ref('')

export function provideI18nHandler()
{
    const loadLocale = async (locale: string) => {
        loadingState.value = true
        loadingText.value = 'Loading language...'
        try {
            const response = await I18nService.getLocale(locale)
            if (response.status) {
                i18n.global.setLocaleMessage(locale, response.data)
                i18n.global.locale.value = locale
                localStorage.setItem('locale', locale)
                return true
            }
        }
        catch (error) {
            console.error('Failed to load locale:', error)
        }
        finally {
            loadingState.value = false
        }
        return false
    }

    provide(I18N_KEY, {
        loadLocale,
        loadingState,
        loadingText
    })

    return {
        loadLocale,
        loadingState,
        loadingText
    }
}

// Hook
export function useI18nHandler()
{
    const i18nHandler = inject(I18N_KEY)
    if (!i18nHandler) {
        throw new Error('useI18nHandler must be used after provideI18nHandler')
    }
    return i18nHandler
}

export default i18n
