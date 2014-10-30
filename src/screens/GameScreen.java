package ygg.ygg.ssgame.screens;

import ygg.ygg.ssgame.controller.Controller;


import ygg.ygg.ssgame.model.World;
import ygg.ygg.ssgame.view.WorldRenderer;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen, InputProcessor {

	private World 			world;
	private WorldRenderer 	renderer;
	private Controller	controller;
	
	private int width, height;
	
	@Override
	public void show() {
		
		Gdx.graphics.setVSync((true));
		
		world = new World();
		renderer = new WorldRenderer(world, true);
		controller = new Controller(world,renderer.getCameraDimensions());
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//cap large framerate changes, in case app is suspended improperly
		if (delta > 0.06f) delta = 0.06f;
		//delta = Gdx.graphics.getDeltaTime();
		//delta = Gdx.graphics.getRawDeltaTime();
		
		
		//System.out.println(	 "fps: " + Gdx.graphics.getFramesPerSecond() );
		
		
		
		
		
		controller.update(delta);
		world.updateWorld(delta);
		
		renderer.render();
	}
	
	@Override
	public void resize(int width, int height) {
		renderer.setSize(width, height);
		this.width = width;
		this.height = height;
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
		
		world.dispose();
		renderer.dispose();
	}

	// * InputProcessor methods ***************************//
	
	@Override
	public boolean keyDown(int keycode) {
		/*
		if (keycode == Keys.LEFT)
			controller.leftPressed();
		if (keycode == Keys.RIGHT)
			controller.rightPressed();
		if (keycode == Keys.Z)
			controller.jumpPressed();
		if (keycode == Keys.X)
			controller.firePressed();
			*/
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		/*
		if (keycode == Keys.LEFT)
			controller.leftReleased();
		if (keycode == Keys.RIGHT)
			controller.rightReleased();
		if (keycode == Keys.Z)
			controller.jumpReleased();
		if (keycode == Keys.X)
			controller.fireReleased();
			*/
		return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean mouseMoved(int x, int y)
	{
		
		
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		//if (!Gdx.app.getType().equals(ApplicationType.Android))
		//	return false;
		
		/*
		if (x < width / 2 && y > height / 2) {
			controller.leftPressed();
		}
		if (x > width / 2 && y > height / 2) {
			controller.rightPressed();
		}
		*/
		
		controller.addTouch(new Vector2(x,y));
		
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		//if (!Gdx.app.getType().equals(ApplicationType.Android))
		//	return false;
		
		/*
		if (x < width / 2 && y > height / 2) {
			controller.leftReleased();
		}
		if (x > width / 2 && y > height / 2) {
			controller.rightReleased();
		}
		*/
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
