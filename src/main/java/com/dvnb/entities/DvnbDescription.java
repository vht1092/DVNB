package com.dvnb.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;


/**
 * The persistent class for the DVNB_DESCRIPTION database table.
 * 
 */
@Entity
@Table(name="DVNB_DESCRIPTION")
public class DvnbDescription implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, length=50)
	private String description;

	@Id
	@Column(nullable=false, length=3)
	private String id;

	@Column(length=50)
	private String type;
	
	@Column(nullable = false, precision = 3)
	private BigDecimal sequenceno;
	
	public DvnbDescription() {
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the sequenceno
	 */
	public BigDecimal getSequenceno() {
		return sequenceno;
	}

	/**
	 * @param sequenceno the sequenceno to set
	 */
	public void setSequenceno(BigDecimal sequenceno) {
		this.sequenceno = sequenceno;
	}


}
