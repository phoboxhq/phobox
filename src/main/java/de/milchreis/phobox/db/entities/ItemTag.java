package de.milchreis.phobox.db.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "item_tag")
public class ItemTag {
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh=true, foreignColumnName = "path", columnName="id_item")
	private Item item;
	
	@DatabaseField(canBeNull = false, columnName="tag_value")
	private String tagValue;

	public ItemTag() {
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getTagValue() {
		return tagValue;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}
	
}
