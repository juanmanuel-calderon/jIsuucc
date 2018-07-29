package com.jmc.jisuucc.entity.api;

import java.awt.Point;

import com.jmc.jisuucc.render.api.Renderable;
import com.jmc.jisuucc.render.api.Texture;

public interface Entity extends Renderable {
	
	public Point position();
	public Texture texture();
	public void setPosition(Point position);
}
