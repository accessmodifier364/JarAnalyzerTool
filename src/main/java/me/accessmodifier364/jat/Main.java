package me.accessmodifier364.jat;

import me.accessmodifier364.jat.gui.GraphicalInterface;

/**
 * @author accessmodifier364
 * @since 4/18/2022 at 18:42
 */

public final class Main {

    public static StringBuilder message;

    /**
     * Main method which initializes our application.
     *
     * @param args Input arguments (we won't use this).
     */
    public static void main(String[] args) {
        new GraphicalInterface();
    }
}