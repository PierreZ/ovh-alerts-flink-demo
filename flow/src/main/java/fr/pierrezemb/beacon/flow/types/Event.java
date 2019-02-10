package fr.pierrezemb.beacon.flow.types;

import java.util.HashMap;

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

}
