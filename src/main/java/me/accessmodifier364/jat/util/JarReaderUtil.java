package me.accessmodifier364.jat.util;

import me.accessmodifier364.jat.gui.GraphicalInterface;
import me.accessmodifier364.jat.repo.impl.ClassRepoImpl;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * @author accessmodifier364
 * @since 4/18/2022 at 19:01
 */

public final class JarReaderUtil {

    /**
     * This method read the jar classes converting every class inside into ClassNode.
     *
     * @param jar The jar file with the classes.
     */
    public static void readJar(File jar) {
        if (!jar.exists()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Jar does not exist!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (!jar.getName().endsWith(".zip") && !jar.getName().endsWith(".jar")) {
            JOptionPane.showMessageDialog(
                    null,
                    "Is not a valid jar file!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        ClassRepoImpl.getInstance().listAll().clear();

        GraphicalInterface.textArea.setText(jar.getAbsolutePath());

        try (final JarInputStream jis = new JarInputStream(new ByteArrayInputStream(Files.readAllBytes(jar.toPath())))) {

            JarEntry jarEntry;
            while ((jarEntry = jis.getNextJarEntry()) != null) {
                final String name = jarEntry.getName();
                final ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
                int bytesRead;
                final byte[] tempBuffer = new byte[32767];

                while ((bytesRead = jis.read(tempBuffer)) != -1) {
                    streamBuilder.write(tempBuffer, 0, bytesRead);
                }

                if (name.endsWith(".class") || name.endsWith(".class/")) {
                    final ClassNode node = new ClassNode();
                    new ClassReader(streamBuilder.toByteArray()).accept(node, ClassReader.SKIP_FRAMES);

                    if (isClassExcluded(node)) {
                        continue;
                    }
                    ClassRepoImpl.getInstance().listAll().add(node);
                }
            }
        } catch (IOException e) {
            // Invalid CRC32 patch (taken from Recaf).
            if (e instanceof ZipException &&
                    e.getMessage().contains("invalid entry CRC")) {

                try (ZipFile zf = new ZipFile(jar)) {
                    final Enumeration<? extends ZipEntry> entries = zf.entries();
                    while (entries.hasMoreElements()) {
                        final ZipEntry entry = entries.nextElement();
                        final String name = entry.getName();

                        if (name.endsWith(".class") || name.endsWith(".class/")) {
                            try (final InputStream is = zf.getInputStream(entry)) {

                                final ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
                                int bytesRead;
                                final byte[] tempBuffer = new byte[32767];

                                while ((bytesRead = is.read(tempBuffer)) != -1) {
                                    streamBuilder.write(tempBuffer, 0, bytesRead);
                                }

                                final ClassNode node = new ClassNode();
                                new ClassReader(streamBuilder.toByteArray()).accept(node, ClassReader.SKIP_FRAMES);

                                if (isClassExcluded(node)) {
                                    continue;
                                }
                                ClassRepoImpl.getInstance().listAll().add(node);
                            }
                        }
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /**
     * Check if the checkers should ignore some classes.
     *
     * @param node The ClassNode to check if is excluded.
     * @return True or False.
     */
    private static boolean isClassExcluded(ClassNode node) {
        if (!GraphicalInterface.checkBox.isSelected()) {
            return false;
        }

        for (int i = 0; i < GraphicalInterface.model.getRowCount(); i++) {
            final String exclusion = (String) GraphicalInterface.model.getValueAt(i, 0);

            if (exclusion == null || exclusion.isEmpty()) continue;

            if (node.name.startsWith(exclusion)) {
                return true;
            }
        }

        return false;
    }
}