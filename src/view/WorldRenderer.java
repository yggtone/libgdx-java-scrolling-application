package ygg.ygg.ssgame.view;

import ygg.ygg.ssgame.model.*;
import ygg.ygg.ssgame.model.Enemy.EnemyStateType;
import ygg.ygg.ssgame.model.Enemy.EnemyVersionType;
import ygg.ygg.ssgame.model.Player.PlayerStateType;
import ygg.ygg.ssgame.model.World.WorldStateType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Animation;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.audio.*;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;

public class WorldRenderer {

	//320x200  640,400
	private static final float CAMERA_WIDTH = 320f;
	private static final float CAMERA_HEIGHT = 200f;
	
	private World world;
	private OrthographicCamera cam;

	/** for debug rendering **/
	ShapeRenderer debugRenderer = new ShapeRenderer();

	/** Textures **/
	//add dispose code for every texture
	private Texture sidewalkTexture;
	private Texture skyTexture;
	private Texture houseTextureA, houseTextureB;
	private Texture treeTextureA, treeTextureB;
	private Texture hillsBackTextureA;
	private Texture hillsFrontTextureA;
	
	private Texture logoTextureA;
	private Texture logoTextureB;
	
	
	
	private Texture whiteTexture;
	private Sprite whiteScreen;
	
	
	//private Sprite sprite;
	
	private Texture catTexture;
	private Animation catSkating;
	private Animation catStop;
	private Animation catFall;
	private Animation catSit;
	
	private Texture birdATexture;
	private Animation birdAIdle;
	private Animation birdADive;
	private Animation birdADeath;
	
	private Texture birdBTexture;
	private Animation birdBIdle;
	private Animation birdBDive;
	private Animation birdBDeath;
	
	private Texture fatBirdTexture;
	private Animation fatBirdIdle;
	private Animation fatBirdHit;
	private Animation fatBirdDeath;

	private Texture birdGodTexture;
	private Animation birdGodIdle;
	TextureRegion birdGodDamage;
	
	
	private SpriteBatch spriteBatch;
	private boolean debug = false;
	private int width;
	private int height;
	private float ppuX;	// pixels per unit on the X axis
	private float ppuY;	// pixels per unit on the Y axis
	
	



	
	
	private BitmapFont font;
	
	
	
	
	public void setSize (int w, int h) {
		this.width = w;
		this.height = h;
		ppuX = (float)width / CAMERA_WIDTH;
		ppuY = (float)height / CAMERA_HEIGHT;
	}
	
	public Vector2 getCameraDimensions() {
		return new Vector2(CAMERA_WIDTH,CAMERA_HEIGHT);
	}
	
	public WorldRenderer(World world, boolean debug) {
		
		
		font = new BitmapFont(Gdx.files.internal("data/terminal.fnt"), false);
		//font.setScale(1.1f);
		//Gdx.graphics.setVSync((false));
		//Gdx.graphics.setDisplayMode((int)CAMERA_WIDTH,(int)CAMERA_HEIGHT, true);
		
		this.world = world;
		//this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam = new OrthographicCamera((int)CAMERA_WIDTH,(int)CAMERA_HEIGHT);
		//this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		//set y to down(-) up(+)?default false
		this.cam.setToOrtho(false, (int)CAMERA_WIDTH,(int)CAMERA_HEIGHT);
		
		this.cam.update();
		
		
		
		this.debug = debug;
		
		
		Matrix4 projection = new Matrix4();
		//projection.setToOrtho(0, 320, 240, 0, -1, 1);
		projection.setToOrtho(0, 320, 240, 0, -1, 1);
		
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(this.cam.combined);
				
		
		
		//ShaderProgram shader = new ShaderProgram(Gdx.files.internal("data/shaders/batch.vert").readString(), Gdx.files.internal(
		//		"data/shaders/batch.frag").readString());
		//spriteBatch.setShader(shader);
		
		loadTextures();
		
		//Sound sound = Gdx.audio.newSound(Gdx.files.internal("data/mysound.mp3"));
		
	}
	
