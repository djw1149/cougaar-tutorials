/* =============================================================================
*
*                  COPYRIGHT 2007 BBN Technologies Corp.
*                  10 Moulton St
*                  Cambridge MA 02138
*                  (617) 873-8000
*
*       This program is the subject of intellectual property rights
*       licensed from BBN Technologies
*
*       This legend must continue to appear in the source code
*       despite modifications or enhancements by any party.
*
*
* =============================================================================
*
* Created : Sep 28, 2007
* Workfile: PingBBTesterPlugin.java
* $Revision: 1.1 $
* $Date: 2008-02-26 15:31:57 $
* $Author: jzinky $
*
* =============================================================================
*/
 
package org.cougaar.test.ping.regression;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cougaar.test.ping.Anova;
import org.cougaar.test.ping.PingQuery;
import org.cougaar.util.UnaryPredicate;

public class PingBBTesterPlugin extends PingTesterPlugin {
    
    protected Map<String, Anova> gatherStatistics() {
        Map<String, Anova> statistics = new HashMap<String, Anova>();
        @SuppressWarnings("unchecked")
        Collection<PingQuery> querys = blackboard.query(new IsPingQuery());
        try {
            for (PingQuery query : querys) {
                Anova statistic = (Anova) query.getStatistic();
                String sessionName=statistic.getName();
                statistics.put(sessionName, statistic.clone());
            }
        } catch (CloneNotSupportedException e) {
            log.error("Failed to clone a Statistic!");
            return null;
        }
        return statistics;
    }
   
    
    /**
     * Blackboard query predicate to gather all the relays
     * holding PingQuery objects.
     */
    private class IsPingQuery implements UnaryPredicate {
        public boolean execute(Object arg) {
            return arg instanceof PingQuery ;
        }
    }
}