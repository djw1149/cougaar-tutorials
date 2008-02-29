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
* Created : Aug 14, 2007
* Workfile: PingReply.java
* $Revision: 1.1 $
* $Date: 2008-02-26 15:31:56 $
* $Author: jzinky $
*
* =============================================================================
*/
 
package org.cougaar.test.ping;

import org.cougaar.core.mts.MessageAddress;
import org.cougaar.core.service.UIDService;
import org.cougaar.core.util.UniqueObjectBase;

public class PingReply extends UniqueObjectBase {
    private int count;
    private MessageAddress senderAgent;
    private String senderPlugin;
    private MessageAddress receiverAgent;
    private String receiverPlugin;
 

   public PingReply(UIDService uids,
                     int count,
                     MessageAddress originatorAgent,
                     String orginatorPlugin,
                     MessageAddress targetAgent,
                     String targetPlugin) {
        super(uids.nextUID());
        this.count = count;
        this.senderAgent = originatorAgent;
        this.senderPlugin = orginatorPlugin;
        this.receiverAgent = targetAgent;
        this.receiverPlugin = targetPlugin;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public MessageAddress getSenderAgent() {
        return senderAgent;
    }

    public String getSenderPlugin() {
        return senderPlugin;
    }

    public MessageAddress getReceiverAgent() {
        return receiverAgent;
    }

    public String getReceiverPlugin() {
        return receiverPlugin;
    }

    public void setReceiverAgent(MessageAddress logicalServerAddress) {
        receiverAgent = logicalServerAddress;
    }
}