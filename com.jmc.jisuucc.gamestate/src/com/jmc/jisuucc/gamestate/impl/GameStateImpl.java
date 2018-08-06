package com.jmc.jisuucc.gamestate.impl;

import java.awt.Point;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;

import com.jmc.jisuucc.collision.api.CollisionChecker;
import com.jmc.jisuucc.entity.api.JIsuucc;
import com.jmc.jisuucc.event.api.Direction;
import com.jmc.jisuucc.event.api.EventQueue;
import com.jmc.jisuucc.event.api.GameEvent;
import com.jmc.jisuucc.gamestate.api.GameState;
import com.jmc.jisuucc.map.api.Map;
import com.jmc.jisuucc.map.api.Tile;
import com.jmc.jisuucc.render.api.Renderer;

@Component
public class GameStateImpl implements GameState {
	
	@ServiceDependency(filter = "(name=helivg)")
	private JIsuucc isuucc;
	
	@ServiceDependency(filter = "(name=map1)")
	private Map map;
	
	@ServiceDependency
	private EventQueue eventQueue;
	
	@ServiceDependency
	private Renderer renderer;
	
	@ServiceDependency
	private CollisionChecker collisionChecker;
	
	private boolean[] moveEvents = new boolean[Direction.values().length];

	@Start
	public void start() {
		isuucc.setPosition(new Point(map.width() * Tile.TILE_SIZE / 2, map.height() * Tile.TILE_SIZE / 2));
	}
	
	@Override
	public void render() {
		map.render();
		isuucc.render();
		renderer.drawScreen();
	}
	
	@Override
	public void event() {
		GameEvent event = eventQueue.poll();
		consume(event);
	}
	
	private void consume(GameEvent event) {
		if(event == null) return;
		System.out.println(event);
		switch(event) {
		case MOVE_UP: moveEvents[0] = true; break;
		case MOVE_DOWN: moveEvents[1] = true; break;
		case MOVE_RIGHT: moveEvents[2] = true; break;
		case MOVE_LEFT: moveEvents[3] = true; break;
		case STOP_MOVE_UP: moveEvents[0] = false; break;
		case STOP_MOVE_DOWN: moveEvents[1] = false; break;
		case STOP_MOVE_RIGHT: moveEvents[2] = false; break;
		case STOP_MOVE_LEFT: moveEvents[3] = false; break;
		default:
		}
	}

	@Override
	public void update() {
		if(moveEvents[0]) isuucc.move(Direction.UP);
		if(moveEvents[1]) isuucc.move(Direction.DOWN);
		if(moveEvents[2]) isuucc.move(Direction.RIGHT);
		if(moveEvents[3]) isuucc.move(Direction.LEFT);
		while(checkCollisions());
	}
	
	private boolean checkCollisions() {
		boolean collision = false;
		if(moveEvents[0]) collision = (collision || collisionChecker.checkCollision(isuucc, map, Direction.UP));
		if(moveEvents[1]) collision = (collision || collisionChecker.checkCollision(isuucc, map, Direction.DOWN));
		if(moveEvents[2]) collision = (collision || collisionChecker.checkCollision(isuucc, map, Direction.RIGHT));
		if(moveEvents[3]) collision = (collision || collisionChecker.checkCollision(isuucc, map, Direction.LEFT));
		return collision;
	}

}
