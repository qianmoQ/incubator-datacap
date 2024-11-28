package io.edurt.datacap.test.alioss

import io.edurt.datacap.fs.alioss.IOUtils
import io.edurt.datacap.test.BaseIOUtilsTest

class IOUtilsTest : BaseIOUtilsTest(
    pluginPrefix = "alioss",
    ioUtils = IOUtils::class.java
)
