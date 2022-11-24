package org.udeshika.laundryroommanager.database.repositories;

import org.udeshika.laundryroommanager.models.User;

public interface UserRepository {
    boolean insert(User user);
    void deleteAll();
}
