package com.project.spring.digitalwallet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.spring.digitalwallet.utils.UserPermission;

@Service
public class PermissionService {

    public List<UserPermission> getAllPermissions(){
        return UserPermission.getAllPermissions();
    }
}
