package org.scriptorium.api.controllers;

import io.javalin.http.Context;
import org.scriptorium.core.services.UserService;

/**
 * Controller for handling user-related API requests.
 * This class exposes endpoints for retrieving user data.
 */
public class UserController {

    private final UserService userService;

    /**
     * Constructs a UserController with the necessary UserService.
     * I'm using dependency injection here to provide the service.
     * @param userService The service for user operations.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles the GET /api/users request.
     * Retrieves all users using the UserService and returns them as JSON.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getAll(Context ctx) {
        ctx.json(userService.findAllUsers());
    }

    /**
     * Handles the GET /api/users/{id} request.
     * Retrieves a single user by ID using the UserService and returns it as JSON.
     * If the user is not found, it returns a 404 status.
     * @param ctx The Javalin Context object for handling the request and response.
     */
    public void getOne(Context ctx) {
        Long userId = Long.parseLong(ctx.pathParam("id"));
        userService.findUserById(userId)
                .ifPresentOrElse(
                        ctx::json,
                        () -> ctx.status(404).result("User not found")
                );
    }
}
