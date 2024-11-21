package io.edurt.datacap.plugin;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * 插件上下文管理器
 * Plugin Context Manager
 */
@Slf4j
public class PluginContextManager
{
    private static final ThreadLocal<ClassLoader> contextClassLoader = new ThreadLocal<>();

    /**
     * 设置当前线程的插件类加载器
     * Set the plugin class loader for the current thread
     *
     * @param classLoader 插件类加载器 Plugin class loader
     */
    public static void setPluginClassLoader(ClassLoader classLoader)
    {
        log.debug("Setting plugin class loader: {}", classLoader);
        contextClassLoader.set(classLoader);
    }

    /**
     * 获取当前线程的插件类加载器
     * Get the plugin class loader for the current thread
     *
     * @return 插件类加载器 Plugin class loader
     */
    public static ClassLoader getPluginClassLoader()
    {
        ClassLoader loader = contextClassLoader.get();
        if (loader != null) {
            log.debug("Returning plugin class loader: {}", loader);
            return loader;
        }
        else {
            log.debug("Returning thread's context class loader: {}", Thread.currentThread().getContextClassLoader());
            return Thread.currentThread().getContextClassLoader();
        }
    }

    /**
     * 清除当前线程的插件类加载器
     * Clear the plugin class loader for the current thread
     */
    public static void clearPluginClassLoader()
    {
        log.debug("Clearing plugin class loader");
        contextClassLoader.remove();
    }

    /**
     * 在指定的类加载器上下文中执行任务
     * Execute a task within the specified class loader context
     *
     * @param classLoader 类加载器 Class loader
     * @param task 任务 Task
     * @param <T> 返回值类型 Return type
     * @return 任务的执行结果 Result of the task execution
     * @throws Exception 任务执行过程中发生的异常 Exception occurred during task execution
     */
    public static <T> T runWithClassLoader(ClassLoader classLoader, Callable<T> task)
            throws Exception
    {
        log.debug("Running task with class loader: {}", classLoader);
        ClassLoader previous = Thread.currentThread().getContextClassLoader();
        try {
            setPluginClassLoader(classLoader);
            Thread.currentThread().setContextClassLoader(classLoader);
            return task.call();
        }
        finally {
            clearPluginClassLoader();
            Thread.currentThread().setContextClassLoader(previous);
            log.debug("Restored previous class loader: {}", previous);
        }
    }
}
