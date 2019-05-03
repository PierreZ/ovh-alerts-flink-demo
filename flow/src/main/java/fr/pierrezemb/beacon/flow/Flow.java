package fr.pierrezemb.beacon.flow;

import fr.pierrezemb.beacon.flow.operations.map.SetSince;
import fr.pierrezemb.beacon.flow.operations.map.StringToEvent;
import fr.pierrezemb.beacon.flow.sources.FakeSource;
import fr.pierrezemb.beacon.flow.types.AlertEvent;
import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Flow {
	public void start() throws Exception {

		// Retrieve an executionEnvironment
		Configuration config = new Configuration();
		config.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true);
		config.setBoolean("queryable-state.enable", true);
		StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config);
		env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);


		// Create a Stream from Source
		DataStreamSource<String> source = env.addSource(new FakeSource(2000));

		DataStream<AlertEvent> parsedEvents = source
				// Transform String to AlertEvent
				.map(StringToEvent::new)
				// split events per team
				.keyBy(AlertEvent::getTeam)
				// set Since on every event. This map is sharded per-team.
				.map(SetSince::new);

		// and print it
		parsedEvents.print();

		// Job needs to be started
		env.execute("beacon");
	}

}
