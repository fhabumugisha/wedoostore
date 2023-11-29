package com.wedogift.backend.entities;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "companies")
public class CompanyEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, insertable = false, updatable = false)
    protected UUID id;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private Double balance;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company", cascade = CascadeType.ALL)
    private List<UserEntity> users = new ArrayList<>();

    public void addUser(UserEntity userEntity) {
        if (null == users) {
            users = new ArrayList<>();
        }
        users.add(userEntity);
        userEntity.setCompany(this);
    }

    public void removeUser(UserEntity userEntity) {
        if (null != users) {
            users.remove(userEntity);
        }
        userEntity.setCompany(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
