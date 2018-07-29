package com.jmc.jisuucc.core;

import java.awt.Point;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;

import com.jmc.jisuucc.entity.api.JIsuucc;
import com.jmc.jisuucc.map.api.Map;
import com.jmc.jisuucc.map.api.Tile;
import com.jmc.jisuucc.map.api.TileManager;

@Component
public class Game {

	@ServiceDependency
	private TileManager mapParser;
	
	@ServiceDependency(filter = "(name=map1)")
	private Map map;
	
	@ServiceDependency(filter = "(name=helivg)")
	private JIsuucc isuucc;
	
	@Start
	public void start() throws Exception {
		new Thread(() -> {
			isuucc.setPosition(new Point(map.width() * Tile.TILE_SIZE / 2, map.height() * Tile.TILE_SIZE / 2));
			//while(true) {
			//map.render();
			isuucc.render();
			//}
		}).start();
	}
}
