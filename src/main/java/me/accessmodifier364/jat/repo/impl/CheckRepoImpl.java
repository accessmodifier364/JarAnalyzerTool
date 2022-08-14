package me.accessmodifier364.jat.repo.impl;

import me.accessmodifier364.jat.checks.*;
import me.accessmodifier364.jat.models.AbstractCheck;
import me.accessmodifier364.jat.repo.Repo;

import java.util.Arrays;
import java.util.List;

/**
 * @author accessmodifier364
 * @since 8/14/2022 at 00:12
 */

public class CheckRepoImpl implements Repo<AbstractCheck> {

    private static CheckRepoImpl instance;
    private final List<AbstractCheck> checks;

    public CheckRepoImpl() {
        this.checks = Arrays.asList(
                new ClassLoadingCheck(),
                new CommandCheck(),
                new ConnectionCheck(),
                new FileDeletionCheck(),
                new NativeLibraryCheck(),
                new ReflectionCheck(),
                new URLCheck()
        );
    }

    public static CheckRepoImpl getInstance() {
        if (instance == null) {
            instance = new CheckRepoImpl();
        }

        return instance;
    }

    @Override
    public List<AbstractCheck> listAll() {
        return checks;
    }
}