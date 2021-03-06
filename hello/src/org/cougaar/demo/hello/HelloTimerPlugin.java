/*
 * <copyright>
 *  
 *  Copyright 1997-2012 BBNT Solutions, LLC
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

import org.cougaar.core.agent.service.alarm.Alarm;
import org.cougaar.core.plugin.TodoPlugin;
import org.cougaar.util.annotations.Cougaar;
import org.cougaar.util.annotations.Subscribe;

/** 
 * This plugin publishes a change to a HelloObject periodically, forever 
 */
public class HelloTimerPlugin
      extends TodoPlugin {

   @Cougaar.Arg(defaultValue = "1000", 
         description = "The millisecond period to publish a change to the hello message")
   public int periodMillis;

   @Cougaar.Arg(defaultValue = "Hello from timer",
         description = "Message to be published on blackboard")
   public String message;

   @SuppressWarnings("unused")
   private Alarm alarm;
   private HelloObject hello;

   // log Logging service initialized by parent ParameterizedPlugin 
   // uids UID service initialized by parent ParameterizedPlugin 

   /**
    * Get the HelloObject from the blackboard
    */
   @Cougaar.Execute(on = Subscribe.ModType.ADD)
   public void executeGetHello(HelloObject hello) {
      this.hello = hello;
      // Create a timer task that periodically updates the HelloObject
      alarm = executeLater(periodMillis, new UpdateHelloTask());
   }

   /*
    * Timer task updates the hello message, then restarts the timer.
    * executeLater runs the task in the execute transaction, so blackboard
    * objects can be modified during the task.
    */
   private final class UpdateHelloTask
         implements Runnable {
      public void run() {
         long value = hello.getValue();
         hello.setValue(++value);
         hello.setMessage(message);
         blackboard.publishChange(hello);
         log.shout("Timer incremented value to " + value );
         alarm = executeLater(periodMillis, new UpdateHelloTask());
      }
   }
}
