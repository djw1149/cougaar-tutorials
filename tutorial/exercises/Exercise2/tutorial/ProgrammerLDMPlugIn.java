/*
 * <copyright>
 *  Copyright 1997-2000 Defense Advanced Research Projects
 *  Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 *  Raytheon Systems Company (RSC) Consortium).
 *  This software to be used only in accordance with the
 *  COUGAAR licence agreement.
 * </copyright>
 */
package tutorial;

import org.cougaar.core.plugin.*;
import tutorial.assets.*;
import org.cougaar.domain.planning.ldm.asset.Asset;
import org.cougaar.domain.planning.ldm.asset.NewItemIdentificationPG;
import org.cougaar.domain.planning.ldm.asset.ItemIdentificationPG;

/**
 * This ALP PlugIn creates and publishes ProgrammerAsset objects.
 * @author ALPINE (alpine-software@bbn.com)
 * @version $Id: ProgrammerLDMPlugIn.java,v 1.2 2000-12-18 15:40:58 wwright Exp $
 */

 // todo:  add code to make this a subclass
public class ProgrammerLDMPlugIn {
  /**
   * Used for initialization to populate the PLAN with ProgrammerAsset objects
   */
protected void setupSubscriptions() {

    // todo: Register our new PropertyFactory so we can refer to properties by name

    // todo: Create the prototypes that will be used to create the programmer assets

    // todo: Create an asset for programmer 'Bill Gates' based on an existing prototype

    // todo: Create an asset for programmer 'Linus Torvalds' based on an existing prototype

}

/**
 * todo: Create and populate a Language property group
 */
private LanguagePG makeALanguagePG(boolean knowsJava, boolean knowsJavaScript) {
}

/**
 * todo: Create and populate a Skills property group
 */
private SkillsPG makeASkillsPG(int yearsExperience, int productivity) {
}

/**
 * todo: Create and populate an ItemIdentification property group
 */
private ItemIdentificationPG makeAItemIdentificationPG(String name) {
}

/**
 * No subscriptions, so this method does nothing
 */
protected void execute () {}

}

