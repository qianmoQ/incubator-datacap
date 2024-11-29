package io.edurt.datacap.test.s3

import io.edurt.datacap.fs.s3.AmazonS3Utils
import io.edurt.datacap.test.BaseIOUtilsTest

class AmazonS3UtilsTest : BaseIOUtilsTest(
    pluginPrefix = "s3",
    ioUtils = AmazonS3Utils::class.java
)
