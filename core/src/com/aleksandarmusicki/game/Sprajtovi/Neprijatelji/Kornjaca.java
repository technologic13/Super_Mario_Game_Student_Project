package com.aleksandarmusicki.game.Sprajtovi.Neprijatelji;

import com.aleksandarmusicki.game.Sprajtovi.Mario;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.aleksandarmusicki.game.SuperMario;
import com.aleksandarmusicki.game.Ekrani.EkranIgre;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */
public class Kornjaca extends Neprijatelj {
    public static final int SUT_LEVO = -2;
    public static final int SUT_DESNO = 2;
    public enum State {SETANJE, KRETANJE_OKLOPA, OKLOP_STOJI}
    public State trenutnoStanje;
    public State predhodnoStanje;
    private float stateTime;
    private Animation<TextureRegion> animacijaKretanja;
    private Array<TextureRegion> frejmovi;
    private TextureRegion oklop;


    public Kornjaca(EkranIgre ekran, float x, float y) {
        super(ekran, x, y);
        frejmovi = new Array<TextureRegion>();
        frejmovi.add(new TextureRegion(ekran.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
        frejmovi.add(new TextureRegion(ekran.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
        oklop = new TextureRegion(ekran.getAtlas().findRegion("turtle"), 64, 0, 16, 24);
        animacijaKretanja = new Animation(0.2f, frejmovi);
        trenutnoStanje = predhodnoStanje = State.SETANJE;

        setBounds(getX(), getY(), 16 / SuperMario.PPM, 24 / SuperMario.PPM);

    }

    @Override
    protected void definisiNeprijatelja() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = svet.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape oblik = new CircleShape();
        oblik.setRadius(6 / SuperMario.PPM);
        fdef.filter.categoryBits = SuperMario.NEPRIJATELJ_BIT;
        fdef.filter.maskBits = SuperMario.ZEMLJA_BIT |
                SuperMario.NOVCIC_BIT |
                SuperMario.CIGLA_BIT |
                SuperMario.NEPRIJATELJ_BIT |
                SuperMario.OBJEKAT_BIT |
                SuperMario.MARIO_BIT;

        fdef.shape = oblik;
        b2body.createFixture(fdef).setUserData(this);

        //Kreiranje kornjacine glave
        PolygonShape glava = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / SuperMario.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / SuperMario.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / SuperMario.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / SuperMario.PPM);
        glava.set(vertice);

        fdef.shape = glava;
        fdef.restitution = 1.8f;
        fdef.filter.categoryBits = SuperMario.GLAVA_NEPRIJATELJA_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public TextureRegion getFrame(float dt){
        TextureRegion region;

        switch (trenutnoStanje){
            case KRETANJE_OKLOPA:
            case OKLOP_STOJI:
                region = oklop;
                break;
            case SETANJE:
            default:
                region = animacijaKretanja.getKeyFrame(stateTime, true);
                break;
        }

        if(brzina.x > 0 && region.isFlipX() == false){
            region.flip(true, false);
        }
        if(brzina.x < 0 && region.isFlipX() == true){
            region.flip(true, false);
        }
        stateTime = trenutnoStanje == predhodnoStanje ? stateTime + dt : 0;
        //azuriraj prethodno stanje
        predhodnoStanje = trenutnoStanje;
        //Vraca poslednji prilagodjen frejm
        return region;
    }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        if(trenutnoStanje == State.OKLOP_STOJI && stateTime > 5){
            trenutnoStanje = State.SETANJE;
            brzina.x = 1;
            System.out.println("KORNJACA SE PROBUDILA IZ OKLOPA");
        }

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / SuperMario.PPM);
        b2body.setLinearVelocity(brzina);
    }

    @Override
    public void udaracUGlavu(Mario mario) {
        if(trenutnoStanje == State.OKLOP_STOJI) {
            if(mario.b2body.getPosition().x > b2body.getPosition().x)
                brzina.x = -2;
            else
                brzina.x = 2;
            trenutnoStanje = State.KRETANJE_OKLOPA;
            System.out.println("Oklop se krece");
        }
        else {
            trenutnoStanje = State.OKLOP_STOJI;
            brzina.x = 0;
        }
    }

    @Override
    public void udaracNeprijatelja(Neprijatelj neprijatelj) {
        kretnjaUDrugomSmeru(true, false);
    }

    public void sut(int direkcija){
        brzina.x = direkcija;
        trenutnoStanje = State.KRETANJE_OKLOPA;
    }
}
