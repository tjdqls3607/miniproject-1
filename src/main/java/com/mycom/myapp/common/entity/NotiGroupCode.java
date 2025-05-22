package com.mycom.myapp.common.entity;

import org.springframework.data.domain.Persistable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name="noti_group_code")
public class NotiGroupCode implements Persistable<String> {
	@Id
	@Column(name="noti_code")
	private String notiCode;

	@Column(name="noti_code_name")
	private String notiCodeName;

	@Column(name="noti_code_desc")
	private String notiCodeDesc;

	@Override
	public String getId() {
		return null;
	}

	@Transient
	private boolean isNew = false;
	
}
