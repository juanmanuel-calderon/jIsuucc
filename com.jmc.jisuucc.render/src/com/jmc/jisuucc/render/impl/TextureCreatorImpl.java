package com.jmc.jisuucc.render.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.felix.dm.annotation.api.Component;

import com.jmc.jisuucc.render.api.Texture;
import com.jmc.jisuucc.render.api.TextureCreator;

@Component
public class TextureCreatorImpl implements TextureCreator {
	
	@Override
	public Optional<Texture> fromFile(String filename) {
		try {
			return Optional.ofNullable(new TextureImpl(ImageIO.read(new File(filename))));
		} catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public Optional<Texture> fromImage(BufferedImage image) {
		return Optional.ofNullable(new TextureImpl(image));
	}
	
	

}
