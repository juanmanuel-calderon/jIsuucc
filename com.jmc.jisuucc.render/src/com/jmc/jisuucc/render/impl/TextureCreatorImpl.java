package com.jmc.jisuucc.render.impl;

import java.awt.image.BufferedImage;

import org.apache.felix.dm.annotation.api.Component;

import com.jmc.jisuucc.render.api.Texture;
import com.jmc.jisuucc.render.api.TextureCreator;

@Component
public class TextureCreatorImpl implements TextureCreator {

	@Override
	public Texture fromFile(String filename) {
		return new TextureImpl();
	}

	@Override
	public Texture fromImage(BufferedImage image) {
		return new TextureImpl();
	}
	
	

}
