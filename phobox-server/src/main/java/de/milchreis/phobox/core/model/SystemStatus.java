package de.milchreis.phobox.core.model;

import lombok.Data;

import java.lang.management.ManagementFactory;

@Data
public class SystemStatus extends Status {
	
	protected String importStatus;
	protected String state;
	protected String file;
	protected double freespace;
	protected double maxspace;
	protected int remainingfiles;
	protected long numberOfPictures;
	protected long uptime = ManagementFactory.getRuntimeMXBean().getUptime();

}
