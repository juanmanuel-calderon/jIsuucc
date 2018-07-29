package com.jmc.jisuucc.render.impl;

import com.jmc.jisuucc.render.api.Texture;
import com.jogamp.opengl.util.texture.TextureData;

public class TextureImpl implements Texture {
	
	private static int id_gen = 0;
	
	public int id;
	private int width;
	private int height;
	private TextureData texture;
	
	TextureImpl(TextureData texture) {
		this.id = id_gen++;
		this.texture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
	}
	
	@Override
	public int id() {
		return id;
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
	public TextureData texture() {
		return texture;
	}

}
