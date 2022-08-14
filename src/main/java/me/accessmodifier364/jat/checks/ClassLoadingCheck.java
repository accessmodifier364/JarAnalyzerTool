package me.accessmodifier364.jat.checks;

import me.accessmodifier364.jat.models.AbstractCheck;
import me.accessmodifier364.jat.repo.impl.ClassRepoImpl;
import me.accessmodifier364.jat.util.ASMUtil;

/**
 * @author accessmodifier364
 * @since 4/18/2022 at 18:56
 */

public final class ClassLoadingCheck extends AbstractCheck {

    public ClassLoadingCheck() {
        super("Class Loading Check", "Dynamically load classes into JVM (can bypass other checks).");
    }

    @Override
    public void run() {
        ClassRepoImpl.getInstance().listAll().forEach(c -> {
            ASMUtil.getMethodCallsInClass(c).forEach((wrapper) -> {
                switch (wrapper.getMethodInsnNode().name) {
                    case "defineClass":
                    case "defineAnonymousClass":
                        addAlert(Severity.HIGH, wrapper, "Dynamically defined class.");
                        break;
                }
            });

            if (c.superName != null) {
                switch (c.superName) {
                    case "java/lang/ClassLoader":
                    case "java/security/SecureClassLoader":
                    case "java/net/URLClassLoader":
                    case "net/minecraft/launchwrapper/LaunchClassLoader":
                    case "net/minecraftforge/fml/common/ModClassLoader":
                    case "org/spongepowered/tools/agent/MixinAgentClassLoader":
                        addAlert(Severity.HIGH, c, "Is son of " + c.superName);
                        break;
                }
            }
        });
    }
}