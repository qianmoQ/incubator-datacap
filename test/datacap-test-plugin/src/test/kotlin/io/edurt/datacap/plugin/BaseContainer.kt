package io.edurt.datacap.plugin

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

@SuppressFBWarnings(value = ["CT_CONSTRUCTOR_THROW", "EQ_DOESNT_OVERRIDE_EQUALS"])
open class BaseContainer<SELF : BaseContainer<SELF>> : GenericContainer<SELF>
{
    private val containerPorts: MutableMap<String, Int> = mutableMapOf()

    protected constructor(
        imageName: String,
        ports: Map<String, Int>
    ) : super(DockerImageName.parse(imageName))
    {
        containerPorts.putAll(ports)
        setupContainer()
    }

    protected constructor(
        dockerImageName: DockerImageName,
        ports: Map<String, Int>
    ) : super(dockerImageName)
    {
        dockerImageName.assertCompatibleWith(dockerImageName)
        containerPorts.putAll(ports)
        setupContainer()
    }

    private fun setupContainer()
    {
        withExposedPorts(*containerPorts.values.toTypedArray())
    }

    fun getPort(name: String): Int
    {
        return getMappedPort(
            containerPorts[name]
                ?: throw IllegalArgumentException("Port $name not found")
        )
    }

    // 获取容器内部端口
    // Get container internal port
    fun getContainerPort(name: String): Int
    {
        return containerPorts[name]
            ?: throw IllegalArgumentException("Port $name not found")
    }

    // 获取所有端口映射
    // Get all port mappings
    fun getAllPorts(): Map<String, Int>
    {
        return containerPorts.mapValues { (name, port) ->
            getMappedPort(port)
        }
    }

    // 获取所有容器内部端口
    // Get all container internal ports
    fun getAllContainerPorts(): Map<String, Int>
    {
        return containerPorts.toMap()
    }
}
