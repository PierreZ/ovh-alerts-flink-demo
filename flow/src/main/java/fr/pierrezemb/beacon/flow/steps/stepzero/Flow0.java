package fr.pierrezemb.beacon.flow.steps.stepzero;

import fr.pierrezemb.beacon.flow.sources.FakeSource;
import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Flow0 {
    public void start() throws Exception {

        // Retrieve an executionEnvironment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Configuration config = new Configuration();
        // config.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true);
        // StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config);

        // Create a Stream from Source
        DataStreamSource<String> source = env.addSource(new FakeSource(2000));

        // and print it
        source.print();

        // Job needs to be started
        env.execute("beacon");
    }

}
