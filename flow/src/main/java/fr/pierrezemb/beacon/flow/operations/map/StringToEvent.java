package fr.pierrezemb.beacon.flow.operations.map;

import fr.pierrezemb.beacon.flow.types.Event;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class StringToEvent implements MapFunction<String, Event> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Event map(String json) throws Exception {
        return objectMapper.readValue(json, Event.class);
    }
}
