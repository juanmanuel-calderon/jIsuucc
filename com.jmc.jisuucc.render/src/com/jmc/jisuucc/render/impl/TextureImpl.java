package com.jmc.jisuucc.render.impl;

import java.awt.image.BufferedImage;

import com.jmc.jisuucc.render.api.Texture;

public class TextureImpl implements Texture {
	
	private static int id_gen = 0;
	
	public int id;
	private int width;
	private int height;
	private BufferedImage texture;
	
	TextureImpl(BufferedImage texture) {
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
	public BufferedImage texture() {
		return texture;
	}

}
