package com.jmc.jisuucc.collision.impl;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.apache.felix.dm.annotation.api.Component;

import com.jmc.jisuucc.collision.api.CollisionChecker;
import com.jmc.jisuucc.entity.api.Entity;
import com.jmc.jisuucc.entity.api.Mobile;
import com.jmc.jisuucc.event.api.CollisionEvent;
import com.jmc.jisuucc.event.api.CollisionEvent.CollisionEntity;
import com.jmc.jisuucc.event.api.Direction;
import com.jmc.jisuucc.map.api.Map;
import com.jmc.jisuucc.map.api.Tile;

@Component
public class CollisionCheckerImpl implements CollisionChecker {

	@Override
	public boolean checkCollision(Entity entity, Map map, Direction direction) {
		Dimension entityDimensions = new Dimension(entity.texture().width() / 2, entity.texture().height() / 2);
		
	    //check tile collision
	    //we need to check all possible tiles
	    //this means, we start at the first possible tile
	    //if going up it is the tile that contains the (x1, y1) point
	    //and then jump tile size to check the next tile, until we are out of our colliding box
	    Rectangle collisionBox = new Rectangle(entity.position().x - entityDimensions.width, entity.position().y - entityDimensions.height, 
	    						  			   entityDimensions.width * 2, entityDimensions.height * 2);
	    switch(direction) {
	    case UP: return upwardsMapScan(entity, map, collisionBox, collisionBox.y, Direction.UP);
	    case DOWN: return upwardsMapScan(entity, map, collisionBox, (collisionBox.y + collisionBox.height), Direction.DOWN);
	    case RIGHT: return sidewaysMapScan(entity, map, collisionBox, (collisionBox.x + collisionBox.width), Direction.RIGHT);
	    case LEFT: return sidewaysMapScan(entity, map, collisionBox, collisionBox.x, Direction.LEFT);
	    }
	    return false;
	}
	
	private boolean upwardsMapScan(Entity entity, Map map, Rectangle collisionBox, int initialY, Direction dir) {
		Point tilePoint = new Point(collisionBox.x / Tile.TILE_SIZE * Tile.TILE_SIZE, 
									initialY / Tile.TILE_SIZE * Tile.TILE_SIZE);
		int i = tilePoint.x;
		while(i < (collisionBox.x + collisionBox.width)) {
			Rectangle tileBox = new Rectangle(i, tilePoint.y, 
			   						  i + Tile.TILE_SIZE, tilePoint.y + Tile.TILE_SIZE);
			Tile tile = map.tileAtCoord(i, tilePoint.y);
			if(tile.hasCollision() && collisionBox.intersects(tileBox)){  
				CollisionEvent event = new CollisionEvent(CollisionEntity.MAP, tile, tileBox, dir);
				((Mobile) entity).onCollision(event);
				return true;
			}
			i += Tile.TILE_SIZE;
		}
		return false;
	}
	
	private boolean sidewaysMapScan(Entity entity, Map map, Rectangle collisionBox, int initialX, Direction dir) {
		Point tilePoint = new Point(initialX / Tile.TILE_SIZE * Tile.TILE_SIZE, 
				 					collisionBox.y / Tile.TILE_SIZE * Tile.TILE_SIZE);
		int i = tilePoint.y;
		while(i < (collisionBox.y + collisionBox.height)) {
			Rectangle tileBox = new Rectangle(tilePoint.x, i, 
									  	  tilePoint.x + Tile.TILE_SIZE, i + Tile.TILE_SIZE);
			Tile tile = map.tileAtCoord(tilePoint.x, i);
			if(tile.hasCollision() && collisionBox.intersects(tileBox)){  
				CollisionEvent event = new CollisionEvent(CollisionEntity.MAP, tile, tileBox, dir);
				((Mobile) entity).onCollision(event);
				return true;
			}
				i += Tile.TILE_SIZE;
		}
		return false;
	}
	
}
