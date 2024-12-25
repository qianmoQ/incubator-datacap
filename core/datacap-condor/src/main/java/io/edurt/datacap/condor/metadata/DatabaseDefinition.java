package io.edurt.datacap.condor.metadata;

import io.edurt.datacap.condor.StorageEngine;

public class DatabaseDefinition
{
    private static final String ROOT_DIR = "data";

    private String name;
    private StorageEngine storageEngine;

    public DatabaseDefinition(String name)
    {
        this.name = name;
        this.storageEngine = new StorageEngine();
    }

    public String getName()
    {
        return name;
    }

    public StorageEngine getStorageEngine()
    {
        return storageEngine;
    }
}
