package com.jmc.jisuucc.render.api;

import java.awt.image.BufferedImage;
import java.util.Optional;

public interface TextureCreator {
	
	public Optional<Texture> fromFile(String filename);
	public Optional<Texture> fromImage(BufferedImage image);

}
