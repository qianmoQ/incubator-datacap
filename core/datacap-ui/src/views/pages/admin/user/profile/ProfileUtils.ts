const createHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'device', label: i18n.t('common.device') },
        { key: 'client', label: i18n.t('common.client') },
        { key: 'ip', label: i18n.t('common.ip') },
        { key: 'state', label: i18n.t('common.state'), slot: 'state' },
        { key: 'ua', label: i18n.t('common.ua'), width: 350 },
        { key: 'createTime', label: i18n.t('common.loginTime') }
    ]
}

export {
    createHeaders
}
