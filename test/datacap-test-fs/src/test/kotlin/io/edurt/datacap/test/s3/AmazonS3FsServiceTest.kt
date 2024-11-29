package io.edurt.datacap.test.s3

import io.edurt.datacap.test.BaseServiceTest

class AmazonS3FsServiceTest : BaseServiceTest(
    pluginName = "AmazonS3Fs",
    pluginHome = "fs/datacap-fs-amazon-s3",
    pluginPrefix = "s3"
)
