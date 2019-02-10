package fr.pierrezemb.beacon.flow.operations.flatmap;


import fr.pierrezemb.beacon.flow.types.Alert;
import fr.pierrezemb.beacon.flow.types.Event;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.util.Collector;

import java.awt.*;

public class StringToTuple implements FlatMapFunction<String, Tuple3<String, String, Alert>> {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void flatMap(String json, Collector<Tuple3<String, String, Alert>> out) throws Exception {

        Event event = objectMapper.readValue(json, Event.class);
        event.forEach((namespace, alerts) ->
                alerts.forEach((selector, alert) -> out.collect(new Tuple3<>(namespace, selector, alert))));
    }
}
