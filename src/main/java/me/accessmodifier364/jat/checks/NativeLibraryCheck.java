package me.accessmodifier364.jat.checks;

import me.accessmodifier364.jat.models.AbstractCheck;
import me.accessmodifier364.jat.repo.impl.ClassRepoImpl;
import me.accessmodifier364.jat.util.ASMUtil;

/**
 * @author accessmodifier364
 * @since 4/18/2022 at 19:08
 */

public final class NativeLibraryCheck extends AbstractCheck {

    public NativeLibraryCheck() {
        super("Native Library Check", "Native libraries loading (can bypass other checks).");
    }

    @Override
    public void run() {
        ClassRepoImpl.getInstance().listAll().forEach(c -> ASMUtil.getMethodCallsInClass(c).forEach((wrapper) -> {
            switch (wrapper.getMethodInsnNode().owner + ":" + wrapper.getMethodInsnNode().name) {
                case "java/lang/System:loadLibrary":
                case "java/lang/System:load":
                case "java/lang/Runtime:loadLibrary":
                case "com/sun/jna/Native:loadLibrary":
                    addAlert(Severity.LOW, wrapper, "Native library loaded.");
                    break;
            }
        }));
    }
}