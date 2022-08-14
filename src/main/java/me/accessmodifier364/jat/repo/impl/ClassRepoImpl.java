package me.accessmodifier364.jat.repo.impl;

import me.accessmodifier364.jat.repo.Repo;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author accessmodifier364
 * @since 8/14/2022 at 00:14
 */

public class ClassRepoImpl implements Repo<ClassNode> {

    private static ClassRepoImpl instance;
    private final List<ClassNode> classes;

    public ClassRepoImpl() {
        this.classes = new ArrayList<>();
    }

    public static ClassRepoImpl getInstance() {
        if (instance == null) {
            instance = new ClassRepoImpl();
        }

        return instance;
    }

    @Override
    public List<ClassNode> listAll() {
        return classes;
    }
}