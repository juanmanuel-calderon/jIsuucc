package com.jmc.jisuucc.map.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.Start;

import com.jmc.jisuucc.map.api.Tile;
import com.jmc.jisuucc.map.api.TileManager;

@Component
public class TileManagerImpl implements TileManager {

	private Map<Integer, Tile> tiles;
	
	@Start
	public void start() throws IOException {
		this.tiles = loadTiles();
	}
	
	@Override
	public Map<Integer, Tile> tiles() {
		return tiles;
	}
	
	private java.util.Map<Integer, Tile> loadTiles() throws IOException {
		return
		Files.lines(Paths.get(Tile.TILE_DEFINITION_FILENAME))
		 	 .filter(l -> !l.startsWith("//"))
		 	 .map(this::fromString)
		 	 .collect(Collectors.toMap(Tile::id, Function.identity()));
	}
	
	private Tile fromString(String line) {
		String[] tileDefinition = line.split(",");
		int idX = Integer.parseInt(tileDefinition[0]);
		int idY = Integer.parseInt(tileDefinition[1]);
		String description = tileDefinition[2];
		boolean hasCollision = Boolean.parseBoolean(tileDefinition[3]);
		return new TileImpl(idX, idY, description, hasCollision);
	}

}
