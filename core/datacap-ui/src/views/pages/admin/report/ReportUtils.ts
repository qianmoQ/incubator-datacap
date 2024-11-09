const createHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'name', label: i18n.t('common.name') },
        { key: 'type', label: i18n.t('common.type') },
        { key: 'realtime', label: i18n.t('common.realtime'), slot: 'realtime' },
        { key: 'source', label: i18n.t('common.source'), slot: 'source' },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.updateTime') },
        { key: 'action', label: i18n.t('common.action'), slot: 'action' }
    ]
}

export {
    createHeaders
}
