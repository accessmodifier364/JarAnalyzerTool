package me.accessmodifier364.jat.checks;

import me.accessmodifier364.jat.models.AbstractCheck;
import me.accessmodifier364.jat.repo.impl.ClassRepoImpl;
import me.accessmodifier364.jat.util.ASMUtil;

/**
 * @author accessmodifier364
 * @since 4/18/2022 at 18:56
 */

public final class CommandCheck extends AbstractCheck {

    public CommandCheck() {
        super("Command Check", "Shell commands execution (can execute arbitrary code).");
    }

    @Override
    public void run() {
        ClassRepoImpl.getInstance().listAll().forEach(c -> ASMUtil.getMethodCallsInClass(c).forEach((wrapper) -> {
            switch (wrapper.getMethodInsnNode().owner + ":" + wrapper.getMethodInsnNode().name) {
                case "java/lang/Runtime:exec":
                case "java/lang/ProcessBuilder:command":
                case "java/lang/ProcessBuilder:<init>":
                    addAlert(Severity.HIGH, wrapper, "Shell command executed.");
                    break;
            }
        }));
    }
}