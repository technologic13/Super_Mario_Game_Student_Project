package com.aleksandarmusicki.game.Ekrani;

import com.aleksandarmusicki.game.SuperMario;
import com.aleksandarmusicki.game.Scene.Hud;
import com.aleksandarmusicki.game.Sprajtovi.Neprijatelji.Neprijatelj;
import com.aleksandarmusicki.game.Sprajtovi.Itemi.Item;
import com.aleksandarmusicki.game.Sprajtovi.Itemi.ItemDef;
import com.aleksandarmusicki.game.Sprajtovi.Itemi.Pecurka;
import com.aleksandarmusicki.game.Sprajtovi.Mario;
import com.aleksandarmusicki.game.Alatke.B2KreatorSveta;
import com.aleksandarmusicki.game.Alatke.ContactListenerZaSvet;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public class EkranIgre implements Screen{
    //Referenca igre, koriscena za postavljanje Screen-ova
    private SuperMario igra;
    private TextureAtlas atlas;

    //osnovne varijable za EkranIgre
    private OrthographicCamera kameraIgre;
    private Viewport vidnoPoljeIgre;
    private Hud hud;

    //Tiled mapa varijable
    private TmxMapLoader maploader;
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d varijable
    private World svet;

    private B2KreatorSveta kreator;

    //Sprajtovi
    private Mario mario;

    private Music muzika;

    private Array<Item> itemi;
    private LinkedBlockingQueue<ItemDef> itemiZaStvaranje;


    public EkranIgre(SuperMario igra){
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.igra = igra;
        //Kreiranje kamere koja prati Maria kroz svet igre
        kameraIgre = new OrthographicCamera();

        //kreiranje FitViewport da bi se uvek drzala ista razmera ekrana nezavisno od uredjaja
        vidnoPoljeIgre = new FitViewport(SuperMario.V_SIRINA / SuperMario.PPM, SuperMario.V_VISINA / SuperMario.PPM, kameraIgre);

        //Kreiranje HUD-a za skor/tajmer/level
        hud = new Hud(igra.batch);

        //Ucitavanje mape i postavljanje renderera mape
        maploader = new TmxMapLoader();
        mapa = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(mapa, 1  / SuperMario.PPM);

        //Inicijalizovanje varijable kameraIgre i setovanje da bude centrirana na pocetku mape
        kameraIgre.position.set(vidnoPoljeIgre.getWorldWidth() / 2, vidnoPoljeIgre.getWorldHeight() / 2, 0);

        //Kreiranje Box2D sveta, nema gravitacije na x osi, -10 gravitacija na y osi, i dozvola da bodiji mogu otici u sleep mod
        svet = new World(new Vector2(0, -10), true);

        kreator = new B2KreatorSveta(this);

        //kreiranje maria u svetu igre
        mario = new Mario(this);

        svet.setContactListener(new ContactListenerZaSvet());
        //Postavljanje muzike koja se pusta u pozadini igre
        muzika = SuperMario.manager.get("audio/music/mario_music.ogg", Music.class);
        muzika.setLooping(true);
        muzika.setVolume(0.3f);
        muzika.play();
        

        itemi = new Array<Item>();
        itemiZaStvaranje = new LinkedBlockingQueue<ItemDef>();
    }

    public void stvoriItem(ItemDef idef){
        itemiZaStvaranje.add(idef);
    }


    public void hendlujStvaranjeItema(){
        if(!itemiZaStvaranje.isEmpty()){
            ItemDef idef = itemiZaStvaranje.poll();
            if(idef.tip == Pecurka.class){
                itemi.add(new Pecurka(this, idef.pozicija.x, idef.pozicija.y));
            }
        }
    }


    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {


    }

    public void hendlovanjeUnosa(float dt){
        //kontrolisanje Maria koriscenjem trenutnih impulsa
        if(mario.trenutnoStanje != Mario.State.MRTAV) {
            if (hud.IdiGore())
                mario.skok();
            if (hud.IdiDesno() && mario.b2body.getLinearVelocity().x <= 2)
                mario.b2body.applyLinearImpulse(new Vector2(0.1f, 0), mario.b2body.getWorldCenter(), true);
            if (hud.IdiLevo() && mario.b2body.getLinearVelocity().x >= -2)
                mario.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), mario.b2body.getWorldCenter(), true);

        }

    }

    public void update(float dt){
        //Obrada unosa igraca
        hendlovanjeUnosa(dt);
        hendlujStvaranjeItema();

        //Koristi jedan korak u fizickoj simulaciji (60 puta u sekundi)
        svet.step(1 / 60f, 6, 2);

        mario.update(dt);
        for(Neprijatelj neprijatelj : kreator.getNeprijatelje()) {
            neprijatelj.update(dt);
            if(neprijatelj.getX() < mario.getX() + 224 / SuperMario.PPM) {
                neprijatelj.b2body.setActive(true);
            }
        }

        for(Item item : itemi)
            item.update(dt);

        hud.update(dt);

        //postavljanje kameraIgre na koordinatu x ose Maria
        if(mario.trenutnoStanje != Mario.State.MRTAV) {
            kameraIgre.position.x = mario.b2body.getPosition().x;
        }

        //Azuriraj kameraIgre sa tacnim koordinatama nakon promena
        kameraIgre.update();
        //Govori renderer-u da renderuje samo ono sto kamera moze da vidi
        renderer.setView(kameraIgre);

    }


    @Override
    public void render(float delta) {
        //Odvoji update logiku od render-era
        update(delta);

        //Ocisti ekran igre sa crnom bojom
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Renderovanje mape
        renderer.render();



        igra.batch.setProjectionMatrix(kameraIgre.combined);
        igra.batch.begin();
        mario.draw(igra.batch);
        for (Neprijatelj neprijatelj : kreator.getNeprijatelje())
            neprijatelj.draw(igra.batch);
        for (Item item : itemi)
            item.draw(igra.batch);
        igra.batch.end();

        //Setuj batch da crta sta Hud kamera vidi.
        igra.batch.setProjectionMatrix(hud.scena.getCamera().combined);
        hud.scena.draw();

        if(igraJeZavrsena()){
            igra.setScreen(new IgraZavrsenaEkran(igra));
            dispose();
        }

    }

    public boolean igraJeZavrsena(){
        if(mario.trenutnoStanje == Mario.State.MRTAV && mario.getTajmerStanja() > 3){
            return true;
        }
        return false;
    }

    @Override
    public void resize(int sirina, int visina) {
        //Azurirano vidnoPoljeIgre
        vidnoPoljeIgre.update(sirina,visina);

    }

    public TiledMap getMapa(){
        return mapa;
    }
    public World getSvet(){
        return svet;
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
        //Izbaci sve resurse koji su korisceni
        mapa.dispose();
        renderer.dispose();
        svet.dispose();
        hud.dispose();
    }

    public Hud getHud(){ return hud; }
}

