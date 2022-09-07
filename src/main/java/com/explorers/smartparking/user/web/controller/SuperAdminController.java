package com.explorers.smartparking.user.web.controller;

import com.explorers.smartparking.user.persistence.model.RoleName;
import com.explorers.smartparking.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/superAdmin", "/{lang}/superAdmin"})
public class SuperAdminController {

    private final RoleService roleService;

    @Autowired
    public SuperAdminController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PutMapping("/addRole")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addRole(@RequestParam Long userId,
                        @RequestParam RoleName roleName) {

        roleService.addRoleByUserId(userId, roleName);
    }

    @PutMapping("/removeRole")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRole(@RequestParam Long userId,
                           @RequestParam RoleName roleName) {

        roleService.removeRoleByUserId(userId, roleName);
    }
}
