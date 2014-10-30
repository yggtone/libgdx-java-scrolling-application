package ygg.ygg.ssgame;

import  ygg.ygg.ssgame.screens.GameScreen;

import com.badlogic.gdx.Game;

public class ssGame extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}

}
