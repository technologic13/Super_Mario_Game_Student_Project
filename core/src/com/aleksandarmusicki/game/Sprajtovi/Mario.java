package com.aleksandarmusicki.game.Sprajtovi;

import com.aleksandarmusicki.game.SuperMario;
import com.aleksandarmusicki.game.Ekrani.EkranIgre;
import com.aleksandarmusicki.game.Sprajtovi.Neprijatelji.Neprijatelj;
import com.aleksandarmusicki.game.Sprajtovi.Neprijatelji.Kornjaca;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public class Mario extends Sprite {
    public enum State {PADANJE, SKAKANJE, STAJANJE, TRCANJE, RASTE, MRTAV};
    public State trenutnoStanje;
    public State prethodnoStanje;

    public World svet;
    public Body b2body;

    private TextureRegion marioStoji;
    private Animation<TextureRegion> marioTrci;
    private TextureRegion marioSkace;
    private TextureRegion marioMrtav;
    private TextureRegion velikiMarioStoji;
    private TextureRegion velikiMarioSkace;
    private Animation<TextureRegion> velikiMarioTrci;
    private Animation<TextureRegion> marioRaste;

    private float tajmerStanja;
    private boolean trcanjeUdesno;
    private boolean marioJeVelik;
    private boolean pokreniAnimacijuRasta;
    private boolean vremeZaDefinisanjeMaria;
    private boolean vremeZaRedefinisanjeMaria;
    private boolean marioJeMrtav;
    private EkranIgre ekran;



    public Mario(EkranIgre ekran){
        //inicializovanje osnovnih vrednosti
        this.ekran = ekran;
        this.svet = ekran.getSvet();
        trenutnoStanje = State.STAJANJE;
        prethodnoStanje = State.STAJANJE;
        tajmerStanja = 0;
        trcanjeUdesno = true;


        Array<TextureRegion> frejmovi = new Array<TextureRegion>();

        //getuj frejmove animacije trcanja i stavi u animaciju marioTrci
        for(int i = 1; i < 4; i++)
            frejmovi.add(new TextureRegion(ekran.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
        marioTrci = new Animation(0.1f, frejmovi);

        frejmovi.clear();

        for(int i = 1; i < 4; i++)
            frejmovi.add(new TextureRegion(ekran.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
        velikiMarioTrci = new Animation(0.1f, frejmovi);

        frejmovi.clear();

        //getovanje i setovanje frejmova animacije rasta Maria
        frejmovi.add(new TextureRegion(ekran.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frejmovi.add(new TextureRegion(ekran.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frejmovi.add(new TextureRegion(ekran.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frejmovi.add(new TextureRegion(ekran.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        marioRaste = new Animation(0.2f, frejmovi);


        //getovanje frejmova od animacije za skok i ubacivanje u animaciju marioSkace i velikiMarioSkace
        marioSkace = new TextureRegion(ekran.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        velikiMarioSkace = new TextureRegion(ekran.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);

        //kreiranje TextureRegion-a za Maria koji stoji
        marioStoji = new TextureRegion(ekran.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
        velikiMarioStoji = new TextureRegion(ekran.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);

        //kreiranje TextureRegion-a za mrtvog Maria
        marioMrtav = new TextureRegion(ekran.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

        //definisi Maria u Box2d
        definisiMaria();

        //setovanje inicijalnih vrednosti za njegovu lokaciju, sirinu i visinu. Inicijalni frejm je marioStoji.
        setBounds(0, 0, 16 / SuperMario.PPM, 16 / SuperMario.PPM);
        setRegion(marioStoji);



    }

    public void update(float dt){

        if (ekran.getHud().VremeJeIsteklo() && !jeMrtav()) {
            umire();
        }

        //azuriranje sprite-a tako da odgovara polozaju Box2D body-a
        if(marioJeVelik)
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 6 / SuperMario.PPM);
        else
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //ažuriranje sprite-a ispravnim frejmom u zavisnosti od trenutne akcije Maria
        setRegion(getFrejm(dt));
        if(vremeZaDefinisanjeMaria)
            definisiVelikogMaria();
        if(vremeZaRedefinisanjeMaria)
            redefinisiMaria();



    }

    public TextureRegion getFrejm(float dt){
        //getovanje mariovog trenutnog stanja. tj. skakanje, trčanje, stajanje...
        trenutnoStanje = getStanje();

        TextureRegion region;

        //u zavisnosti od stanja, dobij odgovarajuci keyFrame animacije.
        switch(trenutnoStanje){
            case MRTAV:
                region = marioMrtav;
                break;
            case RASTE:
                region = marioRaste.getKeyFrame(tajmerStanja);
                if(marioRaste.isAnimationFinished(tajmerStanja)) {
                    pokreniAnimacijuRasta = false;
                }
                break;
            case SKAKANJE:
                region = marioJeVelik ? velikiMarioSkace : marioSkace;
                break;
            case TRCANJE:
                region = marioJeVelik ? velikiMarioTrci.getKeyFrame(tajmerStanja, true) : marioTrci.getKeyFrame(tajmerStanja, true);
                break;
            case PADANJE:
            case STAJANJE:
            default:
                region = marioJeVelik ? velikiMarioStoji : marioStoji;
                break;
        }

        //Ako Mario trci ulevo i textura ne pokazuje u levu stranu, obrni je.
        if((b2body.getLinearVelocity().x < 0 || !trcanjeUdesno) && !region.isFlipX()){
            region.flip(true, false);
            trcanjeUdesno = false;
        }

        //Ako Mario trci udesno i textura ne pokazuje u desnu stranu, obrni je.
        else if((b2body.getLinearVelocity().x > 0 || trcanjeUdesno) && region.isFlipX()){
            region.flip(true, false);
            trcanjeUdesno = true;
        }

        //ako je trenutno stanje isto kao prethodno stanje, poveca se tajmer stanja.
        //u suprotnom se stanje promenilo i moramo resetovati tajmer.
        tajmerStanja = trenutnoStanje == prethodnoStanje ? tajmerStanja + dt : 0;
        //azuriraj prethodno stanje
        prethodnoStanje = trenutnoStanje;
        //vratiti poslednji prilagodjeni frejm
        return region;

    }

    public State getStanje(){
        //Ako Mario ide pozitivno u y osi onda on skace... ako je skocio vec i pocinje da pada treba da ostane u skok animaciji
        if(marioJeMrtav)
            return State.MRTAV;
        else if(pokreniAnimacijuRasta)
            return State.RASTE;
        else if((b2body.getLinearVelocity().y > 0 && trenutnoStanje == State.SKAKANJE) || (b2body.getLinearVelocity().y < 0 && prethodnoStanje == State.SKAKANJE))
            return State.SKAKANJE;
            //Ako je Mario negativno u y osi onda Mario pada
        else if(b2body.getLinearVelocity().y < 0)
            return State.PADANJE;
            //Ako je Mario pozitivan ili negativan u x osi, on trci
        else if(b2body.getLinearVelocity().x != 0)
            return State.TRCANJE;
            //ako nista od ovoga se ne vraca onda Mario stoji
        else
            return State.STAJANJE;
    }

    public void raste(){
        if( !jeVelik() ) {
            pokreniAnimacijuRasta = true;
            marioJeVelik = true;
            vremeZaDefinisanjeMaria = true;
            setBounds(getX(), getY(), getWidth(), getHeight() * 2);
            SuperMario.manager.get("audio/sounds/powerup.wav", Sound.class).play();
        }
    }

    public void umire() {

        if (!jeMrtav()) {

            SuperMario.manager.get("audio/music/mario_music.ogg", Music.class).stop();
            SuperMario.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            marioJeMrtav = true;
            Filter filter = new Filter();
            filter.maskBits = SuperMario.NISTA_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }
    }

    public boolean jeMrtav(){
        return marioJeMrtav;
    }

    public float getTajmerStanja(){
        return tajmerStanja;
    }

    public boolean jeVelik(){
        return marioJeVelik;
    }

    public void skok(){
        if ( trenutnoStanje != State.SKAKANJE) {
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            trenutnoStanje = State.SKAKANJE;
        }
    }

    public void udarac(Neprijatelj neprijatelj){
        if(neprijatelj instanceof Kornjaca && ((Kornjaca) neprijatelj).trenutnoStanje == Kornjaca.State.OKLOP_STOJI)
            ((Kornjaca) neprijatelj).sut(neprijatelj.b2body.getPosition().x > b2body.getPosition().x ? Kornjaca.SUT_DESNO : Kornjaca.SUT_LEVO);
        else {
            if (marioJeVelik) {
                marioJeVelik = false;
                vremeZaRedefinisanjeMaria = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
                SuperMario.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
            } else {
                umire();
            }
        }
    }

    public void redefinisiMaria(){
        Vector2 position = b2body.getPosition();
        svet.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = svet.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape oblik = new CircleShape();
        oblik.setRadius(6 / SuperMario.PPM);
        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.ZEMLJA_BIT |
                SuperMario.NOVCIC_BIT |
                SuperMario.CIGLA_BIT |
                SuperMario.NEPRIJATELJ_BIT |
                SuperMario.OBJEKAT_BIT |
                SuperMario.GLAVA_NEPRIJATELJA_BIT |
                SuperMario.ITEM_BIT;

        fdef.shape = oblik;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape glava = new EdgeShape();
        glava.set(new Vector2(-2 / SuperMario.PPM, 6 / SuperMario.PPM), new Vector2(2 / SuperMario.PPM, 6 / SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.GLAVA_MARIA_BIT;
        fdef.shape = glava;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);

        vremeZaRedefinisanjeMaria = false;

    }

    public void definisiVelikogMaria(){
        Vector2 currentPosition = b2body.getPosition();
        svet.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / SuperMario.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = svet.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape oblik = new CircleShape();
        oblik.setRadius(6 / SuperMario.PPM);
        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.ZEMLJA_BIT |
                SuperMario.NOVCIC_BIT |
                SuperMario.CIGLA_BIT |
                SuperMario.NEPRIJATELJ_BIT |
                SuperMario.OBJEKAT_BIT |
                SuperMario.GLAVA_NEPRIJATELJA_BIT |
                SuperMario.ITEM_BIT;

        fdef.shape = oblik;
        b2body.createFixture(fdef).setUserData(this);
        oblik.setPosition(new Vector2(0, -14 / SuperMario.PPM));
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape glava = new EdgeShape();
        glava.set(new Vector2(-2 / SuperMario.PPM, 6 / SuperMario.PPM), new Vector2(2 / SuperMario.PPM, 6 / SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.GLAVA_MARIA_BIT;
        fdef.shape = glava;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        vremeZaDefinisanjeMaria = false;
    }

    public void definisiMaria(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(600 / SuperMario.PPM, 600 / SuperMario.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = svet.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape oblik = new CircleShape();
        oblik.setRadius(6 / SuperMario.PPM);
        fdef.filter.categoryBits = SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.ZEMLJA_BIT |
                SuperMario.NOVCIC_BIT |
                SuperMario.CIGLA_BIT |
                SuperMario.NEPRIJATELJ_BIT |
                SuperMario.OBJEKAT_BIT |
                SuperMario.GLAVA_NEPRIJATELJA_BIT |
                SuperMario.ITEM_BIT;

        fdef.shape = oblik;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape glava = new EdgeShape();
        glava.set(new Vector2(-2 / SuperMario.PPM, 6 / SuperMario.PPM), new Vector2(2 / SuperMario.PPM, 6 / SuperMario.PPM));
        fdef.filter.categoryBits = SuperMario.GLAVA_MARIA_BIT;
        fdef.shape = glava;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }



    public void draw(Batch batch){
        super.draw(batch);

    }
}
