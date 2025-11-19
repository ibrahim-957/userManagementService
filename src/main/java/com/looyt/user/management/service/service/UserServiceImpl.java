package com.looyt.user.management.service.service;

import com.looyt.user.management.service.model.request.CreateUserRequest;
import com.looyt.user.management.service.model.request.UpdateUserRequest;
import com.looyt.user.management.service.model.response.PagedResponse;
import com.looyt.user.management.service.model.response.UserResponse;
import com.looyt.user.management.service.exception.DuplicateEmailException;
import com.looyt.user.management.service.mapper.UserMapper;
import com.looyt.user.management.service.repository.UserRepository;
import com.looyt.user.management.service.entity.User;
import com.looyt.user.management.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        log.info("Starting user creation process for email: {}", createUserRequest.getEmail());

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            log.warn("User creation failed -email already exists: {}", createUserRequest.getEmail());
            throw new DuplicateEmailException("Email already exists: " + createUserRequest.getEmail());
        }

        User user = userMapper.toEntity(createUserRequest);

        User savedUser = userRepository.save(user);
        log.info("User persisted to database with id: {}, username: {}",
                savedUser.getId(), savedUser.getUsername());

        UserResponse response = userMapper.toResponse(savedUser);
        log.info("User creation completed successfully -id: {}, email: {}",
                savedUser.getId(), response.getEmail());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.info("Fetching user by id : {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id : {}", id);
                    return new UserNotFoundException("User not found with id : " + id);
                });

        UserResponse response = userMapper.toResponse(user);
        log.info("User fetched successfully -id: {}", id);
        return response;
    }

    @Override
    public PagedResponse<UserResponse> getAllUsers(Pageable pageable) {
        log.info("Fetching users with pagination -page: {}, size: {}, sort:{} ",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort());

        Page<User> users = userRepository.findAll(pageable);

        log.info("Retrieved {} users on page {} of {} (total elements: {})",
                users.getNumberOfElements(),
                pageable.getPageNumber(),
                users.getTotalPages(),
                users.getTotalElements());

        if (users.isEmpty()) {
            log.debug("No users found for requested page: {}", pageable.getPageNumber());
        }

        List<UserResponse> userResponses = userMapper.toResponseList(users.getContent());


        return PagedResponse.<UserResponse>builder()
                .content(userResponses)
                .pageNumber(users.getNumber())
                .pageSize(users.getSize())
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .last(users.isLast())
                .build();
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest updateUserRequest) {
        log.info("Starting user update process for id : {}", updateUserRequest.getId());

        User user = userRepository.findById(updateUserRequest.getId())
                .orElseThrow(() -> {
                    log.warn("Attempted to update non-existent user with id : {}", updateUserRequest.getId());
                    return new UserNotFoundException("User not found with id : " + updateUserRequest.getId());
                });

        userMapper.updateEntityFromRequest(updateUserRequest, user);

        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully in database -id: {}, username: {}",
                updatedUser.getId(), updatedUser.getUsername());

        return userMapper.toResponse(updatedUser);

    }

    @Override
    public void deleteUser(Long id) {
        log.info("Starting user delete process for id : {}", id);

        userRepository.deleteById(id);
        log.info("User deleted successfully from database -id: {}", id);
    }
}