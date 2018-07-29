package com.jmc.jisuucc.entity.impl;

import java.awt.Point;
import java.util.Dictionary;

import org.apache.felix.dm.annotation.api.FactoryConfigurationAdapterService;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;

import com.jmc.jisuucc.entity.api.JIsuucc;
import com.jmc.jisuucc.render.api.Renderer;
import com.jmc.jisuucc.render.api.Texture;
import com.jmc.jisuucc.render.api.TextureCreator;

@FactoryConfigurationAdapterService(factoryPid = "com.jmc.jisuucc.jisuucc", propagate = true)
public class JIsuuccImpl implements JIsuucc {
	
	@ServiceDependency
	private Renderer renderer;
	
	@ServiceDependency
	private TextureCreator textureCreator;
	
	private int hp;
	private double dmg;
	private double speed;
	private double tears;
	private Point position = new Point(0, 0);
	private String textureFilename;
	private Texture texture;
	
	public void updated(Dictionary<String, Object> conf) {
		this.hp = Integer.parseInt(conf.get("hp").toString());
		this.dmg = Double.parseDouble(conf.get("dmg").toString());
		this.speed = Double.parseDouble(conf.get("speed").toString());
		this.tears = Double.parseDouble(conf.get("tears").toString());
		this.textureFilename = conf.get("texture").toString();
	}
	
	@Start
	public void start() {
		this.texture = textureCreator.fromFile(textureFilename).get();
	}
	
	@Override
	public int hp() {
		return this.hp;
	}

	@Override
	public double dmg() {
		return this.dmg;
	}

	@Override
	public double speed() {
		return this.speed;
	}

	@Override
	public double tears() {
		return this.tears;
	}
	
	@Override
	public Point position() {
		return this.position;
	}
	
	@Override
	public void setPosition(Point position) {
		this.position = position;
	}

	@Override
	public Texture texture() {
		return this.texture;
	}

	@Override
	public void render() {
		int x = position.x - texture.width() / 2;
		int y = position.y - texture.height() / 2;
		renderer.drawImage(texture, x, y);
	}

	@Override
	public void move(Direction direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveTo(int x, int y) {
		// TODO Auto-generated method stub
		
	}

}