	public void dispose() {
		spriteBatch.dispose();
		
		
		
		birdGodTexture.dispose();
		
		
		sidewalkTexture.dispose();
		skyTexture.dispose();
		houseTextureA.dispose();
		houseTextureB.dispose();
		treeTextureA.dispose();
		treeTextureB.dispose();
		
		hillsBackTextureA.dispose();
		hillsFrontTextureA.dispose();
		
		
		catTexture.dispose();
		fatBirdTexture.dispose();
		birdATexture.dispose();
		birdBTexture.dispose();
		
		logoTextureA.dispose();
		logoTextureB.dispose();
		
		
		
		
		
	}
	
	
	private void loadTextures() {
		
		TextureFilter minFilter = TextureFilter.Nearest;
		TextureFilter maxFilter = TextureFilter.Nearest;
		
		logoTextureA = new Texture(Gdx.files.internal("data/logo.png"));
		logoTextureA.setFilter(minFilter, maxFilter);
		logoTextureB = new Texture(Gdx.files.internal("data/logoB.png"));
		logoTextureB.setFilter(minFilter, maxFilter);
		
	
		sidewalkTexture = new  Texture(Gdx.files.internal("data/sidewalk.png"));
		sidewalkTexture.setFilter(minFilter, maxFilter);	
		skyTexture = new Texture(Gdx.files.internal("data/sky.png"));
		skyTexture.setFilter(minFilter, maxFilter);
		houseTextureA = new Texture(Gdx.files.internal("data/houseA.png"));
		houseTextureA.setFilter(minFilter, maxFilter);
		houseTextureB = new Texture(Gdx.files.internal("data/houseB.png"));
		houseTextureB.setFilter(minFilter, maxFilter);
		
		treeTextureA = new Texture(Gdx.files.internal("data/treeA.png"));
		treeTextureA.setFilter(minFilter, maxFilter);
		treeTextureB = new Texture(Gdx.files.internal("data/treeB.png"));
		treeTextureB.setFilter(minFilter, maxFilter);
		
		hillsBackTextureA = new Texture(Gdx.files.internal("data/hillsBackA.png"));
		hillsBackTextureA.setFilter(minFilter, maxFilter);
		hillsFrontTextureA = new Texture(Gdx.files.internal("data/hillsFrontA.png"));
		hillsFrontTextureA.setFilter(minFilter, maxFilter);
		
		
		birdGodTexture = new Texture(Gdx.files.internal("data/birdGod.png"));
		birdGodTexture.setFilter(minFilter, maxFilter);		
		TextureRegion[][] allBirdGodFrames = TextureRegion.split(birdGodTexture, 70, 170);		
		TextureRegion[] birdGodIdleFrames = new TextureRegion[2];
		birdGodIdleFrames[0] = new TextureRegion(allBirdGodFrames[0][0]);
		birdGodIdleFrames[1] = new TextureRegion(allBirdGodFrames[0][1]);		
		birdGodIdle = new Animation(0.15f, birdGodIdleFrames);
		//damage, single frame
		birdGodDamage = new TextureRegion(allBirdGodFrames[0][2]);
		
		//31x43
		catTexture = new Texture(Gdx.files.internal("data/cat.png")); 
		catTexture.setFilter(minFilter, maxFilter);		
		TextureRegion[][] allFrames = TextureRegion.split(catTexture, 31, 43);		
		TextureRegion[] skateFrames = new TextureRegion[4];
		skateFrames[0] = new TextureRegion(allFrames[0][0]);
		skateFrames[1] = new TextureRegion(allFrames[0][1]);
		skateFrames[2] = new TextureRegion(allFrames[0][2]);
		skateFrames[3] = new TextureRegion(allFrames[0][3]);
		catSkating = new Animation(0.15f, skateFrames);
		
		TextureRegion[] fallFrames = new TextureRegion[3];
		fallFrames[0] = new TextureRegion(allFrames[1][1]);
		fallFrames[1] = new TextureRegion(allFrames[1][2]);
		fallFrames[2] = new TextureRegion(allFrames[1][3]);
		catFall = new Animation(0.15f, fallFrames);
		
		//catSit
		TextureRegion[] sitFrames = new TextureRegion[2];
		sitFrames[0] = new TextureRegion(allFrames[2][0]);
		sitFrames[1] = new TextureRegion(allFrames[2][1]);
		catSit = new Animation(0.15f, sitFrames);
		
		TextureRegion[] stopFrames = new TextureRegion[1];
		stopFrames[0] = new TextureRegion(allFrames[1][0]);
		catStop = new Animation(0.15f, stopFrames);
		
		//50x50
		fatBirdTexture = new Texture(Gdx.files.internal("data/fatBird.png")); 
		fatBirdTexture.setFilter(minFilter, maxFilter);	
		TextureRegion[][] allFatBirdFrames = TextureRegion.split(fatBirdTexture, 50,50);		
		TextureRegion[] fatBirdIdleFrames = new TextureRegion[4];
		fatBirdIdleFrames[0] = new TextureRegion(allFatBirdFrames[0][0]);
		fatBirdIdleFrames[1] = new TextureRegion(allFatBirdFrames[0][1]);
		fatBirdIdleFrames[2] = new TextureRegion(allFatBirdFrames[0][2]);
		fatBirdIdleFrames[3] = new TextureRegion(allFatBirdFrames[0][3]);
		fatBirdIdle = new Animation(0.15f, fatBirdIdleFrames);
		//fat bird hit
		TextureRegion[] fatBirdhitFrames = new TextureRegion[4];
		fatBirdhitFrames[0] = new TextureRegion(allFatBirdFrames[1][0]);
		fatBirdhitFrames[1] = new TextureRegion(allFatBirdFrames[1][1]);
		fatBirdhitFrames[2] = new TextureRegion(allFatBirdFrames[1][2]);
		fatBirdhitFrames[3] = new TextureRegion(allFatBirdFrames[1][3]);
		fatBirdHit = new Animation(0.15f, fatBirdhitFrames);
		//fat bird death
		TextureRegion[] fatbirdFrames = new TextureRegion[8];
		fatbirdFrames[0] = new TextureRegion(allFatBirdFrames[2][0]);
		fatbirdFrames[1] = new TextureRegion(allFatBirdFrames[2][1]);
		fatbirdFrames[2] = new TextureRegion(allFatBirdFrames[2][2]);
		fatbirdFrames[3] = new TextureRegion(allFatBirdFrames[2][3]);
		fatbirdFrames[4] = new TextureRegion(allFatBirdFrames[3][0]);
		fatbirdFrames[5] = new TextureRegion(allFatBirdFrames[3][1]);
		fatbirdFrames[6] = new TextureRegion(allFatBirdFrames[3][2]);
		fatbirdFrames[7] = new TextureRegion(allFatBirdFrames[3][3]);
		fatBirdDeath = new Animation(0.05f, fatbirdFrames);
		
		
		whiteTexture = new Texture(Gdx.files.internal("data/white.png"));
		whiteTexture.setFilter(minFilter, maxFilter);	
		whiteScreen = new Sprite(whiteTexture);
		whiteScreen.setSize(CAMERA_WIDTH, CAMERA_HEIGHT);
		whiteScreen.setOrigin(0,0);
		whiteScreen.setPosition(0, 0);
		
		
		//sprite = new Sprite(sidewalkTexture);
		//sprite.setSize(1.0f, 1.0f * sprite.getHeight() / sprite.getWidth());
		//sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		//sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		
		//
		//bird 48x33 birdATexture
		birdATexture = new Texture(Gdx.files.internal("data/birdA.png"));
		birdATexture.setFilter(minFilter, maxFilter);
		TextureRegion[][] BirdAallFrames = TextureRegion.split(birdATexture, 48, 33);	
		TextureRegion[] BirdAidleFrames = new TextureRegion[4];
		BirdAidleFrames[0] = new TextureRegion(BirdAallFrames[0][0]);
		BirdAidleFrames[1] = new TextureRegion(BirdAallFrames[0][1]);
		BirdAidleFrames[2] = new TextureRegion(BirdAallFrames[0][2]);
		BirdAidleFrames[3] = new TextureRegion(BirdAallFrames[0][3]);
		birdAIdle = new Animation(0.10f, BirdAidleFrames);
		TextureRegion[] BirdAdiveFrames = new TextureRegion[4];
		BirdAdiveFrames[0] = new TextureRegion(BirdAallFrames[1][0]);
		BirdAdiveFrames[1] = new TextureRegion(BirdAallFrames[1][1]);
		BirdAdiveFrames[2] = new TextureRegion(BirdAallFrames[1][2]);
		BirdAdiveFrames[3] = new TextureRegion(BirdAallFrames[1][3]);
		birdADive = new Animation(0.15f, BirdAdiveFrames);
		TextureRegion[] BirdAdeathFrames = new TextureRegion[8];
		BirdAdeathFrames[0] = new TextureRegion(BirdAallFrames[2][0]);
		BirdAdeathFrames[1] = new TextureRegion(BirdAallFrames[2][1]);
		BirdAdeathFrames[2] = new TextureRegion(BirdAallFrames[2][2]);
		BirdAdeathFrames[3] = new TextureRegion(BirdAallFrames[2][3]);
		BirdAdeathFrames[4] = new TextureRegion(BirdAallFrames[3][0]);
		BirdAdeathFrames[5] = new TextureRegion(BirdAallFrames[3][1]);
		BirdAdeathFrames[6] = new TextureRegion(BirdAallFrames[3][2]);
		BirdAdeathFrames[7] = new TextureRegion(BirdAallFrames[3][3]);
		birdADeath = new Animation(0.05f, BirdAdeathFrames);
		//birdADeath.setPlayMode(Animation.NORMAL);
		
		//birdATexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		//spriteBatch.setShader(TextureFilter.Linear);
		
		
		
		birdBTexture = new Texture(Gdx.files.internal("data/birdB.png"));
		birdBTexture.setFilter(minFilter, maxFilter);
		TextureRegion[][] birdBallFrames = TextureRegion.split(birdBTexture, 48, 33);	
		TextureRegion[] birdBidleFrames = new TextureRegion[4];
		birdBidleFrames[0] = new TextureRegion(birdBallFrames[0][0]);
		birdBidleFrames[1] = new TextureRegion(birdBallFrames[0][1]);
		birdBidleFrames[2] = new TextureRegion(birdBallFrames[0][2]);
		birdBidleFrames[3] = new TextureRegion(birdBallFrames[0][3]);
		birdBIdle = new Animation(0.10f, birdBidleFrames);
		TextureRegion[] birdBdiveFrames = new TextureRegion[4];
		birdBdiveFrames[0] = new TextureRegion(birdBallFrames[1][0]);
		birdBdiveFrames[1] = new TextureRegion(birdBallFrames[1][1]);
		birdBdiveFrames[2] = new TextureRegion(birdBallFrames[1][2]);
		birdBdiveFrames[3] = new TextureRegion(birdBallFrames[1][3]);
		birdBDive = new Animation(0.15f, birdBdiveFrames);
		TextureRegion[] birdBdeathFrames = new TextureRegion[8];
		birdBdeathFrames[0] = new TextureRegion(birdBallFrames[2][0]);
		birdBdeathFrames[1] = new TextureRegion(birdBallFrames[2][1]);
		birdBdeathFrames[2] = new TextureRegion(birdBallFrames[2][2]);
		birdBdeathFrames[3] = new TextureRegion(birdBallFrames[2][3]);
		birdBdeathFrames[4] = new TextureRegion(birdBallFrames[3][0]);
		birdBdeathFrames[5] = new TextureRegion(birdBallFrames[3][1]);
		birdBdeathFrames[6] = new TextureRegion(birdBallFrames[3][2]);
		birdBdeathFrames[7] = new TextureRegion(birdBallFrames[3][3]);
		birdBDeath = new Animation(0.05f, birdBdeathFrames);
		
		
		//fireballTexture = new Texture(Gdx.files.internal("data/fireball.png"));
		
	}
	
	
	
	
	
	
	
	
	
	
	
