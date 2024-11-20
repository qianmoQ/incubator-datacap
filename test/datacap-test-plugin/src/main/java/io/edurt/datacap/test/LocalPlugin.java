package io.edurt.datacap.test;

import com.google.inject.name.Names;
import io.edurt.datacap.plugin.Plugin;

public class LocalPlugin
        extends Plugin
{
    @Override
    protected void configurePlug()
    {
        bind(String.class).annotatedWith(Names.named("host")).toInstance("localhost");
    }
}
