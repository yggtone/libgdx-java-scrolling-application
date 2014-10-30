package ygg.ygg.ssgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;


public class Enemy {

	
	public enum EnemyVersionType {
		REDBIRD, BLUEBIRD, FATBIRD, BIRDGOD, FIREBALL
	}
	public enum EnemyStateType {
		IDLE, ATTACKING, DAMAGED, PREDAMAGED, DEAD
	}
	
	
	private EnemyVersionType enemyVersion = EnemyVersionType.REDBIRD;
	private EnemyStateType enemyState = EnemyStateType.IDLE;
	
	private Vector2 pos;
	private Vector2 velocity;
	private float speed = 100f;
	
	//private Vector2 hitboxOffset;
	
	private float dyingTimer = 0;
	private float divePosition = 150.f;
	private float height = 50f; 
	
	private float animTime = 0;
	
	private boolean floatingUp = false;
	private float recoverTimer = 0;
	
	
	public Enemy(float x, float y) {
		pos = new Vector2().set(x, y);
		
		this.animTime = 0;//(float)Math.random();
		enemyState = EnemyStateType.DEAD;
	}
	
	
	public void SpawnBoss() {
		enemyState = EnemyStateType.IDLE;
		dyingTimer = 0;
		velocity = new Vector2(0,0);
		
		this.enemyVersion = EnemyVersionType.BIRDGOD;
		this.pos = new Vector2(330.f,30.f);
	}
	
	public void SpawnFireball() {
		enemyState = EnemyStateType.IDLE;
		dyingTimer = 0;
		velocity = new Vector2(-200,0);
		this.divePosition = -50;
		
		this.enemyVersion = EnemyVersionType.FIREBALL;
		this.pos = new Vector2(330.f,10.f);
	}
	
	
	public void Spawn(float worldTimer) {
		
		
		
		
		
		enemyState = EnemyStateType.IDLE;
		dyingTimer = 0;
		velocity = new Vector2(-speed,0);
		
		float birdChance = 6.f;
		float blueChance = 6.f;
		if(worldTimer <= 30f) {
			birdChance = 11f;
			blueChance = 11f;
		}
		else if(worldTimer <= 130f) {
			birdChance = 8f;
		}
		else if(worldTimer <= 170f) {
			birdChance = 7f;
			blueChance = 6f;
		}
		
		
		//speed increases as worldTime increases
		//velocity = new Vector2(-1.27f * worldTimer - 50f, 0);
		//speed = 0.7f * worldTimer + 50f;
		speed = 0.6f * worldTimer + 50f;
		//speed = 300f;
		velocity = new Vector2(-speed, 0);
		
		
		if((Math.random() * 10.f) < birdChance)		{
			height = 50f + (float) Math.random() * 130f;
			this.pos = new Vector2(330.f,height);
			
			if((Math.random() * 10.f) < blueChance)
			{
				this.enemyVersion = EnemyVersionType.REDBIRD;
				divePosition = 150.f;
				//later in game, chance of sneaky dive from top
				if(worldTimer > 100f) {
					if((Math.random() * 10.f) < 7)
					{
						divePosition = 20f;
						height = 180f;
					}
				}
				
			}
			else {
				
				this.enemyVersion = EnemyVersionType.BLUEBIRD;
				divePosition = 320.f;
				velocity.mul(1.3f);//blue has extra boost
				speed *= 1.3f;
			}
			
			
			
		}
		else {
			
			
			
			
			enemyVersion = EnemyVersionType.FATBIRD;
			divePosition = -50.f;
			this.pos = new Vector2(330.f,10.f);
		}
		
	}
	
	
	public EnemyStateType getState() {
		return enemyState;
	}
	
	public void setState(EnemyStateType enemyState) {
		this.enemyState = enemyState;
	}
	
	public void resetAnimation() {
		this.animTime = 0;
	}

	public EnemyVersionType getType(){
		return enemyVersion;
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
		if(enemyState == EnemyStateType.DEAD)
			return;		
		else if(enemyState == EnemyStateType.DAMAGED)
		{
			dyingTimer += delta;
			if(dyingTimer > 0.4f)
			{
				dyingTimer = 0;
				enemyState = EnemyStateType.DEAD;
			}
		}
		else if(enemyState == EnemyStateType.IDLE) {
			
			//boss update
			if(this.enemyVersion == EnemyVersionType.BIRDGOD) {
				//move left until in view
				if(this.pos.x > 270.f) {
					this.pos.x -= 10.f * delta;
				}
				
				//floating/bobbing
				if(floatingUp) {
					this.pos.y += 10.f * delta;
					if(this.pos.y > 40 ) {
						floatingUp = false;
					}
				}
				else {
					this.pos.y -= 10.f * delta;
					if(this.pos.y < 25 ) {
						floatingUp = true;
					}
				}
				
				
				
			}
			//flying bird types, diving update
			else if( this.enemyVersion != EnemyVersionType.FATBIRD &&
					this.pos.x < this.divePosition ) {
				Vector2 playerpos = new Vector2(5,12);
				velocity = playerpos.add(this.pos.cpy().mul(-1f) );
				velocity.nor();
				velocity.mul(speed);
				enemyState = EnemyStateType.ATTACKING;
			}
			
			
		}
		
		
		if(this.enemyVersion == EnemyVersionType.BIRDGOD) {
			if(enemyState == EnemyStateType.PREDAMAGED) {				
				recoverTimer += delta;
				if(recoverTimer > 0.2f)
				{
					recoverTimer = 0;
					enemyState = EnemyStateType.IDLE;
				}
			}
			
		}
		
		
		animTime += delta;
		this.pos.add(velocity.cpy().mul(delta));
		
		
		if(this.pos.x < -50.f) {
			enemyState = EnemyStateType.DEAD;
		}
	}
}
