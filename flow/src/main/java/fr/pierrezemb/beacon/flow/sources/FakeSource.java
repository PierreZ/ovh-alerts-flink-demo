package fr.pierrezemb.beacon.flow.sources;

import com.esotericsoftware.kryo.util.ObjectMap;
import fr.pierrezemb.beacon.flow.types.Alert;
import fr.pierrezemb.beacon.flow.types.AlertMessage;
import fr.pierrezemb.beacon.flow.types.Event;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FakeSource implements SourceFunction<String> {

    private boolean shouldProduce = true;
    private int wait = 1000;

    private static final List<String> fakeHostList =
            Collections.unmodifiableList(Arrays.asList("a", "b", "c"));

    private static final List<String> fakeTeamList =
            Collections.unmodifiableList(Arrays.asList("kubernetes", "cloud"));

    private final ObjectMapper objectMapper = new ObjectMapper();

    public FakeSource(int wait) {
        this.wait = wait;
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        while (shouldProduce) {
            TimeUnit.MILLISECONDS.sleep(wait);

            Event event = new Event();
            for (String team: fakeTeamList) {
                AlertMessage alertMessage = new AlertMessage();
                for (String host: fakeHostList) {
                    String selector = createSelector(team, "host.down", host);
                    Boolean value = Math.random() < 0.5;
                   alertMessage.put(selector, new Alert(selector,  value));
                }
                event.put(team, alertMessage);
            }

            ctx.collectWithTimestamp(objectMapper.writeValueAsString(event), System.currentTimeMillis());
        }
    }

    public String createSelector(String team, String classname,String host) {
        return team + "." + classname + "{host=" + host + "}";
    }

    @Override
    public void cancel() {
        this.shouldProduce = false;
    }
}
