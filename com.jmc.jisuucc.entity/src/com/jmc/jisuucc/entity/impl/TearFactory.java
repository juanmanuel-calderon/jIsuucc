package com.jmc.jisuucc.entity.impl;

import java.awt.Point;
import java.io.File;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;

import com.jmc.jisuucc.entity.api.Tear;
import com.jmc.jisuucc.event.api.Direction;
import com.jmc.jisuucc.render.api.Renderer;
import com.jmc.jisuucc.render.api.Texture;
import com.jmc.jisuucc.render.api.TextureCreator;

@Component(provides = TearFactory.class)
public class TearFactory {
	
	@ServiceDependency
	TextureCreator textureCreator;
	
	@ServiceDependency
	Renderer renderer;
	
	private Texture texture;
	
	@Start
	public void start() {
		System.out.println("TearFactory started");
		texture = textureCreator.fromFile("assets" + File.separator + "others" + File.separator + "tear.png").get();
	}
	
	public Tear newTear(Point position, double dmg, double speed, Direction direction, int range) {
		TearImpl t = new TearImpl(position, texture, dmg, speed, direction, range);
		t.setRenderer(renderer);
		return t;
	}
	
}
