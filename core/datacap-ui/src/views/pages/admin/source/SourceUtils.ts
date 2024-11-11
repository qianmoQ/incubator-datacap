const createHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'name', label: i18n.t('common.name') },
        { key: 'type', label: i18n.t('common.type'), slot: 'type' },
        { key: 'protocol', label: i18n.t('common.protocol') },
        { key: 'host', label: i18n.t('common.host') },
        { key: 'port', label: i18n.t('common.port') },
        { key: 'public', label: i18n.t('common.public'), slot: 'public' },
        { key: 'version', label: i18n.t('common.version'), slot: 'version' },
        { key: 'available', label: i18n.t('common.available'), slot: 'available' },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.updateTime') },
        { key: 'action', label: i18n.t('common.action'), slot: 'action' }
    ]
}

const createHistoryHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id'), width: 80 },
        { key: 'name', label: i18n.t('common.name') },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.updateTime') },
        { key: 'elapsed', label: i18n.t('common.elapsed'), slot: 'elapsed' },
        { key: 'state', label: i18n.t('common.state'), slot: 'state' },
        { key: 'result', label: i18n.t('common.result'), slot: 'result' }
    ]
}

export {
    createHeaders,
    createHistoryHeaders
}
