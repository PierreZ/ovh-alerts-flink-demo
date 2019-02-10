package fr.pierrezemb.beacon.flow.types;

import org.apache.flink.shaded.curator.org.apache.curator.shaded.com.google.common.base.Joiner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.StringJoiner;

/**
 * {
 * 	"kubernetes": {
 * 		"host.down{host=a}": {
 * 			" selector": "host.down{host=a}",
 * 			" active": false
 *                },
 * 		"host.down{host=c}": {
 * 			" selector": "host.down{host=c}",
 * 			" active": false
 *        },
 * 		"host.down{host=b}": {
 * 			" selector": "host.down{host=b}",
 * 			" active": true
 *        }* 	},
 * 	"cloud": {
 * 		"host.down{host=a}": {
 * 			" selector": "host.down{host=a}",
 * 			" active": false
 *        },
 * 		"host.down{host=c}": {
 * 			" selector": "host.down{host=c}",
 * 			" active": true
 *        },
 * 		"host.down{host=b}": {
 * 			" selector": "host.down{host=b}",
 * 			" active": false
 *        }* 	}
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
