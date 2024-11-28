package io.edurt.datacap.fs.qiniu

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import io.edurt.datacap.fs.FsRequest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.io.FileInputStream

@SuppressFBWarnings(value = ["OBL_UNSATISFIED_OBLIGATION"])
class IOUtilsTest
{
    private val log: Logger = getLogger(this::class.java)
    private val request = FsRequest()
    private val fileName = "QiniuFsTest.kt"

    @Before
    fun before()
    {
        request.access = System.getProperty("qiniu.access")
        request.secret = System.getProperty("qiniu.secret")
        request.bucket = System.getProperty("qiniu.bucket")
        request.fileName = fileName
        request.endpoint = System.getProperty("qiniu.endpoint")
    }

    @Test
    fun step1_copy()
    {
        val stream = FileInputStream("src/test/kotlin/io/edurt/datacap/test/qiniu/QiniuFsTest.kt")
        val response = IOUtils.copy(request, stream, fileName)
        assertNotNull(response)

        log.info("Copy response [ {} ]", response)
    }

    @Test
    fun step2_reader()
    {
        assertNotNull(IOUtils.reader(request))
    }

    @Test
    fun step3_delete()
    {
        assertTrue(IOUtils.delete(request))
    }
}
