package org.example;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.TableRowJsonCoder;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
// import org.example.MyOptions;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;



public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    public static TableSchema schema;
    public static TableSchema mySchema;
    public static  String maxCreatedDate;
    public static String newMaxDate;
    

    public static void main(String[] args) {
   


        PipelineOptionsFactory.register(MyOptions.class);
        MyOptions options = PipelineOptionsFactory.fromArgs(args)
                                    .withValidation()
                                    .as(MyOptions.class);
        Pipeline p2 = Pipeline.create(options);
      
        


        String query2= "select * from [dbo].[cssEnqComments]";
        System.out.println(query2);


        PCollection<TableRow> rows = p2.apply(JdbcIO.<TableRow>read()
                .withDataSourceConfiguration(JdbcIO.DataSourceConfiguration.create(
                                "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://10.8.41.127:21553;database=CLSrepl;")
                        .withUsername(options.getUsername())
                        .withPassword(options.getPassword()))
                .withQuery(query2)
                .withCoder(TableRowJsonCoder.of())
                .withRowMapper(new JdbcIO.RowMapper<TableRow>() {
                    @Override
                    public TableRow mapRow(ResultSet resultSet) throws Exception {
                        schema = getSchemaFromResultSet(resultSet);

                        TableRow tableRow = new TableRow();

                        List<TableFieldSchema> columnNames = schema.getFields();

                        for(int i =1; i<= resultSet.getMetaData().getColumnCount(); i++) {
                            tableRow.put(columnNames.get(i-1).get("name").toString(), String.valueOf(resultSet.getObject(i)));
                        }

                        return tableRow;
                    }
                })
        );

        // Write to BigQuery
        rows.apply(BigQueryIO.writeTableRows()
                .to("fg-dt-lumiq-dev:fg_dt_lumiq_dev_sql.CLSrepl_cssEnqComments")
                .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
                .withMethod(BigQueryIO.Write.Method.STORAGE_WRITE_API)
        );

        p2.run().waitUntilFinish();

    }


    private static TableSchema getSchemaFromResultSet(ResultSet resultSet) {
        FieldSchemaListBuilder fieldSchemaListBuilder = new FieldSchemaListBuilder();
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();

            for(int i=1; i <= rsmd.getColumnCount(); i++) {
                fieldSchemaListBuilder.stringField(resultSet.getMetaData().getColumnName(i));
            }
        }
        catch (SQLException ex) {
            LOG.error("Error getting metadata: " + ex.getMessage());
        }

        return fieldSchemaListBuilder.schema();
    }
}
