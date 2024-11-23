package io.edurt.datacap.test.lib.http

import io.edurt.datacap.lib.http.HttpClient
import io.edurt.datacap.lib.http.HttpConfigure
import io.edurt.datacap.lib.http.HttpMethod
import org.junit.Assert.assertNotNull
import org.junit.Test

class HttpClientTest
{
    private val configure: HttpConfigure = HttpConfigure()

    init
    {
        configure.autoConnected = java.lang.Boolean.FALSE
        configure.retry = 0
        configure.params = null
        configure.protocol = "https"
        configure.host = "datacap.devlive.org"
        configure.port = 443
        configure.method = HttpMethod.GET
    }

    @Test
    fun test()
    {
        val client: HttpClient = HttpClient(this.configure)
        assertNotNull(client.execute())
    }
}
