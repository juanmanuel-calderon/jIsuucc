package com.jmc.jisuucc.map.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;

import com.jmc.jisuucc.map.api.Map;
import com.jmc.jisuucc.map.api.MapParser;
import com.jmc.jisuucc.map.api.Tile;
import com.jmc.jisuucc.render.api.TextureCreator;

@Component
public class MapParserImpl implements MapParser {

	@ServiceDependency
	private TextureCreator textureCreator;
	
	@Override
	public Map parse(String filename) throws IOException {
		return null;
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
		int id_x = Integer.parseInt(tileDefinition[0]);
		int id_y = Integer.parseInt(tileDefinition[1]);
		String description = tileDefinition[2];
		boolean hasCollision = Boolean.parseBoolean(tileDefinition[3]);
		return new TileImpl(id_x, id_y, description, hasCollision, textureCreator.fromTile(TileImpl.TILES_PNG, id_x, id_y));
	}

}
