package io.edurt.datacap.plugin;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import io.edurt.datacap.plugin.service.ServiceBindings;
import io.edurt.datacap.plugin.service.ServiceSpiLoader;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class Plugin
        extends AbstractModule
{
    private final Map<Class<? extends Service>, Boolean> binds = new HashMap<>();

    // 依赖注入器
    // Dependency injector
    @Setter
    private Injector injector;

    /**
     * 类型安全的服务绑定方法
     * Type-safe service binding method
     *
     * @param service 服务接口类型
     * @param service service interface type
     * @param implementation 服务实现类型
     * @param implementation service implementation type
     */
    @SuppressWarnings("unchecked")
    private <T extends Service> void bindService(Class<? extends Service> service, Class<? extends Service> implementation, boolean multiple, String qualifier)
    {
        // 确保实现类是服务接口的子类
        // Ensure implementation class is a subclass of service interface
        if (!service.isAssignableFrom(implementation)) {
            log.warn("Implementation {} is not compatible with service {}", implementation.getName(), service.getName());
            return;
        }

        // 由于已经进行了类型检查，这里的转换是安全的
        // Type casting is safe here as we've done the type check
        Class<T> serviceType = (Class<T>) service;
        Class<? extends T> implementationType = (Class<? extends T>) implementation;

        if (multiple) {
            // 指定别名绑定服务
            // Bind service with alias
            bind(serviceType).annotatedWith(Names.named(qualifier))
                    .to(implementationType)
                    .in(Singleton.class);
        }
        else {
            // 绑定服务
            // Bind service
            bind(serviceType).to(implementationType).in(Singleton.class);
        }
    }

    /**
     * 获取需要加载的服务类型列表
     * Get list of service types to load
     *
     * @return 服务类型列表
     * @return list of service types
     */
    protected Set<Class<? extends Service>> getServiceTypes()
    {
        return Set.of(Service.class);
    }

    @Override
    protected void configure()
    {
        getServiceTypes().forEach(serviceType -> {
            ServiceBindings bindings = ServiceSpiLoader.loadServices(serviceType);
            // 支持多个实现
            // Support multiple implementations
            bindings.getBindings().forEach((service, implementation) -> {
                // 使用命名绑定来支持多个实现
                // Use named binding to support multiple implementations
                String implName = implementation.getSimpleName();
                bindService(service, implementation, true, implName);

                // 默认绑定第一个实现
                // Default binding first implementation
                if (!binds.containsKey(service)) {
                    bindService(service, implementation, false, null);
                    binds.put(service, true);
                }
            });
        });

        // 调用子类的配置方法
        // Call subclass configuration method
        configurePlug();
    }

    protected void configurePlug() {}

    String getName()
    {
        return StringUtils.remove(this.getClass().getSimpleName(), "Plugin");
    }

    String getVersion()
    {
        return this.getClass().getPackage().getImplementationVersion();
    }

    PluginType getType()
    {
        return PluginType.CONNECTOR;
    }

    /**
     * 获取服务实例
     * Get service instance
     *
     * @param serviceClass 服务类型
     * @param serviceClass service class type
     * @return 服务实例
     * @return service instance
     */
    public <T> T getService(Class<T> serviceClass)
    {
        if (injector == null) {
            throw new IllegalStateException("Injector not set for plugin: " + getName());
        }
        return injector.getInstance(serviceClass);
    }

    /**
     * 获取指定名称的服务实例
     * Get named service instance
     *
     * @param serviceClass 服务接口类型
     * @param serviceClass service interface type
     * @param name 服务名称
     * @param name service name
     * @return 服务实例
     * @return service instance
     */
    public <T extends Service> T getService(Class<T> serviceClass, String name)
    {
        if (injector == null) {
            throw new IllegalStateException("Injector not set for plugin: " + getName());
        }
        return injector.getInstance(Key.get(serviceClass, Names.named(name)));
    }

    // 获取所有服务
    // Get all services
    public <T extends Service> Set<T> getAllServices(Class<T> serviceClass)
    {
        if (injector == null) {
            throw new IllegalStateException("Injector not set for plugin: " + getName());
        }

        Set<T> services = Sets.newHashSet();
        ServiceBindings bindings = ServiceSpiLoader.loadServices(serviceClass);
        bindings.getBindings().get(serviceClass).forEach(impl -> {
            String name = impl.getSimpleName();
            services.add(getService(serviceClass, name));
        });

        return services;
    }
}
