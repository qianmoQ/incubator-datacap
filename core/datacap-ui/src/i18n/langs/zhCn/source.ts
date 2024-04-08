export default {
    common: {
        list: '数据源列表',
        modify: '修改数据源 [ $NAME ]',
        source: '数据源',
        configure: '配置',
        authorization: '授权',
        advanced: '高级',
        custom: '自定义',
        host: '主机地址',
        port: '端口',
        name: '名称',
        username: '用户名',
        password: '密码',
        database: '数据库',
        ssl: 'SSL',
        file: '文件',
        create: '创建数据源',
        delete: '删除数据源 [ $NAME ]',
        syncMetadata: '同步元数据',
        syncHistory: '同步历史',
        manager: '数据管理',
        info: '基本信息',
        notSpecified: '未指定',
        notUpdated: '未更新',
        engine: '引擎',
        notSpecifiedEngine: '未指定引擎',
        collation: '排序规则',
        notSpecifiedCollation: '未指定排序规则',
        dataInfo: '数据信息',
        totalRows: '总行数',
        format: '格式',
        notSpecifiedFormat: '未指定格式',
        avgRowLength: '平均行长度',
        dataSize: '数据大小',
        indexSize: '索引大小',
        notSpecifiedIndex: '未指定索引',
        autoIncrement: '自增列',
        notSpecifiedPrimaryKey: '未指定主键',
        resetAutoIncrement: '重置自增列',
        resetTo: '重置为',
        comment: '表注释',
        menuNew: '新建',
        menuNewTable: '新建表',
        tableName: '表名',
        columnName: '列名',
        columnType: '类型',
        columnLength: '长度',
        columnDefaultValue: '默认值',
        columnPrimaryKey: '主键',
        columnAutoIncrement: '自增列',
        columnIsNullable: '允许为空',
        columnComment: '列注释',
        newColumn: '新建列',
        menuExport: '导出',
        exportData: '导出数据',
        exportDataTable: '导出 [ $VALUE ] 表数据',
        exportDataFormat: '导出格式',
        exportDataCount: '导出条数',
        downloadPath: '下载路径',
        downloadFile: '下载文件',
        truncateTable: '截断表',
        truncateTableInfo: '截断 [ $VALUE ] 表',
        dropTable: '删除表',
        dropTableInfo: '删除 [ $VALUE ] 表',
        structure: '结构',
        isNullable: '允许为空',
        defaultValue: '默认值',
        extra: '额外信息',
        changeColumn: '修改列',
        changeColumnInfo: '修改 [ $VALUE ] 列',
        dropColumn: '删除列',
        dropColumnInfo: '删除 [ $VALUE ] 列',
        tableData: '表数据',
        firstPage: '首页',
        previousPage: '上一页',
        nextPage: '下一页',
        lastPage: '尾页',
        jumpPage: '跳转至',
        showPageSize: '每页显示',
        records: '条记录',
        addRows: '添加行',
        previewPendingChanges: '预览未保存的变更',
        previewDML: '预览 DML 语句',
        copyRows: '复制行',
        deleteRows: '删除行',
        visibleColumn: '可见列',
        filterData: '筛选数据',
        filterCondition: '筛选条件',
        addFilter: '添加筛选条件',
    },
    tip: {
        selectSource: '请选择数据源',
        deleteSourceSuccess: '删除数据源 [ $NAME ] 成功',
        deleteAlert1: '您正在删除数据源。此操作将永久删除所有与该数据源相关的数据和配置。请务必在继续操作之前确认您的操作。',
        deleteAlert2: '警告：执行此操作将不可逆。所有与该数据源相关的数据和配置都会被永久删除。',
        deleteAlert3: '要确认，请在下面的框中键入 [ $NAME ]',
        syncMetadata1: '同步元数据将在后台运行',
        syncMetadata2: '同步元数据将会覆盖当前的元数据，可能会导致数据丢失，是否继续？',
        syncMetadata3: '要确认，请在下面的框中键入 [ $NAME ]',
        syncMetadata4: '任务 [ $NAME ] 已开始',
        selectDatabase: '请选择数据库',
        notSelectedNode: '请在左侧选择节点方可展示结果',
        resetAutoIncrementSuccess: '重置自增列为 [ $VALUE ] 成功',
        createTableSuccess: '创建表 [ $VALUE ] 成功',
        createColumnSuccess: '创建列 [ $VALUE ] 成功',
        truncateTableSuccess: '截断表 [ $VALUE ] 成功',
        truncateTable1: '您即将执行截断表操作。这将会删除表中的所有数据。您确定要继续吗？',
        truncateTable2: '请注意，截断表操作是不可逆的。执行此操作将永久删除表中的所有数据。请确保您已经备份了重要数据。',
        truncateTable3: '执行截断表操作将立即删除表中的所有数据，这可能会对正在进行的工作造成影响。请确保您已经保存了需要的数据，并且其他用户不会受到影响。',
        truncateTable4: '我们建议您首先在非生产环境中测试截断表操作，以确保它不会对您的生产数据造成意外的影响。',
        truncateTable5: '如果您对执行截断表操作有任何疑问或需要帮助，请联系您的数据库管理员或技术支持团队。',
        dropTableSuccess: '删除表 [ $VALUE ] 成功',
        dropTable1: '您即将执行删除表的操作。此操作将永久删除表及其所有数据。您确定要继续吗？',
        dropTable2: '请注意，删除表操作是不可逆的。执行此操作将永久删除表及其所有数据。请确保您已经备份了重要数据。',
        dropTable3: '执行删除表操作将立即删除表及其所有数据，这可能会对正在进行的工作造成影响。请确保您已经保存了需要的数据，并且其他用户不会受到影响。',
        dropTable4: '我们建议您首先在非生产环境中测试删除表操作，以确保它不会对您的生产数据造成意外的影响。',
        dropTable5: '如果您对执行删除表操作有任何疑问或需要帮助，请联系您的数据库管理员或技术支持团队。',
        changeColumnSuccess: '修改列 [ $VALUE ] 成功',
        dropColumnSuccess: '删除列 [ $VALUE ] 成功',
        dropColumn1: '您即将执行删除列的操作。此操作将永久删除列及其所有数据。您确定要继续吗？',
        dropColumn2: '请注意，删除列操作是不可逆的。执行此操作将永久删除列及其所有数据。请确保您已经备份了重要数据。',
        dropColumn3: '执行删除列操作将立即删除列及其所有数据，这可能会对正在进行的工作造成影响。请确保您已经保存了需要的数据，并且其他用户不会受到影响。',
        dropColumn4: '我们建议您首先在非生产环境中测试删除列操作，以确保它不会对您的生产数据造成意外的影响。',
        dropColumn5: '如果您对执行删除列操作有任何疑问或需要帮助，请联系您的数据库管理员或技术支持团队。',
        updateSuccess: '更新成功',
        deleteSuccess: '删除成功'
    }
}