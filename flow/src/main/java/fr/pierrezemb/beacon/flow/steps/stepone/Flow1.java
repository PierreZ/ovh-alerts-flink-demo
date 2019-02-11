package fr.pierrezemb.beacon.flow.steps.stepone;

import fr.pierrezemb.beacon.flow.operations.map.StringToEvent;
import fr.pierrezemb.beacon.flow.sources.FakeSource;
import fr.pierrezemb.beacon.flow.types.Event;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Flow1 {
    public void start() throws Exception {

        // Retrieve an executionEnvironment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        // Create a Stream from Source
        DataStreamSource<String> source = env.addSource(new FakeSource(1000));

        DataStream<Event> parsedEvents = source.map(new StringToEvent());

        // and print it
        parsedEvents.print();

        // Job needs to be started
        env.execute("beacon");
    }

}
