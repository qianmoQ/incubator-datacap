package io.edurt.datacap.fs;

import io.edurt.datacap.plugin.Service;

public interface FsService
        extends Service
{
    default String name()
    {
        return this.getClass()
                .getSimpleName()
                .replace("Fs", "");
    }

    default String description()
    {
        return String.format("Integrate %s file system", this.name());
    }

    FsResponse writer(FsRequest request);

    FsResponse reader(FsRequest request);

    /**
     * Delete file or directory
     *
     * @param request Request info
     * @return delete info
     */
    default FsResponse delete(FsRequest request)
    {
        throw new UnsupportedOperationException(request.getFileName() + " does not support delete");
    }
}
