package com.example.shareEdu.controller;

import com.example.shareEdu.dto.response.ApiResponse;
import com.example.shareEdu.dto.request.RoleRequest;
import com.example.shareEdu.dto.response.RoleResponse;
import com.example.shareEdu.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest roleRequest) {
        return  ApiResponse.<RoleResponse>builder()
                .result(roleService.create(roleRequest))
                .build();
    }
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }
    @GetMapping("/{role}")
    public ApiResponse<RoleResponse> get(@PathVariable String role) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.get(role))
                .build();
    }
    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder().build();

    }


}
