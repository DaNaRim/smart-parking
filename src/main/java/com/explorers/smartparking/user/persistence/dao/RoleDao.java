package com.explorers.smartparking.user.persistence.dao;

import com.explorers.smartparking.user.persistence.model.Role;
import com.explorers.smartparking.user.persistence.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface RoleDao extends JpaRepository<Role, Long> {

    Role findByRoleName(RoleName roleName);

    @Query(value = "SELECT *"
            + "  FROM Role AS r"
            + "       JOIN User_role AS ur"
            + "            ON ur.role_id = r.id"
            + " WHERE ur.user_id = :id",
            nativeQuery = true)
    Set<Role> getRolesByUserId(Long id);

    @Query(value = "INSERT INTO User_role (user_id, role_id)"
            + " VALUES (:id, "
            + "         (SELECT id FROM Role"
            + "           WHERE role_name = :roleName))",
            nativeQuery = true)
    @Modifying(clearAutomatically = true)
    @Transactional
    void addRoleById(long id, String roleName);

    @Query(value = "DELETE FROM User_role"
            + " WHERE user_id = :id"
            + "   AND role_id = (SELECT id FROM Role "
            + "                   WHERE role_name = :roleName)",
            nativeQuery = true)
    @Modifying(clearAutomatically = true)
    @Transactional
    void removeRoleById(long id, String roleName);
}
