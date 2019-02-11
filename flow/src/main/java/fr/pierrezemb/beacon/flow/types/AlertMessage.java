package fr.pierrezemb.beacon.flow.types;


import org.apache.flink.shaded.curator.org.apache.curator.shaded.com.google.common.base.Joiner;

import java.util.HashMap;


public class AlertMessage extends HashMap<String, Boolean> {
    public AlertMessage() {
        super();
    }

    @Override
    public String toString() {
        return Joiner.on('\n').withKeyValueSeparator("  -> ").join(this);
    }
}
