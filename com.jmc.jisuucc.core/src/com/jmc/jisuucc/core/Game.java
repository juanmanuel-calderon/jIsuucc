package com.jmc.jisuucc.core;

import java.util.concurrent.TimeUnit;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;

import com.jmc.jisuucc.gamestate.api.GameState;

@Component
public class Game {

	@ServiceDependency
	private GameState state;
	
	private static final int FPS = 1000 / 30;
	
	@Start
	public void start() {
		System.out.println("Game start");
		state.render();
		new Thread(() -> {
			long start = System.currentTimeMillis();
			while(true) {
				state.event();
				state.update();
				state.render();
				
				long end = System.currentTimeMillis();
				try { TimeUnit.MILLISECONDS.sleep(FPS - end + start); }
				catch(Exception e) {}
				start = end;
			}
		}).start();
	}
	
	
}
