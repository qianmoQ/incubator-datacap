const createHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'name', label: i18n.t('common.name') },
        { key: 'type', label: i18n.t('common.type'), slot: 'type', class: 'text-center' },
        { key: 'protocol', label: i18n.t('common.protocol') },
        { key: 'host', label: i18n.t('common.host') },
        { key: 'port', label: i18n.t('common.port') },
        { key: 'public', label: i18n.t('common.public'), slot: 'public', class: 'text-center' },
        { key: 'version', label: i18n.t('common.version'), slot: 'version', class: 'text-center' },
        { key: 'available', label: i18n.t('common.available'), slot: 'available' },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.updateTime') },
        { key: 'action', label: i18n.t('common.action'), slot: 'action', class: 'text-center' }
    ]
}

const createHistoryHeaders = (i18n: any) => {
    return [
        { key: 'id', header: i18n.t('common.id'), width: 80 },
        { key: 'name', header: i18n.t('common.name') },
        { key: 'createTime', header: i18n.t('common.createTime') },
        { key: 'updateTime', header: i18n.t('common.updateTime') },
        { key: 'elapsed', header: i18n.t('common.elapsed'), slot: 'elapsed', class: 'text-center' },
        { key: 'state', header: i18n.t('common.state'), slot: 'state', class: 'text-center' },
        { key: 'result', header: i18n.t('common.result'), slot: 'result' }
    ]
}

export {
    createHeaders,
    createHistoryHeaders
}
