/**
 * Generates headers for a table based on i18n translations.
 *
 * @param {any} i18n - the internationalization object for translations
 * @return {Array} an array of header objects for the table
 */
const createHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'name', label: i18n.t('common.name') },
        { key: 'description', label: i18n.t('common.description') },
        { key: 'source', label: i18n.t('common.source'), slot: 'source' },
        { key: 'syncMode', label: i18n.t('dataset.common.syncMode'), slot: 'syncMode' },
        { key: 'scheduler', label: i18n.t('common.scheduler') },
        { key: 'executor', label: i18n.t('common.executor') },
        { key: 'state', label: i18n.t('common.state'), slot: 'state' },
        { key: 'totalRows', label: i18n.t('dataset.common.totalRows') },
        { key: 'totalSize', label: i18n.t('dataset.common.totalSize') },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.updateTime') },
        { key: 'action', label: i18n.t('common.action'), slot: 'action' }
    ]
}

const createHistoryHeaders = (i18n: any) => {
    return [
        { key: 'id', label: i18n.t('common.id') },
        { key: 'elapsed', label: i18n.t('common.elapsed') },
        { key: 'count', label: i18n.t('common.count') },
        { key: 'createTime', label: i18n.t('common.createTime') },
        { key: 'updateTime', label: i18n.t('common.updateTime') },
        { key: 'state', label: i18n.t('common.state'), slot: 'state' }
    ]
}

export {
    createHeaders,
    createHistoryHeaders
}