	public void render() {
		
		
		
		
		spriteBatch.begin();
		
			
			
			drawScenery();
		
			drawPlayer();
			
			drawLogo();
			
			drawEnemies();
			
			
			//white screen fade effect
			if(world.getScreenFade() > 0) {
				whiteScreen.setColor(1,1,1,world.getScreenFade());
				whiteScreen.draw(spriteBatch);
			}
			
			drawText();
			
			
			
			
			
		spriteBatch.end();
		
		
		//if (debug)
		//	drawDebug();
	}
	
	private void drawLogo() {
		if(world.getLogoHeight() < 201)
		{
			spriteBatch.draw(logoTextureA, 40, world.getLogoHeight());
			/*
			if(world.getLogoHeight() < 0) {
				spriteBatch.draw(logoTextureA, 0, world.getLogoHeight());
				
			}
			else {
				spriteBatch.draw(logoTextureB, 0, world.getLogoHeight());
			}
			*/
		}
	}
	
	private void drawText() {
		//String text = " ";// = "Sphinx of black quartz, judge my vow.";
		
		
		if(world.getState() == WorldStateType.TITLE) {
			//text = " ";		
		}
		else if(world.getState() == WorldStateType.PLAYING) {
			//text = " ";		
		}
		else if(world.getState() == WorldStateType.GAMEOVER	) {
			//text = world.getScore();	
			
			String labels = "GAME OVER \nTime Bonus: " +  "\nCombos: " +  "\nBoss Bonus: " +  "\nTotal Score: " + "\nHigh Score: " ;
			font.setColor(Color.BLACK);
			
			
			float alignmentWidth = 180;
			// font.drawMultiLine(spriteBatch, text, x, viewHeight - y, alignmentWidth, HAlignment.RIGHT);
			font.drawWrapped(spriteBatch, labels, 20, 190, alignmentWidth, HAlignment.LEFT);
			
			
			String scoreNumbersText = world.getScore();
			//alignmentWidth = 200;
			font.drawWrapped(spriteBatch, scoreNumbersText, 20, 190, alignmentWidth, HAlignment.RIGHT);
			
		}
		
		
		
		
	}
	
