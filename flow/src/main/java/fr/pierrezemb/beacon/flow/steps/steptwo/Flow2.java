package fr.pierrezemb.beacon.flow.steps.steptwo;

import fr.pierrezemb.beacon.flow.operations.flatmap.StringToTuple;
import fr.pierrezemb.beacon.flow.sources.FakeSource;
import fr.pierrezemb.beacon.flow.types.Alert;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Flow2 {
    public void start() throws Exception {

        // Retrieve an executionEnvironment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Create a Stream from Source
        DataStreamSource<String> source = env.addSource(new FakeSource(1000));


        DataStream<Tuple3<String, String, Alert>> parsedEvents = source
                .flatMap(new StringToTuple());

        // and print it
        parsedEvents.print();

        // Job needs to be started
        env.execute();
    }

}
