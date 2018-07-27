package com.jmc.jisuucc.map.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.mapeditor.core.TileLayer;
import org.mapeditor.io.TMXMapReader;

import com.jmc.jisuucc.map.api.Map;
import com.jmc.jisuucc.map.api.MapParser;
import com.jmc.jisuucc.map.api.Tile;
import com.jmc.jisuucc.render.api.TextureCreator;

@Component
public class MapParserImpl implements MapParser {

	@ServiceDependency
	private TextureCreator textureCreator;
	
	@Override
	public Map parse(String filename) throws Exception {
		java.util.Map<Integer, Tile> tilesMapping = loadTiles();
		org.mapeditor.core.Map tmxMap = new TMXMapReader().readMap(filename);
		int width = tmxMap.getWidth();
		int height = tmxMap.getHeight();
		TileLayer layer = (TileLayer) tmxMap.getLayer(0);
		
		Tile[][] tiles = new Tile[width][height];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				org.mapeditor.core.Tile tile = layer.getTileAt(i, j);
				Tile t = tilesMapping.get(tile.getId());
				t.setTexture(textureCreator.fromImage(tile.getImage()));
				tiles[i][j] = t;
			}
		}
		
		return new MapImpl(width, height, tiles);
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
