package org.udeshika.laundryroommanager.services;

import org.udeshika.laundryroommanager.database.repositories.UserRepository;
import org.udeshika.laundryroommanager.models.User;

import java.util.logging.Logger;

/**
 * A service class responsible for managing application users.
 */
public class UserService {
    private final Logger logger = Logger.getLogger(UserService.class.getName());
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(User user) {
        return userRepository.insert(user);
    }

    /**
     * Remove all users for test purposes
     *
     * @return indicates whether successfully deleted
     */
    public void removeAllUsers() {
        userRepository.deleteAll();
    }
}