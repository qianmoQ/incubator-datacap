import state from '@/i18n/langs/en/state'
import query from '@/i18n/langs/en/query'
import source from '@/i18n/langs/en/source'
import grid from '@/i18n/langs/en/grid'
import heatmap from '@/i18n/langs/en/heatmap'

export default {
    state: state,
    query: query,
    source: source,
    grid: grid,
    heatmap: heatmap,
    region: {
        common: {
            selectLanguage: 'Select Language',
            asia: {
                default: 'Asia',
                chineseSimple: 'Simple Chinese'
            },
            northAmerica: {
                default: 'North America',
                english: 'English'
            }
        }
    },
    template: {
        common: {
            list: 'Template List',
            create: 'Create Template',
            modify: 'Modify Template [ $NAME ]',
        }
    },
    menu: {
        common: {
            list: 'Menu List',
            create: 'Create Menu',
            modify: 'Modify Menu [ $NAME ]',
            parent: 'Parent Menu',
            redirect: 'Redirect Menu',
            new: 'New Menu',
            i18nKey: 'I18N Key',
            icon: 'Icon',
        },
        tip: {
            selectType: 'Please select a type',
            selectParent: 'Please select a parent',
            selectRedirect: 'Please select a redirect'
        }
    },
    snippet: {
        common: {
            list: 'Snippet List',
            create: 'Create Snippet',
            modify: 'Modify Snippet [ $VALUE ]',
            delete: 'Delete Snippet',
            deleteInfo: 'Delete Snippet [ $VALUE ]'
        },
        tip: {
            createSuccess: 'Snippet [ $VALUE ] created successfully',
            deleteSuccess: 'Snippet [ $VALUE ] deleted successfully',
            deleteAlert1: 'Are you sure you want to delete this code snippet? This action cannot be undone. ',
            deleteAlert2: 'After deleting the code fragment, the data related to it will be permanently deleted. ',
            deleteAlert3: 'After a code fragment is deleted, it cannot be restored. ',
            deleteAlert4: 'To confirm, type [ $VALUE ] in the box below'
        }
    },
    report: {
        common: {
            list: 'Report List',
            view: 'View Report [ $VALUE ]',
            modify: 'Modify Report',
            delete: 'Delete Report',
            deleteInfo: 'Delete Report [ $VALUE ]'
        },
        tip: {
            deleteSuccess: 'Delete report [ $VALUE ] successfully',
            deleteAlert1: 'You are deleting a report. This action permanently deletes the report. Please be sure to confirm your actions before proceeding.',
            deleteAlert2: 'Warning: This cannot be undone.',
            deleteAlert3: 'To confirm, type [ $VALUE ] in the box below',
            publishSuccess: 'Report [ $VALUE ] published successfully',
        }
    },
    pipeline: {
        common: {
            list: 'Pipeline List',
            logger: 'Pipeline Logger',
            loggerInfo: 'Pipeline [ $VALUE ] Logger',
            delete: 'Delete Pipeline',
            deleteInfo: 'Delete Pipeline [ $VALUE ]',
            stop: 'Stop Pipeline',
            stopInfo: 'Stop Pipeline [ $VALUE ]',
            flow: 'Pipeline Flow',
            flowInfo: 'Pipeline [ $VALUE ] Flow',
            create: 'Create Pipeline',
            input: 'Input Source',
            output: 'Output Source',
            resetTransform: 'Reset Transform',
        },
        validator: {
            from: 'Please configure the input source information',
            to: 'Please configure the output source information',
            edge: 'Please connect the input and output source'
        },
        tip: {
            deleteSuccess: 'Delete pipeline [ $VALUE ] successfully',
            deleteAlert1: 'You are deleting a pipeline. This action permanently deletes the pipeline. Please be sure to confirm your actions before proceeding.',
            deleteAlert2: 'Warning: This cannot be undone.',
            deleteAlert3: 'To confirm, type [ $VALUE ] in the box below',
            stopAlert1: 'You are stopping a pipeline. This action permanently stops the pipeline. Please be sure to confirm your actions before proceeding.',
            stopAlert2: 'Warning: This cannot be undone.',
            stopAlert3: 'To confirm, type [ $VALUE ] in the box below',
            stopSuccess: 'Pipeline [ $VALUE ] stopped successfully',
            publishSuccess: 'Pipeline [ $VALUE ] published successfully',
        }
    },
    common: {
        home: 'Home',
        dashboard: 'Dashboard',
        query: 'Query',
        dataset: 'Dataset',
        admin: 'Admin',
        system: 'System Settings',
        source: 'Source',
        snippet: 'Snippet',
        history: 'Query History',
        pipeline: 'Pipeline',
        report: 'Report',
        function: 'Function',
        template: 'Template',
        menu: 'Menu',
        schedule: 'Schedule',
        authority: 'Authority',
        user: 'User',
        pageNotFound: 'Oops! Page Not Found!',
        pageNotFoundTip: 'It seems like the page you\'re looking for, does not exist or might have been removed.',
        pageNotAuthorized: 'Oops! Page Not Authorized!',
        pageNotAuthorizedTip: 'Sorry, you don\'t have permission to access this page.',
        backToHome: 'Back to Home',
        backToSignin: 'Back to Sign In',
        profile: 'Profile',
        id: 'Id',
        username: 'Username',
        createTime: 'Create Time',
        updateTime: 'Update Time',
        action: 'Action',
        column: 'Column',
        description: 'Description',
        name: 'Name',
        code: 'Code',
        editData: 'Edit Data',
        successfully: 'Successfully',
        expression: 'Expression',
        active: 'Active Status',
        type: 'Type',
        elapsed: 'Elapsed Time',
        state: 'State',
        result: 'Result',
        noData: 'No Data to display',
        invalidParam: 'If the parameter is invalid, check whether the parameter is correct',
        role: 'Role',
        cancel: 'Cancel',
        scheduler: 'Scheduler',
        executor: 'Executor',
        configure: 'Configure',
        remove: 'Remove',
        publish: 'Publish',
        save: 'Save',
        value: 'Value',
        alias: 'Alias',
        sort: 'Sort',
        count: 'Count',
        content: 'Content',
        feedback: 'Feedback',
        createEditor: 'Create Editor',
        device: 'Device',
        client: 'Client',
        ip: 'IP Address',
        ua: 'User Agent',
        loginTime: 'Login Time',
        submit: 'Submit',
        create: 'Create',
        plugin: 'Plugin',
        group: 'Group',
        sorted: 'Sorted',
        url: 'Url',
        pageNotNetwork: 'Oops! Not Network!',
        protocol: 'Protocol',
        host: 'Host',
        port: 'Port',
        public: 'Public',
        version: 'Version',
        available: 'Available',
        test: 'Test',
        field: 'Field',
        upload: 'Upload',
        deleteData: 'Delete Data',
        apply: 'Apply',
        length: 'Length',
        preview: 'Preview',
        refresh: 'Refresh',
        endTime: 'End Time',
        from: 'From',
        adhoc: 'Adhoc Query',
        error: 'Error',
        realtime: 'Realtime',
        to: 'To',
        work: 'Work Home',
        chat: 'Chat',
        avatar: 'Avatar',
        file: 'File',
        backTo: 'Back',
        tip: {
            pageNotNetwork: 'Oops! Unable to connect to the network, please check if the network is normal!'
        }
    },
    user: {
        common: {
            username: 'Username',
            password: 'Password',
            confirmPassword: 'Confirm Password',
            signin: 'Sign in',
            signup: 'Sign up',
            captcha: 'Captcha',
            sourceCount: 'Sources',
            queryCount: 'Queries',
            signout: 'Sign out',
            list: 'User List',
            assignAuthority: 'Assign Authority',
            assignRole: 'Assign Role',
            setting: 'Settings',
            info: 'Contribution Info',
            profile: 'User profile',
            contribution: 'Contribution',
            radar7Days: 'Radar chart of the data source within 7 days',
            createTime: 'Create Time',
            avatar: 'Avatar',
            log: 'Login Log',
            editor: 'Editor',
            fontSize: 'Font Size',
            theme: 'Theme',
            assistant: 'Ai Assistant',
            host: 'Host Address',
            token: 'Token',
            timeout: 'Timeout',
            contentCount: 'Content Count',
            modifyUsername: 'Modify Username',
            oldUsername: 'Old Username',
            newUsername: 'New Username',
            modifyPassword: 'Modify Password',
            oldPassword: 'Old Password',
            newPassword: 'New Password'
        },
        auth: {
            signinTip: 'Enter your username and password to login',
            signupTip: 'Enter your username and password to sign up',
            usernameTip: 'Please enter your username',
            passwordTip: 'Please enter your password',
            confirmPasswordTip: 'Please confirm your password',
            captchaTip: 'Please enter the captcha',
            notUserTip: 'Don\'t have an account?',
            hasUserTip: 'Already have an account?',
            passwordNotMatchTip: 'Password does not match',
            usernameSizeTip: 'Username must be between 3 and 20 characters',
            passwordSizeTip: 'Password must be between 6 and 20 characters',
            captchaSizeTip: 'Captcha must be between 1 and 6 characters'
        },
        tip: {
            sourceCountTip: 'Statistics on the total number of access data sources',
            queryCountTip: 'Statistics on the total number of access queries',
            assignRoleSuccess: 'Role assignment succeeded',
            info: 'The main information displayed here is some personal contribution information, including some queries, data sources and other information',
            contribution: 'The contribution degree is calculated based on the number of data source queries, the larger the number of queries, the higher the contribution degree.',
            radar7Days: 'The data here refers to the number of times the data source was used in a 7-day period',
            profile: 'The main display here is some basic personal information, including some avatars, nicknames and other information',
            username: 'The username is unique, currently it is not supported to modify the username',
            createTime: 'The user\'s creation time is defaulted to the user\'s first registration time, generated by the system',
            avatar: 'The default is the system avatar, the user can upload their own avatar',
            log: 'The main display here is some login log information, including the login time, IP address and other information',
            editor: 'The main display here is some editor information, including the editor\'s configuration information, such as code highlighting, themes and other information',
            fontSize: 'Here you can modify some text size, the default is 12',
            theme: 'Here you can modify some themes, including various theme styles',
            assistant: 'The main display here is some AI assistant configuration information, including the configuration information, such as the operator, proxy and other information',
            host: 'This is mainly used to configure the host address of the AI assistant',
            token: 'This is mainly used to configure the token of the AI assistant',
            timeout: 'This is mainly used to configure the timeout of the AI assistant',
            contentCount: 'This is mainly used to configure the content count of the AI assistant',
            modifyUsername: 'This is mainly used to modify the username of the user',
            oldUsername: 'This is mainly used to modify the old username of the user',
            newUsername: 'This is mainly used to modify the new username of the user',
            password: 'The password is the user\'s login password',
            changeUsernameSuccessfully: 'Username modification succeeded, please log in again',
            modifyPassword: 'This is mainly used to modify the password of the user',
            changePasswordSuccessfully: 'Password modification succeeded, please log in again',
            oldPassword: 'This is mainly used to modify the old password of the user',
            newPassword: 'This is mainly used to modify the new password of the user',
            confirmPassword: 'This is mainly used to confirm the new password of the user'
        }
    },
    role: {
        common: {
            list: 'Role List',
            create: 'Create Role',
            edit: 'Edit Role [ $NAME ]',
            name: 'Role Name',
            description: 'Role Description',
            assignRole: 'Assign Role [ $NAME ]',
            assignMenu: 'Assign Menu [ $NAME ]'
        },
        tip: {
            name: 'Please enter the role name',
            description: 'Please enter the role description'
        },
        validate: {
            nameSize: 'The role name must be between 3 and 20 characters',
            descriptionSize: 'The role description must be between 3 and 50 characters'
        }
    },
    schedule: {
        common: {
            list: 'Schedule List',
            history: 'Schedule History'
        }
    },
    dashboard: {
        common: {
            list: 'Dashboard List',
            delete: 'Delete Dashboard',
            create: 'Create Dashboard',
            modify: 'Modify Dashboard',
            modifyInfo: 'Modify Dashboard [ $VALUE ]',
            addReport: 'Add Report'
        },
        tip: {
            deleteTip1: 'You are deleting a dashboard. This action permanently deletes the dashboard. Please be sure to confirm your actions before proceeding. ',
            deleteTip2: 'Warning: This cannot be undone. ',
            deleteTip3: 'To confirm, type [ $NAME ] in the box below',
            publishSuccess: 'Dashboard [ $VALUE ] published successfully',
            notFound: 'Dashboard [ $VALUE ] not found'
        }
    },
    function: {
        common: {
            list: 'Function List',
            keyword: 'Keyword',
            operator: 'Operator',
            function: 'Function',
            example: 'Example',
            import: 'Import Data',
            importFromUrl: 'Import from URL',
            create: 'Create Function',
            modify: 'Modify Function [ $NAME ]'
        },
        tip: {
            selectPluginHolder: 'Please select a plugin',
            selectTypeHolder: 'Please select a type'
        }
    },
    dataset: {
        common: {
            list: 'Dataset List',
            adhoc: 'Adhoc Query',
            showPageSize: 'Show Page Size',
            totalRows: 'Total Rows',
            totalSize: 'Total Size',
            dataPreview: 'Data Preview',
            dataColumn: 'Data Columns',
            dataConfigure: 'Data Configure',
            dataLifeCycle: 'Data Life Cycle',
            onlyPreviewCreate: 'Only preview data can be used to create datasets',
            returnQuery: 'Return Query',
            columnName: 'Column Name',
            columnAlias: 'Column Alias',
            columnType: 'Column Type',
            columnTypeString: 'String',
            columnTypeNumber: 'Number',
            columnTypeNumberSigned: 'Number (Signed)',
            columnTypeBoolean: 'Boolean',
            columnTypeDateTime: 'DateTime',
            columnDescription: 'Column Description',
            columnComment: 'Column Comment',
            columnDefaultValue: 'Default Value',
            columnIsNullable: 'Is Nullable',
            columnLength: 'Length',
            columnIsOrderByKey: 'Sort key',
            columnIsPartitionKey: 'Partition key',
            columnIsPrimaryKey: 'Primary key',
            columnIsSampling: 'Is Sampling',
            columnExpression: 'Expression',
            columnMode: 'Column Mode',
            columnModeMetric: 'Metric',
            columnModeDimension: 'Dimension',
            columnModeGroup: 'Group',
            columnModeFilter: 'Filter',
            columnSortNone: 'None',
            columnOrderAsc: 'Ascending',
            columnOrderDesc: 'Descending',
            create: 'Create Dataset',
            modify: 'Modify Dateset',
            actuator: 'Actuator',
            syncMode: 'Sync Mode',
            syncModeManual: 'Manual',
            syncModeTiming: 'Timing synchronization',
            syncModeOutSync: 'Out Sync',
            rebuild: 'Rebuild',
            complete: 'Complete',
            failed: 'Failed',
            stateOfStart: 'Start',
            stateOfMetadata: 'Metadata State',
            stateOfMetadataStarted: 'Metadata Started',
            stateOfCreateTable: 'Create Table State',
            syncData: 'Sync Data',
            visualType: 'Visual Type',
            visualTypeTable: 'Table',
            visualTypeLine: 'Line',
            visualTypeBar: 'Bar',
            visualTypeArea: 'Area',
            visualTypePie: 'Pie',
            visualTypeHistogram: 'Histogram',
            visualTypeWordCloud: 'Word Cloud',
            visualTypeScatter: 'Scatter',
            visualTypeRadar: 'Radar',
            visualTypeFunnel: 'Funnel',
            visualTypeGauge: 'Gauge',
            visualConfigure: 'Visual Configure',
            visualConfigureNotSpecified: 'No configuration items are available',
            visualConfigureXAxis: 'X Axis',
            visualConfigureYAxis: 'Y Axis',
            visualConfigureCategoryField: 'Category',
            visualConfigureCategoryLeftField: 'Left Category',
            visualConfigureCategoryRightField: 'Right Category',
            visualConfigureValueField: 'Value',
            visualConfigureSeriesField: 'Series',
            visualConfigureOuterRadius: 'Outer Radius',
            visualConfigureShowLegend: 'Show Legend',
            visualConfigureInnerRadius: 'Inner Radius',
            visualConfigureStartAngle: 'Start Angle',
            visualConfigureEndAngle: 'End Angle',
            visualConfigureDataBreakpoint: 'Data Breakpoint',
            visualConfigureDataBreakpointBreak: 'Break',
            visualConfigureDataBreakpointContinuous: 'Continuous',
            visualConfigureDataBreakpointZero: 'Zero',
            visualConfigureDataBreakpointIgnore: 'Ignore',
            visualConfigureGeneralGroup: 'General Configure',
            visualConfigureTitleGroup: 'Title Configure',
            visualConfigureTitleGroupVisible: 'Visible',
            visualConfigureTitleGroupText: 'Title',
            visualConfigureTitleGroupSubText: 'Sub Title',
            visualConfigureTitleGroupPosition: 'Position',
            visualConfigureTitleGroupPositionLeft: 'Left',
            visualConfigureTitleGroupPositionRight: 'Right',
            visualConfigureTitleGroupPositionTop: 'Top',
            visualConfigureTitleGroupPositionBottom: 'Bottom',
            visualConfigureTitleGroupAlign: 'Align',
            visualConfigureTitleGroupAlignLeft: 'Left',
            visualConfigureTitleGroupAlignCenter: 'Center',
            visualConfigureTitleGroupAlignRight: 'Right',
            columnExpressionMax: 'Maximum',
            columnExpressionMin: 'Minimum',
            columnExpressionSum: 'Sum',
            columnExpressionAvg: 'Average',
            columnExpressionCount: 'Count',
            columnExpressionEquals: 'Equals',
            columnExpressionNotEquals: 'Not Equals',
            columnExpressionIsNull: 'Is Null',
            columnExpressionIsNotNull: 'Is Not Null',
            columnExpressionIsIn: 'Is In',
            columnExpressionIsNotIn: 'Is Not In',
            columnExpressionIsLike: 'Is Like',
            columnExpressionIsNotLike: 'Is Not Like',
            columnExpressionIsContains: 'Is Contains',
            columnExpressionGreaterThan: 'Greater Than',
            columnExpressionGreaterThanOrEquals: 'Greater Than Or Equals',
            columnExpressionLessThan: 'Less Than',
            columnExpressionLessThanOrEquals: 'Less Than Or Equals',
            columnExpressionIsNotContains: 'Is Not Contains',
            customFunction: 'Custom Function',
            lifeCycleMonth: 'Month',
            lifeCycleWeek: 'Week',
            lifeCycleDay: 'Day',
            lifeCycleHour: 'Hour',
            notSpecifiedTitle: 'Not Specified',
            history: 'Sync History',
            clearData: 'Clear Data',
            error: 'View Error',
            info: 'View Info',
            lifeCycleColumn: 'Lifecycle columns',
            lifeCycleNumber: 'Lifecycle number',
            continuousBuild: 'Continuous Build'
        },
        validator: {
            duplicateColumn: 'Column name [ $VALUE ] already exists',
            specifiedColumn: 'Sort key or primary key must be specified',
            specifiedName: 'Name must be specified'
        },
        tip: {
            selectExpression: 'Please select the expression',
            syncData: 'The data synchronization schedule will run in the background, see the logs for the specific synchronization results',
            clearData: 'Clear data will not be able to rollback, clear operation will run in the background, please be patient',
            lifeCycle: 'Data set life cycle will be calculated according to the specified list expression',
            validatorSampling: 'The order by key must contain a sampling key',
            adhocDnd: 'Drag the indicator dimension on the left to the corresponding position to query and render the data',
            rebuildProgress: 'Rebuilding will only progress unfinished',
            lifeCycleMustDateColumn: 'The lifecycle must contain a date column',
            modifyNotSupportDataPreview: 'Data preview is not supported to modify',
            publishSuccess: 'Dataset [ $VALUE ] published successfully'
        }
    }
}
