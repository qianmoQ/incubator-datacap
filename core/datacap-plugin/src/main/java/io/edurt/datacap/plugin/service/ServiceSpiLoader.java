package io.edurt.datacap.plugin.service;

import com.google.common.collect.Sets;
import io.edurt.datacap.plugin.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.ServiceLoader;
import java.util.Set;

@Slf4j
public class ServiceSpiLoader
{
    /**
     * 加载服务实现
     * Load service implementations
     *
     * @param serviceType 服务类型（必须继承自Service）
     * @param serviceType service type (must extend Service)
     * @return 服务绑定
     * @return service bindings
     * @throws IllegalArgumentException 如果服务类型不是Service的子类
     * @throws IllegalArgumentException if service type is not a Service subclass
     */
    public static ServiceBindings loadServices(Class<? extends Service> serviceType)
    {
        ServiceBindings bindings = new ServiceBindings();

        // 加载所有 Service 实现
        // Load all Service implementations
        ServiceLoader<Service> loader = ServiceLoader.load(Service.class);
        for (Service service : loader) {
            Class<? extends Service> implementationType = service.getClass();

            // 找到这个实现类实现的所有服务接口
            // Find all service interfaces implemented by this implementation
            Set<Class<?>> interfaces = getAllInterfaces(implementationType);
            for (Class<?> aClass : interfaces) {
                // 如果这个接口是我们要找的服务类型的子类型
                // If this interface is a subclass of the service type
                if (serviceType.isAssignableFrom(aClass)) {
                    // 安全的类型转换，因为我们已经验证了类型关系
                    // Safe type casting, because we've verified the type relationship
                    @SuppressWarnings("unchecked")
                    Class<? extends Service> serviceInterface = (Class<? extends Service>) aClass;
                    bindings.addBinding(serviceInterface, implementationType);
                }
            }
        }

        return bindings;
    }

    private static Set<Class<?>> getAllInterfaces(Class<?> clazz)
    {
        Set<Class<?>> interfaces = Sets.newHashSet();
        while (clazz != null) {
            interfaces.addAll(Arrays.asList(clazz.getInterfaces()));
            clazz = clazz.getSuperclass();
        }
        return interfaces;
    }
}
