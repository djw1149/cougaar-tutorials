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
package org.cougaar.tutorial.exercise3;

import java.util.*;

import org.cougaar.util.UnaryPredicate;

import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.service.DomainService;
import org.cougaar.planning.ldm.PlanningFactory;
import org.cougaar.planning.ldm.asset.Asset;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.glm.ldm.asset.Organization;
import org.cougaar.glm.ldm.asset.OrganizationPG;

import org.cougaar.tutorial.assets.*;

/**
 * This COUGAAR Plugin creates and publishes "CODE" tasks
 */
// todo:  add code to make this a subclass
public class ManagerPlugin {

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
   * todo: Use setupSubscriptions to create the initial CODE tasks
   */
  protected void setupSubscriptions() {
    // todo: Create a task to code "the next killer app"
    // todo: Part 1: create the AbstractAsset representing the project.
    //       Store it in instance variable 'what_to_code'

    // todo: Part 2: put the 'what_to_code' asset on the Blackboard

    // todo: Part 3: create and publish a task to CODE the 'what_to_code' asset
    //       (complete and use the makeTask function which is later in this file)

    // todo: Create an task to code "something java"
    //       (store it in instance variable 'what_else_to_code')
  }


  /**
   * This Plugin has no subscriptions so this method does nothing
   */
  protected void execute () {
  }

  /**
   * This is a convenience funciont that should create a CODE task.
   * @param what the direct object of the task
   */
  protected Task makeTask(Asset what) {
    NewTask new_task = null;

    // todo: create the task with the verb "CODE"
    // new_task =



    return new_task;
  }

}