	private void drawPlayer() {
		
		
		if(world.getPlayer().getState() == PlayerStateType.SKATING) {
			TextureRegion playerFrame = catSkating.getKeyFrame(world.getPlayer().getStateTime(), true);
			spriteBatch.draw(playerFrame, world.getPlayer().getPosition().x, world.getPlayer().getPosition().y);	
		} else if(world.getPlayer().getState() == PlayerStateType.FALLING) {
			TextureRegion playerFrame = catFall.getKeyFrame(world.getPlayer().getStateTime(), true);
			spriteBatch.draw(playerFrame, world.getPlayer().getPosition().x, world.getPlayer().getPosition().y);
			
		} else if(world.getPlayer().getState() == PlayerStateType.SITTING) {
			TextureRegion playerFrame = catSit.getKeyFrame(world.getPlayer().getStateTime(), true);
			spriteBatch.draw(playerFrame, world.getPlayer().getPosition().x, world.getPlayer().getPosition().y);
			
		} else  {
			TextureRegion playerFrame = catStop.getKeyFrame(world.getPlayer().getStateTime(), true);
			spriteBatch.draw(playerFrame, world.getPlayer().getPosition().x, world.getPlayer().getPosition().y);
			
		}
		
		
		
		
	}

	private void drawEnemies() {
		
		
		
		
		if(world.isBossSpawned()) {
			Enemy boss = world.getBoss();
			if(boss.getState() == EnemyStateType.IDLE) {
				TextureRegion frame = birdGodIdle.getKeyFrame(boss.getStateTime(), true);
				spriteBatch.draw(frame, boss.getPosition().x,  boss.getPosition().y);
			}
			else {				
				spriteBatch.draw(birdGodDamage, boss.getPosition().x,  boss.getPosition().y);
			}
		}
		
		
		
		
		
		for(Enemy enemy : world.getEnemies()) {
			if(enemy.getState() != EnemyStateType.DEAD)
			{
							
				if(enemy.getType() == EnemyVersionType.REDBIRD) {
					if(enemy.getState() == EnemyStateType.IDLE) {
						TextureRegion birdAFrame = birdAIdle.getKeyFrame(enemy.getStateTime(), true);
						spriteBatch.draw(birdAFrame, enemy.getPosition().x,  enemy.getPosition().y);
					}
					else if(enemy.getState() == EnemyStateType.ATTACKING) {
						TextureRegion birdAFrame = birdADive.getKeyFrame(enemy.getStateTime(), true);
						spriteBatch.draw(birdAFrame, enemy.getPosition().x,  enemy.getPosition().y);
					}
					else {
						TextureRegion birdAFrame = birdADeath.getKeyFrame(enemy.getStateTime(), false);
						spriteBatch.draw(birdAFrame, enemy.getPosition().x,  enemy.getPosition().y);
					}
					
				} else if(enemy.getType() == EnemyVersionType.FATBIRD) {
					if(enemy.getState() == EnemyStateType.IDLE) {
						TextureRegion birdAFrame = fatBirdIdle.getKeyFrame(enemy.getStateTime(), true);
						spriteBatch.draw(birdAFrame, enemy.getPosition().x,  enemy.getPosition().y);
					}
					else if(enemy.getState() == EnemyStateType.PREDAMAGED) {
						TextureRegion birdAFrame = fatBirdHit.getKeyFrame(enemy.getStateTime(), true);
						spriteBatch.draw(birdAFrame, enemy.getPosition().x,  enemy.getPosition().y);
					}
					else {
						TextureRegion birdAFrame = fatBirdDeath.getKeyFrame(enemy.getStateTime(), false);
						spriteBatch.draw(birdAFrame, enemy.getPosition().x,  enemy.getPosition().y);
					}
					
				}
				else if(enemy.getType() == EnemyVersionType.BLUEBIRD) {
					if(enemy.getState() == EnemyStateType.IDLE) {
						TextureRegion birdBFrame = birdBIdle.getKeyFrame(enemy.getStateTime(), true);
						spriteBatch.draw(birdBFrame, enemy.getPosition().x,  enemy.getPosition().y);
					}
					else if(enemy.getState() == EnemyStateType.ATTACKING) {
						TextureRegion birdBFrame = birdBDive.getKeyFrame(enemy.getStateTime(), true);
						spriteBatch.draw(birdBFrame, enemy.getPosition().x,  enemy.getPosition().y);
					}
					else {
						TextureRegion birdBFrame = birdBDeath.getKeyFrame(enemy.getStateTime(), false);
						spriteBatch.draw(birdBFrame, enemy.getPosition().x,  enemy.getPosition().y);
					}
					
				}
				
				
			}
		}
		
		
		
		
		
	}
	
