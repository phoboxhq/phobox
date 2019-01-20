package de.milchreis.phobox.server.api.requestmodel;

import lombok.Data;

@Data
public class AddToAlbumRequest {

	private String albumName;
	private String itemPath;
}
