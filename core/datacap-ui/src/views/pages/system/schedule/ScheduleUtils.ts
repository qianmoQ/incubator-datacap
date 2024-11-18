import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

export function useHeaders()
{
    const { t } = useI18n()

    const headers = computed(() => [
        { key: 'id', label: t('common.id') },
        { key: 'name', label: t('common.name') },
        { key: 'description', label: t('common.description') },
        { key: 'expression', label: t('common.expression') },
        { key: 'active', label: t('common.active'), slot: 'active' },
        { key: 'system', label: t('common.system'), slot: 'system' },
        { key: 'type', label: t('common.type') },
        { key: 'createTime', label: t('common.createTime') },
        { key: 'updateTime', label: t('common.updateTime') },
        { key: 'action', label: t('common.action'), slot: 'action' }
    ])

    /**
     * Creates history headers with internationalization support.
     *
     * @param {any} i18n - the internationalization object
     * @return {Array} an array of history headers
     */
    const historyHeaders = computed(() => [
            { key: 'id', label: t('common.id') },
            { key: 'name', label: t('common.name') },
            { key: 'createTime', label: t('common.createTime') },
            { key: 'updateTime', label: t('common.updateTime') },
            { key: 'elapsed', label: t('common.elapsed') },
            { key: 'state', label: t('common.state') },
            { key: 'result', label: t('common.result') }
        ]
    )

    return {
        headers,
        historyHeaders
    }
}
