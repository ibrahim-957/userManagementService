package com.looyt.user.management.service.controller;

import com.looyt.user.management.service.service.UserService;
import com.looyt.user.management.service.model.request.CreateUserRequest;
import com.looyt.user.management.service.model.request.UpdateUserRequest;
import com.looyt.user.management.service.model.response.PagedResponse;
import com.looyt.user.management.service.model.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for managing users in the system")
@Validated
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user with the provided information. Username must be unique."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or validation error"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Username already exists"
            )
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User creation request with required fields",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateUserRequest.class))
            )
            @Valid @RequestBody CreateUserRequest request) {
        log.info("POST /api/users - Creating new user with username: {}", request.getUsername());
        UserResponse createdUser = userService.createUser(request);
        log.info("POST /api/users - User created successfully with id: {}, username: {}",
                createdUser.getId(), createdUser.getUsername());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a specific user by their unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with the given ID"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @Parameter(description = "ID of the user to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        log.info("GET /api/users/{} - Fetching user by id", id);
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Get all users with pagination",
            description = "Retrieves a paginated list of all users with optional sorting"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PagedResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination or sorting parameters"
            )
    })
    @GetMapping
    public ResponseEntity<PagedResponse<UserResponse>> getAllUsers(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Field to sort by", example = "id")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction (asc or desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("GET /api/users - Fetching users with pagination " +
                        "page: {}, size: {}, sortBy: {}, sortDirection: {}]",
                page, size, sortBy, sortDirection);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        PagedResponse<UserResponse> responses = userService.getAllUsers(pageable);

        log.info("GET /api/users - Returning {} users on page {} of {} (total elements: {})",
                responses.getContent().size(), responses.getPageNumber(),
                responses.getTotalPages(), responses.getTotalElements());
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "Update an existing user",
            description = "Updates an existing user's information. User ID must be provided in the request body."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or validation error"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with the given ID"
            )
    })
    @PutMapping
    public ResponseEntity<UserResponse> updateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User update request with user ID and fields to update",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateUserRequest.class))
            )
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("PUT /api/users - Updating user with id: {}", request.getId());
        UserResponse updatedUser = userService.updateUser(request);
        log.info("PUT /api/users - User updated successfully with id: {}", updatedUser.getId());
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
            summary = "Delete a user",
            description = "Permanently deletes a user from the system by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with the given ID"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true, example = "1")
            @PathVariable Long id) {
        log.info("DELETE /api/users/{} - Deleting user", id);
        userService.deleteUser(id);
        log.info("DELETE /api/users/{} - User deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}