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

package org.cougaar.pizza.plugin.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import org.cougaar.pizza.Constants;
import org.cougaar.planning.ldm.plan.Role;
import org.cougaar.servicediscovery.description.ServiceClassification;
import org.cougaar.servicediscovery.description.ServiceInfo;
import org.cougaar.servicediscovery.description.ServiceInfoScorer;

import org.cougaar.util.log.Logger;
import org.cougaar.util.log.Logging;

/**
 * ServiceDiscovery Service scoring function using <code>Role</code> name and an
 * exclusion list.<br>
 * <pre>
 * Uses 2 criteria -  
 *   service role must match scorer role
 *   service provider name must not match one of the names on the blacklist.
 *</pre><p>
 * All passing descriptions get a score of 1, all failing descriptions get
 * a score of -1.
 * <p>
 * <code>SDClientPlugin</code> creates the <code>RoleScorer</code> and attaches it to the <code>MMQueryRequest</code>.
 * <code>MatchmakerPlugin</code> uses the <code>RoleScorer</code> to evaluate service descriptions
 * returned from the yellow pages. All passing service descriptions are added
 * to the <code>MMQueryRequest</code> results field.
 * 
 */
public class RoleScorer implements ServiceInfoScorer, Serializable {
  // Note this is how a non-component can get a Logger
  private static Logger logger = Logging.getLogger(RoleScorer.class);
  Role myRole; // The role we want
  String myAgentName = null;
  Collection myBlacklist; // providers to exclude -- for example, those we've already tried

  public RoleScorer(Role role, Collection blacklist) {
    myRole = role;
    myBlacklist = blacklist;
  }

  /**
   * @return the Role required for this request
   **/
  public Role getRole() {
    return myRole;
  }
  

  /**
   * @return the Collection of excluded provider names (Strings)
   **/
  public Collection getBlacklist() {
    return myBlacklist;
  }

  /**
   * Will be called by MatchmakerPlugin for each ServiceInfo. Returned score will
   * be added to the ScoredServiceDescription associated with the Service.
   * 
   * @return int representing score. Client responsible for 
   * understanding the precise value. Current usage assumes lowest value >= 0
   * is the best. Values less than 0 indicate the provider is not suitable.
   * 
   */
  public int scoreServiceInfo(ServiceInfo serviceInfo) {
    // If the blacklist scoring says the provider is no good, its no good
    if (getBlacklistScore(serviceInfo) < 0) {
      return -1;
    }

    // Otherwise, we just use the role score
    int roleScore = getRoleScore(serviceInfo);

    if (logger.isDebugEnabled()) {
      logger.debug("scoreServiceProvider: Role score " + roleScore);
    }

    return roleScore;
  }

  // Score the role portion -- lowest non-negative score is best
  private int getRoleScore(ServiceInfo serviceInfo) {
    String serviceRole = null;

    // Find the correct service classification code
    for (Iterator iterator = serviceInfo.getServiceClassifications().iterator();
	 iterator.hasNext();) {
      ServiceClassification classification =
	(ServiceClassification) iterator.next();
      if (classification.getClassificationSchemeName().equals(Constants.UDDIConstants.COMMERCIAL_SERVICE_SCHEME)) {

	serviceRole = classification.getClassificationCode();
	break;
      }
    }

    if (serviceRole == null) {
      if (logger.isInfoEnabled()) {
	logger.info(myAgentName + 
		    ": Ignoring service with a bad service role for provider: " + 
		    serviceInfo.getProviderName());
      }
      return -1;
    } else if (!serviceRole.equals(myRole.toString())) {
      // If this is not the role we're looking for, score it down
      if (logger.isInfoEnabled()) {
	logger.info(myAgentName + ": Ignoring service with Role of : " +
		    serviceRole + 
		    " for provider: " + serviceInfo.getProviderName());
      }
      return -1;
    } else {
      // This is the role we're looking for!
      return 0;
    }
  }

  // Score the service provider relative to the blacklist - if it's blacklisted, it gets -1
  private int getBlacklistScore(ServiceInfo serviceInfo) {
    // Look for the serviceInfo's ProviderName on the blacklist
    for (Iterator iterator = myBlacklist.iterator();
	 iterator.hasNext();) {
      String blacklistedProvider = (String) iterator.next();

      if (serviceInfo.getProviderName().equals(blacklistedProvider)) {
	if (logger.isInfoEnabled()) {
	  logger.info(myAgentName + 
		      ": Ignoring service from provider - " + 
		      serviceInfo.getProviderName() +
		      ". Provider on the blacklist - " + myBlacklist);
	}
	return -1;
      }
    }

    // Didn't find it on the Blacklist -- its good
    return 0;
  }

  public boolean equals(Object o) {
    if (o instanceof RoleScorer) {
      RoleScorer scorer = (RoleScorer) o;

      return (scorer.getRole().equals(getRole()));
    } else {
      return false;
    }
  }

  public String toString() {
    return "<RoleScorer Role: " + myRole + ", Blacklist: " + myBlacklist + ">";
  }

}





