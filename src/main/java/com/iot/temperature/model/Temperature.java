package com.iot.temperature.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Temperature implements Serializable {

	private static final long serialVersionUID = 4198943515224478533L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private Float temperature;
	private Float humidity;
	private Float pressure;
	
	@NotNull
//	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime timestamp;
}
