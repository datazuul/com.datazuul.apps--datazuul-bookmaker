package com.datazuul.apps.bookmaker.gui;

import com.datazuul.apps.bookmaker.Main;
import com.datazuul.apps.commons.gui.WindowSizer;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class AboutDialog extends JDialog {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    private JButton btnClose = null;

    /**
     * @param parent
     */
    public AboutDialog(JFrame parent) {
        super(parent, "About " + Main.PROGRAM_NAME, true);

        getContentPane().add(this.buildGUI());
        getRootPane().setDefaultButton(this.btnClose);

        pack();
        new WindowSizer().centerOnScreen(this);
        // new WindowSizer(300, 150).centerOnScreen(this);
    }

    private JPanel buildGUI() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(this.buildAboutPanel(), BorderLayout.CENTER);
        panel.add(this.buildButtonPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildAboutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        panel.add(new JViewport(), BorderLayout.WEST);
        panel.add(this.buildInfoPanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        EmptyBorder innerBorder = new EmptyBorder(5, 5, 5, 5);
        TitledBorder outerBorder = new TitledBorder(Main.VERSION);
        panel.setBorder(new CompoundBorder(outerBorder, innerBorder));

        panel.add(new JLabel("Written by Ralf Eichinger"));
        panel.add(new JLabel("ralf.eichinger@javapro.de"));
        panel.add(new JLabel("http://www.javapro.de/"));

        return panel;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        this.btnClose = new JButton("Close");
        this.btnClose.setMnemonic(KeyEvent.VK_C);
        this.btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        panel.add(this.btnClose);

        return panel;
    }
}
