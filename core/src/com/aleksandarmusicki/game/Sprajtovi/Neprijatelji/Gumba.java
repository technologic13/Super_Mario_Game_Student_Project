package com.aleksandarmusicki.game.Sprajtovi.Neprijatelji;

import com.aleksandarmusicki.game.Sprajtovi.Mario;
import com.aleksandarmusicki.game.SuperMario;
import com.aleksandarmusicki.game.Ekrani.EkranIgre;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public class Gumba extends com.aleksandarmusicki.game.Sprajtovi.Neprijatelji.Neprijatelj
{
    private float stateTime;
    private Animation<TextureRegion> animacijaKretanja;
    private Array<TextureRegion> frejmovi;
    private boolean unisti;
    private boolean unisteno;
    float ugao;


    public Gumba(EkranIgre ekran, float x, float y) {
        super(ekran, x, y);
        frejmovi = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++)
            frejmovi.add(new TextureRegion(ekran.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        animacijaKretanja = new Animation(0.4f, frejmovi);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / SuperMario.PPM, 16 / SuperMario.PPM);
        unisti = false;
        unisteno = false;
        ugao = 0;
    }

    public void update(float dt){
        stateTime += dt;
        if(unisti && !unisteno){
            svet.destroyBody(b2body);
            unisteno = true;
            setRegion(new TextureRegion(ekran.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime = 0;
        }
        else if(!unisteno) {
            b2body.setLinearVelocity(brzina);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(animacijaKretanja.getKeyFrame(stateTime, true));
        }
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

        //Kreiranje Goombine glave
        PolygonShape glava = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / SuperMario.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / SuperMario.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / SuperMario.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / SuperMario.PPM);
        glava.set(vertice);

        fdef.shape = glava;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = SuperMario.GLAVA_NEPRIJATELJA_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if(!unisteno || stateTime < 1)
            super.draw(batch);
    }



    @Override
    public void udaracUGlavu(Mario mario) {
        unisti = true;
        SuperMario.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }

    @Override
    public void udaracNeprijatelja(Neprijatelj neprijatelj) {
        if(neprijatelj instanceof com.aleksandarmusicki.game.Sprajtovi.Neprijatelji.Kornjaca && ((com.aleksandarmusicki.game.Sprajtovi.Neprijatelji.Kornjaca) neprijatelj).trenutnoStanje == Kornjaca.State.KRETANJE_OKLOPA)
            unisti = true;
        else
            kretnjaUDrugomSmeru(true, false);
    }
}
