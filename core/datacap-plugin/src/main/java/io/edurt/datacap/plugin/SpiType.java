package io.edurt.datacap.plugin;

public enum SpiType
{
    COMPILED_POM("CompiledPom"),
    DIRECTORY("Directory"),
    POM("Pom"),
    PROPERTIES("Properties"),
    SPI("Spi");

    private String name;

    SpiType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public static SpiType fromName(String name)
    {
        return valueOf(name);
    }
}
