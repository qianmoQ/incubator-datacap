package io.edurt.datacap.condor.io;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class AppendableObjectOutputStream
        extends ObjectOutputStream
{
    private boolean append;

    public AppendableObjectOutputStream(OutputStream out)
            throws IOException
    {
        super(out);
        this.append = false;
    }

    public AppendableObjectOutputStream(OutputStream out, boolean append)
            throws IOException
    {
        super(out);
        this.append = append;
    }

    @Override
    protected void writeStreamHeader()
            throws IOException
    {
        if (!append) {
            super.writeStreamHeader();
        }
    }
}
