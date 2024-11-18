import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

export function useHeaders()
{
    const { t } = useI18n()

    const headers = computed(() => [
        { key: 'id', label: t('common.id') },
        { key: 'source', label: t('common.plugin'), slot: 'source' },
        { key: 'type', label: t('common.type'), slot: 'type' },
        { key: 'createTime', label: t('common.createTime') },
        { key: 'updateTime', label: t('common.endTime') },
        { key: 'elapsed', label: t('common.elapsed') },
        { key: 'mode', label: t('common.from'), slot: 'mode' },
        { key: 'count', label: t('common.count') },
        { key: 'state', label: t('common.state'), slot: 'state' },
        { key: 'action', label: t('common.action'), slot: 'action' }
    ])

    return {
        headers
    }
}
