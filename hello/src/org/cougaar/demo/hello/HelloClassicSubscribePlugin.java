/*
 * <copyright>
 *  
 *  Copyright 1997-2006 BBNT Solutions, LLC
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
package org.cougaar.demo.hello;

import java.util.Collection;
import java.util.HashSet;

import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.plugin.AnnotatedSubscriptionsPlugin;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.util.annotations.Cougaar;
import org.cougaar.util.annotations.Subscribe;

/**
 * Example of old-style subscriptions.
 */
public class HelloClassicSubscribePlugin
      extends AnnotatedSubscriptionsPlugin {
   
   private IncrementalSubscription subscription;

   @Override
   /* Clunky old way to add subscriptions */
   protected void setupSubscriptions() {
      super.setupSubscriptions();
      
      UnaryPredicate predicate = new UnaryPredicate() {
         private static final long serialVersionUID = 1L;

         @Override
         public boolean execute(Object o) {
            return o instanceof HelloObject;
         }
      };
      subscription = new IncrementalSubscription(predicate, new HashSet<Object>());
      blackboard.subscribe(subscription);
      
   }

   @Override
   /* Clunky old way to process subscriptions */
   protected void execute() {
      super.execute();
      if (!subscription.isEmpty()) {
         @SuppressWarnings("unchecked")
         Collection<HelloObject> added = subscription.getAddedCollection();
         for (HelloObject hello : added) {
            log.shout(hello.getMessage() + " (classic) " + hello.getChangeCount());
         }
      }
   }



   /**
    * The execute annotation sets up a subscription that: 1) The blackboard
    * object of a specific type, here HelloObject 2) The blackboard object's
    * content has been modified, here changed or added 3) The blackboard
    * object's content matches a predicate, here blank
    * 
    * The body of the method will be run on the matching object The name of the
    * method is arbitrary, but convention uses a "execute" prefix because the
    * method will be run inside the execute event
    * 
    * Note, If multiple changes were made to the object before this plugin was
    * called, only the last value of the matching object will be used.
    * 
    */
   @Cougaar.Execute(on = {
      Subscribe.ModType.CHANGE,
      Subscribe.ModType.ADD
   })
   public void executeLogHello(HelloObject hello) {
      log.shout(hello.getMessage() + " " + hello.getChangeCount());
   }

}