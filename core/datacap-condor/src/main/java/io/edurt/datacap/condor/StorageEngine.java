package io.edurt.datacap.condor;

import io.edurt.datacap.condor.metadata.ColumnDefinition;
import io.edurt.datacap.condor.metadata.RowDefinition;
import io.edurt.datacap.condor.metadata.TableDefinition;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class StorageEngine
{
    private static final String DATA_DIR = "db_data/";
    private Map<String, TableDefinition> tableMetadata;
    private Map<String, List<RowDefinition>> tableData;

    public StorageEngine()
    {
        this.tableMetadata = new ConcurrentHashMap<>();
        this.tableData = new ConcurrentHashMap<>();
        initializeDataDirectory();
    }

    private void initializeDataDirectory()
    {
        File directory = new File(DATA_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void createTable(TableDefinition metadata)
    {
        tableMetadata.put(metadata.getTableName(), metadata);
        tableData.put(metadata.getTableName(), new ArrayList<>());
        persistTableMetadata(metadata);
    }

    public void insert(String tableName, RowDefinition row)
            throws Exception
    {
        TableDefinition metadata = tableMetadata.get(tableName);
        if (metadata == null) {
            throw new Exception("Table " + tableName + " does not exist");
        }

        validateRow(metadata, row);

        // 保存数据
        List<RowDefinition> rows = tableData.get(tableName);
        rows.add(row);

        // 持久化到文件
        persistData(tableName);
    }

    private void validateRow(TableDefinition metadata, RowDefinition row)
            throws Exception
    {
        for (ColumnDefinition column : metadata.getColumns()) {
            Object value = row.getValue(column.getName());

            // 检查非空约束
            if (!column.isNullable() && value == null) {
                throw new Exception("Column " + column.getName() + " cannot be null");
            }

            // 检查数据类型
            if (value != null) {
                switch (column.getType()) {
                    case INTEGER:
                        if (!(value instanceof Integer)) {
                            throw new Exception("Invalid type for column " + column.getName());
                        }
                        break;
                    case VARCHAR:
                        if (!(value instanceof String)) {
                            throw new Exception("Invalid type for column " + column.getName());
                        }
                        break;
                    case BOOLEAN:
                        if (!(value instanceof Boolean)) {
                            throw new Exception("Invalid type for column " + column.getName());
                        }
                        break;
                    case DOUBLE:
                        if (!(value instanceof Double)) {
                            throw new Exception("Invalid type for column " + column.getName());
                        }
                        break;
                }
            }
        }
    }

    // 将表的元数据持久化到文件
    private void persistTableMetadata(TableDefinition metadata)
    {
        try {
            File file = new File(DATA_DIR + metadata.getTableName() + ".meta");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(metadata);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 将数据持久化到文件
    private void persistData(String tableName)
    {
        try {
            File file = new File(DATA_DIR + tableName + ".data");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(tableData.get(tableName));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从文件加载数据
    @SuppressWarnings("unchecked")
    public void loadData(String tableName)
    {
        try {
            // 加载元数据
            File metaFile = new File(DATA_DIR + tableName + ".meta");
            if (metaFile.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(metaFile))) {
                    TableDefinition metadata = (TableDefinition) ois.readObject();
                    tableMetadata.put(tableName, metadata);
                }
            }

            // 加载数据
            File dataFile = new File(DATA_DIR + tableName + ".data");
            if (dataFile.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
                    List<RowDefinition> rows = (List<RowDefinition>) ois.readObject();
                    tableData.put(tableName, rows);
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
