package org.example;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;

import java.util.ArrayList;
import java.util.List;

public class FieldSchemaListBuilder {

    public static final String INTEGER = "INTEGER";
    public static final String STRING = "STRING";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String BOOLEAN = "BOOLEAN";
    public static final String RECORD = "RECORD";
    public static final String REQUIRED = "REQUIRED";
    public static final String NULLABLE = "NULLABLE";
    public static final String REPEATED = "REPEATED";

    List<TableFieldSchema> schemaFields = new ArrayList<TableFieldSchema>();


    public static FieldSchemaListBuilder create() {
        return new FieldSchemaListBuilder();
    }

    public TableFieldSchema fieldSchema(FieldSchemaListBuilder list) {
        TableFieldSchema tfs = new TableFieldSchema();
        tfs.setType("RECORD");
        tfs.setFields(list.schemaFields);
        return tfs;
    }


    public TableFieldSchema fieldSchema(String type, String name, String mode) {
        return fieldSchema(type, name, mode, "");
    }


    public TableFieldSchema fieldSchema(String type, String name, String mode, String description) {
        TableFieldSchema tfs = new TableFieldSchema();
        tfs.setType(type);
        tfs.setName(name);
        tfs.setMode(mode);
        tfs.setDescription(description);
        return tfs;
    }


    public FieldSchemaListBuilder intField(String name, String mode, String description) {
        schemaFields.add(fieldSchema(INTEGER, name, mode, description));
        return this;
    }

    public FieldSchemaListBuilder intField(String name, String mode) {
        return intField(name, mode, "");
    }

    public FieldSchemaListBuilder intField(String name) {
        return intField(name, NULLABLE);
    }


    public FieldSchemaListBuilder stringField(String name, String mode, String description) {
        schemaFields.add(fieldSchema(STRING, name, mode, description));
        return this;
    }

    public FieldSchemaListBuilder stringField(String name, String mode) {
        return stringField(name, mode, "");
    }

    public FieldSchemaListBuilder stringField(String name) {
        return stringField(name, NULLABLE);
    }


    public FieldSchemaListBuilder boolField(String name, String mode, String description) {
        schemaFields.add(fieldSchema(BOOLEAN, name, mode, description));
        return this;
    }

    public FieldSchemaListBuilder boolField(String name, String mode) {
        return boolField(name, mode, "");
    }


    public FieldSchemaListBuilder boolField(String name) {
        return boolField(name, NULLABLE);
    }


    public FieldSchemaListBuilder timestampField(String name, String mode, String description) {
        schemaFields.add(fieldSchema(TIMESTAMP, name, mode, description));
        return this;
    }


    public FieldSchemaListBuilder timestampField(String name, String mode) {
        return timestampField(name, mode, "");
    }

    public FieldSchemaListBuilder timestampField(String name) {
        return timestampField(name, NULLABLE);
    }


    public FieldSchemaListBuilder field(TableFieldSchema field) {
        schemaFields.add(field);
        return this;
    }

    public TableFieldSchema repeatedRecord(String name) {
        TableFieldSchema tfs = fieldSchema(this);
        tfs.setName(name);
        tfs.setMode("REPEATED");
        return tfs;
    }

    public TableSchema schema() {
        TableSchema result = new TableSchema();
        result.setFields(schemaFields);
        return result;
    }
}
