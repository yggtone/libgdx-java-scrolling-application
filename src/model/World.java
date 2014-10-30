package ygg.ygg.ssgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Files;


import ygg.ygg.ssgame.model.Enemy.EnemyStateType;
import ygg.ygg.ssgame.model.Enemy.EnemyVersionType;
import ygg.ygg.ssgame.model.Player.PlayerStateType;
import ygg.ygg.ssgame.model.Scenery;
import ygg.ygg.ssgame.model.Scenery.SceneryType;




public class World {

	public enum WorldStateType {
		TITLE, PLAYING, GAMEOVER, FADE, WIN
	}
	private WorldStateType worldState = WorldStateType.TITLE;
	
	public WorldStateType getState() {
		return worldState;
	}
	
	private Sound hitASound;
	private Sound hitBSound;
	private Sound hitCSound;
	private Sound hitDSound;
	private Sound hitCat;
	
	private float screenFade;
	
	private boolean floatingUp = false;
	private float logoHeight = 0;
	public float getLogoHeight(){
		return logoHeight;
	}
	
	private Player catPlayer;
	
	private Array<Scenery> sidewalks = new Array<Scenery>();
	private Array<Scenery> houses = new Array<Scenery>();
	private Array<Scenery> trees = new Array<Scenery>();
	private Array<Scenery> hillsFront = new Array<Scenery>();
	private Array<Scenery> hillsBack = new Array<Scenery>();
	
	private float scenerySpeed = 0.0f;
	
	private float spawnTimer = 0.0f;
	private float worldTimer = 0f;
	
	
	private boolean bossSpawned = false;
	private float bossAttackTimer = 0;
	private float bossHealth = 0;
	private float bossTimer = 0;
	
	private Array<Enemy> enemyPool = new Array<Enemy>();
	private int enemyPoolIndex = 0;
	
	private Enemy Boss = new Enemy(400,100);
	
	
	
	private Music titleMusic;
	private Music playMusic;
	
	
	public float getScreenFade(){
		return screenFade;
	}
	
	public boolean isBossSpawned() {
		return this.bossSpawned;
	}
	
	public Enemy getBoss() {
		return this.Boss;
	}
	
	public float getTimer() {
		return worldTimer;
	}	
	public Array<Enemy> getEnemies(){
		return enemyPool;
	}
	public Player getPlayer() {
		return catPlayer;
	}
	public Array<Scenery> getSidewalks() {
		return sidewalks;
	}
	public Array<Scenery> getHouses() {
		return houses;
	}
	public Array<Scenery> getTrees() {
		return trees;
	}
	public Array<Scenery> getHillsFront() {
		return hillsFront;
	}
	public Array<Scenery> getHillsBack() {
		return hillsBack;
	}
	// --------------------
	
	
	private float totalScore;
	private float comboCount;
	private float birdGodBonus;
	
	private String scoreString = " ";
	public String getScore() {
		return scoreString;
	}

	public World() {
		//initialize sound files
		playMusic = Gdx.audio.newMusic(Gdx.files.internal("data/TitleA2.ogg"));
		playMusic.setLooping(true);
		//playMusic.play();
		
		titleMusic = Gdx.audio.newMusic(Gdx.files.internal("data/IntroA1.ogg"));
		titleMusic.setLooping(true);
		//titleMusic.play();
		
		
	        	
		hitASound =  Gdx.audio.newSound(Gdx.files.internal("data/hitA.wav"));
		hitBSound =  Gdx.audio.newSound(Gdx.files.internal("data/hitB.wav"));
		hitCSound =  Gdx.audio.newSound(Gdx.files.internal("data/hitC.wav"));
		hitDSound =  Gdx.audio.newSound(Gdx.files.internal("data/hitD.wav"));
		hitCat =  Gdx.audio.newSound(Gdx.files.internal("data/hitCat.wav"));
		
		
		createWorld();
		
	}
	
