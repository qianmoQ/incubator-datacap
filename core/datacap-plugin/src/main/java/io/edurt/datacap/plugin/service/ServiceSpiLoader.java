package io.edurt.datacap.plugin.service;

import com.google.common.collect.Sets;
import io.edurt.datacap.plugin.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
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
        return loadServices(serviceType, Thread.currentThread().getContextClassLoader());
    }

    /**
     * 加载服务实现
     * Load service implementations
     *
     * @param serviceType 服务类型（必须继承自Service）
     * @param serviceType service type (must extend Service)
     * @param classLoader 类加载器
     * @param classLoader class loader
     * @return 服务绑定
     * @return service bindings
     * @throws IllegalArgumentException 如果服务类型不是Service的子类
     * @throws IllegalArgumentException if service type is not a Service subclass
     */
    public static ServiceBindings loadServices(Class<? extends Service> serviceType, ClassLoader classLoader)
    {
        ServiceBindings bindings = new ServiceBindings();

        log.debug("Loading services for type {} using ClassLoader: {}", serviceType.getName(), classLoader.getClass().getName());

        try {
            // 使用指定的类加载器检查服务定义文件
            // Check for service definitions using the specified class loader
            String servicePath = "META-INF/services/" + serviceType.getName();
            Enumeration<URL> resources = classLoader.getResources(servicePath);

            boolean found = false;
            while (resources.hasMoreElements()) {
                found = true;
                URL url = resources.nextElement();
                log.debug("Found service file: {}", url);

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.trim().isEmpty() && !line.startsWith("#")) {
                            line = line.trim();
                            log.debug("Service implementation defined in file: {}", line);
                            try {
                                // 使用指定的类加载器加载实现类
                                // Load the implementation class using the specified class loader
                                Class<?> implementationClass = classLoader.loadClass(line);
                                log.debug("Successfully loaded implementation class: {}", implementationClass.getName());

                                if (Service.class.isAssignableFrom(implementationClass)) {
                                    @SuppressWarnings("unchecked")
                                    Class<? extends Service> serviceImpl = (Class<? extends Service>) implementationClass;

                                    // 添加直接绑定
                                    // Add direct binding
                                    if (serviceType.isAssignableFrom(serviceImpl)) {
                                        if (!bindings.getBindings().containsKey(serviceType)) {
                                            bindings.addBinding(serviceType, serviceImpl);
                                            log.debug("Added direct binding: {} -> {}", serviceType.getName(), serviceImpl.getName());
                                        }
                                    }

                                    // 检查并添加接口绑定
                                    // Check and add interface binding
                                    for (Class<?> iface : getAllInterfaces(serviceImpl)) {
                                        if (serviceType.isAssignableFrom(iface)) {
                                            @SuppressWarnings("unchecked")
                                            Class<? extends Service> serviceInterface = (Class<? extends Service>) iface;
                                            if (!bindings.getBindings().containsKey(serviceInterface)) {
                                                bindings.addBinding(serviceInterface, serviceImpl);
                                                log.debug("Added interface binding: {} -> {}", serviceInterface.getName(), serviceImpl.getName());
                                            }
                                        }
                                    }
                                }
                            }
                            catch (ClassNotFoundException e) {
                                log.error("Failed to load implementation class: {}", line, e);
                            }
                        }
                    }
                }
            }

            if (!found) {
                log.warn("No service definition files found for {}", serviceType.getName());
            }
        }
        catch (IOException e) {
            log.error("Error loading services for type: " + serviceType.getName(), e);
        }

        // 记录找到的所有绑定
        // Record all found bindings
        bindings.getBindings().forEach((service, impl) ->
                log.debug("Service: {} has implementations: {}", service.getName(), impl.getName())
        );

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
