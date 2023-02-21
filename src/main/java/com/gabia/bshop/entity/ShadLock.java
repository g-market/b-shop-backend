package com.gabia.bshop.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	name = "shedlock",
	indexes = {})
@Entity
public class ShadLock {
	@Id
	@Column(name = "name", columnDefinition = "varchar(64)", nullable = false)
	private String name;

	@Column(name = "lock_until", columnDefinition = "timestamp(3)")
	private Timestamp lockUntil;

	@Column(name = "locked_at", columnDefinition = "timestamp(3)")
	private Timestamp lockedAt;

	@Column(name = "locked_by", columnDefinition = "varchar(255)")
	private String lockedBy;
}
