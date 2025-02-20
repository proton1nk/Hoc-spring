package com.example.first_pj.mapper;

import com.example.first_pj.Entity.Permission;
import com.example.first_pj.dto.request.PermissionRequest;
import com.example.first_pj.dto.response.PermissionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}