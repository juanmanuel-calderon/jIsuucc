package com.jmc.jisuucc.map.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

@Component
public class MapFactory {
	
	@ServiceDependency
	private ConfigurationAdmin cm;
	
	private static String CFG_DIRECTORY = "assets" + File.separator + "map-cfg";
	
	@Start
	public void start() throws IOException {
		scanMaps();
	}

	private void scanMaps() throws IOException {
		List<Path> cfgs = 
			Files.walk(Paths.get(CFG_DIRECTORY))
				 .filter(s -> s.toString().endsWith(".cfg"))
				 .map(s -> s.toAbsolutePath())
				 .collect(Collectors.toList());
		
		cfgs.parallelStream()
			.map(Path::toAbsolutePath)
			.map(this::parseCfg)
			.map(Hashtable::new)
			.forEach(this::registerService);
	}
	
	private Map<String, Object> parseCfg(Path filename) {
		try {
			return
				Files.lines(filename)
					 .map(l -> l.split("="))
					 .collect(Collectors.toMap(l -> l[0], l -> l[1]));
		} catch(IOException e) {
			return Collections.emptyMap();
		}
	}
	
	private void registerService(Dictionary<String, Object> dict) {
		try {
			Configuration c = cm.createFactoryConfiguration("com.jmc.jisuucc.map", "?");
			c.update(dict);
		} catch(IOException e) {
			System.out.println("Error while creating config for map " + dict);
		}
	}
}
