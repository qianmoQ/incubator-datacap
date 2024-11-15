const createHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'name', label: i18n.t('common.name'), length: 20 },
        { key: 'work', label: i18n.t('common.work'), ellipsis: true },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.endTime') },
        { key: 'elapsed', label: i18n.t('common.elapsed') },
        { key: 'executor', label: i18n.t('common.executor'), slot: 'executor' },
        { key: 'from', label: i18n.t('common.from'), slot: 'from' },
        { key: 'to', label: i18n.t('common.to'), slot: 'to' },
        { key: 'state', label: i18n.t('common.state'), slot: 'state' },
        { key: 'action', label: i18n.t('common.action'), slot: 'action' }
    ]
}

export {
    createHeaders
}
