package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.service.PermissionService;
import com.project.spring.digitalwallet.utils.UserPermission;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public List<UserPermission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

}
