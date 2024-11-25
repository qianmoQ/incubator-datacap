import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

export function useHeaders()
{
    const { t } = useI18n()

    const headers = computed(() => [
            { key: 'id', label: t('common.id') },
            { key: 'name', label: t('common.name') },
            { key: 'description', label: t('common.description') },
            { key: 'group', label: t('common.group') },
            { key: 'sorted', label: t('common.sorted') },
            { key: 'type', label: t('common.type') },
            { key: 'active', label: t('common.active'), slot: 'active' },
            { key: 'createTime', label: t('common.createTime') },
            { key: 'updateTime', label: t('common.updateTime') },
            { key: 'action', label: t('common.action'), slot: 'action' }
        ]
    )

    return {
        headers
    }
}
