package org.scriptorium.api.controllers;

import org.scriptorium.core.domain.User;
import org.scriptorium.core.services.UserService;

/**
 * Controller for handling user-related API requests.
 * This class exposes endpoints for retrieving user data.
 */
public class UserController extends CrudController<User, Long, UserService> {

    /**
     * Constructs a UserController.
     *
     * @param userService The service for user operations, injected via dependency injection.
     */
    public UserController(UserService userService) {
        super(userService);
    }

    @Override
    protected String getPathPrefix() {
        return "/api/users";
    }

    @Override
    protected String getEntityName() {
        return "User";
    }
}
