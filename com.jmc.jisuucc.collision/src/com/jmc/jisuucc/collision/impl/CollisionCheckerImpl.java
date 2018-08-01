package com.jmc.jisuucc.collision.impl;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.apache.felix.dm.annotation.api.Component;

import com.jmc.jisuucc.collision.api.CollisionChecker;
import com.jmc.jisuucc.entity.api.Entity;
import com.jmc.jisuucc.entity.api.Mobile.Direction;
import com.jmc.jisuucc.map.api.Map;
import com.jmc.jisuucc.map.api.Tile;

@Component
public class CollisionCheckerImpl implements CollisionChecker {

	@Override
	public boolean checkCollisionMap(Entity entity, Map map, Direction direction) {
		boolean collision = false;
		
		Point currentPos = entity.position();
		Point newPos = new Point(0, 0);
		Dimension entityDimensions = new Dimension(entity.texture().width() / 2, entity.texture().height() / 2);
		Dimension mapDimensions = new Dimension(map.width() * Tile.TILE_SIZE, map.height() * Tile.TILE_SIZE);
		
		//check map boundaries
		if(currentPos.x - entityDimensions.width < 0) newPos.setLocation(entityDimensions.width, newPos.y);
	    else newPos.setLocation(currentPos.x, newPos.y);
		
		if(currentPos.y - entityDimensions.height < 0) newPos.setLocation(newPos.x, entityDimensions.height);
	    else newPos.setLocation(newPos.x, currentPos.y);
		
	    if(currentPos.x + entityDimensions.width > mapDimensions.width) newPos.setLocation(entityDimensions.width - mapDimensions.width, newPos.y);
	    if(currentPos.y + entityDimensions.height > mapDimensions.height) newPos.setLocation(newPos.x, entityDimensions.height + mapDimensions.height);

	    //check tile collision
	    //we need to check all possible tiles
	    //this means, we start at the first possible tile
	    //if going up it is the tile that contains the (x1, y1) point
	    //and then jump tile size to check the next tile, until we are out of our colliding box
	    Rectangle collisionBox = new Rectangle(newPos.x - entityDimensions.width, newPos.y - entityDimensions.height, 
	    						  			   entityDimensions.width * 2, entityDimensions.height * 2);
	    switch(direction) {
	    case UP: {
	    	Point tilePoint = new Point(collisionBox.x / Tile.TILE_SIZE * Tile.TILE_SIZE, 
	    								collisionBox.y / Tile.TILE_SIZE * Tile.TILE_SIZE);
	    	int i = tilePoint.x;
            while(i < (collisionBox.x + collisionBox.width)) {
            	Rectangle tileBox = new Rectangle(i, tilePoint.y, 
			  			   						  i + Tile.TILE_SIZE, tilePoint.y + Tile.TILE_SIZE);
                Tile tile = map.tileAtCoord(i, tilePoint.y);
                if(tile.hasCollision() && collisionBox.intersects(tileBox)){  
                    newPos.setLocation(newPos.x, tileBox.y + Tile.TILE_SIZE + entityDimensions.height);
                    collision = true;
                }
                i += Tile.TILE_SIZE;
            }
            break;
	    }
	    
	    case DOWN: {
	    	Point tilePoint = new Point(collisionBox.x / Tile.TILE_SIZE * Tile.TILE_SIZE, 
										(collisionBox.y + collisionBox.height) / Tile.TILE_SIZE * Tile.TILE_SIZE);
			int i = tilePoint.x;
			while(i < (collisionBox.x + collisionBox.width)) {
				Rectangle tileBox = new Rectangle(i, tilePoint.y, 
						   						  i + Tile.TILE_SIZE, tilePoint.y + Tile.TILE_SIZE);
				Tile tile = map.tileAtCoord(i, tilePoint.y);
				if(tile.hasCollision() && collisionBox.intersects(tileBox)){  
					newPos.setLocation(newPos.x, tileBox.y - entityDimensions.height);
					collision = true;
				}
				i += Tile.TILE_SIZE;
			}
			break;
	    }
	    
	    case RIGHT: {
	    	Point tilePoint = new Point((collisionBox.x + collisionBox.width) / Tile.TILE_SIZE * Tile.TILE_SIZE, 
										 collisionBox.y / Tile.TILE_SIZE * Tile.TILE_SIZE);
			int i = tilePoint.y;
			while(i < (collisionBox.y + collisionBox.height)) {
				Rectangle tileBox = new Rectangle(tilePoint.x, i, 
					   						  	  tilePoint.x + Tile.TILE_SIZE, i + Tile.TILE_SIZE);
				Tile tile = map.tileAtCoord(tilePoint.x, i);
				if(tile.hasCollision() && collisionBox.intersects(tileBox)){  
					newPos.setLocation(tileBox.x - entityDimensions.width, newPos.y);
					collision = true;
				}
				i += Tile.TILE_SIZE;
			}
			break;
	    }
	    
	    case LEFT: {
	    	Point tilePoint = new Point(collisionBox.x / Tile.TILE_SIZE * Tile.TILE_SIZE, 
										collisionBox.y / Tile.TILE_SIZE * Tile.TILE_SIZE);
			int i = tilePoint.y;
			while(i < (collisionBox.y + collisionBox.height)) {
				Rectangle tileBox = new Rectangle(tilePoint.x, i, 
				   						  	  	  tilePoint.x + Tile.TILE_SIZE, i + Tile.TILE_SIZE);
				Tile tile = map.tileAtCoord(tilePoint.x, i);
				if(tile.hasCollision() && collisionBox.intersects(tileBox)){  
					newPos.setLocation(tileBox.x + Tile.TILE_SIZE + entityDimensions.width, newPos.y);
					collision = true;
				}
				i += Tile.TILE_SIZE;
			}
			break;
	    }
	    }
	    
	    entity.setPosition(newPos);
		return collision;
	}
	
}
