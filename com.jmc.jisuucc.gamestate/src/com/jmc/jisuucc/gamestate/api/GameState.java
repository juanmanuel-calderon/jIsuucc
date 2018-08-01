package com.jmc.jisuucc.gamestate.api;

import com.jmc.jisuucc.render.api.Renderable;

public interface GameState extends Renderable {
	
	public void event();
	public void update();

}
