package com.jmc.jisuucc.event.impl;

import java.awt.event.KeyEvent;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.felix.dm.annotation.api.Component;

import com.jmc.jisuucc.event.api.EventQueue;
import com.jmc.jisuucc.event.api.GameEvent;

@Component
public class EventQueueImpl implements EventQueue {
	
	private Queue<GameEvent> eventQueue = new ConcurrentLinkedQueue<>();
	
	private final int W = KeyEvent.VK_W;
	private final int A = KeyEvent.VK_A;
	private final int S = KeyEvent.VK_S;
	private final int D = KeyEvent.VK_D;

	@Override
	public GameEvent poll() {
		return eventQueue.poll();
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case W: eventQueue.add(GameEvent.MOVE_UP); break;
		case S: eventQueue.add(GameEvent.MOVE_DOWN); break;
		case D: eventQueue.add(GameEvent.MOVE_RIGHT); break;
		case A: eventQueue.add(GameEvent.MOVE_LEFT); break;
		case KeyEvent.VK_UP: eventQueue.add(GameEvent.FIRE_UP); break;
		case KeyEvent.VK_DOWN: eventQueue.add(GameEvent.FIRE_DOWN); break;
		case KeyEvent.VK_RIGHT: eventQueue.add(GameEvent.FIRE_RIGHT); break;
		case KeyEvent.VK_LEFT: eventQueue.add(GameEvent.FIRE_LEFT); break;
		default:
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case W: eventQueue.add(GameEvent.STOP_MOVE_UP); break;
		case S: eventQueue.add(GameEvent.STOP_MOVE_DOWN); break;
		case D: eventQueue.add(GameEvent.STOP_MOVE_RIGHT); break;
		case A: eventQueue.add(GameEvent.STOP_MOVE_LEFT); break;
		case KeyEvent.VK_UP: eventQueue.add(GameEvent.STOP_FIRE_UP); break;
		case KeyEvent.VK_DOWN: eventQueue.add(GameEvent.STOP_FIRE_DOWN); break;
		case KeyEvent.VK_RIGHT: eventQueue.add(GameEvent.STOP_FIRE_RIGHT); break;
		case KeyEvent.VK_LEFT: eventQueue.add(GameEvent.STOP_FIRE_LEFT); break;
		default:
		}
	}

}
