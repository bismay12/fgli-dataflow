package org.example;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.ValueProvider;

public interface MyOptions extends PipelineOptions{

    ValueProvider<String> getUsername();
    void setUsername(ValueProvider<String> value);
    ValueProvider<String> getPassword();
	void setPassword(ValueProvider<String> value);
}