	public void dispose() {
		hitASound.dispose();
		hitBSound.dispose();
		hitCSound.dispose();
		hitDSound.dispose();
		
		playMusic.dispose();
		titleMusic.dispose();
		hitCat.dispose();
		
	}
	
	public void reset()
	{
			
		totalScore = 0;
		comboCount = 0;
		birdGodBonus = 0;
		
		hitASound.play();
		titleMusic.play();
		
		//TODO reset spawn and progress
		for(Enemy enemy : enemyPool) {
			enemy.setPosition(new Vector2(-100,0) );
			enemy.setState(EnemyStateType.DEAD);
		}
		catPlayer.setState(PlayerStateType.IDLE);
		worldState = WorldStateType.TITLE;
		worldTimer = 0f;
		
		bossSpawned = false;
		screenFade = 0;
		
		//logoHeight = 0;
	}
	
	public void startGame() {
		
		hitASound.play();
		titleMusic.stop();
		playMusic.play();
		
		catPlayer.setState(PlayerStateType.SKATING);
		worldState = WorldStateType.PLAYING;
		worldTimer = 0f;
	}

	public void checkTouch(Vector2 touchPos)
	{
		
		if (catPlayer.getState() != PlayerStateType.SKATING)
			return;
		
		//enemyPool.get(0).setPosition(touch);
		
		//totalScore = 0;
		//comboCount = 0;
		//birdGodBonus = 0;
		
		//check tapping boss
		if(bossSpawned) {
			
			Vector2 enemyPos = Boss.getPosition();		
			if (touchPos.x > enemyPos.x && 
					touchPos.y > 30 && touchPos.y < 180)
			{
				if(Boss.getState() == EnemyStateType.IDLE) {
					Boss.setState(EnemyStateType.PREDAMAGED);
					Boss.resetAnimation();
					hitDSound.play();
					bossHealth -= 1.0f;
					
					birdGodBonus += 300f;
				}
				
			}
		}
		
		//check for one or more hit in a tap, decides sound
		int hitCount = 0;
		boolean playedHitSound = false;
		float currentHitScore = 0;
		
		//check tapping enemies
		for(Enemy enemy : enemyPool) {
			Vector2 offset;
			Vector2 enemyPos;
			//radius collision
			if( 	enemy.getState() != EnemyStateType.DAMAGED &&
					enemy.getState() != EnemyStateType.DEAD ){
				
				
				if(enemy.getType() == EnemyVersionType.FATBIRD){
					
					//offset = new Vector2(24,16);
					//enemyPos = enemy.getPosition().cpy().add(offset);
					enemyPos = enemy.getPosition();
					
					float left = enemyPos.x + 10;
					float right = enemyPos.x + 39;
					float top = enemyPos.y + 46;
					float bottom = enemyPos.y + 3;
					
					//if (touchPos.dst2(enemyPos) < 325  )
					if(touchPos.x > left && touchPos.x < right &&
						touchPos.y > bottom && touchPos.y < top )
					{
						if(enemy.getState() == EnemyStateType.IDLE) {
							enemy.setState(EnemyStateType.PREDAMAGED);
							enemy.resetAnimation();
							if(!playedHitSound){
								playedHitSound = true;
								hitBSound.play();
							}
							
						}
						else if(enemy.getState() == EnemyStateType.PREDAMAGED)	{
							enemy.setState(EnemyStateType.DAMAGED);
							enemy.resetAnimation();
							if(!playedHitSound){
								playedHitSound = true;
								hitCSound.play();
							}
							hitCount++;
							currentHitScore += 200f + enemyPos.x;
						}
						
					}
				}
				else {//other enemies, birds
					
					//offset = new Vector2(24,16);
					//enemyPos = enemy.getPosition().cpy().add(offset);
					enemyPos = enemy.getPosition();
					
					float left = enemyPos.x + 2;
					float right = enemyPos.x + 44;
					float bottom = enemyPos.y + 7;
					float top = enemyPos.y + 28;
					
					//if (touchPos.dst2(enemyPos) < 235  )
					if(touchPos.x > left && touchPos.x < right &&
						touchPos.y > bottom && touchPos.y < top )
					{
						enemy.setState(EnemyStateType.DAMAGED);
						enemy.resetAnimation();
						
						if(enemy.getType() == EnemyVersionType.FIREBALL){
							enemy.setState(EnemyStateType.DEAD);
						}
						
						if(!playedHitSound){
							playedHitSound = true;
							hitASound.play();
						}
						hitCount++;
						currentHitScore += 100f + enemyPos.x;
						
					}
				}
			
			}
		}
		
		totalScore += currentHitScore * hitCount;
		if(hitCount > 1)
		{
			comboCount++;
		}
		
	}
	
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    float d = Float.parseFloat(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	private void gameEnd() {
		
		catPlayer.setState(PlayerStateType.FALLING);
		playMusic.stop();
		
		hitCat.play();
		
		//totalScore = 0;
		//comboCount = 0;
		//birdGodBonus = 0;
		float worldTimerMultiply = 10.f;
		totalScore += birdGodBonus;
		totalScore += worldTimer * worldTimerMultiply;
		//scoreString = "GAME OVER \nTime Bonus: " + (int)worldTimer + "\nCombos: " + (int)comboCount + "\nBird God Bonus: " + (int)birdGodBonus + "\nTotal Score: " + (int)totalScore;
		scoreString = "  \n " + (int)(worldTimer * worldTimerMultiply) + "\n " + (int)comboCount + "\n " + (int)birdGodBonus + "\n " + (int)totalScore;
		
		
		//check high score and save
		
		boolean isLocAvailable = Gdx.files.isLocalStorageAvailable();
		
		if(isLocAvailable) {
			String highscoreFilename = "hss";
			boolean exists = Gdx.files.local(highscoreFilename).exists();
			if(exists) {
				//FileHandle file = Gdx.files.local(highscoreFilename);
				//file.writeString("My god, it's full of stars", false);//overwrite/append?
				
				FileHandle file = Gdx.files.local(highscoreFilename);
				String highScore = file.readString();
				if(isNumeric(highScore)) {
					
					
					float fileScore = Float.parseFloat(highScore);
					
					if(fileScore < totalScore) {
						//new high score, write to file
						FileHandle fileb = Gdx.files.local(highscoreFilename);
						fileb.writeString(new String(totalScore + ""), false);
						
						scoreString += "\n" + (int) totalScore + "\n NEW HIGH SCORE!";
					}
					else {
						scoreString += "\n" + (int)fileScore ;
					}
					
					
				}
				else {//invalid file contents, overwrite with the current score
					FileHandle fileb = Gdx.files.local(highscoreFilename);
					fileb.writeString(new String(totalScore + ""), false);
					scoreString += "\n" + (int)totalScore;
				}
								
			
				
			}
			else {//file not created yet, fill in current score
				
				FileHandle file = Gdx.files.local(highscoreFilename);
				file.writeString(new String(totalScore + ""), false);//overwrite/append?
				
				
				scoreString += "\n" + (int)totalScore;
			}

		}
		
	}
	
	public void updateWorld(float delta) {
		
		
		if(worldState == WorldStateType.TITLE)
		{
			//floating/bobbing logo
			if(floatingUp) {
				logoHeight += 30.f * delta;
				if(logoHeight > 10 ) {
					floatingUp = false;
				}
			}
			else {
				logoHeight -= 30.f * delta;
				if(logoHeight < -10 ) {
					floatingUp = true;
				}
			}
			
			if(logoHeight > 10)
				logoHeight -= 90.f * delta;
		}
		else
		{
			if(logoHeight < 220){
				logoHeight += 90.f * delta;
			}
		}
		
		if(worldState == WorldStateType.PLAYING)
		{
			worldTimer += delta;
			
			//if(worldTimer > 10) {
			//	worldState = WorldStateType.FADE;
			//}
		
		}
		else if( worldState == WorldStateType.FADE){
			screenFade += delta;
			if(screenFade >= 1.f){
				screenFade = 1.f;
				worldState = WorldStateType.WIN;
				//TODO clear enemies, etc. ,check winstate by boss hits,
			}
		}
		else if( worldState == WorldStateType.WIN){
			if(screenFade > 0)
			{
				screenFade -= delta;
				if(screenFade < 0){
					screenFade = 0;
				}
			}
		}
		
		//update enemy spawn cycle
		if( catPlayer.getState() == PlayerStateType.SKATING 
		   && scenerySpeed >= 1f		) {
		
			spawnTimer -= delta;
			if(spawnTimer <= 0.0f) {
				
				//basetime that decreases as worldTimer increases (time between enemies decreases)
				//float baseTime = 3.f / (worldTimer * .3f + 1f);
				float baseTime = 3.f / (worldTimer * .6f + 1f);
				spawnTimer = (float) (baseTime + Math.random() * 1.0 );
				
				enemyPool.get(enemyPoolIndex).Spawn(worldTimer);
				
				enemyPoolIndex++;
				if(enemyPoolIndex >= enemyPool.size) {
					enemyPoolIndex = 0;
				}
			}
		
		}
		
		
		bossTimer += delta;
		if(!bossSpawned) {
			if(worldTimer > 98f && bossTimer > 30f) {
				Boss.SpawnBoss();
				bossAttackTimer = 0;
				bossHealth = 10;
				bossSpawned = true;
				bossTimer = 0;
			
			}
		}
		else {
			Boss.update(delta);
			
			/*
			bossAttackTimer += delta;
			if(bossAttackTimer > 5  && catPlayer.getState() == PlayerStateType.SKATING) {
				//spawn fireball attack as an enemy object
				
				enemyPool.get(enemyPoolIndex).SpawnFireball();				
				enemyPoolIndex++;
				if(enemyPoolIndex >= enemyPool.size) {
					enemyPoolIndex = 0;
				}
				bossAttackTimer = 0;
			}
			*/
			
			if(bossTimer > 12)
			{
				Boss.getPosition().add(new Vector2(100 * delta,0) );
				if(Boss.getPosition().x > 400f)
				{
					Boss.setState(EnemyStateType.DEAD);
					bossSpawned = false;
					bossTimer = 0;
				}
				
			}
			
			//screenFade += delta; //TODO fade on ending
		}
		
		catPlayer.update(delta);
		if( catPlayer.getState() == PlayerStateType.SITTING ) {
			worldState = WorldStateType.GAMEOVER;
		}
		
		
		//update enemies and check hitting player
		if( worldState == WorldStateType.PLAYING ) {		
			for(Enemy enemy : enemyPool) {				
				enemy.update(delta);
				
				//check collision between player and enemies
				if( enemy.getState() != EnemyStateType.DAMAGED &&
					catPlayer.getState() == PlayerStateType.SKATING && 
					enemy.getState() != EnemyStateType.DEAD ){
					
					if( catPlayer.getPosition().dst2(enemy.getPosition()) < 20) {
						
						gameEnd();
						
						
					
					}
				}
				
			}		
		} else if( worldState == WorldStateType.GAMEOVER ) {
			//scroll off any remaining enemies
			for(Enemy enemy : enemyPool) {
				if(enemy.getState() != EnemyStateType.DEAD) {
					//enemy.setPosition(enemy.getPosition().add(new Vector2(-100 * delta,0) )  );
					enemy.getPosition().add(new Vector2(-100 * delta,0) ) ;					
					if(enemy.getPosition().x < -200f)
						enemy.setState(EnemyStateType.DEAD);
				}
			}
			if(Boss.getState() != EnemyStateType.DEAD) {
				//Boss.setPosition(Boss.getPosition().add(new Vector2(100 * delta,0) )  );
				Boss.getPosition().add(new Vector2(100 * delta,0) );
				if(Boss.getPosition().x > 400f)
					Boss.setState(EnemyStateType.DEAD);
			}
		}
		
		
		//move scenery for skating/moving effect, slow down when cat falls
		if(catPlayer.getState() == PlayerStateType.SKATING) {
			if(scenerySpeed < 1f) {
				scenerySpeed += delta* .21f;
				if(scenerySpeed > 1f)
					scenerySpeed = 1f;
			}
		}
		else {
			if(scenerySpeed > 0)
			{
				scenerySpeed -= delta* .21f;
				if(scenerySpeed < 0)
				{
					scenerySpeed = 0;
					catPlayer.setState(PlayerStateType.SITTING);
					
				}
			
			}
		}
	
		
		float scrollIncrement = delta * scenerySpeed ;
		//update scenery
		for(Scenery sidewalk :sidewalks) {
			sidewalk.update(scrollIncrement);
		}
		for(Scenery house : houses) {
			house.update(scrollIncrement);
		}
		for(Scenery tree : trees) {
			tree.update(scrollIncrement);
		}
		for(Scenery hillFront : hillsFront) {
			hillFront.update(scrollIncrement);
		}
		for(Scenery hillBack : hillsBack) {
			hillBack.update(scrollIncrement);
		}
	}
	
        //create player and reusable pools for enemies and background	
	private void createWorld() {
		
		catPlayer = new Player(5,13);
	        	
		int numEnemies = 26;
		//float sidewalkWidth = 72.f; 
		for (int i = 0; i < numEnemies; i++) {
			//tile by pixel width, and set scroll length to sum
			Enemy newEnemy = new Enemy(400,100);		
		
			enemyPool.add(newEnemy);
		} 
		
		int numSidewalks = 26;
		float sidewalkWidth = 72.f; 
		for (int i = 0; i < numSidewalks; i++) {
			//tile by pixel width, and set scroll length to sum
			Scenery newSidewalk = new Scenery(new Vector2(i *sidewalkWidth, 0), new Vector2(-250.f,0),
					sidewalkWidth, numSidewalks * sidewalkWidth, SceneryType.SIDEWALK );		
		
			sidewalks.add(newSidewalk);
		} 
		
		int numHouses = 5;
		float houseWidth = 69;
		float houseSpacing = 200.f;
		for (int i=0; i < numHouses ; i++) {
			Scenery newHouse = new Scenery(new Vector2(i * houseSpacing, 37f), new Vector2(-100f,0),
					houseWidth, (numHouses) * houseSpacing, SceneryType.HOUSE);
			newHouse.setSubType(i);
			
			houses.add(newHouse);
			
		}
		
		int numtrees = 5;
		float treeWidth = 69;
		float treeSpacing = 200.f;
		for (int i=0; i < numtrees ; i++) {
			Scenery newtree = new Scenery(new Vector2(i * treeSpacing, 37f), new Vector2(-120f,0),
					treeWidth, (numtrees) * treeSpacing, SceneryType.TREE);
			newtree.setSubType(i);
			
			trees.add(newtree);
			
		}
		
		int numhillsFront = 4;
		float hillsFrontWidth = 128;
		float hillsFrontSpacing =  hillsFrontWidth;
		for (int i=0; i < numhillsFront ; i++) {
			Scenery newhillsFront = new Scenery(new Vector2(i * hillsFrontSpacing, 35f), new Vector2(-60f,0),
					hillsFrontWidth, (numhillsFront) * hillsFrontSpacing, SceneryType.HILLS);
			newhillsFront.setSubType(i);
			
			hillsFront.add(newhillsFront);
			
		}
		int numhillsBack = 4;
		float hillsBackWidth = 127;//128
		float hillsBackSpacing =  hillsBackWidth;
		for (int i=0; i < numhillsBack ; i++) {
			Scenery newhillsBack = new Scenery(new Vector2(i * hillsBackSpacing, 45f), new Vector2(-30f,0),
					hillsBackWidth, (numhillsBack) * hillsBackSpacing, SceneryType.HILLS);
			newhillsBack.setSubType(i);
			
			hillsBack.add(newhillsBack);
			
		}
		
		reset();
	}
	
	
}
