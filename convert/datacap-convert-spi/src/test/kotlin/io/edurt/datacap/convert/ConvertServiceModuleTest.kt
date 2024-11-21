package io.edurt.datacap.convert

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import org.junit.Assert
import org.junit.Test

class ConvertServiceModuleTest
{
    private val injector: Injector = Guice.createInjector(ConvertManager())

    @Test
    fun test()
    {
        injector.getInstance(Key.get(object : TypeLiteral<Set<ConvertService>>()
        {}))
            .stream()
            .findFirst()
            .ifPresent {
                Assert.assertEquals("Test", it.name())
            }
    }
}
