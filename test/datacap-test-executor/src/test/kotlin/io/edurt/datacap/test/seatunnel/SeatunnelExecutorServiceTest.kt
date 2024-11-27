package io.edurt.datacap.test.seatunnel

import io.edurt.datacap.executor.configure.ExecutorConfigure
import io.edurt.datacap.executor.configure.ExecutorRequest
import io.edurt.datacap.executor.seatunnel.SeatunnelExecutorService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class SeatunnelExecutorServiceTest
{
    private var request: ExecutorRequest? = null
    private var supportOptions: MutableSet<String> = mutableSetOf(
        "host",
        "database",
        "sql",
        "username",
        "password"
    )

    @Before
    fun before()
    {
        val properties = Properties()
        properties["host"] = "127.0.0.1"
        properties["username"] = "root"
        properties["password"] = "123456"
        properties["database"] = "default"
        properties["sql"] = "SHOW DATABASES"

        val input = ExecutorConfigure("ClickHouse", properties, supportOptions)
        request = ExecutorRequest(System.getProperty("user.dir"), input, input)
    }

    @Test
    fun testStart()
    {
        Assert.assertNotNull(request?.let { SeatunnelExecutorService().start(it) })
    }
}
