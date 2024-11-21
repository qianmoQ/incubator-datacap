package io.edurt.datacap.convert

import com.google.inject.multibindings.Multibinder

class TestConvertModule : ConvertModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(this.binder(), ConvertService::class.java)
            .addBinding()
            .to(TestConvertService::class.java)
    }
}
