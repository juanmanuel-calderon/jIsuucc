package com.jmc.jisuucc.event.api;

import java.awt.event.KeyListener;

public interface EventQueue extends KeyListener {
	
	public GameEvent poll();

}
