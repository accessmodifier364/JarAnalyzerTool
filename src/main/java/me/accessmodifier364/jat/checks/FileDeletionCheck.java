package me.accessmodifier364.jat.checks;

import me.accessmodifier364.jat.models.AbstractCheck;
import me.accessmodifier364.jat.repo.impl.ClassRepoImpl;
import me.accessmodifier364.jat.util.ASMUtil;

/**
 * @author accessmodifier364
 * @since 4/18/2022 at 18:54
 */

public final class FileDeletionCheck extends AbstractCheck {

    public FileDeletionCheck() {
        super("File Deletion Check", "System files deletion (can erase important files).");
    }

    @Override
    public void run() {
        ClassRepoImpl.getInstance().listAll().forEach(c -> ASMUtil.getMethodCallsInClass(c).forEach((wrapper) -> {
            switch (wrapper.getMethodInsnNode().owner + ":" + wrapper.getMethodInsnNode().name) {
                case "java/io/File:delete":
                case "java/io/File:deleteOnExit":
                case "java/nio/file/Files:deleteIfExists":
                case "java/nio/file/Files:delete":
                case "org/apache/commons/io/FileUtils:forceDelete":
                case "org/apache/commons/io/FileUtils:forceDeleteOnExit":
                case "org/apache/commons/io/FileUtils:deleteDirectory":
                case "org/apache/commons/io/FileUtils:cleanDirectory":
                case "org/apache/commons/io/FileUtils:deleteQuietly":
                case "org/springframework/util/FileSystemUtils:deleteRecursively":
                    addAlert(Severity.LOW, wrapper, "File deletion.");
                    break;
            }
        }));
    }
}