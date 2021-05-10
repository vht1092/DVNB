package com.dvnb.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the FDS_SYS_USER database table.
 * 
 */
@Entity
@Table(name = "DVNB_SYS_USER")
@NamedQuery(name = "DvnbSysUser.findAll", query = "SELECT f FROM DvnbSysUser f")
public class DvnbSysUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 12)
	private String userid;

	@Column(nullable = false)
	private Boolean activeflag;

	@Column(length = 3)
	private String branchcode;

	@Column(nullable = false, length = 17)
	private String createdate;

	@Column(nullable = false, length = 40)
	private String email;

	@Column(nullable = false, length = 30)
	private String fullname;

	@Column(length = 17)
	private String lastlogin;

	@Column(name = "USERTYPE", length = 3)
	private String usertype;

	@Column(length = 17)
	private String updatedate;

	public DvnbSysUser() {
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Boolean getActiveflag() {
		return this.activeflag;
	}

	public void setActiveflag(Boolean activeflag) {
		this.activeflag = activeflag;
	}

	public String getBranchcode() {
		return this.branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}

	public String getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getLastlogin() {
		return this.lastlogin;
	}

	public void setLastlogin(String lastlogin) {
		this.lastlogin = lastlogin;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getUpdatedate() {
		return this.updatedate;
	}

	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}

}