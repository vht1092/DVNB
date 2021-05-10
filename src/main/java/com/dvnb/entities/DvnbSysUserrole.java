package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the FDS_SYS_USERROLE database table.
 * 
 */
@Entity
@Table(name="DVNB_SYS_USERROLE")
@NamedQuery(name="DvnbSysUserrole.findAll", query="SELECT f FROM DvnbSysUserrole f")
public class DvnbSysUserrole implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DvnbSysUserrolePK id;

	public DvnbSysUserrole() {
	}
	

	public DvnbSysUserrole(DvnbSysUserrolePK id) {
		super();
		this.id = id;
	}



	public DvnbSysUserrolePK getId() {
		return this.id;
	}

	public void setId(DvnbSysUserrolePK id) {
		this.id = id;
	}

}