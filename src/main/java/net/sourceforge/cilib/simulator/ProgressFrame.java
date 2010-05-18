/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.simulator;

import java.text.NumberFormat;
import net.sourceforge.cilib.algorithm.ProgressEvent;
import net.sourceforge.cilib.algorithm.ProgressListener;

/**
 *
 * @author  Edwin Peer
 */
final class ProgressFrame extends javax.swing.JFrame implements ProgressListener {

    private static final long serialVersionUID = 4007873302370282732L;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JProgressBar jProgressBar1;
    private NumberFormat nf;
    private int simulations;

    /** Creates new form ProgressFrame. */
    ProgressFrame(int simulations) {
        initComponents();
        jProgressBar1.setMinimum(0);
        jProgressBar1.setMaximum(100);
        jProgressBar1.setStringPainted(true);
        jProgressBar2.setMinimum(0);
        jProgressBar2.setMaximum(simulations);
        jProgressBar2.setStringPainted(true);
        nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(1);
        this.simulations = simulations;
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();
        jProgressBar2 = new javax.swing.JProgressBar();

        getContentPane().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        setTitle("Simulation Progress");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Current Simulation:");
        getContentPane().add(jLabel1);

        getContentPane().add(jProgressBar1);

        jLabel2.setText("All Simulations:");
        getContentPane().add(jLabel2);

        getContentPane().add(jProgressBar2);

        setBounds(0, 0, 165, 130);
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        System.exit(0);
    }

    @Override
    public void handleProgressEvent(ProgressEvent event) {
        double percentage = 100 * event.getPercentage();
        jProgressBar1.setValue((int) percentage);
        jProgressBar1.setString(nf.format(percentage) + "%");
    }

    @Override
    public void setSimulation(int simulation) {
        jProgressBar2.setValue(simulation);
        jProgressBar2.setString(String.valueOf(simulation) + "/" + String.valueOf(simulations));
    }
}
