/* 
 * <copyright>
 *  
 *  Copyright 2002-2004 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects
 *  Agency (DARPA).
 * 
 *  You can redistribute this software and/or modify it under the
 *  terms of the Cougaar Open Source License as published on the
 *  Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 * </copyright>
 */

package org.cougaar.pizza.plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.cougaar.core.mts.MessageAddress;
import org.cougaar.core.service.LoggingService;
import org.cougaar.core.util.UID;
import org.cougaar.core.util.UniqueObject;

/**
 * Stores mapping from friend to their pizza preference
 *
 * Counts meat and veg preferences.
 */

public class PizzaPreferences implements UniqueObject {
  protected UID uid;
  protected Map friendToPizza = new HashMap();
  private   LoggingService log;
  protected int numMeat = 0;
  protected int numVeg  = 0;

  // for UniqueObject interface
  public UID getUID() { return uid; }
  public void setUID(UID uid) { this.uid = uid; }

  public void addFriendToPizza (String friend, String preference) {
    friendToPizza.put(friend, preference);

    if (preference.toLowerCase().equals ("meat"))
      numMeat++;
    else if (preference.toLowerCase().equals ("veg"))
      numVeg++;
    else
      log.warn ("unknown preference " + preference + " for " + friend);
  }

  public String getPreferenceForFriend (String friend) {
    return (String) friendToPizza.get (friend);
  }

  public Set getFriends () { return friendToPizza.keySet(); }

  public String getFriendNames () { return friendToPizza.keySet().toString(); }
  public String getPreferenceValues () { return friendToPizza.values().toString(); }
  public String getFriendToPreference () { return friendToPizza.toString(); }

  public int getNumMeat() { return numMeat; }
  public int getNumVeg () { return numVeg; }

  public String toString () {
    return "Party guests pizza preferences : " + friendToPizza;
  }
}