package fr.pierrezemb.beacon.flow.operations.aggregator;

import fr.pierrezemb.beacon.flow.types.Alert;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class AlertAggregator implements AggregateFunction<
        Tuple4<String, String, Alert, Long>, // IN
        HashMap<String, Tuple4<String, String, Alert, Long>>, // Accumulator
        List<Tuple4<String, String, Alert, Long>>>  { // Output


    @Override
    public HashMap<String, Tuple4<String, String, Alert, Long>> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Tuple4<String, String, Alert, Long>> add(Tuple4<String, String, Alert, Long> value, HashMap<String, Tuple4<String, String, Alert, Long>> accumulator) {
        accumulator.put(value.f1, value);
        return accumulator;
    }

    @Override
    public List<Tuple4<String, String, Alert, Long>> getResult(HashMap<String, Tuple4<String, String, Alert, Long>> accumulator) {
        return new ArrayList<>(accumulator.values());
    }


    @Override
    public HashMap<String, Tuple4<String, String, Alert, Long>> merge(HashMap<String, Tuple4<String, String, Alert, Long>> a, HashMap<String, Tuple4<String, String, Alert, Long>> b) {
        return null;
    }
}
