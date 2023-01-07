package com.aleksandarmusicki.game;

import com.aleksandarmusicki.game.Ekrani.EkranIgre;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public class SuperMario extends Game {
	//Velicina virtuelnog ekrana i Box2D skaliranje("Pixels Per Meter" skraceno PPM)
	public static final int V_SIRINA = 400;
	public static final int V_VISINA = 208;
	public static final float PPM = 100;

	//Box2D Bitovi kolizije
	public static final short NISTA_BIT = 0;
	public static final short ZEMLJA_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short CIGLA_BIT = 4;
	public static final short NOVCIC_BIT = 8;
	public static final short UNISTEN_BIT = 16;
	public static final short OBJEKAT_BIT = 32;
	public static final short NEPRIJATELJ_BIT = 64;
	public static final short GLAVA_NEPRIJATELJA_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short GLAVA_MARIA_BIT = 512;

	public SpriteBatch batch;
	public static AssetManager manager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/music/mario_music.ogg", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
		manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
		manager.load("audio/sounds/powerup.wav", Sound.class);
		manager.load("audio/sounds/powerdown.wav", Sound.class);
		manager.load("audio/sounds/stomp.wav", Sound.class);
		manager.load("audio/sounds/mariodie.wav", Sound.class);

		manager.finishLoading();

		setScreen(new EkranIgre(this));
	}


	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}

	@Override
	public void render () {
		super.render();
	}
}
