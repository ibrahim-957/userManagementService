package com.looyt.user.management.service.service;

import com.looyt.user.management.service.model.request.CreateUserRequest;
import com.looyt.user.management.service.model.request.UpdateUserRequest;
import com.looyt.user.management.service.model.response.PagedResponse;
import com.looyt.user.management.service.model.response.UserResponse;
import org.springframework.data.domain.Pageable;


public interface UserService {
    public UserResponse createUser(CreateUserRequest createUserRequest);
    public UserResponse getUserById(Long id);
    public PagedResponse<UserResponse> getAllUsers(Pageable pageable);
    public UserResponse updateUser(UpdateUserRequest updateUserRequest);
    public void deleteUser(Long id);
}