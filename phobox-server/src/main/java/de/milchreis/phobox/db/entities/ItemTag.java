package de.milchreis.phobox.db.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude="items")
public class ItemTag {
	
	@Id
	private String name;

	@Column(name="type")
	private String type;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy="tags")
	private Set<Item> items;
	
	public ItemTag() {}
	
	public ItemTag(String name) {
		this.name = name;
	}

}
