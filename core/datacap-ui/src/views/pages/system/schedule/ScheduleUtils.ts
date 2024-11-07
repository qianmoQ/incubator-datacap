/**
 * Creates headers for a table using the given internationalization object.
 *
 * @param {any} i18n - the internationalization object used for translating header labels
 * @return {Array} an array of header objects
 */
const createHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'name', label: i18n.t('common.name') },
        { key: 'description', label: i18n.t('common.description') },
        { key: 'expression', label: i18n.t('common.expression') },
        { key: 'active', label: i18n.t('common.active'), slot: 'active' },
        { key: 'system', label: i18n.t('common.system'), slot: 'system' },
        { key: 'type', label: i18n.t('common.type') },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.updateTime') },
        { key: 'action', label: i18n.t('common.action'), slot: 'action' }
    ]
}

/**
 * Creates history headers with internationalization support.
 *
 * @param {any} i18n - the internationalization object
 * @return {Array} an array of history headers
 */
const createHistoryHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'name', label: i18n.t('common.name') },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.updateTime') },
        { key: 'elapsed', label: i18n.t('common.elapsed') },
        { key: 'state', label: i18n.t('common.state') },
        { key: 'result', label: i18n.t('common.result') }
    ]
}

export {
    createHeaders,
    createHistoryHeaders
}
