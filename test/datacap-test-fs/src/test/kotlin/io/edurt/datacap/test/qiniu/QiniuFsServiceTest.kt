package io.edurt.datacap.test.qiniu

import io.edurt.datacap.test.BaseServiceTest

class QiniuFsServiceTest : BaseServiceTest(
    pluginName = "QiniuFs",
    pluginPrefix = "qiniu",
    pluginHome = "fs/datacap-fs-qiniu"
)
