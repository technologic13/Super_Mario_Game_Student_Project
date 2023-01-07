package com.aleksandarmusicki.game.Ekrani;

import com.aleksandarmusicki.game.SuperMario;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */
public class IgraZavrsenaEkran implements Screen {
    private Viewport vidnoPolje;
    private Stage scena;

    private Game igra;

    public IgraZavrsenaEkran(Game igra){
        this.igra = igra;
        vidnoPolje = new FitViewport(SuperMario.V_SIRINA, SuperMario.V_VISINA, new OrthographicCamera());
        scena = new Stage(vidnoPolje, ((SuperMario) igra).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table tabela = new Table();
        tabela.center();
        tabela.setFillParent(true);

        Label igraJeZavrsenaLabel = new Label("IGRA   JE   ZAVRSENA", font);
        Label pokusajPonovoLabel = new Label("Pokusaj ponovo...", font);

        tabela.add(igraJeZavrsenaLabel).expandX();
        tabela.row();
        tabela.add(pokusajPonovoLabel).expandX().padTop(10f);

        scena.addActor(tabela);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            igra.setScreen(new EkranIgre((SuperMario) igra));
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        scena.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        scena.dispose();
    }
}
