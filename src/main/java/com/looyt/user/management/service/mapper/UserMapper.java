package com.looyt.user.management.service.mapper;

import com.looyt.user.management.service.model.request.CreateUserRequest;
import com.looyt.user.management.service.model.request.UpdateUserRequest;
import com.looyt.user.management.service.model.response.UserResponse;
import com.looyt.user.management.service.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(CreateUserRequest createUserRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateUserRequest updateUserRequest, @MappingTarget User user);

    UserResponse toResponse( User user);

    List<UserResponse> toResponseList(List<User> users);
}