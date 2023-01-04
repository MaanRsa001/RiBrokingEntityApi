package com.maan.insurance.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Dual")
@Table(name = "SYS.DUAL")
public class Dual {
    @Id
    @Column(name = "DUMMY")
    private String dummy;
}