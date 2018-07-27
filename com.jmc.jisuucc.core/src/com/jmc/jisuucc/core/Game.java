package com.jmc.jisuucc.core;

import java.io.File;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;

import com.jmc.jisuucc.map.api.Map;
import com.jmc.jisuucc.map.api.MapParser;

@Component
public class Game {
	
	@ServiceDependency
	private MapParser mapParser;
	
	@Start
	public void start() throws Exception {
		System.out.println("coucou");
		Map map = mapParser.parse("assets" + File.separator + "map1.tmx");
		while(true) {
			System.out.println("coucou");
			map.render();
		}
	}

}
