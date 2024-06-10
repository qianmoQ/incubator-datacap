package io.edurt.datacap.file.none

import com.google.inject.Guice.createInjector
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.file.File
import io.edurt.datacap.file.FileManager
import org.junit.Assert.assertEquals
import org.junit.Test

class NoneModuleTest
{
    private val injector: Injector = createInjector(FileManager())

    @Test
    fun test()
    {
        injector.getInstance(Key.get(object : TypeLiteral<Set<File>>()
        {}))
            .stream()
            .findFirst()
            .ifPresent {
                assertEquals("None", it.name())
            }
    }
}
