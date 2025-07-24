package org.scriptorium.api.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.scriptorium.core.exceptions.DataAccessException;
import org.scriptorium.core.services.BaseService;

import java.util.List;
import java.util.Optional;

/**
 * Abstract base class for CRUD (Create, Retrieve, Update, Delete) operations via API endpoints.
 * This class provides generic implementations for common REST endpoints (GET all, GET by ID).
 * Subclasses need to define the entity type, ID type, and provide the specific service.
 * They also need to implement `getPathPrefix()` to define the base URL for their resources.
 *
 * @param <T> The entity type (e.g., Book, Author).
 * @param <ID> The ID type of the entity (e.g., Long).
 * @param <S> The specific BaseService implementation for the entity.
 */
public abstract class CrudController<T, ID, S extends BaseService<T, ID>> {

    protected final S service;

    /**
     * Constructs a CrudController with the provided service.
     * @param service The service responsible for business logic of the entity.
     */
    public CrudController(S service) {
        this.service = service;
    }

    /**
     * Defines the base path for the API resource (e.g., "/api/books").
     * @return The base path as a String.
     */
    protected abstract String getPathPrefix();

    /**
     * Registers the common CRUD routes for this controller with the Javalin app.
     * Subclasses can override this to add more specific routes or modify behavior.
     * @param app The Javalin instance to register routes with.
     */
    public void registerRoutes(Javalin app) {
        app.get(getPathPrefix(), this::getAll);
        app.get(getPathPrefix() + "/{id}", this::getOne);
        // Add POST, PUT, DELETE methods here later if needed
    }

    /**
     * Handles GET all entities request.
     * Retrieves all entities using the service and returns them as JSON.
     * @param ctx The Javalin Context object.
     */
    public void getAll(Context ctx) {
        try {
            List<T> entities = service.findAll();
            ctx.json(entities);
        } catch (DataAccessException e) {
            ctx.status(500).result("Error retrieving entities: " + e.getMessage());
        }
    }

    /**
     * Handles GET entity by ID request.
     * Retrieves a single entity by ID using the service and returns it as JSON.
     * Returns 404 if the entity is not found.
     * @param ctx The Javalin Context object.
     */
    public void getOne(Context ctx) {
        try {
            // Assuming ID is Long for now, will need a more generic way for other ID types
            ID id = (ID) Long.valueOf(ctx.pathParam("id"));
            Optional<T> entity = service.findById(id);
            entity.ifPresentOrElse(
                    ctx::json,
                    () -> ctx.status(404).result(getEntityName() + " not found")
            );
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid ID format");
        } catch (DataAccessException e) {
            ctx.status(500).result("Error retrieving entity: " + e.getMessage());
        }
    }

    /**
     * Provides a human-readable name for the entity type, used in error messages.
     * Subclasses should override this.
     * @return The name of the entity (e.g., "Book", "Author").
     */
    protected String getEntityName() {
        // Default implementation, can be overridden by subclasses
        String className = service.getClass().getSimpleName();
        if (className.endsWith("Service")) {
            return className.substring(0, className.length() - "Service".length());
        }
        return className;
    }
}
