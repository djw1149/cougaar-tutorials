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

import tutorial.assets.*;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.core.agent.*;
import org.cougaar.core.domain.*;
import org.cougaar.core.blackboard.*;
import org.cougaar.core.mts.Message;
import org.cougaar.core.mts.MessageAddress;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.lib.planserver.*;
import java.io.*;
import java.util.*;


  /**
   * todo:  Create Predicate matching all ProgrammerAssets
   */






/**
 * This PSP responds with HTML tables showing the schedule maintained by
 * each programmer asset.
 * @author ALPINE (alpine-software@bbn.com)
 * @version $Id: PSP_ProgrammerSchedule.java,v 1.4 2002-01-15 20:19:33 cbrundic Exp $
 */

 // todo:  add code to make this a subclass implementing the needed interfaces

public class PSP_ProgrammerSchedule
{
  /** A zero-argument constructor is required for dynamically loaded PSPs,
   *         required by Class.newInstance()
   **/
  public PSP_ProgrammerSchedule()
  {
    super();
  }

  /**
   * This constructor includes the URL path as arguments
   */
  public PSP_ProgrammerSchedule( String pkg, String id ) throws RuntimePSPException
  {
    setResourceLocation(pkg, id);
  }

  /**
   * Some PSPs can respond to queries -- URLs that start with "?"
   * I don't respond to queries
   */
  public boolean test(HttpInput query_parameters, PlanServiceContext sc)
  {
    super.initializeTest(); // IF subclass off of PSP_BaseAdapter.java
    return false;  // This PSP is only accessed by direct reference.
  }


  /**
   * Called when a HTTP request is made of this PSP.
   * @param out data stream back to the caller.
   * @param query_parameters tell me what to do.
   * @param psc information about the caller.
   * @param psu unused.
   */
  public void execute( PrintStream out,
                       HttpInput query_parameters,
                       PlanServiceContext psc,
                       PlanServiceUtilities psu ) throws Exception
  {
    IncrementalSubscription subscription = null;
    try {
      System.out.println("PSP_ProgrammerSchedule called from " + psc.getSessionAddress());

      // todo: query the PLAN for ProgrammerAssets





      // todo: use dumpProgrammerSchedule to send each ProgrammerAsset
      //       to browser.  note that dumpProgrammerSchedule is defined below




    } catch (Exception ex) {
      out.println(ex.getMessage());
      ex.printStackTrace(out);
      System.out.println(ex);
      out.flush();
    }
  }


  /**
   * Print an HTML dump of this programmer's schedule to the PrintStream
   */
  private void dumpProgrammerSchedule(ProgrammerAsset pa, PrintStream out) {
      // todo: print programmer's name and a line break


      // get the schedule from the ProgrammerAsset (set s to the schedule)
      Schedule s = pa.getSchedule();


      // Sort the keys (months) for printing
      TreeSet ts = new TreeSet(s.keySet());
      Iterator iter = ts.iterator();

      int i = 0;
      while (iter.hasNext()) {
        Object key = iter.next();
        Object o = s.get(key);

        out.print(" "+i+++" ");
        if (o instanceof Task) {
          Task task = (Task)o;

          // todo:  print the verb and the item to be coded


        } else {
          out.print(o);
        }
        // the key is the month
        out.println("<br>");
      }
      out.flush();
  }

  /**
   * A PSP can output either HTML or XML (for now).  The server
   * should be able to ask and find out what type it is.
   **/
  public boolean returnsXML() {
    return false;
  }

  public boolean returnsHTML() {
    return true;
  }

  /**  Any PlanServiceProvider must be able to provide DTD of its
   *  output IFF it is an XML PSP... ie.  returnsXML() == true;
   *  or return null
   **/
  public String getDTD()  {
    return null;
  }

  /**
   * The UISubscriber interface. (not needed)
   */
  public void subscriptionChanged(Subscription subscription) {
  }
}

