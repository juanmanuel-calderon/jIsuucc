package com.jmc.jisuucc.render.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

import org.apache.felix.dm.annotation.api.Component;

import com.jmc.jisuucc.render.api.Texture;
import com.jmc.jisuucc.render.api.TextureCreator;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

@Component
public class TextureCreatorImpl implements TextureCreator {
	
	@Override
	public Optional<Texture> fromFile(String filename) {
		try {
			TextureData texData = TextureIO.newTextureData(RendererImpl.GL_PROFILE, new File(filename), false, null);
			return Optional.ofNullable(new TextureImpl(texData));
		} catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public Optional<Texture> fromImage(BufferedImage image) {
		try {
			TextureData texData = AWTTextureIO.newTextureData(RendererImpl.GL_PROFILE, image, false);
			return Optional.ofNullable(new TextureImpl(texData));
		} catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
	

}
