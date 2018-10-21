package de.milchreis.phobox.core.storage;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageScanQueueInfo {

	private File currentDirectory;
	private File currentFile;
	private long numberOfWaitingDirectories;

}
