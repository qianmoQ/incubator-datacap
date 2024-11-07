const createHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'name', label: i18n.t('common.name') },
        { key: 'description', label: i18n.t('common.description') },
        { key: 'plugin', label: i18n.t('common.plugin'), slot: 'plugin' },
        { key: 'system', label: i18n.t('common.system'), slot: 'system' },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.updateTime') },
        { key: 'action', label: i18n.t('common.action'), slot: 'action' }
    ]
}

export {
    createHeaders
}
