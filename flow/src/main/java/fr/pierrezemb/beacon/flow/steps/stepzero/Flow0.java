package fr.pierrezemb.beacon.flow.steps.stepzero;

import fr.pierrezemb.beacon.flow.sources.FakeSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Flow0 {
    public void start() throws Exception {

        // Retrieve an executionEnvironment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Create a Stream from Source
        DataStreamSource<String> source = env.addSource(new FakeSource(1000));

        // and print it
        source.print();

        // Job needs to be started
        env.execute();
    }

}
