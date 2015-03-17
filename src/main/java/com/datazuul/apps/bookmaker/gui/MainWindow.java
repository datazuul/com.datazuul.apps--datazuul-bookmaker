package com.datazuul.apps.bookmaker.gui;

import com.datazuul.apps.bookmaker.Main;
import com.datazuul.apps.commons.Datazuul;
import com.datazuul.apps.commons.gui.WindowSizer;
import com.pdfjet.A4;
import com.pdfjet.Font;
import com.pdfjet.Image;
import com.pdfjet.ImageType;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

public class MainWindow extends JFrame implements WindowListener {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    final private JFrame mainWindow = this;
    private File file = null;
    private boolean isModified = false;

    private JMenuItem mnuItemNew = null;
    private JMenuItem mnuItemOpen = null;
    private JMenuItem mnuItemSave = null;
    private JMenuItem mnuItemSaveAs = null;
    private JMenuItem mnuItemPrint = null;

    private JMenuItem mnuItemUndo = null;
    private JMenuItem mnuItemCut = null;
    private JMenuItem mnuItemCopy = null;
    private JMenuItem mnuItemPaste = null;
    private JMenuItem mnuItemDelete = null;
    private JMenuItem mnuItemSelectAll = null;
    private JMenuItem mnuItemInvertSelection = null;
    private JMenuItem mnuItemFindReplace = null;
    private JMenuItem mnuItemFindNext = null;
    private JMenuItem mnuItemFindPrev = null;
    private JMenuItem mnuItemGoTo = null;
    private JMenuItem mnuItemTimeDate = null;

    private JMenuItem mnuItemFont = null;
    private JMenuItem mnuItemColor = null;

    private JTextArea txtArea = null;
    private ThumbnailsPanel thumbnailsPanel = null;

    private Document txtContents = null;

    private JPanel statusBar = null;
    private JLabel statusLabelLeft = null;
    private JLabel statusLabelRight = null;

    public MainWindow() {
        super();
        this.buildGUI();
        this.setTitle("Untitled - " + Main.PROGRAM_NAME);
        setVisible(true);
    }

    public MainWindow(File file) {
        super();
        this.buildGUI();
        this.file = file;
        this.setTitle(file.getName() + " - " + Main.PROGRAM_NAME);
        setVisible(true);
    }

    private void buildGUI() {
        getContentPane().add(this.buildMainPanel());
        setJMenuBar(this.buildMenuBar());

        addWindowListener(this);
        new WindowSizer(800, 500).centerOnScreen(this);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(this.buildFileMenu());
        menuBar.add(this.buildImageMenu());
        // menuBar.add(this.buildEditMenu());
        // menuBar.add(this.buildFormatMenu());
        // menuBar.add(this.buildViewMenu());
        menuBar.add(this.buildHelpMenu());

        return menuBar;
    }

