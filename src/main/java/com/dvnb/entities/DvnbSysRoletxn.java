package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "DVNB_SYS_ROLETXN")
@NamedQuery(name = "DvnbSysRoletxn.findAll", query = "SELECT f FROM DvnbSysRoletxn f")
public class DvnbSysRoletxn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_DVNB_SYS_ROLETXN")
	@SequenceGenerator(name = "SQ_DVNB_SYS_ROLETXN", sequenceName = "SQ_DVNB_SYS_ROLETXN", allocationSize = 1)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(nullable = false)
	private Boolean flgauth;

	@Column(nullable = false)
	private Boolean flginit;

	@Column(nullable = false)
	private Boolean flgview;

	@Column(nullable = false)
	private int idrole;

	@Column(nullable = false, length = 4)
	private String idtxn;

	public DvnbSysRoletxn() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean getFlgauth() {
		return this.flgauth;
	}

	public void setFlgauth(Boolean flgauth) {
		this.flgauth = flgauth;
	}

	public Boolean getFlginit() {
		return this.flginit;
	}

	public void setFlginit(Boolean flginit) {
		this.flginit = flginit;
	}

	public Boolean getFlgview() {
		return this.flgview;
	}

	public void setFlgview(Boolean flgview) {
		this.flgview = flgview;
	}

	public int getIdrole() {
		return this.idrole;
	}

	public void setIdrole(int idrole) {
		this.idrole = idrole;
	}

	public String getIdtxn() {
		return this.idtxn;
	}

	public void setIdtxn(String idtxn) {
		this.idtxn = idtxn;
	}

}