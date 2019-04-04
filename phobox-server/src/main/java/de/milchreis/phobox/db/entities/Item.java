package de.milchreis.phobox.db.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.milchreis.phobox.utils.exif.ExifHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

	@Id
	@GeneratedValue
	@Column(name = "uid", unique = true)
	private UUID uid;
	
	@NotNull
	@Column(name="full_path")
	private String fullPath;
	
	@NotNull
	@Column
	private String path;
	
	@NotNull
	@Column(name = "file_name")
	private String fileName;
	
	@NotNull
	@Column(name = "file_extension")
	private String fileExtension;
	
	@Column
	private Integer rotation;

	@Column
	private Integer width;

	@Column
	private Integer height;
	
	@Column
	private String description;

	@Column
	private String camera;

	@Column
	private String hash;

	@Column
	private Timestamp creation;
	
	@Column
	private Date imported;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Album> albums;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ItemTag> tags;

	@JsonIgnore
	public boolean isLandscape() {
		return rotation != ExifHelper.ImageRotation.CLOCKWISE_90.getEncoding()
				&& rotation != ExifHelper.ImageRotation.CLOCKWISE_270.getEncoding();
	}
}
