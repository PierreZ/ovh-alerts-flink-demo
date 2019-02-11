package fr.pierrezemb.beacon.flow.operations.aggregator;


import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlertAggregator implements AggregateFunction<
        Tuple4<String, String, Boolean, Long>, // IN
        HashMap<String, Tuple4<String, String, Boolean, Long>>, // Accumulator
        ArrayList<Tuple4<String, String, Boolean, Long>>> { // Output


    @Override
    public HashMap<String, Tuple4<String, String, Boolean, Long>> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Tuple4<String, String, Boolean, Long>> add(Tuple4<String, String, Boolean, Long> value, HashMap<String, Tuple4<String, String, Boolean, Long>> accumulator) {
        accumulator.put(value.f1, value);
        return accumulator;
    }

    @Override
    public ArrayList<Tuple4<String, String, Boolean, Long>> getResult(HashMap<String, Tuple4<String, String, Boolean, Long>> accumulator) {
        return new ArrayList<>(accumulator.values());
    }


    @Override
    public HashMap<String, Tuple4<String, String, Boolean, Long>> merge(HashMap<String, Tuple4<String, String, Boolean, Long>> a, HashMap<String, Tuple4<String, String, Boolean, Long>> b) {
        return null;
    }
}
