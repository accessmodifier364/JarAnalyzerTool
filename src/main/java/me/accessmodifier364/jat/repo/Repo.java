package me.accessmodifier364.jat.repo;

import java.util.List;

/**
 * @author accessmodifier364
 * @since 8/14/2022 at 00:11
 */

public interface Repo<T> {

    List<T> listAll();
}