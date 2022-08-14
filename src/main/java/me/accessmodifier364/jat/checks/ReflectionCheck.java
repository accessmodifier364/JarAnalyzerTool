package me.accessmodifier364.jat.checks;

import me.accessmodifier364.jat.models.AbstractCheck;
import me.accessmodifier364.jat.repo.impl.ClassRepoImpl;
import me.accessmodifier364.jat.util.ASMUtil;

/**
 * @author accessmodifier364
 * @since 4/18/2022 at 18:54
 */

public final class ReflectionCheck extends AbstractCheck {

    public ReflectionCheck() {
        super("Reflection Check", "Can dynamically load classes into resources.");
    }

    @Override
    public void run() {
        ClassRepoImpl.getInstance().listAll().forEach(c -> ASMUtil.getMethodCallsInClass(c).forEach((wrapper) -> {
            if (wrapper.getMethodInsnNode().name.equals("getDeclaredField")) {
                addAlert(Severity.MEDIUM, wrapper, "Accesses field using Reflection.");
            } else if (wrapper.getMethodInsnNode().name.equals("getDeclaredMethod")) {
                addAlert(Severity.MEDIUM, wrapper, "Accesses method using Reflection.");
            }
        }));
    }
}