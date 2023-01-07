package com.aleksandarmusicki.game.Sprajtovi.Itemi;

import com.aleksandarmusicki.game.Sprajtovi.Mario;
import com.aleksandarmusicki.game.SuperMario;
import com.aleksandarmusicki.game.Ekrani.EkranIgre;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public class Pecurka extends Item {
    public Pecurka(EkranIgre screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        brzina = new Vector2(0.7f, 0);
    }

    @Override
    public void definisiItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = svet.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape oblik = new CircleShape();
        oblik.setRadius(6 / SuperMario.PPM);
        fdef.filter.categoryBits = SuperMario.ITEM_BIT;
        fdef.filter.maskBits = SuperMario.MARIO_BIT |
                SuperMario.OBJEKAT_BIT |
                SuperMario.ZEMLJA_BIT |
                SuperMario.NOVCIC_BIT |
                SuperMario.CIGLA_BIT;

        fdef.shape = oblik;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void koristi(Mario mario) {
        destroy();
        mario.raste();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        brzina.y = body.getLinearVelocity().y;
        body.setLinearVelocity(brzina);
    }
}
