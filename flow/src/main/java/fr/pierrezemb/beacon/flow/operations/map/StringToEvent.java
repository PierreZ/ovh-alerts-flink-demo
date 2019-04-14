package fr.pierrezemb.beacon.flow.operations.map;

import fr.pierrezemb.beacon.flow.types.AlertEvent;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class StringToEvent implements MapFunction<String, AlertEvent> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AlertEvent map(String json) throws Exception {
        return objectMapper.readValue(json, AlertEvent.class);
    }
}
