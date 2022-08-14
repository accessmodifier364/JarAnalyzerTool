package me.accessmodifier364.jat.models;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * @author accessmodifier364
 * @since 4/20/2022 at 06:29
 */

public final class WrapperNode {

    private final ClassNode classNode;
    private final MethodNode methodNode;
    private MethodInsnNode methodInsnNode;
    private AbstractInsnNode abstractInsnNode;

    public WrapperNode(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        this.classNode = classNode;
        this.methodNode = methodNode;
        this.methodInsnNode = methodInsnNode;
    }

    public WrapperNode(ClassNode classNode, MethodNode methodNode, AbstractInsnNode abstractInsnNode) {
        this.classNode = classNode;
        this.methodNode = methodNode;
        this.abstractInsnNode = abstractInsnNode;
    }

    public ClassNode getClassNode() {
        return classNode;
    }

    public MethodNode getMethodNode() {
        return methodNode;
    }

    public MethodInsnNode getMethodInsnNode() {
        return methodInsnNode;
    }

    public AbstractInsnNode getAbstractInsnNode() {
        return abstractInsnNode;
    }
}