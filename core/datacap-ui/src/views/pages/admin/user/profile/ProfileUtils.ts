import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

export function useHeaders()
{
    const { t } = useI18n()

    const headers = computed(() => [
        { key: 'id', label: t('common.id') },
        { key: 'device', label: t('common.device') },
        { key: 'client', label: t('common.client') },
        { key: 'ip', label: t('common.ip') },
        { key: 'state', label: t('common.state'), slot: 'state' },
        { key: 'ua', label: t('common.ua'), width: 350 },
        { key: 'createTime', label: t('common.loginTime') }
    ])

    return {
        headers
    }
}
