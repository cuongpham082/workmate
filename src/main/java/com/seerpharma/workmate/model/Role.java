package com.seerpharma.workmate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "role")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20)
	private String name;

	@Column(name = "stamp")
    @JsonIgnore
    private Long stamp = 0L;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;
	
	public Role() {
	}

	public Role(String name, Long stamp) {
		this.name = name;
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
	
	public Long getStamp() { return stamp; }

    public void setStamp(Long stamp) { this.stamp = stamp; }
}