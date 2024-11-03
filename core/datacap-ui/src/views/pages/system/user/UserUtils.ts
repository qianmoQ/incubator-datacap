const createHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'username', label: i18n.t('common.username') },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.updateTime') },
        { key: 'role', label: i18n.t('common.role'), slot: 'role', width: 100 },
        { key: 'action', label: i18n.t('common.action'), slot: 'action' }
    ]
}

export {
    createHeaders
}
