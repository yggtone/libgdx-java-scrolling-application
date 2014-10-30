package ygg.ygg.ssgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;


public class Scenery {

	public enum SceneryType {
		SIDEWALK, HOUSE, TREE, CLOUD, HILLS
	}
	
	//for variations on scenery, different sprite types
	public int subType = 0;
	
	public static final float SPEED = 4f;	// unit per second
	public static final float JUMP_VELOCITY = 4f;
	public static final float SIZE = 0.5f; // half a unit
	
	Vector2 	position = new Vector2();
	Vector2 	acceleration = new Vector2();
	Vector2 	velocity = new Vector2();
	Rectangle 	bounds = new Rectangle();
	SceneryType	sceneType = SceneryType.SIDEWALK;
	boolean		facingLeft = true;
	
	private float width;
	//pixel length of tiled scenery
	private float scrollWidth;
	
	public Scenery(Vector2 position, Vector2 velocity, float width, float scrollWidth, SceneryType stype) {
		this.position = position;
		this.scrollWidth = scrollWidth;
		this.width = width;
		this.velocity = velocity;
		this.sceneType = stype;
		
		//this.subType = MathUtils.random(1,3);
		
		//this.bounds.height = SIZE;
		//this.bounds.width = SIZE;
	}

	
	public boolean isFacingLeft() {
		return facingLeft;
	}

	public void setFacingLeft(boolean facingLeft) {
		this.facingLeft = facingLeft;
	}

	public Vector2 getPosition() {
		return position;
	}
	
	public void setPosition(Vector2 npos) {
		position = npos;
	}

	public Vector2 getAcceleration() {
		return acceleration;
	}

	public Vector2 getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector2 newVel) {
		velocity = newVel;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public SceneryType getState() {
		return sceneType;
	}
	
	public void setSceneryType(SceneryType newSceneryType) {
		this.sceneType = newSceneryType;
	}
	
	public void setSubType(int newType) {
		this.subType = newType;
	}
	
	public int getSubType() {
		return subType;
	}
	
	public void update(float delta) {
		//position.add(velocity.tmp().mul(delta)); 
		//position.add(velocity.mul(delta)); 
		
		if(sceneType == SceneryType.SIDEWALK)	{
			position.x += delta * velocity.x;
			
			//scenery has gone offscreen left, respawn offscreen right
			if(position.x < -width) {
				position.x += scrollWidth;
			}
	
		}
		else if(sceneType == SceneryType.HOUSE)	{
			position.x += delta * velocity.x;
			
			//scenery has gone offscreen left, respawn offscreen right
			if(position.x < -width) {
				position.x += scrollWidth + MathUtils.random(1,30);
				//choose a new random subtype for the house
				//subType = MathUtils.random(1,3);
			}
	
		}
		else if(sceneType == SceneryType.TREE)	{
			position.x += delta * velocity.x;
			
			//scenery has gone offscreen left, respawn offscreen right
			if(position.x < -width) {
				position.x += scrollWidth + MathUtils.random(1,30);
				//choose a new random subtype for the house
				//subType = MathUtils.random(1,3);
			}
	
		}
		else if(sceneType == SceneryType.HILLS)	{
			position.x += delta * velocity.x;
			
			//scenery has gone offscreen left, respawn offscreen right
			if(position.x < -width) {
				position.x += scrollWidth;
			}
	
		}
		//int r = MathUtils.random(1,3);
	
		 
		//System.out.println("x:" + position.x  );
		
		//position.x += delta * 120.f;
		//position.y += delta * 10.f;
		//position.y = 0;
	}
}
