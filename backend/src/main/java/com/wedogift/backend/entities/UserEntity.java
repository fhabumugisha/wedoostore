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
@Table(name = "users")
public class UserEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, insertable = false, updatable = false)
    protected UUID id;
    private String name;
    @ManyToOne
    @JoinColumn(name="company_id")
    private CompanyEntity company;
    // List of deposits made by the user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DepositEntity> deposits = new ArrayList<>();

    public void addDeposit(DepositEntity depositEntity) {
        if(null == deposits){
            deposits =  new ArrayList<>();
        }
        deposits.add(depositEntity);
        depositEntity.setUser(this);
    }

    public void removeDeposit(DepositEntity depositEntity) {
        if(null != deposits){
            deposits.remove(depositEntity);
        }
        depositEntity.setUser(null);
    }
}
