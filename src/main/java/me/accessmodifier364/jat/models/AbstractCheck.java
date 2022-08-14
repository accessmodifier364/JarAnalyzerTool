package me.accessmodifier364.jat.models;

import me.accessmodifier364.jat.Main;
import org.objectweb.asm.tree.ClassNode;

/**
 * @author accessmodifier364
 * @since 4/18/2022 at 18:45
 */

abstract public class AbstractCheck {

    private final String name;
    private final String description;

    public AbstractCheck(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void preRun() {
        Main.message.append(name)
                .append(": ")
                .append(description)
                .append('\n');
    }

    abstract public void run();

    public void postRun() {
        Main.message.append('\n');
    }

    public void addAlert(Severity severity, WrapperNode wrapper, String message) {
        Main.message.append("Severity")
                .append(':')
                .append(severity.level)
                .append(" -> ")
                .append(wrapper.getClassNode().name)
                .append(':')
                .append(wrapper.getMethodNode().name)
                .append(" -> ")
                .append(message)
                .append('\n');
    }

    public void addAlert(Severity severity, ClassNode classNode, String message) {
        Main.message.append("Severity")
                .append(':')
                .append(severity.level)
                .append(" -> ")
                .append(classNode.name)
                .append(" -> ")
                .append(message)
                .append('\n');
    }

    public enum Severity {

        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High");

        private final String level;

        Severity(String level) {
            this.level = level;
        }
    }
}