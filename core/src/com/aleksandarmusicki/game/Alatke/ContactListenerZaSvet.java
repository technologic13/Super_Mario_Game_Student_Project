package com.aleksandarmusicki.game.Alatke;

import com.aleksandarmusicki.game.SuperMario;
import com.aleksandarmusicki.game.Sprajtovi.Neprijatelji.Neprijatelj;
import com.aleksandarmusicki.game.Sprajtovi.Itemi.Item;
import com.aleksandarmusicki.game.Sprajtovi.Mario;
import com.aleksandarmusicki.game.Sprajtovi.TileObjekti.InteraktivniTileObject;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public class ContactListenerZaSvet implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case SuperMario.GLAVA_MARIA_BIT | SuperMario.CIGLA_BIT:
            case SuperMario.GLAVA_MARIA_BIT | SuperMario.NOVCIC_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.GLAVA_MARIA_BIT)
                    ((InteraktivniTileObject) fixB.getUserData()).udaracGlavom((Mario) fixA.getUserData());
                else
                    ((InteraktivniTileObject) fixA.getUserData()).udaracGlavom((Mario) fixB.getUserData());
                break;
            case SuperMario.GLAVA_NEPRIJATELJA_BIT | SuperMario.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.GLAVA_NEPRIJATELJA_BIT)
                    ((Neprijatelj)fixA.getUserData()).udaracUGlavu((Mario) fixB.getUserData());
                else
                    ((Neprijatelj)fixB.getUserData()).udaracUGlavu((Mario) fixA.getUserData());
                break;
            case SuperMario.NEPRIJATELJ_BIT | SuperMario.OBJEKAT_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.NEPRIJATELJ_BIT)
                    ((Neprijatelj)fixA.getUserData()).kretnjaUDrugomSmeru(true, false);
                else
                    ((Neprijatelj)fixB.getUserData()).kretnjaUDrugomSmeru(true, false);
                break;
            case SuperMario.MARIO_BIT | SuperMario.NEPRIJATELJ_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.MARIO_BIT)
                    ((Mario) fixA.getUserData()).udarac((Neprijatelj)fixB.getUserData());
                else
                    ((Mario) fixB.getUserData()).udarac((Neprijatelj)fixA.getUserData());
                break;
            case SuperMario.NEPRIJATELJ_BIT | SuperMario.NEPRIJATELJ_BIT:
                ((Neprijatelj)fixA.getUserData()).udaracNeprijatelja((Neprijatelj)fixB.getUserData());
                ((Neprijatelj)fixB.getUserData()).udaracNeprijatelja((Neprijatelj)fixA.getUserData());
                break;
            case SuperMario.ITEM_BIT | SuperMario.OBJEKAT_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.ITEM_BIT)
                    ((Item)fixA.getUserData()).kretnjaUDrugomSmeru(true, false);
                else
                    ((Item)fixB.getUserData()).kretnjaUDrugomSmeru(true, false);
                break;
            case SuperMario.ITEM_BIT | SuperMario.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == SuperMario.ITEM_BIT)
                    ((Item)fixA.getUserData()).koristi((Mario) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).koristi((Mario) fixA.getUserData());
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
