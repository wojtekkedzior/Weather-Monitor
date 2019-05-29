package com.iot.temperature.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Temperature implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4198943515224478533L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private Float temperature;
	private Float humidity;
	private Float pressure;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp; 
	
	public Temperature() {}
	

}
