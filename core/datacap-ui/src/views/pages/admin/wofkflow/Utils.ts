import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

export function useHeaders()
{
    const { t } = useI18n()

    const headers = computed(() => [
        { key: 'id', label: t('common.id') },
        { key: 'name', label: t('common.name'), length: 20 },
        { key: 'createTime', label: t('common.createTime') },
        { key: 'updateTime', label: t('common.endTime') },
        { key: 'elapsed', label: t('common.elapsed') },
        { key: 'executor', label: t('common.executor'), slot: 'executor' },
        { key: 'state', label: t('common.state'), slot: 'state' },
        { key: 'action', label: t('common.action'), slot: 'action' }
    ])

    return {
        headers
    }
}
