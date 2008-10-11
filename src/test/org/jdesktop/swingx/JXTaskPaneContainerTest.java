/*
 * $Id$
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.plaf.UIResource;
import javax.swing.JPanel;

import junit.framework.TestCase;

import org.jdesktop.swingx.action.AbstractActionExt;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;



@RunWith(JUnit4.class)
public class JXTaskPaneContainerTest extends TestCase {

    @Test
  public void testAddon() throws Exception {
    // move around all addons
    TestUtilities.cycleAddons(new JXTaskPaneContainer());
  }

    /**
     * Issue #843-swingx: BasicTaskPaneContainerUI must respect custom Layout.
     */
    @Test
    public void testRespectCustomLayoutGap() {
        JXTaskPaneContainer container = new JXTaskPaneContainer();
        VerticalLayout layout = (VerticalLayout) container.getLayout();
        VerticalLayout custom = new VerticalLayout(layout.getGap() + 10);
        container.setLayout(custom);
        container.updateUI();
        assertEquals(custom.getGap(), ((VerticalLayout) container.getLayout())
                .getGap());
    }

    /**
     * Issue #843-swingx: BasicTaskPaneContainerUI must respect custom Layout.
     */
    @Test
    public void testRespectCustomLayout() {
        JXTaskPaneContainer container = new JXTaskPaneContainer();
        VerticalLayout layout = (VerticalLayout) container.getLayout();
        VerticalLayout custom = new VerticalLayout(layout.getGap() + 10);
        container.setLayout(custom);
        container.updateUI();
        assertSame(custom, container.getLayout());
    }

    /**
     * Issue #843-swingx: BasicTaskPaneContainerUI must respect custom Layout.
     */
    @Test
    public void testLayoutUIResource() {
        JXTaskPaneContainer container = new JXTaskPaneContainer();
        assertTrue(container.getLayout() instanceof UIResource);
    }

    private void fillTaskPane(JXTaskPane first) {
        first.add(new AbstractActionExt("some") {

            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

            }

        });

        first.add(new AbstractActionExt("other") {

            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

            }

        });
    }

}
