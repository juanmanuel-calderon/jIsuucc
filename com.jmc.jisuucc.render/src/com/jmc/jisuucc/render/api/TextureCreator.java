package com.jmc.jisuucc.render.api;

public interface TextureCreator {
	
	public Texture fromFile(String filename);
	public Texture fromTile(String filename, int x, int y);

}