	private void drawScenery() {
		spriteBatch.draw(skyTexture, 0, 0);
		
		for(Scenery backHill : world.getHillsBack()) {
			spriteBatch.draw(hillsBackTextureA, backHill.getPosition().x,  backHill.getPosition().y);			
		}
		for(Scenery frontHill : world.getHillsFront()) {
			spriteBatch.draw(hillsFrontTextureA, frontHill.getPosition().x, frontHill.getPosition().y);			
		}
		
		
		for(Scenery house : world.getHouses()) {
			if(house.getSubType()%2 == 1)
				spriteBatch.draw(houseTextureA, house.getPosition().x, house.getPosition().y);
			else
				spriteBatch.draw(houseTextureB, house.getPosition().x, house.getPosition().y);
		}
		
		for(Scenery tree : world.getTrees()) {
			if(tree.getSubType()%2 == 1)
				spriteBatch.draw(treeTextureA, tree.getPosition().x, tree.getPosition().y);
			else
				spriteBatch.draw(treeTextureB, tree.getPosition().x, tree.getPosition().y);
		}
		
		
		//spriteBatch.draw(texture, x, y, originX, originY, CAMERA_WIDTH, CAMERA_HEIGHT,
		//scaleX, scaleY, rotation, srcX, srcY, srcWidth, srcHeight, flipX, flipY)
		
		//draw (Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX,
		//float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY)
		
		for(Scenery sidewalk : world.getSidewalks()) {
			spriteBatch.draw(sidewalkTexture, sidewalk.getPosition().x, 0);
			
			//sprite.setScale(2,3);
			//sprite.setPosition(sidewalk.getPosition().x, 0);
			//sprite.draw(spriteBatch);
			
		}
		
		
		
	}

	

	
}
