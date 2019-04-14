package fr.pierrezemb.beacon.flow.sources;

import fr.pierrezemb.beacon.flow.types.AlertEvent;
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

            int i = 0;
            for (String team: fakeTeamList) {
                for (String host: fakeHostList) {
                    String selector = createSelector("cpu.usage", Character.toString ((char) (65 + i)));
                    i++;
                    Boolean value = Math.random() < 0.5;
	                AlertEvent event = new AlertEvent(team, selector, value);
	                ctx.collectWithTimestamp(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event), System.currentTimeMillis());
                }
            }
        }
    }

    public String createSelector(String classname,String host) {
        return classname + "{host=" + host + "}";
    }

    @Override
    public void cancel() {
        this.shouldProduce = false;
    }
}