    private JMenu buildFileMenu() {
        JMenu mnuFile = new JMenu("File");
        mnuFile.setMnemonic(KeyEvent.VK_F);

        this.mnuItemNew = new JMenuItem("New", KeyEvent.VK_N);
        this.mnuItemNew.setIcon(new ImageIcon(Datazuul.class.getResource("images/New16.gif")));
        this.mnuItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        this.mnuItemNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (confirmSaveFile()) {
                    case JOptionPane.YES_OPTION:
                        // save();
                        break;

                    case JOptionPane.NO_OPTION:
                        file = null;
                        break;

                    case JOptionPane.CANCEL_OPTION:
                        // Don't do anything
                        break;
                }
            }
        });

        final JFrame mainWindow = this;
        this.mnuItemOpen = new JMenuItem("Open...", KeyEvent.VK_O);
        this.mnuItemOpen.setIcon(new ImageIcon(Datazuul.class.getResource("images/Open16.gif")));
        this.mnuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        this.mnuItemOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (confirmSaveFile()) {
                    case JOptionPane.YES_OPTION:
                        if (save()) {
                            open();
                        }
                        break;

                    case JOptionPane.NO_OPTION:
                        open();
                        break;

                    case JOptionPane.CANCEL_OPTION:
                        // Don't do anything
                        break;
                }
            }
        });

        this.mnuItemSave = new JMenuItem("Save", KeyEvent.VK_S);
        this.mnuItemSave.setIcon(new ImageIcon(Datazuul.class.getResource("images/Save16.gif")));
        this.mnuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        this.mnuItemSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        this.mnuItemSave.setEnabled(false);

        this.mnuItemSaveAs = new JMenuItem("Save as...", KeyEvent.VK_A);
        // this.mnuItemSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
        // ActionEvent.CTRL_MASK));
        this.mnuItemSaveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });

        this.mnuItemPrint = new JMenuItem("Print...", KeyEvent.VK_P);
        this.mnuItemPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        this.mnuItemPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrinterJob printJob = PrinterJob.getPrinterJob();
                // printJob.setPrintable(txtArea.getDocument());
                printJob.printDialog();
            }
        });

        JMenuItem mnuItemQuit = new JMenuItem("Quit", KeyEvent.VK_Q);
        mnuItemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        mnuItemQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Only ask the user a question if the buffer was modified
                if (isModified) {
                    switch (confirmSaveFile()) {
                        /*
                         * The user wants to save the file, so let them do so. We
                         * will exit the program only on a successful save
                         */
                        case JOptionPane.YES_OPTION:
                            if (save()) {
                                System.exit(1);
                            }
                            break;

                        // The user does not want to save the file, so let's
                        // just exit
                        case JOptionPane.NO_OPTION:
                            System.exit(1);
                            break;

                        case JOptionPane.CANCEL_OPTION:
                            // Don't do anything
                            break;
                    }
                } else {
                    System.exit(1);
                }
            }
        });

        mnuFile.add(this.mnuItemNew);
        mnuFile.add(this.mnuItemOpen);
        mnuFile.add(this.mnuItemSave);
        mnuFile.add(this.mnuItemSaveAs);
        mnuFile.addSeparator();
        mnuFile.add(this.mnuItemPrint);
        mnuFile.addSeparator();
        mnuFile.add(mnuItemQuit);

        return mnuFile;
    }

    private JMenu buildImageMenu() {
        JMenu mnuImage = new JMenu("Image");
        mnuImage.setMnemonic(KeyEvent.VK_I);

        JMenu mnuImageTransform = new JMenu("Transform");
        mnuImageTransform.setMnemonic(KeyEvent.VK_T);

        JMenuItem mnuItem1 = new JMenuItem("Rotate 90° clockwise");
        mnuItem1.setMnemonic(KeyEvent.VK_C);
        mnuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }

        });
        mnuImageTransform.add(mnuItem1);

        JMenuItem mnuItem2 = new JMenuItem("Rotate 90° counter-clockwise");
        mnuItem2.setMnemonic(KeyEvent.VK_W);
        mnuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }

        });
        mnuImageTransform.add(mnuItem2);

        JMenuItem mnuItem3 = new JMenuItem("Rotate 180°");
        mnuItem3.setMnemonic(KeyEvent.VK_1);
        mnuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }

        });
        mnuImageTransform.add(mnuItem3);

        mnuImage.add(mnuImageTransform);
        return mnuImage;
    }

    private boolean save() {
        if (file == null) {
            return saveAs();
        }

        System.out.println("Save the file.");
        return true;
    }

    private boolean saveAs() {
        JFileChooser fileDialog = new JFileChooser();
        int response = fileDialog.showSaveDialog(this.mainWindow);

        switch (response) {
            case JFileChooser.APPROVE_OPTION:
                System.out.println("Save the file.");
                File selectedFile = fileDialog.getSelectedFile();
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(selectedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }

                PDF pdf;
                try {
                    pdf = new PDF(fos);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

                Font f1;
                try {
                    f1 = new Font(pdf, "Helvetica");
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

                Vector<ThumbControl> thumbnails = this.thumbnailsPanel.getThumbnails();
                for (Iterator iterator = thumbnails.iterator(); iterator.hasNext();) {
                    ThumbControl thumbControl = (ThumbControl) iterator.next();
                    File imageFile = thumbControl.file;
                    FileInputStream fin = null;
                    try {
                        fin = new FileInputStream(imageFile);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Image image = null;
                    BufferedInputStream bin = new BufferedInputStream(fin);
                    try {
                        image = new Image(pdf, bin, ImageType.JPEG);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Page page = null;
                    try {
                        page = new Page(pdf, A4.PORTRAIT);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    double width = image.getWidth();
                    if (width > A4.PORTRAIT[0]) {
                        image.scaleBy(A4.PORTRAIT[0] / width);
                    }
                    image.setPosition(0, 0);
                    try {
                        image.drawOn(page);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                try {
                    pdf.flush();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return true;

            case JFileChooser.ERROR_OPTION:
                String errorMessage = "Unable to save the file.";
                JOptionPane.showMessageDialog(this.mainWindow, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                return false;

            case JFileChooser.CANCEL_OPTION:
            default:
                System.out.println("Cancel save as.");
                return false;
        }
    }

    private void open() {
        JFileChooser fileDialog = new JFileChooser();
        int response = fileDialog.showOpenDialog(this.mainWindow);
        switch (response) {
            case JFileChooser.APPROVE_OPTION:
                file = fileDialog.getSelectedFile();

                FileReader fin = null;
                try {
                    fin = new FileReader(file);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                int filesize = (int) file.length();
                char[] data = new char[filesize];
                try {
                    fin.read(data, 0, filesize);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String text = new String(data);
                txtArea.setText(text);
                System.out.println(file.getAbsolutePath());
                break;

            default:
                break;
        }
    }

    private JMenu buildEditMenu() {
        JMenu mnuEdit = new JMenu("Edit");
        mnuEdit.setMnemonic(KeyEvent.VK_E);

        this.mnuItemUndo = new JMenuItem("Undo");

        this.mnuItemCut = new JMenuItem(this.txtArea.getActionMap().get(DefaultEditorKit.cutAction));
        this.mnuItemCut.setText("Cut");
        this.mnuItemCut.setMnemonic(KeyEvent.VK_X);
        this.mnuItemCut.setIcon(new ImageIcon(Datazuul.class.getResource("images/Cut16.gif")));
        this.mnuItemCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        this.mnuItemCut.setEnabled(false);

        this.mnuItemCopy = new JMenuItem(this.txtArea.getActionMap().get(DefaultEditorKit.copyAction));
        this.mnuItemCopy.setText("Copy");
        this.mnuItemCopy.setMnemonic(KeyEvent.VK_C);
        this.mnuItemCopy.setIcon(new ImageIcon(Datazuul.class.getResource("images/Copy16.gif")));
        this.mnuItemCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        this.mnuItemCopy.setEnabled(false);

        this.mnuItemPaste = new JMenuItem(this.txtArea.getActionMap().get(DefaultEditorKit.pasteAction));
        this.mnuItemPaste.setText("Paste");
        this.mnuItemPaste.setMnemonic(KeyEvent.VK_V);
        this.mnuItemPaste.setIcon(new ImageIcon(Datazuul.class.getResource("images/Paste16.gif")));
        this.mnuItemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        this.mnuItemPaste.setEnabled(false);

        this.mnuItemDelete = new JMenuItem("Delete");

        this.mnuItemFindReplace = new JMenuItem("Find/Replace...");
        this.mnuItemFindReplace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FindReplaceDialog(mainWindow, null).setVisible(true);
            }
        });

        this.mnuItemFindNext = new JMenuItem("Find Next");
        this.mnuItemFindPrev = new JMenuItem("Find Previous");

        this.mnuItemGoTo = new JMenuItem("Go To...");
        this.mnuItemGoTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputValue = JOptionPane.showInputDialog(mainWindow,
                        "Line number:", "Go To Line", JOptionPane.QUESTION_MESSAGE);
            }
        });

        this.mnuItemSelectAll = new JMenuItem("Select All", KeyEvent.VK_A);
        this.mnuItemSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        // this.mnuItemSelectAll.setEnabled(false);
        this.mnuItemSelectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtArea.selectAll();
            }
        });

        this.mnuItemInvertSelection = new JMenuItem("Invert Selection", KeyEvent.VK_I);
        // this.mnuItemInvertSelection.setEnabled(false);

        this.mnuItemTimeDate = new JMenuItem("Time/Date");
        this.mnuItemTimeDate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        this.mnuItemTimeDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR);
                int minute = now.get(Calendar.MINUTE);
                String padded_minute = (minute < 10) ? ("0" + minute) : String.valueOf(minute);
                String am_pm = (now.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM";

                int month = now.get(Calendar.MONTH) + 1;
                int day = now.get(Calendar.DAY_OF_MONTH);
                int year = now.get(Calendar.YEAR);

                String date = hour + ":" + padded_minute + " " + am_pm + " "
                        + month + "/" + day + "/" + year;
                txtArea.insert(date, txtArea.getCaretPosition());
            }
        });

        mnuEdit.add(this.mnuItemUndo);
        mnuEdit.addSeparator();
        mnuEdit.add(this.mnuItemCut);
        mnuEdit.add(this.mnuItemCopy);
        mnuEdit.add(this.mnuItemPaste);
        mnuEdit.add(this.mnuItemDelete);
        mnuEdit.addSeparator();
        mnuEdit.add(this.mnuItemFindReplace);
        mnuEdit.add(this.mnuItemFindNext);
        mnuEdit.add(this.mnuItemFindPrev);
        mnuEdit.add(this.mnuItemGoTo);
        mnuEdit.addSeparator();
        mnuEdit.add(this.mnuItemSelectAll);
        mnuEdit.add(this.mnuItemInvertSelection);
        mnuEdit.addSeparator();
        mnuEdit.add(this.mnuItemTimeDate);

        return mnuEdit;
    }

    private JMenu buildFormatMenu() {
        JMenu mnuFormat = new JMenu("Format");
        mnuFormat.setMnemonic(KeyEvent.VK_M);

        JCheckBoxMenuItem mnuItemWordWrap = new JCheckBoxMenuItem("Word Wrap");
        mnuItemWordWrap.setMnemonic(KeyEvent.VK_W);
        mnuItemWordWrap.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                txtArea.setWrapStyleWord(!txtArea.getWrapStyleWord());
            }
        });

        this.mnuItemFont = new JMenuItem("Font...");
        this.mnuItemFont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        this.mnuItemColor = new JMenuItem("Color...");
        this.mnuItemColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color fontColor = JColorChooser.showDialog(mainWindow,
                        "Choose a font color", null);
            }
        });

        mnuFormat.add(mnuItemWordWrap);
        mnuFormat.add(this.mnuItemFont);
        mnuFormat.add(this.mnuItemColor);

        return mnuFormat;
    }

    private JMenu buildViewMenu() {
        JMenu mnuView = new JMenu("View");
        mnuView.setMnemonic(KeyEvent.VK_V);

        JCheckBoxMenuItem mnuItemStatusBar = new JCheckBoxMenuItem("Status bar", true);
        mnuItemStatusBar.setMnemonic(KeyEvent.VK_S);
        mnuItemStatusBar.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                statusBar.setVisible(!statusBar.isVisible());
            }
        });

        mnuView.add(mnuItemStatusBar);

        return mnuView;
    }

    private JMenu buildHelpMenu() {
        JMenu mnuHelp = new JMenu("Help");
        mnuHelp.setMnemonic(KeyEvent.VK_H);

        JMenuItem mnuItemHelpAbout = new JMenuItem("About " + Main.PROGRAM_NAME + "...", KeyEvent.VK_A);
        mnuItemHelpAbout.setIcon(new ImageIcon(Datazuul.class.getResource("images/Help16.gif")));
        mnuItemHelpAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        mnuItemHelpAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AboutDialog(mainWindow).setVisible(true);
            }
        });
        mnuHelp.add(mnuItemHelpAbout);

        return mnuHelp;
    }

    private JPanel buildMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // panel.add(this.buildTextArea(), BorderLayout.CENTER);
        this.thumbnailsPanel = new ThumbnailsPanel(new File(
                "/mnt/fileserver/Dateien-Ralf/Projekte/Alexandria/buch-scans/Deutsches_Einheits-Familienstammbuch_(1938)"));
        JScrollPane scroll = new JScrollPane(thumbnailsPanel);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(this.buildStatusBar(), BorderLayout.SOUTH);

        return panel;
    }

    private JScrollPane buildTextArea() {
        this.txtArea = new JTextArea();
        this.txtArea.setDragEnabled(true);
        this.txtArea.setMargin(new Insets(4, 4, 4, 4));

        JScrollPane scrollPane = new JScrollPane(this.txtArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        return scrollPane;
    }

    private JPanel buildStatusBar() {
        this.statusBar = new JPanel(new BorderLayout());

        EmptyBorder innerBorder = new EmptyBorder(5, 5, 5, 5);
        BevelBorder outerBorder = new BevelBorder(BevelBorder.LOWERED);

        this.statusLabelLeft = new JLabel(Main.PROGRAM_NAME + " by Ralf Eichinger");
        this.statusLabelLeft.setBorder(new CompoundBorder(outerBorder, innerBorder));
        this.statusLabelRight = new JLabel("Line 1, Column 1");
        this.statusLabelRight.setBorder(new CompoundBorder(outerBorder, innerBorder));

        this.statusBar.add(statusLabelLeft, BorderLayout.CENTER);
        this.statusBar.add(statusLabelRight, BorderLayout.EAST);

        return this.statusBar;
    }

    private int confirmSaveFile() {
        if (!this.isModified) {
            return JOptionPane.NO_OPTION;
        }

        String filename = (this.file == null) ? "Untitled" : this.file
                .getName();
        String question = "The text in the " + filename + " file has changed.\nDo you want to save the changes?";
        int response = JOptionPane.showConfirmDialog(this.mainWindow, question,
                "File Modified", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        return response;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        this.updateWindowList();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        this.updateWindowList();
    }

    private void updateWindowList() {
        // this.windowList.remove(this);
        // if (this.windowList.isEmpty()) {
        dispose();
        // }
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }
}
