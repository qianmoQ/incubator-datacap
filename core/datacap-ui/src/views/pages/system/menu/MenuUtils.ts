const createHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'name', label: i18n.t('common.name') },
        { key: 'code', label: i18n.t('common.code') },
        { key: 'description', label: i18n.t('common.description') },
        { key: 'group', label: i18n.t('common.group') },
        { key: 'sorted', label: i18n.t('common.sorted') },
        { key: 'type', label: i18n.t('common.type') },
        { key: 'active', label: i18n.t('common.active'), slot: 'active' },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.updateTime') },
        { key: 'action', label: i18n.t('common.action'), slot: 'action' }
    ]
}

export {
    createHeaders
}
