package com.jmc.jisuucc.map.api;

import java.io.IOException;

public interface MapParser {
	public Map parse(String filename) throws IOException;
}
