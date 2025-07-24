package org.scriptorium.api.controllers;

import org.scriptorium.core.domain.User;
import org.scriptorium.core.services.UserService;

/**
 * Controller for handling user-related API requests.
 * This class exposes endpoints for retrieving user data.
 */
public class UserController extends CrudController<User, Long, UserService> {

    /**
     * Constructs a UserController with the necessary UserService.
     * I'm using dependency injection here to provide the service.
     * @param userService The service for user operations.
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
