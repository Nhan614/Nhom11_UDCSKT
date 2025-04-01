package com.example.shareEdu.service;

import com.example.shareEdu.dto.request.UserCreationRequest;
import com.example.shareEdu.dto.request.UserUpdateRequest;
import com.example.shareEdu.dto.response.UserResponse;
import com.example.shareEdu.entity.User;
import com.example.shareEdu.enums.Role;
import com.example.shareEdu.exception.AppException;
import com.example.shareEdu.exception.ErrorCode;
import com.example.shareEdu.mapper.UserMapper;
import com.example.shareEdu.repository.RoleRepository;
import com.example.shareEdu.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
// tự động tạo constructor cho các trường có final

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // tự động tạo final
@Slf4j

public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;



    public UserResponse createUser(UserCreationRequest userCreationRequest) {
        log.info("service create user");
        if(userRepository.existsByUserName(userCreationRequest.getUserName()))
            throw new AppException(ErrorCode.USER_EXISTED);
        User user = userMapper.toUser(userCreationRequest);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setCreateDate(LocalDate.now());
      //  user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);

    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.USER_EXISTED));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAnyAuthority('UPDATE_DATA')")

    public List<UserResponse> getUsers(){
        log.info("in method getUsers");
        return userRepository.findAll().stream().map(userMapper ::toUserResponse).toList();
    }
    /**
    public UserResponse getMyInfo(){
        log.info("in method getMyInfo");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return userMapper.toUserResponse(currentUser);

    }  **/
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        log.info("in method getMyInfo:" + context.getAuthentication().getPrincipal().toString());
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUserName(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);

    }
    @PostAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id){
        log.info("in method get user by id");
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED)));
    }
}
