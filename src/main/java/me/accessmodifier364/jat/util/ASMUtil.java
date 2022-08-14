package me.accessmodifier364.jat.util;

import me.accessmodifier364.jat.models.WrapperNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author accessmodifier364
 * @since 4/18/2022 at 19:51
 */

public final class ASMUtil {

    /**
     * This method get every method call inside a ClassNode.
     *
     * @param c The ClassNode.
     * @return A list of WrapperNode.
     */
    public static List<WrapperNode> getMethodCallsInClass(ClassNode c) {
        final List<WrapperNode> nodes = new ArrayList<>();

        c.methods.forEach(m -> m.instructions.forEach(i -> {
            if (i instanceof MethodInsnNode) {
                nodes.add(new WrapperNode(c, m, (MethodInsnNode) i));
            }
        }));

        return nodes;
    }

    /**
     * This method get every instruction inside a ClassNode.
     *
     * @param c The ClassNode.
     * @return A list of WrapperNode.
     */
    public static List<WrapperNode> getInstructionsInClass(ClassNode c) {
        final List<WrapperNode> nodes = new ArrayList<>();

        c.methods.forEach(m -> m.instructions.forEach(i -> nodes.add(new WrapperNode(c, m, i))));

        return nodes;
    }
}