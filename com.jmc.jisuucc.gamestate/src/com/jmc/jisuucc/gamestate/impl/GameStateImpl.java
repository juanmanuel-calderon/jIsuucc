package com.jmc.jisuucc.gamestate.impl;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;

import com.jmc.jisuucc.collision.api.CollisionChecker;
import com.jmc.jisuucc.entity.api.Agressive;
import com.jmc.jisuucc.entity.api.Entity;
import com.jmc.jisuucc.entity.api.JIsuucc;
import com.jmc.jisuucc.entity.api.Mobile;
import com.jmc.jisuucc.entity.api.Tear;
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
	private boolean[] fireEvents = new boolean[Direction.values().length];
	private List<Tear> isuucTears = new ArrayList<>();

	@Start
	public void start() {
		System.out.println("Game state started");
		isuucc.setPosition(new Point(map.width() * Tile.TILE_SIZE / 2, map.height() * Tile.TILE_SIZE / 2));
	}
	
	@Override
	public void render() {
		map.render();
		isuucc.render();
		for(Tear t : isuucTears) t.render();
		renderer.drawScreen();
		
	}
	
	@Override
	public void event() {
		GameEvent event = eventQueue.poll();
		consume(event);
	}
	
	private void consume(GameEvent event) {
		if(event == null) return;
		switch(event) {
		case MOVE_UP: moveEvents[0] = true; break;
		case MOVE_DOWN: moveEvents[1] = true; break;
		case MOVE_RIGHT: moveEvents[2] = true; break;
		case MOVE_LEFT: moveEvents[3] = true; break;
		case STOP_MOVE_UP: moveEvents[0] = false; break;
		case STOP_MOVE_DOWN: moveEvents[1] = false; break;
		case STOP_MOVE_RIGHT: moveEvents[2] = false; break;
		case STOP_MOVE_LEFT: moveEvents[3] = false; break;
		case FIRE_UP: setFireEvent(0, true); break;
		case FIRE_DOWN: setFireEvent(1, true); break;
		case FIRE_RIGHT: setFireEvent(2, true); break;
		case FIRE_LEFT: setFireEvent(3, true); break;
		case STOP_FIRE_UP: setFireEvent(0, false); break;
		case STOP_FIRE_DOWN: setFireEvent(1, false); break;
		case STOP_FIRE_RIGHT: setFireEvent(2, false); break;
		case STOP_FIRE_LEFT: setFireEvent(3, false); break;
		default:
		}
	}
	
	private void setFireEvent(int pos, boolean val) {
		for(int i = 0; i < fireEvents.length; i++) fireEvents[i] = false;
		fireEvents[pos] = val;
	}

	@Override
	public void update() {
		
		isuucTears.stream().forEach(t -> registerMove(t, t.direction()));
		if(moveEvents[0]) registerMove(isuucc, Direction.UP);
		if(moveEvents[1]) registerMove(isuucc, Direction.DOWN);
		if(moveEvents[2]) registerMove(isuucc, Direction.RIGHT);
		if(moveEvents[3]) registerMove(isuucc, Direction.LEFT);
		if(fireEvents[0]) registerAttack(isuucc, Direction.UP, isuucTears);
		if(fireEvents[1]) registerAttack(isuucc, Direction.DOWN, isuucTears);
		if(fireEvents[2]) registerAttack(isuucc, Direction.RIGHT, isuucTears);
		if(fireEvents[3]) registerAttack(isuucc, Direction.LEFT, isuucTears);
		
		Iterator<Tear> tears = isuucTears.iterator();
		while(tears.hasNext()) {
			Tear t = tears.next();
			if(!t.isAlive()) tears.remove();
		}
	}
	
	private void registerMove(Entity entity, Direction dir) {
		((Mobile) entity).move(dir);
		collisionChecker.checkCollision(entity, map, dir);
	}
	
	private void registerAttack(Agressive entity, Direction dir, List<Tear> list) {
		Optional<Tear> tear = entity.attack(dir);
		if(tear.isPresent()) list.add(tear.get());
	}
}
