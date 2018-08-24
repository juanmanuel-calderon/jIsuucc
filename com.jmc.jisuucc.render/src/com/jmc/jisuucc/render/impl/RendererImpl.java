package com.jmc.jisuucc.render.impl;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;

import com.jmc.jisuucc.event.api.EventQueue;
import com.jmc.jisuucc.render.api.Renderer;
import com.jmc.jisuucc.render.api.Texture;

@Component
public class RendererImpl implements Renderer {

	@ServiceDependency
	private EventQueue eventListener;

	private Queue<DrawingElement> drawingQueue = new ConcurrentLinkedQueue<>();
	private Canvas gameScreen;

	private class DrawingElement {
		final Texture tex;
		final int x;
		final int y;

		DrawingElement(Texture tex, int x, int y) {
			this.tex = tex;
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "DrawingElement [tex=" + tex.id() + ", x=" + x + ", y=" + y + "]";
		}		
	}

	@Start
	public void start() {
		SwingUtilities.invokeLater(Window::new);
	}

	private class Window extends JFrame {
		private static final long serialVersionUID = -2759363744832257790L;
		private static final int WIDTH = 1024;
		private static final int HEIGHT = 768;

		Window() {
			init();
			gameScreen.createBufferStrategy(2);
		}

		private void init() {
			setTitle("Map Test");

			gameScreen = new Canvas();
			addKeyListener(eventListener);
			add(gameScreen);
			setSize(WIDTH, HEIGHT);
			setLocationRelativeTo(null);
			//setUndecorated(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		}
	}

	@Override
	public void drawImage(Texture texture, int x, int y) {
		drawingQueue.add(new DrawingElement(texture, x, y));
	}

	@Override
	public void drawScreen() {
		Graphics2D g2d = null;
		try {
			g2d = (Graphics2D) gameScreen.getBufferStrategy().getDrawGraphics();
			DrawingElement[] des = drawingQueue.toArray(new DrawingElement[0]);
			if(des.length == 0) return;
			for(DrawingElement de : des) {
				BufferedImage bi = (BufferedImage) de.tex.texture();
				g2d.drawImage(bi, de.x, de.y, bi.getWidth(), bi.getHeight(), null);
			}
			drawingQueue.clear();
		} finally {
			if (g2d != null) {
				g2d.dispose();
			}
		}
		gameScreen.getBufferStrategy().show();
	}
}
