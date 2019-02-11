package fr.pierrezemb.beacon.flow.steps.stepfour;

import fr.pierrezemb.beacon.flow.operations.aggregator.AlertAggregator;
import fr.pierrezemb.beacon.flow.operations.flatmap.StringToTuple;
import fr.pierrezemb.beacon.flow.operations.map.SetSince;
import fr.pierrezemb.beacon.flow.sources.FakeSource;
import fr.pierrezemb.beacon.flow.types.Alert;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.Window;

import java.util.List;

public class Flow4 {
    public void start() throws Exception {

        Configuration config = new Configuration();
        config.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config);
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        // Create a Stream from Source
        DataStreamSource<String> source = env.addSource(new FakeSource(1000));


        DataStream<Tuple3<String, String, Alert>> parsedEvents = source
                .flatMap(new StringToTuple());


        DataStream<Tuple4<String, String, Alert, Long>> tupleWithSince = parsedEvents
                .keyBy(0) // scoping next map per namespace
                .map(new SetSince());

        DataStream<List<Tuple4<String, String, Alert, Long>>> aggregatedAlerts = tupleWithSince
                .keyBy(0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .aggregate(new AlertAggregator());

        // and print it

        aggregatedAlerts.print();

        // Job needs to be started
        env.execute("beacon");
    }

}
