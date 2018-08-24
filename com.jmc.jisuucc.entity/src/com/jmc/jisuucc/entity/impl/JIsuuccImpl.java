package com.jmc.jisuucc.entity.impl;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Dictionary;
import java.util.Optional;

import org.apache.felix.dm.annotation.api.FactoryConfigurationAdapterService;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;

import com.jmc.jisuucc.event.api.CollisionEvent;
import com.jmc.jisuucc.event.api.Direction;
import com.jmc.jisuucc.entity.api.JIsuucc;
import com.jmc.jisuucc.entity.api.Tear;
import com.jmc.jisuucc.map.api.Tile;
import com.jmc.jisuucc.render.api.Renderer;
import com.jmc.jisuucc.render.api.Texture;
import com.jmc.jisuucc.render.api.TextureCreator;

@FactoryConfigurationAdapterService(factoryPid = "com.jmc.jisuucc.jisuucc", propagate = true)
public class JIsuuccImpl implements JIsuucc {
	
	@ServiceDependency
	private Renderer renderer;
	
	@ServiceDependency
	private TextureCreator textureCreator;
	
	@ServiceDependency
	private TearFactory tearFactory;
	
	private int hp;
	private double dmg;
	private double speed;
	private double tears;
	private int range;
	private double tearSpeed;
	private Point position = new Point(0, 0);
	private boolean alive = true;
	private String textureFilename;
	private Texture texture;
	
	private long lastFired = 0;
	private long delay = 0;
	
	public void updated(Dictionary<String, Object> conf) {
		this.hp = Integer.parseInt(conf.get("hp").toString());
		this.dmg = Double.parseDouble(conf.get("dmg").toString());
		this.speed = Double.parseDouble(conf.get("speed").toString());
		this.tears = Double.parseDouble(conf.get("tears").toString());
		this.textureFilename = conf.get("texture").toString();
		this.range = Integer.parseInt(conf.get("range").toString());
		this.tearSpeed = Double.parseDouble(conf.get("tearSpeed").toString());
		this.delay = (long) (1000 * tears);
	}
	
	@Start
	public void start() {
		System.out.println("JIsuucc started");
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
	public int range() {
		return this.range;
	}
	
	@Override
	public double tearSpeed() {
		return this.tearSpeed;
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
	public boolean isAlive() {
		return alive;
	}
	
	@Override
	public void kill() {
		alive = false;
	}

	@Override
	public void render() {
		int x = position.x - texture.width() / 2;
		int y = position.y - texture.height() / 2;
		renderer.drawImage(texture, x, y);
	}

	@Override
	public void move(Direction direction) {
		switch(direction) {
			case UP: position = new Point(position.x, position.y - (int) speed); break;
			case DOWN: position = new Point(position.x, position.y + (int) speed); break;
			case LEFT: position = new Point(position.x - (int) speed, position.y); break;
			case RIGHT: position = new Point(position.x + (int) speed, position.y); break;
		}
	}
	
	@Override
	public void moveTo(Point p) {
		if(p.x < position.x) move(Direction.LEFT);
		if(p.x > position.x) move(Direction.RIGHT);
		if(p.y < position.y) move(Direction.UP);
		if(p.y > position.y) move(Direction.DOWN);
	}

	@Override
	public void onCollision(CollisionEvent event) {
		switch(event.what) {
		case MAP: onCollision(event.box, event.direction); break;
		case ENEMY: break;
		case ISUUCC: break;
		}
	}
	
	private void onCollision(Rectangle tileBox, Direction dir) {
		Dimension entityDimensions = new Dimension(texture().width() / 2, texture().height() / 2);
		switch(dir) {
		case UP: setPosition(new Point(position.x, tileBox.y + Tile.TILE_SIZE + entityDimensions.height)); break;
		case DOWN: setPosition(new Point(position.x, tileBox.y - entityDimensions.height)); break;
		case RIGHT: setPosition(new Point(tileBox.x - entityDimensions.width, position.y)); break;
		case LEFT: setPosition(new Point(tileBox.x + Tile.TILE_SIZE + entityDimensions.width, position.y)); break;
		}
	}

	@Override
	public Optional<Tear> attack(Direction direction) {
		long current = System.currentTimeMillis();
		if(current - lastFired > delay) {
			lastFired = current;
			return Optional.of(tearFactory.newTear(position, dmg, tearSpeed, direction, range));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<Tear> attack(Point p) {
		if(p.x < position.x) return attack(Direction.LEFT);
		if(p.x > position.x) return attack(Direction.RIGHT);
		if(p.y < position.y) return attack(Direction.UP);
		if(p.y > position.y) return attack(Direction.DOWN);
		
		return Optional.empty();
	}

}
