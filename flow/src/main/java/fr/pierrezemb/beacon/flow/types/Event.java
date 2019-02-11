package fr.pierrezemb.beacon.flow.types;

import org.apache.flink.shaded.curator.org.apache.curator.shaded.com.google.common.base.Joiner;

import java.util.HashMap;

/**
 * {
 * 	"cloud": {
 * 		"cloud.host.down{host=D}": false,
 * 		"cloud.host.down{host=F}": false,
 * 		"cloud.host.down{host=E}": true
 *        },
 * 	"kubernetes": {
 * 		"kubernetes.host.down{host=A}": false,
 * 		"kubernetes.host.down{host=B}": false,
 * 		"kubernetes.host.down{host=C}": false
 *    }
 * }
 */

public class Event extends HashMap<String, AlertMessage> {


    public Event() {
        super();
    }


    @Override
    public String toString() {
        return Joiner.on('\n').withKeyValueSeparator(" -> \n").join(this);
    }
}
