package com.jmc.jisuucc.entity.impl;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.jmc.jisuucc.entity.api.Tear;
import com.jmc.jisuucc.event.api.CollisionEvent;
import com.jmc.jisuucc.event.api.Direction;
import com.jmc.jisuucc.map.api.Tile;
import com.jmc.jisuucc.render.api.Renderer;
import com.jmc.jisuucc.render.api.Texture;

public class TearImpl implements Tear {
	
	private Point position;
	private Texture texture;
	private double dmg;
	private double speed;
	private Direction direction;
	private int range;
	private boolean alive = true;
	
	private int moveCounter = 0;
	
	private Renderer renderer;
	
	public TearImpl(Point position, Texture texture, double dmg, double speed, Direction direction, int range) {
		this.position = position;
		this.texture = texture;
		this.dmg = dmg;
		this.speed = speed;
		this.direction = direction;
		this.range = range;
	}
	
	void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
	@Override
	public Point position() {
		return position;
	}

	@Override
	public Texture texture() {
		return texture;
	}

	@Override
	public void setPosition(Point position) {
		this.position = position;
	}

	@Override
	public void render() {
		int x = position.x - texture.width() / 2;
		int y = position.y - texture.height() / 2;
		renderer.drawImage(texture, x, y);
	}

	@Override
	public void move(Direction direction) {
		int moveSpeed = (int) speed;
		moveCounter = moveCounter + (int) speed;
		if(moveCounter > range) {
			alive = false;
			moveSpeed = moveCounter - range;
		}
		switch(direction) {
			case UP: position = new Point(position.x, position.y - moveSpeed); break;
			case DOWN: position = new Point(position.x, position.y + moveSpeed); break;
			case LEFT: position = new Point(position.x - moveSpeed, position.y); break;
			case RIGHT: position = new Point(position.x + moveSpeed, position.y); break;
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
		kill();
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
	public double dmg() {
		return dmg;
	}

	@Override
	public double speed() {
		return speed;
	}

	@Override
	public Direction direction() {
		return direction;
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
	public String toString() {
		return "TearImpl [position=" + position + ", texture=" + texture + ", dmg=" + dmg + ", speed=" + speed
				+ ", direction=" + direction + "]";
	}

}
