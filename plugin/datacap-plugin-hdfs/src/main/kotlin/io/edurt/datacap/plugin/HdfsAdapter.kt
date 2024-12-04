package io.edurt.datacap.plugin

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import io.edurt.datacap.spi.adapter.NativeAdapter
import io.edurt.datacap.spi.model.Configure
import io.edurt.datacap.spi.model.Response
import io.edurt.datacap.spi.model.Time
import io.edurt.datacap.spi.parser.SqlParser
import io.edurt.datacap.sql.statement.SQLStatement
import io.edurt.datacap.sql.statement.SelectStatement
import io.edurt.datacap.sql.statement.ShowStatement
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.lang.Boolean
import java.util.*
import kotlin.Any
import kotlin.Exception
import kotlin.String
import kotlin.require

@SuppressFBWarnings(value = ["BC_BAD_CAST_TO_ABSTRACT_COLLECTION", "EI_EXPOSE_REP2", "BC_IMPOSSIBLE_CAST"])
class HdfsAdapter : NativeAdapter
{
    private val log: Logger = getLogger(HdfsAdapter::class.java)

    private var hdfsConnection: HdfsConnection? = null

    constructor(connection: HdfsConnection?, parser: SqlParser?) : super(connection, parser)
    {
        this.hdfsConnection = connection
    }

    override fun handlerExecute(content: String?): Response
    {
        val processorTime = Time()
        processorTime.start = Date().time
        val response: Response = this.connection.response
        val configure: Configure = this.connection.configure
        if (response.isConnected)
        {
            val headers: MutableList<String> = ArrayList()
            val types: MutableList<String> = ArrayList()
            val columns: MutableList<Any> = ArrayList()
            try
            {
                val configuration = this.hdfsConnection?.hdfsConfigure
                val statement: SelectStatement = this.parser.statement as SelectStatement
                if (statement.selectElements.isNotEmpty())
                {
                    headers.addAll(statement.selectElements.map { it.column })
                }
                else
                {
                    headers.add("*")
                }
                types.add("String")
                this.adapter(configuration, parser.statement).forEach { column -> columns.add(Collections.singletonList(column)) }
                response.isSuccessful = Boolean.TRUE
            }
            catch (ex: Exception)
            {
                log.error("Execute content failed content {} exception ", content, ex)
                response.isSuccessful = Boolean.FALSE
                response.message = ex.message
            }
            finally
            {
                response.headers = headers
                response.types = types
                response.columns = handlerFormatter(configure.pluginManager, configure.format, headers, columns)
            }
        }
        processorTime.end = Date().time
        response.processor = processorTime
        return response
    }

    private fun adapter(configuration: Configuration?, statement: SQLStatement): List<String>
    {
        require(statement is SelectStatement) { "Not supported select statement" }

        val fileSystem = FileSystem.get(configuration)
        val showStatement = statement as ShowStatement

        if (showStatement.showType == ShowStatement.ShowType.DATABASES)
        {
            showStatement.tableName = "/"
        }

        return fileSystem.listStatus(Path("/" + showStatement.tableName))
            .map { it.path.name }
            .toList()
    }
}
