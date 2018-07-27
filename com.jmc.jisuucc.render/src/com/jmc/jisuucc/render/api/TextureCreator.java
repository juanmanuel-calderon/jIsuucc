package com.jmc.jisuucc.render.api;

import java.awt.image.BufferedImage;

public interface TextureCreator {
	
	public Texture fromFile(String filename);
	public Texture fromImage(BufferedImage image);

}
