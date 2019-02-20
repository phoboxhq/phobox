package de.milchreis.phobox.db.entities;

import java.sql.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(exclude="items")
public class Album {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@Column
	private String name;
	
	@NotNull
	@Column
	private Date creation;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy="albums")
	private Set<Item> items;
		
}