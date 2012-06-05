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
package org.cougaar.tutorial.exercise5;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.DomainService;
import org.cougaar.planning.ldm.PlanningFactory;
import org.cougaar.planning.ldm.asset.Asset;
import org.cougaar.planning.ldm.plan.Allocation;
import org.cougaar.planning.ldm.plan.AllocationResult;
import org.cougaar.planning.ldm.plan.AspectType;
import org.cougaar.planning.ldm.plan.AspectValue;
import org.cougaar.planning.ldm.plan.Role;
import org.cougaar.planning.ldm.plan.RoleSchedule;
import org.cougaar.planning.ldm.plan.Task;
import org.cougaar.planning.ldm.plan.Verb;
import org.cougaar.tutorial.assets.ProgrammerAsset;
import org.cougaar.util.UnaryPredicate;

/**
 * This COUGAAR Plugin subscribes to tasks in a workflow and allocates the
 * workflow sub-tasks to programmer assets.
 * 
 * @author ALPINE (alpine-software@bbn.com)
 * @version $Id: DevelopmentAllocatorPlugin.java,v 1.3 2004-01-21 17:25:50 jwong
 *          Exp $
 **/
public class DevelopmentAllocatorPlugin
      extends ComponentPlugin {

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

   private IncrementalSubscription allCodeTasks; // Tasks that I'm interested in
   private IncrementalSubscription allProgrammers; // Programmer assets that I
                                                   // allocate to

   /**
    * Predicate matching all ProgrammerAssets
    */
   private UnaryPredicate allProgrammersPredicate = new UnaryPredicate() {
      private static final long serialVersionUID = 1L;

      public boolean execute(Object o) {
         return o instanceof ProgrammerAsset;
      }
   };

   /**
    * Predicate that matches all Test tasks
    */
   private UnaryPredicate codeTaskPredicate = new UnaryPredicate() {
      private static final long serialVersionUID = 1L;

      public boolean execute(Object o) {
         if (o instanceof Task) {
            Task task = (Task) o;
            return task.getVerb().equals(Verb.get("CODE"));
         }
         return false;
      }
   };

   /**
    * Establish subscription for tasks and assets
    **/
   @Override
   public void setupSubscriptions() {
      allProgrammers = (IncrementalSubscription) getBlackboardService().subscribe(allProgrammersPredicate);
      allCodeTasks = (IncrementalSubscription) getBlackboardService().subscribe(codeTaskPredicate);
   }

   /**
    * Top level plugin execute loop. Handle changes to my subscriptions.
    **/
   @Override
   public void execute() {
      System.out.println("DevelopmentAllocatorPlugin::execute");

      // process unallocated tasks
      Enumeration task_enum = allCodeTasks.elements();
      while (task_enum.hasMoreElements()) {
         Task task = (Task) task_enum.nextElement();
         if (task.getPlanElement() == null) {
            allocateTask(task);
         }
      }
   }

   /**
    * Find an available ProgrammerAsset for this task and allocate the the task
    * to this asset.
    */
   private void allocateTask(Task task) {
      // select an available programmer at random
      Vector programmers = new Vector(allProgrammers.getCollection());
      boolean allocated = false;
      while ((!allocated) && (programmers.size() > 0)) {
         int stuckee = (int) Math.floor(Math.random() * programmers.size());
         ProgrammerAsset asset = (ProgrammerAsset) programmers.elementAt(stuckee);
         programmers.remove(asset);

         System.out.println("\nAllocating the following task to " + asset.getTypeIdentificationPG().getTypeIdentification() + ": "
               + asset.getItemIdentificationPG().getItemIdentification());
         System.out.println("Task: " + task);

         // find the times and make the allocation result that
         // assigns these times
         AllocationResult estAR = new AllocationResult(1.0, true, findInterval(asset));

         Allocation allocation =
               ((PlanningFactory) getDomainService().getFactory("planning")).createAllocation(task.getPlan(), task, asset, estAR,
                                                                                              Role.ASSIGNED);

         getBlackboardService().publishAdd(allocation);
         allocated = true;
      }
   }

   /**
    * Find the three-month interval starting either the beginning of next month
    * or the end of the last task on the asset, and return an array of aspect
    * values indicating the time interval
    */
   private AspectValue[] findInterval(Asset asset) {
      // figure out time interval
      RoleSchedule sched = asset.getRoleSchedule();
      Date startDate = null;
      if (!sched.isEmpty()) {
         // startDate should be right after last task assigned
         startDate = new Date(sched.getEndTime());
      } else {
         // startDate should be first day of next month
         GregorianCalendar cal = new GregorianCalendar();
         cal.add(Calendar.MONTH, 1);
         GregorianCalendar cal2 = new GregorianCalendar();
         cal2.clear();
         cal2.set(Calendar.YEAR, cal.get(Calendar.YEAR));
         cal2.set(Calendar.MONTH, cal.get(Calendar.MONTH));
         startDate = cal2.getTime();
      }

      // set end time to be three months later
      GregorianCalendar cal3 = new GregorianCalendar();
      cal3.setTime(startDate);
      cal3.add(Calendar.MONTH, 3);
      Date endDate = cal3.getTime();

      // tell the dates chosen
      System.out.println(" start: " + startDate + " end: " + endDate);

      return new AspectValue[] {
         AspectValue.newAspectValue(AspectType.START_TIME, startDate.getTime()),
         AspectValue.newAspectValue(AspectType.END_TIME, endDate.getTime())
      };
   }

}
