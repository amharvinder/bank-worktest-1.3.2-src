package com.unibet.worktest.bank.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name="role")
@DiscriminatorValue("r")
public class Role extends BaseModel {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9084494289101869413L;
	
	private RoleType name;

	/**
	 * @return the name
	 */
	@Column(name="name")
	@Enumerated(EnumType.STRING)
	public RoleType getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(RoleType name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + "]";
	}
	
}
