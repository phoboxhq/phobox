package de.milchreis.phobox.db.entities;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private String path;
	
	private String name;
	
	@NotNull
	private Integer rotation;

	private Integer width;

	private Integer height;
	
	private String description;

	private Date creation;
	
	@NotNull
	private Date found;
	
}
