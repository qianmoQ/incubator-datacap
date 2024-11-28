package io.edurt.datacap.test.local

import io.edurt.datacap.test.BaseServiceTest

class LocalFsServiceTest : BaseServiceTest(
    pluginName = "LocalFs",
    pluginHome = "fs/datacap-fs-local",
    pluginPrefix = "local"
)
