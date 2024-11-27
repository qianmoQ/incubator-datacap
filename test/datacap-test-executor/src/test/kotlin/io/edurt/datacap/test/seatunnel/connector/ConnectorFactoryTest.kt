package io.edurt.datacap.test.seatunnel.connector

import com.google.common.collect.Sets
import io.edurt.datacap.executor.configure.ExecutorConfigure
import io.edurt.datacap.executor.seatunnel.connector.ConnectorFactory
import io.edurt.datacap.executor.seatunnel.connector.ConnectorType
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory.getLogger
import java.util.*

class ConnectorFactoryTest
{
    private val log = getLogger(this::class.java)
    private val type = ConnectorType.ClickHouse
    private var input: ExecutorConfigure? = null

    @Before
    fun before()
    {
        val properties = Properties()
        properties["host"] = "127.0.0.1"
        properties["username"] = "root"
        properties["password"] = "123456"
        properties["database"] = "default"
        properties["sql"] = "SHOW DATABASES"
        input = ExecutorConfigure("ClickHouse", properties, Sets.newHashSet())
    }

    @Test
    fun createFormatter()
    {
        val factory = ConnectorFactory.createFormatter(type, this.input)
        val map = factory.formatToMap()
        log.info("Format: {}", map)
        Assert.assertNotNull(factory.formatToMap())
    }
}
