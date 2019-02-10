package fr.pierrezemb.beacon.flow.operations.map;

import fr.pierrezemb.beacon.flow.types.Alert;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;

public class SetSince extends RichMapFunction<Tuple3<String, String, Alert>, Tuple4<String, String, Alert, Long>> {

    private final MapStateDescriptor<String, Long> stateDescriptor = new MapStateDescriptor<String, Long>(
            "running-alerts",
            TypeInformation.of(new TypeHint<String>(){}),
            TypeInformation.of(new TypeHint<Long>(){})
    );

    private MapState<String, Long> state;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        this.stateDescriptor.setQueryable("running-alerts");
        this.state = getRuntimeContext().getMapState(stateDescriptor);
    }

    @Override
    public Tuple4<String, String, Alert, Long> map(Tuple3<String, String, Alert> tuple) throws Exception {

        long now = System.currentTimeMillis();

        long since = 0L;

        if (state.contains(tuple.f1)) {
            if (tuple.f2.getActive()) {
                // state exists and running alert, fetching time

                since = state.get(tuple.f1);
                System.out.println(tuple.f2.getSelector() + " was already running, setting since to " + since);

            } else {
                // state exists but alert is no longer running, erasing it
                System.out.println(tuple.f2.getSelector() + " is no longer in progress, deleting it from state");
                state.remove(tuple.f1);
            }
        } else {
            if (tuple.f2.getActive()) {
                // running alert and empty state, feeding up state
                state.put(tuple.f1, now);
            }
        }

        return new Tuple4<>(tuple.f0, tuple.f1, tuple.f2, since);
    }
}
