package com.aleksandarmusicki.game.Scene;

import com.aleksandarmusicki.game.SuperMario;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public class Hud implements Disposable{

    //Scene2D.ui Stage i njegov Viewport za HUD
    public Stage scena;
    private Viewport vidnoPolje;
    boolean idiGore, idiDole, idiLevo, idiDesno;
    //Varijable za pracenje Mariovog skora i vremena
    private Integer tajmer;
    private boolean vremeJeIsteklo; // tacno samo u slucaju ako tajmer bude na 0
    private float brojac;
    private static Integer skor;

    //Scene2D vidzeti
    private Label odbrojavanjeLabel;
    private static Label skorLabel;
    private Label vremeLabel;
    private Label levelLabel;
    private Label svetLabel;
    private Label marioLabel;

    public Hud(SpriteBatch sb){
        //definisanje varijabli za pracenje vremena i skora
        tajmer = 300;
        brojac = 0;
        skor = 0;


        //Postavljanje HUD viewport-a koristeci novu kameru koja je zasebna od kameraIgre
        //definisanje scene koriscenjem vidnogPolja i spritebatch-a
        vidnoPolje = new FitViewport(SuperMario.V_SIRINA, SuperMario.V_VISINA, new OrthographicCamera());
        scena = new Stage(vidnoPolje, sb);
        Gdx.input.setInputProcessor(scena);

        //Definisanje tabela za hud, jedna tabela je za skor i vreme a druga je za direkcionalne komande kontrolera
        Table tabela = new Table();
        Table kontroler = new Table();
        //Pozicioniranje kontrolera u donji levi ugao
        kontroler.left().bottom();
        //Dodavanje slike direkcionalnog dugmeta kao teksture dugmeta
        Image strelicaGore = new Image(new Texture("flatDark25.png"));
        strelicaGore.setSize(2500/ SuperMario.PPM,2500/ SuperMario.PPM);
        //Dodavanje Listenera dugmetu koji osluskuje vrstu inputa koji igrac upotrebljava, sta se desava ako igrac pritisne dugme a sta kad igrac pusti dugme
        strelicaGore.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                idiGore = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                idiGore = false;
            }
        });
        Image strelicaDole = new Image(new Texture("flatDark26.png"));
        strelicaDole.setSize(2500/ SuperMario.PPM, 2500/ SuperMario.PPM);
        strelicaDole.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                idiDole = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                idiDole = false;
            }
        });
        Image strelicaDesno = new Image(new Texture("flatDark24.png"));
        strelicaDesno.setSize(2500/ SuperMario.PPM, 2500/ SuperMario.PPM);
        strelicaDesno.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                idiDesno = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                idiDesno = false;
            }
        });

        Image strelicaLevo = new Image(new Texture("flatDark23.png"));
        strelicaLevo.setSize(2500/ SuperMario.PPM, 2500/ SuperMario.PPM);
        strelicaLevo.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                idiLevo = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                idiLevo = false;
            }
        });
        //Dodavanje slika direkcionalnih dugmica tabeli kontroler u obliku blokova 3x3
        kontroler.add();
        kontroler.add(strelicaGore).size(strelicaGore.getWidth(), strelicaGore.getHeight());
        kontroler.add();
        kontroler.row().pad(5, 5, 5, 5);
        kontroler.add(strelicaLevo).size(strelicaLevo.getWidth(), strelicaLevo.getHeight());
        kontroler.add();
        kontroler.add(strelicaDesno).size(strelicaDesno.getWidth(), strelicaDesno.getHeight());
        kontroler.row().padBottom(5);
        kontroler.add();
        kontroler.add(strelicaDole).size(strelicaDole.getWidth(), strelicaDole.getHeight());
        kontroler.add();


        //Gornje poravnjanje tabele
        tabela.top();
        //tabela popunjava prostor svog roditelja, u ovom slucaju sirinu ekrana
        tabela.setFillParent(true);

        //definisanje Label-a kao String, i biranje stila fonta i boje Label-a
        odbrojavanjeLabel = new Label(String.format("%03d", tajmer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        skorLabel =new Label(String.format("%06d", skor), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        vremeLabel = new Label("VREME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        svetLabel = new Label("SVET", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //Dodavanje Label-a tabeli, odvajanje od Gornje ivice za 10, i svi su iste sirine sa expandX
        tabela.add(marioLabel).expandX().padTop(10);
        tabela.add(svetLabel).expandX().padTop(10);
        tabela.add(vremeLabel).expandX().padTop(10);
        //Dodavanje drugog reda tabeli
        tabela.row();
        tabela.add(skorLabel).expandX();
        tabela.add(levelLabel).expandX();
        tabela.add(odbrojavanjeLabel).expandX();

        //Dodavanje tabela na scenu
        scena.addActor(tabela);
        scena.addActor(kontroler);

    }

    public void update(float dt){
        brojac += dt;
        if(brojac >= 1){
            if (tajmer > 0) {
                tajmer--;
            } else {
                vremeJeIsteklo = true;
            }
            odbrojavanjeLabel.setText(String.format("%03d", tajmer));
            brojac = 0;
        }
    }

    public static void dodajSkor(int value){
        skor += value;
        skorLabel.setText(String.format("%06d", skor));
    }

    @Override
    public void dispose() { scena.dispose(); }

    public boolean VremeJeIsteklo() { return vremeJeIsteklo; }

    public boolean IdiGore() {
        return idiGore;
    }

    public boolean IdiLevo() {
        return idiLevo;
    }

    public boolean IdiDesno() {
        return idiDesno;
    }
}
