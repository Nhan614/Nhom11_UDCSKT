package com.example.shareEdu.service;


import com.example.shareEdu.dto.request.PermissionRequest;
import com.example.shareEdu.dto.response.PermissionResponse;
import com.example.shareEdu.entity.Permission;
import com.example.shareEdu.mapper.PermissionMapper;
import com.example.shareEdu.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request ) {
        Permission permission = permissionMapper.toPermission(request);
        permission =  permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);
    }
    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }
    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
