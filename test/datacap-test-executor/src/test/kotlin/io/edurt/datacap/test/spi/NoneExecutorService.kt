package io.edurt.datacap.test.spi

import io.edurt.datacap.executor.ExecutorService
import io.edurt.datacap.executor.configure.ExecutorRequest
import io.edurt.datacap.executor.configure.ExecutorResponse
import io.edurt.datacap.plugin.annotation.InjectService

@InjectService
class NoneExecutorService : ExecutorService
{
    override fun start(request: ExecutorRequest): ExecutorResponse
    {
        return ExecutorResponse()
    }

    override fun stop(request: ExecutorRequest): ExecutorResponse
    {
        return ExecutorResponse()
    }
}
