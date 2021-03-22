package com.seerpharma.workmate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "account",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name"),
                @UniqueConstraint(columnNames = "short_name")
        })
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Size(max = 5)
    @Column(name = "short_name")
    private String shortName;

    @Size(max = 200)
    private String logo;

    @Column(name = "stamp")
    @JsonIgnore
    private Long stamp = 0L;

    public Account() {
    }

    public Account(Long id, @NotBlank @Size(max = 200) String name, @NotBlank @Size(max = 3) String shortName, @Size(max = 200) String logo, Long stamp) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.logo = logo;
        this.stamp = stamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getStamp() {
        return stamp;
    }

    public void setStamp(Long stamp) {
        this.stamp = stamp;
    }
}
