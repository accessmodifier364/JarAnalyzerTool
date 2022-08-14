package me.accessmodifier364.jat.gui.dialogs;

import me.accessmodifier364.jat.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author accessmodifier364
 * @since 4/19/2022 at 23:14
 */

public final class ResultsDialog extends JDialog {

    /**
     * <init> Method which shows the output.
     *
     * @param owner The main frame (GraphicalInterface)
     */
    public ResultsDialog(Frame owner) {
        super(owner, "Output");

        /* Set some attributes to our dialog (size, not resizable). */
        setResizable(false);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/icon.png"))).getImage());
        setSize(1000, 600);

        /* Creates the text pane and write the output. */
        final JTextPane textPane = new JTextPane();
        textPane.setText(Main.message.toString());

        /* Make the text pane scrollable. */
        final JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setSize(getWidth(), getHeight());
        add(scrollPane);

        /* Shows the GUI */
        setVisible(true);
    }
}