package io.edurt.datacap.condor.utils;

import io.edurt.datacap.condor.io.AppendableObjectOutputStream;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ObjectStreamUtils
{
    private ObjectStreamUtils() {}

    public static void appendToFile(File file, Object object)
            throws IOException
    {
        boolean isNewFile = !file.exists() || file.length() == 0;

        try (FileOutputStream fos = new FileOutputStream(file, true);
                AppendableObjectOutputStream oos = new AppendableObjectOutputStream(fos, !isNewFile)) {
            oos.writeObject(object);
        }
    }

    public static List<Object> readAllFromFile(File file)
            throws IOException, ClassNotFoundException
    {
        List<Object> objects = new ArrayList<>();

        if (!file.exists() || file.length() == 0) {
            return objects;
        }

        try (FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis)) {

            while (true) {
                try {
                    Object obj = ois.readObject();
                    objects.add(obj);
                }
                catch (EOFException e) {
                    break;
                }
            }
        }

        return objects;
    }
}