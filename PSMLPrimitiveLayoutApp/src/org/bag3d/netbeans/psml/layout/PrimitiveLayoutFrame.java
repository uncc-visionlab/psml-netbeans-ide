/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.awt.ColorComboBox;

/**
 *
 * @author arwillis
 */
public class PrimitiveLayoutFrame extends JFrame implements ActionListener, ChangeListener {

    Dimension framesize;
    private final PrimitiveLayoutCanvas canvas = new PrimitiveLayoutCanvas(); //The component the user draws on
    private final JComponent preview = canvas.getBrushSizeView(); //A component in the toolbar that shows the paintbrush size
    private final JSlider brushSizeSlider = new JSlider(1, 24); //A slider to set the brush size
    private final JToolBar toolbar = new JToolBar(); //The toolbar
    private final ColorComboBox color = new ColorComboBox(); //Our color chooser component from the ColorChooser library
    private final JButton clear = new JButton(Bundle.LBL_Clear()); //A button to clear the canvas
    private final JLabel label = new JLabel(Bundle.LBL_Foreground()); //A label for the color chooser
    private final JLabel brushSizeLabel = new JLabel(Bundle.LBL_BrushSize()); //A label for the brush size slider
    private static int ct = 0; //A counter you use to provide names for new images

    public PrimitiveLayoutFrame() {
        super("PrimitiveLayour - Version 1.0");
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        Toolkit javakit = java.awt.Toolkit.getDefaultToolkit();
        Dimension scrsize = javakit.getScreenSize();
        System.out.println("Screen resolution = " + scrsize);
        if (scrsize.width > 1600) {
            scrsize.width = 1280;
            scrsize.height = 1024;
        }
        if (scrsize.height > 1200) {
            scrsize.width = 1280;
            scrsize.height = 1024;
        }
        framesize = new Dimension((int) (0.7f * scrsize.width), (int) (0.7f * scrsize.height));
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(canvas, BorderLayout.CENTER);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        System.setSecurityManager(null);
        PrimitiveLayoutFrame frame = new PrimitiveLayoutFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
