package io.edurt.datacap.test.spi

import io.edurt.datacap.convert.ConvertService
import io.edurt.datacap.convert.model.ConvertRequest
import io.edurt.datacap.convert.model.ConvertResponse
import lombok.extern.slf4j.Slf4j

@Slf4j
class NoneConvertService : ConvertService
{
    override fun format(request: ConvertRequest): ConvertResponse
    {
        val response = ConvertResponse()
        response.successful = true
        return response
    }

    override fun formatStream(request: ConvertRequest): ConvertResponse
    {
        val response = ConvertResponse()
        response.successful = true
        return response
    }

    override fun writer(request: ConvertRequest): ConvertResponse
    {
        val response = ConvertResponse()
        response.successful = true
        return response
    }

    override fun reader(request: ConvertRequest): ConvertResponse
    {
        val response = ConvertResponse()
        response.successful = true
        return response
    }
}
