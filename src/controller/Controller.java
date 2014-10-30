package ygg.ygg.ssgame.controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import ygg.ygg.ssgame.model.Enemy;
import ygg.ygg.ssgame.model.Player.PlayerStateType;
import ygg.ygg.ssgame.model.Scenery;

import ygg.ygg.ssgame.model.World;

import com.badlogic.gdx.math.Vector2;

public class Controller {

	enum Keys {
		LEFT, RIGHT, JUMP, FIRE
	}
	
	private World 	world;
	
	
	private float camHeight, camWidth;
	
	Array<Vector2> touches = new Array<Vector2>();
	
	static Map<Keys, Boolean> keys = new HashMap<Controller.Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.JUMP, false);
		keys.put(Keys.FIRE, false);
	};

	//  camera dimensions needed for touch screen conversion to pixel coordinates
	public Controller(World world, Vector2 camDimensions) {
		this.world = world;
		
		this.camWidth = camDimensions.x;
		this.camHeight = camDimensions.y;
		
	}

	// ** Key presses and touches **************** //
	
	public void addTouch(Vector2 newTouch) {
		System.out.println("xtouch:" + newTouch.x  );
		touches.add(newTouch);
	}
	
	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	}
	
	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}
	
	public void jumpPressed() {
		keys.get(keys.put(Keys.JUMP, true));
	}
	
	public void firePressed() {
		keys.get(keys.put(Keys.FIRE, false));
	}
	
	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}
	
	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}
	
	public void jumpReleased() {
		keys.get(keys.put(Keys.JUMP, false));
	}
	
	public void fireReleased() {
		keys.get(keys.put(Keys.FIRE, false));
	}
	
	/** The main update method **/
	public void update(float delta) {
		processInput();
		
		//for (Block block : world.getBlocks()) {
		//bob.getPosition().x = touches[0]
		for(Vector2 touch : touches) {
			//convert touch coordinates to pixel coordinates
			touch.x *= (camWidth /Gdx.graphics.getWidth()  );
			touch.y *= -( camHeight / Gdx.graphics.getHeight()  );//flip sign for matrix
			touch.y += camHeight;//screen offset
			
			//System.out.println("xtouch:" + touch.x + "ytouch:" + touch.y  );
			world.checkTouch(touch);
		}
		//start game from title
		if(world.getPlayer().getState() == PlayerStateType.IDLE) {
			if(touches.size > 0) {
				world.startGame();
			}
		}
		//return to title after gameover/score screen
		if(world.getPlayer().getState() == PlayerStateType.SITTING) {
			if(touches.size > 0) {
				world.reset();
			}
		}
		
		touches.clear();
		
		
		
		
	}

	
		
		
		
		
	}
}
