/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
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

import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.planning.ldm.asset.Asset;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.*;
import java.util.*;
import org.cougaar.planning.ldm.PlanningFactory;

import tutorial.assets.*;


/**
 * This COUGAAR Plugin subscribes to tasks in a workflow and allocates
 * the workflow sub-tasks to programmer assets.
 * @author ALPINE (alpine-software@bbn.com)
 * @version $Id: DevelopmentAllocatorPlugin.java,v 1.5 2003-04-17 14:03:30 dmontana Exp $
 **/
public class DevelopmentAllocatorPlugin extends ComponentPlugin
{
  // The domainService acts as a provider of domain factory services
  private DomainService domainService = null;

  /**
   * Used by the binding utility through reflection to set my DomainService
   */
  public void setDomainService(DomainService aDomainService) {
    domainService = aDomainService;
  }

  /**
   * Used by the binding utility through reflection to get my DomainService
   */
  public DomainService getDomainService() {
    return domainService;
  }

  private IncrementalSubscription allCodeTasks;   // Tasks that I'm interested in
  private IncrementalSubscription allProgrammers;  // Programmer assets that I allocate to

  /**
   * Predicate matching all ProgrammerAssets
   */
  private UnaryPredicate allProgrammersPredicate = new UnaryPredicate() {
    public boolean execute(Object o) {
      return o instanceof ProgrammerAsset;
    }
  };

  /**
   * Predicate that matches all CODE tasks
   */
  private UnaryPredicate codeTaskPredicate = new UnaryPredicate() {
    public boolean execute(Object o) {
      if (o instanceof Task)
      {
        Task task = (Task)o;
        return task.getVerb().equals(Verb.getVerb("CODE"));
      }
      return false;
    }
  };


  /**
   * Establish subscription for tasks and assets
   **/
  public void setupSubscriptions() {
    allProgrammers =
      (IncrementalSubscription)getBlackboardService().subscribe(allProgrammersPredicate);
    allCodeTasks =
      (IncrementalSubscription)getBlackboardService().subscribe(codeTaskPredicate);
  }

  /**
   * Top level plugin execute loop.  Handle changes to my subscriptions.
   **/
  public void execute() {
    System.out.println("DevelopmentAllocatorPlugin::execute");

    // process unallocated tasks
    Enumeration task_enum = allCodeTasks.elements();
    while (task_enum.hasMoreElements()) {
      Task task = (Task)task_enum.nextElement();
      if (task.getPlanElement() == null)
        allocateTask(task);
    }

  }

  /**
   * Find an available ProgrammerAsset for this task.  Task must be scheduled
   * after the month "after"
   */
  private void allocateTask(Task task) {
    // todo:  get start_time, end_time and duration from preferences
    long earliest = 0L; // todo: fill in
    int duration = 0; // todo: fill in
    long latest = 0L; // todo: fill in

    // select an available programmer at random
    Vector programmers = new Vector(allProgrammers.getCollection());
    boolean allocated = false;
    while ((!allocated) && (programmers.size() > 0)) {
      int stuckee = (int)Math.floor(Math.random() * programmers.size());
      ProgrammerAsset asset = (ProgrammerAsset)programmers.elementAt(stuckee);
      programmers.remove(asset);

      System.out.println("\nAllocating the following task to "
          +asset.getTypeIdentificationPG().getTypeIdentification()+": "
          +asset.getItemIdentificationPG().getItemIdentification());
      System.out.println("Task: "+task);

      // find the times and make the allocation result that
      // assigns these times
      // if can't fit, go on to next programmer
      AspectValue[] inter = findInterval (asset, earliest, latest, duration);
      if (inter == null)
        continue;
      AllocationResult estAR = new AllocationResult (1.0, true, inter);

      Allocation allocation =
        ((PlanningFactory)getDomainService().getFactory("planning")).
          createAllocation(task.getPlan(), task,
                                  asset, estAR, Role.ASSIGNED);

      getBlackboardService().publishAdd(allocation);
      allocated = true;
    }
  }

  /**
   * Find the three-month interval starting either the beginning of
   * next month or the end of the last task on the asset, and
   * return an array of aspect values indicating the time interval
   */
  private AspectValue[] findInterval (Asset asset, long earliest,
                                      long latest, int durationMonths) {
    // figure out time interal, inserting at earliest possible time
    RoleSchedule sched = asset.getRoleSchedule();
    long start = sched.isEmpty() ? earliest :
                 Math.max (earliest, sched.getEndTime());
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime (new Date (start));
    cal.add (GregorianCalendar.MONTH, durationMonths);
    long end = cal.getTime().getTime();
    String str = " start: " + new Date (start) + " end: " + new Date (end);

    // check that does not violate constraint
    if (end > latest) {
      System.out.println (" Cannot schedule with" + str);
      return null;
    }

    // tell the dates chosen and return the aspect values
    System.out.println (str);
    return new AspectValue[] {
      AspectValue.newAspectValue (AspectType.START_TIME, start),
      AspectValue.newAspectValue (AspectType.END_TIME, end) };
  }

}

