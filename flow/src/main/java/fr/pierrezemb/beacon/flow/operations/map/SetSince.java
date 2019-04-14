package fr.pierrezemb.beacon.flow.operations.map;

import fr.pierrezemb.beacon.flow.types.AlertEvent;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetSince extends RichMapFunction<AlertEvent, AlertEvent> {

    private final Logger log = LoggerFactory.getLogger(SetSince.class);

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
    public AlertEvent map(AlertEvent event) throws Exception {

        long now = System.currentTimeMillis();
        long since = 0L;

        if (state.contains(event.getSelector())) {
            if (event.isActive()) {
                // state exists and running alert, fetching time
                since = state.get(event.getSelector());
                // log.debug("{} was already running, setting since to {}", event.getSelector(), since);
	            event.setSince(since);
            } else {
                // state exists but alert is no longer running, erasing it
                // log.debug( "{} is no longer in progress, deleting it from state", event.getSelector());
                state.remove(event.getSelector());
            }
        } else {
            if (event.isActive()) {
                // running alert and empty state, feeding up state
                state.put(event.getSelector(), now);
            }
        }
        return event;
    }
}
