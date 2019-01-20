package de.milchreis.phobox.core.file;

import java.util.HashMap;
import java.util.Map;

public class LoopInfo {

	private Map<String, Object> globals;
	
	public LoopInfo() {
		setGlobals(new HashMap<String, Object>());
	}

	public Map<String, Object> getGlobals() {
		return globals;
	}

	public void setGlobals(Map<String, Object> globals) {
		this.globals = globals;
	}
}
