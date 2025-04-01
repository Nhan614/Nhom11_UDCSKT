package com.example.shareEdu.service;

import com.example.shareEdu.dto.request.RoleRequest;
import com.example.shareEdu.dto.response.RoleResponse;
import com.example.shareEdu.mapper.RoleMapper;
import com.example.shareEdu.repository.PermissionRepository;
import com.example.shareEdu.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }
    public void delete(String role) {
        roleRepository.deleteById(role);
    }
    public RoleResponse get(String role) {
        return roleMapper.toRoleResponse(roleRepository.findById(role).get());
    }
}
