package com.example.first_pj.mapper;

import com.example.first_pj.Entity.Role;
import com.example.first_pj.dto.request.RoleRequest;
import com.example.first_pj.dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
