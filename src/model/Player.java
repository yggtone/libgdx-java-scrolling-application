package ygg.ygg.ssgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;


public class Player {

	public enum PlayerStateType {
		IDLE, SKATING, FALLING, SITTING
	}
	PlayerStateType playerState = PlayerStateType.IDLE;
	
	private float VELOCITY = 20;
	private Vector2 pos;
	
	private float animTime = 0;
	
	public Player(float x, float y) {
		pos = new Vector2().set(x, y);
		
		this.animTime = 0;
	}

	public void setState(PlayerStateType newState) {
		this.playerState = newState;
	}
	
	public PlayerStateType getState() {
		return playerState;
	}
	
	
	public float getStateTime(){
		return animTime;
	}

	public Vector2 getPosition() {
		return pos;
	}
	
	public void setPosition(Vector2 npos) {
		this.pos = npos;
	}

	
	public void update(float delta) {
		animTime += delta;
	}
}
