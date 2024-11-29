package io.edurt.datacap.test.cos

import io.edurt.datacap.fs.cos.TencentCosUtils
import io.edurt.datacap.test.BaseIOUtilsTest

class TencentCosUtilsTest : BaseIOUtilsTest(
    pluginPrefix = "cos",
    ioUtils = TencentCosUtils::class.java
)
