package com.jmc.jisuucc.render.api;

import com.jogamp.opengl.util.texture.TextureData;

public interface Texture {
	
	public int id();
	public int width();
	public int height();
	public TextureData texture();

}
