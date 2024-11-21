package io.edurt.datacap.convert

import io.edurt.datacap.convert.model.ConvertRequest
import io.edurt.datacap.convert.model.ConvertResponse
import io.edurt.datacap.plugin.Service

interface ConvertService : Service
{
    fun name(): String
    {
        return this.javaClass
            .simpleName
            .removeSuffix("Convert")
    }

    fun description(): String
    {
        return "Integrate ${name()} format convert"
    }

    fun format(request: ConvertRequest): ConvertResponse

    fun formatStream(request: ConvertRequest): ConvertResponse

    fun writer(request: ConvertRequest): ConvertResponse

    fun reader(request: ConvertRequest): ConvertResponse
}
