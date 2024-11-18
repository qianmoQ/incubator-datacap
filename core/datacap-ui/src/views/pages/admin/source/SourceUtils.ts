import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

export function useHeaders()
{
    const { t } = useI18n()

    const headers = computed(() => [
        { key: 'id', label: t('common.id') },
        { key: 'name', label: t('common.name') },
        { key: 'type', label: t('common.type'), slot: 'type' },
        { key: 'protocol', label: t('common.protocol') },
        { key: 'host', label: t('common.host') },
        { key: 'port', label: t('common.port') },
        { key: 'public', label: t('common.public'), slot: 'public' },
        { key: 'version', label: t('common.version'), slot: 'version' },
        { key: 'available', label: t('common.available'), slot: 'available' },
        { key: 'createTime', label: t('common.createTime') },
        { key: 'updateTime', label: t('common.updateTime') },
        { key: 'action', label: t('common.action'), slot: 'action' }
    ])

    const historyHeaders = computed(() => [
        { key: 'id', label: t('common.id'), width: 80 },
        { key: 'name', label: t('common.name') },
        { key: 'createTime', label: t('common.createTime') },
        { key: 'updateTime', label: t('common.updateTime') },
        { key: 'elapsed', label: t('common.elapsed'), slot: 'elapsed' },
        { key: 'state', label: t('common.state'), slot: 'state' },
        { key: 'result', label: t('common.result'), slot: 'result' }
    ])

    return {
        headers,
        historyHeaders
    }
}
