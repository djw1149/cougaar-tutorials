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
package org.cougaar.tutorial.exercise6;

import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.DomainService;
import org.cougaar.planning.ldm.PlanningFactory;
import org.cougaar.planning.ldm.asset.Asset;
import org.cougaar.planning.ldm.asset.NewItemIdentificationPG;
import org.cougaar.planning.ldm.plan.AspectType;
import org.cougaar.planning.ldm.plan.AspectValue;
import org.cougaar.planning.ldm.plan.NewPrepositionalPhrase;
import org.cougaar.planning.ldm.plan.NewTask;
import org.cougaar.planning.ldm.plan.Preference;
import org.cougaar.planning.ldm.plan.ScoringFunction;
import org.cougaar.planning.ldm.plan.Task;
import org.cougaar.planning.ldm.plan.Verb;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

/**
 * This COUGAAR Plugin creates and publishes "CODE" tasks
 */
public class ManagerPlugin extends ComponentPlugin {

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

  // Two assets to use as direct objects for the CODE tasks
  private Asset what_to_code, what_else_to_code;

  /**
   * Using setupSubscriptions to create the initial CODE tasks
   */
protected void setupSubscriptions() {
  // Get the PlanningFactory from the DomainService
  PlanningFactory factory = (PlanningFactory)getDomainService().getFactory("planning");

  // Create a task to code the next killer app
  what_to_code = factory.createPrototype("AbstractAsset", "The Next Killer App");
  NewItemIdentificationPG iipg = (NewItemIdentificationPG)factory.createPropertyGroup("ItemIdentificationPG");
  iipg.setItemIdentification("e-somthing");
  what_to_code.setItemIdentificationPG(iipg);
  getBlackboardService().publishAdd(what_to_code);
  getBlackboardService().publishAdd(makeTask(what_to_code));

  // Create a task to code something java
  what_else_to_code = factory.createInstance(what_to_code);
  iipg = (NewItemIdentificationPG)factory.createPropertyGroup("ItemIdentificationPG");
  iipg.setItemIdentification("something java");
  what_else_to_code.setItemIdentificationPG(iipg);
  getBlackboardService().publishAdd(what_else_to_code);
  getBlackboardService().publishAdd(makeTask(what_else_to_code));

  // Create a task to code something java
  what_else_to_code = factory.createInstance(what_to_code);
  iipg = (NewItemIdentificationPG)factory.createPropertyGroup("ItemIdentificationPG");
  iipg.setItemIdentification("something big java");
  what_else_to_code.setItemIdentificationPG(iipg);
  getBlackboardService().publishAdd(what_else_to_code);
  getBlackboardService().publishAdd(makeTask(what_else_to_code));

  // Create a task to code something java
  what_else_to_code = factory.createInstance(what_to_code);
  iipg = (NewItemIdentificationPG)factory.createPropertyGroup("ItemIdentificationPG");
  iipg.setItemIdentification("distributed intelligent java agent");
  what_else_to_code.setItemIdentificationPG(iipg);
  getBlackboardService().publishAdd(what_else_to_code);
  getBlackboardService().publishAdd(makeTask(what_else_to_code));


}


/**
 * This Plugin has no subscriptions so this method does nothing
 */
protected void execute () {
}

/**
 * Create a CODE task.
 * @param what the direct object of the task
 */
protected Task makeTask(Asset what) {
    PlanningFactory factory = (PlanningFactory) getDomainService().getFactory("planning");

    NewTask new_task = factory.newTask();

    // Set the verb as given
    new_task.setVerb(Verb.get("CODE"));

    // Set the reality plan for the task
    new_task.setPlan(factory.getRealityPlan());

    new_task.setDirectObject(what);

    // Set up prepositions for task : "USING_LANGUAGE" "C++"
    // Prepositions can add information to how the task should be accomplished
    Vector prepositions = new Vector();

    NewPrepositionalPhrase npp = factory.newPrepositionalPhrase();
    npp.setPreposition("USING_LANGUAGE");
    npp.setIndirectObject("C++");
    prepositions.add(npp);
    new_task.setPrepositionalPhrases(prepositions.elements());

    // Establish preferences for task
    // Preferences specify when the task needs to be done
    Vector preferences = new Vector();

    // Add a start_time and end_time strict preference
    GregorianCalendar cal = new GregorianCalendar();
    cal.add (GregorianCalendar.MONTH, 1);
    GregorianCalendar cal2 = new GregorianCalendar();
    cal2.clear();
    cal2.set (Calendar.YEAR, cal.get (GregorianCalendar.YEAR));
    cal2.set (Calendar.MONTH, cal.get (GregorianCalendar.MONTH));
    double slope = 0.0000001;
    ScoringFunction scorefcn = ScoringFunction.createNearOrAbove
      (AspectValue.newAspectValue(AspectType.START_TIME,
       cal2.getTime().getTime()), slope);
    Preference pref =
      factory.newPreference(AspectType.START_TIME, scorefcn);
    preferences.add(pref);

    cal2.add (Calendar.YEAR, 1);
    scorefcn = ScoringFunction.createNearOrBelow
      (AspectValue.newAspectValue(AspectType.END_TIME,
       cal2.getTime().getTime()), slope);
    pref = factory.newPreference(AspectType.END_TIME, scorefcn);
    preferences.add(pref);

    double duration = 6;  // it will take them 6 months effort
    scorefcn = ScoringFunction.createStrictlyAtValue
      (AspectValue.newAspectValue(AspectType.DURATION, duration));
    pref = factory.newPreference(AspectType.DURATION, scorefcn);
    preferences.add(pref);

    new_task.setPreferences(preferences.elements());

    return new_task;
}

}
