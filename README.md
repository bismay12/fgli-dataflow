# fgli-dataflow-code-dummy-dev

Dataflow templates can be created using a Maven command which builds the project and stages the template file on Google Cloud Storage. Any parameters passed at template build time will not be able to be overwritten at execution time.

```
mvn compile exec:java \
    -Dexec.mainClass=org.example.Main \
    -Dexec.args="--project=fg-dt-lumiq-dev \
        --region=asia-south1 \
        --runner=DataflowRunner \
        --gcpTempLocation=gs://dev_fgli/temp_files/ \
        --templateLocation=gs://fg-code/template/Dataflow/JDBC_to_BigQuery_v10"
```
