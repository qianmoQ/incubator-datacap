const createHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'source', label: i18n.t('common.plugin'), slot: 'source' },
        { key: 'type', label: i18n.t('common.type'), slot: 'type' },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.endTime') },
        { key: 'elapsed', label: i18n.t('common.elapsed') },
        { key: 'mode', label: i18n.t('common.from'), slot: 'mode' },
        { key: 'count', label: i18n.t('common.count') },
        { key: 'state', label: i18n.t('common.state'), slot: 'state' },
        { key: 'action', label: i18n.t('common.action'), slot: 'action' }
    ]
}

export {
    createHeaders
}
