package com.jmc.jisuucc.map.impl;

import java.io.File;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.regex.Matcher;

import org.apache.felix.dm.annotation.api.FactoryConfigurationAdapterService;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.mapeditor.core.TileLayer;
import org.mapeditor.io.TMXMapReader;

import com.jmc.jisuucc.map.api.Map;
import com.jmc.jisuucc.map.api.Tile;
import com.jmc.jisuucc.map.api.TileManager;
import com.jmc.jisuucc.render.api.Renderer;
import com.jmc.jisuucc.render.api.TextureCreator;

@FactoryConfigurationAdapterService(factoryPid = "com.jmc.jisuucc.map", propagate = true)
public class MapImpl implements Map {
	
	@ServiceDependency
	private Renderer renderer;
	
	@ServiceDependency
	private TileManager tileManager;
	
	@ServiceDependency
	private TextureCreator textureCreator;
	
	static int TILE_COLUMNS = 3;
	
	private String name;
	private int width;
	private int height;
	private Tile[][] tiles;
	private String filename;
	
	public void updated(Dictionary<String, Object> conf) {
		this.name = conf.get("name").toString();
		this.filename = conf.get("filename").toString();
	}
	
	@Start
	public void start() throws Exception {
		System.out.println("Map started");
		parse(this.filename.toString().replaceAll("/", Matcher.quoteReplacement(File.separator)));
	}
	
	private void parse(String filename) throws Exception {
		java.util.Map<Integer, Tile> tilesMapping = tileManager.tiles();
		org.mapeditor.core.Map tmxMap = new TMXMapReader().readMap(filename);
		int width = tmxMap.getWidth();
		int height = tmxMap.getHeight();
		TileLayer layer = (TileLayer) tmxMap.getLayer(0);
		
		Tile[][] tiles = new Tile[width][height];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				org.mapeditor.core.Tile tile = layer.getTileAt(i, j);
				Tile t = tilesMapping.get(tile.getId());
				if(t.texture() == null) t.setTexture(textureCreator.fromImage(tile.getImage()).get());
				tiles[i][j] = t;
			}
		}
		
		this.width = width;
		this.height = height;
		this.tiles = tiles;
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public int width() {
		return width;
	}

	@Override
	public int height() {
		return height;
	}

	@Override
	public Tile[][] tiles() {
		return tiles;
	}
	
	@Override
	public Tile tileAtCoord(int x, int y) {
		x = x / Tile.TILE_SIZE;
		y = y / Tile.TILE_SIZE;
		return tiles[x][y];
	}
	
	@Override
	public void render() {
		for(int i = 0; i< tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				renderer.drawImage(tiles[i][j].texture(), i * Tile.TILE_SIZE, j * Tile.TILE_SIZE);
			}
		}
	}

	@Override
	public String toString() {
		return "MapImpl [name=" + name + ", width=" + width + ", height=" + height + ", tiles=" + Arrays.toString(tiles)
				+ "]";
	}
	
}
