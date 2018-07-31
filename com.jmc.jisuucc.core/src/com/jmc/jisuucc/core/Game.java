package com.jmc.jisuucc.core;

import java.awt.Point;
import java.util.concurrent.TimeUnit;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;

import com.jmc.jisuucc.entity.api.JIsuucc;
import com.jmc.jisuucc.event.api.EventQueue;
import com.jmc.jisuucc.event.api.GameEvent;
import com.jmc.jisuucc.map.api.Map;
import com.jmc.jisuucc.map.api.Tile;
import com.jmc.jisuucc.map.api.TileManager;
import com.jmc.jisuucc.render.api.Renderer;

@Component
public class Game {

	@ServiceDependency
	private TileManager mapParser;
	
	@ServiceDependency(filter = "(name=map1)")
	private Map map;
	
	@ServiceDependency(filter = "(name=helivg)")
	private JIsuucc isuucc;
	
	@ServiceDependency
	private Renderer renderer;
	
	@ServiceDependency
	private EventQueue eventQueue;
	
	private static int FPS = 1000 / 30;
	
	@Start
	public void start() throws Exception {
		new Thread(() -> {
			isuucc.setPosition(new Point(map.width() * Tile.TILE_SIZE / 2, map.height() * Tile.TILE_SIZE / 2));
			long start = System.currentTimeMillis();
			while(true) {
				GameEvent event = eventQueue.poll();
				treatEvent(event);
				map.render();
				isuucc.render();
				renderer.drawScreen();
				
				long end = System.currentTimeMillis();
				try { TimeUnit.MILLISECONDS.sleep(FPS - end + start); }
				catch(Exception e) {}
				start = end;
			}
		}).start();
	}
	
	private void treatEvent(GameEvent event) {
		if(event == null) return;
		System.out.println(event);
		Point pos = isuucc.position();
		double speed = isuucc.speed();
		if(GameEvent.MOVE_UP.equals(event)) {
			System.out.println("UP");
			isuucc.setPosition(new Point(pos.x, pos.y - (int) speed));
			System.out.println(isuucc.position());
		}
		if(GameEvent.MOVE_DOWN.equals(event)) {
			System.out.println("UP");
			isuucc.setPosition(new Point(pos.x, pos.y + (int) speed));
			System.out.println(isuucc.position());
		}
		if(GameEvent.MOVE_RIGHT.equals(event)) {
			System.out.println("UP");
			isuucc.setPosition(new Point(pos.x + (int) speed, pos.y));
			System.out.println(isuucc.position());
		}
		if(GameEvent.MOVE_LEFT.equals(event)) {
			System.out.println("UP");
			isuucc.setPosition(new Point(pos.x - (int) speed, pos.y));
			System.out.println(isuucc.position());
		}
	}
}
