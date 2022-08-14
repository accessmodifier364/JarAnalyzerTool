package me.accessmodifier364.jat.gui;

import me.accessmodifier364.jat.Main;
import me.accessmodifier364.jat.gui.dialogs.ResultsDialog;
import me.accessmodifier364.jat.repo.impl.CheckRepoImpl;
import me.accessmodifier364.jat.repo.impl.ClassRepoImpl;
import me.accessmodifier364.jat.util.JarReaderUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @author accessmodifier364
 * @since 4/18/2022 at 18:50
 */

public final class GraphicalInterface extends JFrame {

    private static final Color color;
    public static final DefaultTableModel model;
    public static final JCheckBox checkBox;
    public static final JTextArea textArea;

    static {
        color = new Color(184, 217, 209, 255);
        model = new DefaultTableModel();
        checkBox = new JCheckBox();
        textArea = new JTextArea();
    }

    /**
     * <init> Method which initializes our GUI.
     *
     * @throws HeadlessException From parent.
     */
    public GraphicalInterface() throws HeadlessException {
        super("Jar Analyzer Tool v1.1");

        /* Set some attributes to our gui (icon, size, background color, look-and-feel). */
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(color);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/icon.png"))).getImage());
        setSize(450, 400);
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException ignored) {
        }

        /* Add the "Start" button which runs all the checks. */
        final JButton button = new JButton("Start");
        button.setBounds(getWidth() / 2 + 50, getHeight() - 100, 100, 35);
        button.addActionListener(e -> {
            if (!ClassRepoImpl.getInstance().listAll().isEmpty()) {
                Main.message = new StringBuilder();

                CheckRepoImpl.getInstance().listAll().forEach(c -> {
                    c.preRun();
                    c.run();
                    c.postRun();
                });
                new ResultsDialog(this);
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Select a Jar!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        add(button);

        /* Add the "Find jar" button which opens a file chooser dialog. */
        final JButton button2 = new JButton("Find jar");
        button2.setBounds(getWidth() / 4 - 60, getHeight() - 100, 120, 35);
        button2.addActionListener(e -> {
            final JFileChooser chooser = new JFileChooser(System.getProperty("user.home") + File.separator + "Desktop");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setFileFilter(new FileNameExtensionFilter("JAR & ZIP Files", "jar", "zip"));

            final int result = chooser.showOpenDialog(this);

            if (result != JFileChooser.CANCEL_OPTION) {
                final File jar = chooser.getSelectedFile();

                if (jar != null && !jar.getName().isEmpty()) {
                    JarReaderUtil.readJar(jar);
                }
            }
        });
        add(button2);

        /* Add the drag and drop file area */
        textArea.setBounds(8, getHeight() - 190, getWidth() - 30, 80);
        textArea.setText("\n\n\t                         DRAG JAR HERE");
        textArea.setEditable(false);
        textArea.setDropTarget(new DropTarget() {
            @SuppressWarnings("unchecked")
            @Override
            public synchronized void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    ((List<File>) dtde.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor))
                            .stream()
                            .findFirst()
                            .ifPresent(jar -> {
                                if (!jar.getName().isEmpty()) {
                                    JarReaderUtil.readJar(jar);
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(8, getHeight() - 190, getWidth() - 30, 80);
        add(scrollPane);

        /* Add the exclusions checkbox */
        checkBox.setText("Exclude");
        checkBox.setBackground(color);
        checkBox.setBounds(getWidth() - 110, 8, 100, 20);
        add(checkBox);

        /* Add the exclusions table */
        model.addColumn("Exclusions");
        model.addRow(new Object[]{"org/spongepowered/"});
        model.setNumRows(20);
        final JScrollPane scrollPane2 = new JScrollPane(new JTable(model));
        scrollPane2.setBounds(5, 10, 320, 150);
        add(scrollPane2);

        /* Adds the label in the center */
        final JLabel label = new JLabel("Made by accessmodifier364");
        label.setBounds(getWidth() / 2 - 110, 175, 240, 20);
        label.setFont(new Font("Lato", Font.ITALIC, 16));
        add(label);

        /* Shows the GUI */
        setVisible(true);
    }
}