/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects Agency (DARPA).
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the Cougaar Open Source License as published by
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).
 *
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 *  PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.
 * </copyright>
 */
package tutorial;

import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.blackboard.IncrementalSubscription;
import java.util.Enumeration;
import org.cougaar.util.UnaryPredicate;

/**
 * This UnaryPredicate matches all Job objects
 */
class myPredicate implements UnaryPredicate{
  public boolean execute(Object o) {
    return (o instanceof Job);
  }
}

/**
 * This COUGAAR PlugIn subscribes to Job objects and prints them out.
 * @author ALPINE (alpine-software@bbn.com)
 * @version $Id: WorkerPlugIn.java,v 1.4 2002-01-15 20:20:42 cbrundic Exp $
 **/
public class WorkerPlugIn extends ComponentPlugin {
  // holds my subscription for Job objects (matching predicate above)
  private IncrementalSubscription jobs;

  /**
   * Called when the PlugIn is loaded.  Establish the subscription for
   * Job objects
   */
protected void setupSubscriptions() {
  jobs = (IncrementalSubscription)getBlackboardService().subscribe(new myPredicate());
  System.out.println("WorkerPlugIn");
}

/**
 * Called when there is a change on my subscription(s).
 * This plugin just prints all new jobs to stdout
 */
protected void execute () {
  Enumeration new_jobs = jobs.getAddedList();
  while (new_jobs.hasMoreElements()) {
    Job job = (Job)new_jobs.nextElement();
    System.out.println("WorkerPlugIn got a new Job: " + job);
  }
}

}
