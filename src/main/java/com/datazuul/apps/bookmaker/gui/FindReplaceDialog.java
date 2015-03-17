package com.datazuul.apps.bookmaker.gui;

import com.datazuul.apps.commons.gui.WindowSizer;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;

/**
 * @author Ralf Eichinger
 */
public class FindReplaceDialog extends JDialog {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    private Document txtContents = null;

    private JComboBox comboFind = null;
    private JComboBox comboReplaceWith = null;

    private JRadioButton radioUp = null;
    private JRadioButton radioDown = null;

    private JCheckBox checkCaseSensitive = null;
    private JCheckBox checkRegEx = null;

    private JButton btnFind = null;
    private JButton btnReplace = null;
    private JButton btnFindReplace = null;
    private JButton btnReplaceAll = null;
    private JButton btnCancel = null;

    public FindReplaceDialog(JFrame parent, Document txtContents) {
        super(parent, "Find/Replace");
        this.txtContents = txtContents;

        getContentPane().add(this.buildGUI());
        getRootPane().setDefaultButton(this.btnFind);

        pack();
        this.setResizable(false);
        new WindowSizer().centerOnScreen(this);
    }

    private JPanel buildGUI() {
        JPanel panel = new JPanel();

        panel.add(this.buildSearchPanel());
        panel.add(this.buildButtonPanel());

        return panel;
    }

    private JPanel buildSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());

        searchPanel.add(this.buildSearchInput(), BorderLayout.NORTH);
        searchPanel.add(this.buildSearchChoices(), BorderLayout.SOUTH);

        return searchPanel;
    }

    private JPanel buildSearchInput() {
        JPanel searchInput = new JPanel(new GridLayout(4, 1));

        this.comboFind = new JComboBox();
        this.comboFind.setEditable(true);

        this.comboReplaceWith = new JComboBox();
        this.comboReplaceWith.setEditable(true);

        searchInput.add(new JLabel("Find:"));
        searchInput.add(this.comboFind);
        searchInput.add(new JLabel("Replace with:"));
        searchInput.add(this.comboReplaceWith);

        return searchInput;
    }

    private JPanel buildSearchChoices() {
        JPanel searchChoices = new JPanel(new BorderLayout());

        searchChoices.add(this.buildDirections(), BorderLayout.WEST);
        searchChoices.add(this.buildOptions(), BorderLayout.EAST);

        return searchChoices;
    }

    private JPanel buildDirections() {
        JPanel directionPanel = new JPanel();
        BoxLayout layout = new BoxLayout(directionPanel, BoxLayout.Y_AXIS);
        directionPanel.setLayout(layout);

        EmptyBorder innerBorder = new EmptyBorder(3, 3, 3, 3);
        TitledBorder outerBorder = new TitledBorder("Direction");
        directionPanel.setBorder(new CompoundBorder(outerBorder, innerBorder));

        this.radioUp = new JRadioButton("Up");
        this.radioDown = new JRadioButton("Down");
        this.radioDown.setSelected(true);

        ButtonGroup directionGroup = new ButtonGroup();
        directionGroup.add(this.radioUp);
        directionGroup.add(this.radioDown);

        directionPanel.add(this.radioUp);
        directionPanel.add(this.radioDown);

        return directionPanel;
    }

    private JPanel buildOptions() {
        JPanel optionsPanel = new JPanel();
        BoxLayout layout = new BoxLayout(optionsPanel, BoxLayout.Y_AXIS);
        optionsPanel.setLayout(layout);

        EmptyBorder innerBorder = new EmptyBorder(3, 3, 3, 3);
        TitledBorder outerBorder = new TitledBorder("Options");
        optionsPanel.setBorder(new CompoundBorder(outerBorder, innerBorder));

        this.checkCaseSensitive = new JCheckBox("Case Sensitive");
        this.checkRegEx = new JCheckBox("Regular Expressions");

        optionsPanel.add(this.checkCaseSensitive);
        optionsPanel.add(this.checkRegEx);

        return optionsPanel;
    }

    private JPanel buildButtonPanel() {
        GridLayout layout = new GridLayout(4, 1);
        layout.setVgap(4);

        JPanel buttonPanel = new JPanel(layout);

        this.btnFind = new JButton("Find");
        this.btnReplace = new JButton("Replace");
        this.btnFindReplace = new JButton("Find/Replace");

        this.btnCancel = new JButton("Cancel");
        this.btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(this.btnFind);
        buttonPanel.add(this.btnReplace);
        buttonPanel.add(this.btnFindReplace);
        buttonPanel.add(this.btnCancel);

        return buttonPanel;
    }
}
