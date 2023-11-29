package com.wedogift.backend.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "companies")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, insertable = false, updatable = false)
    protected UUID id;
    @Column(unique = true)
    private String email;
    private String name;
    private Double balance;
    @OneToMany( fetch = FetchType.EAGER, mappedBy = "company", cascade = CascadeType.ALL)
    private List<UserEntity> users = new ArrayList<>();

    public void addUser(UserEntity userEntity) {
        if(null == users){
            users =  new ArrayList<>();
        }
        users.add(userEntity);
        userEntity.setCompany(this);
    }

    public void removeUser(UserEntity userEntity) {
        if(null != users) {
            users.remove(userEntity);
        }
        userEntity.setCompany(null);
    }
}
