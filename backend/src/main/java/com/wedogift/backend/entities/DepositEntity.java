package com.wedogift.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="deposits")
public class DepositEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, insertable = false, updatable = false)
    protected UUID id;
    private Double balance;
    private LocalDate depositDate;
    private String depositType;
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;
}
