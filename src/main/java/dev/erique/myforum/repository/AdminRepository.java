package dev.erique.myforum.repository;

import dev.erique.myforum.model.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    UserDetails findByEmail(String email);
}
