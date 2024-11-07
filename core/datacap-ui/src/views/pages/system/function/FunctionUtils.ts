const createHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'name', label: i18n.t('common.name') },
        { key: 'description', label: i18n.t('common.description') },
        { key: 'plugin', label: i18n.t('common.plugin'), slot: 'plugin' },
        { key: 'type', label: i18n.t('common.type'), slot: 'type' },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.updateTime') },
        { key: 'action', label: i18n.t('common.action'), slot: 'action' }
    ]
}

const createDefaultType = (i18n: any) => [
    { label: i18n.t('function.common.keyword'), value: 'KEYWORD' },
    { label: i18n.t('function.common.operator'), value: 'OPERATOR' },
    { label: i18n.t('function.common.function'), value: 'FUNCTION' }
]

export {
    createHeaders,
    createDefaultType
}
