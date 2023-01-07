package com.aleksandarmusicki.game.Sprajtovi.Neprijatelji;

import com.aleksandarmusicki.game.Ekrani.EkranIgre;
import com.aleksandarmusicki.game.Sprajtovi.Mario;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public abstract class Neprijatelj extends Sprite {
    protected World svet;
    protected EkranIgre ekran;
    public Body b2body;
    public Vector2 brzina;

    public Neprijatelj(EkranIgre ekran, float x, float y){
        this.svet = ekran.getSvet();
        this.ekran = ekran;
        setPosition(x, y);
        definisiNeprijatelja();
        brzina = new Vector2(-1, -2);
        b2body.setActive(false);
    }

    protected abstract void definisiNeprijatelja();
    public abstract void update(float dt);
    public abstract void udaracUGlavu(Mario mario);
    public abstract void udaracNeprijatelja(Neprijatelj neprijatelj);

    public void kretnjaUDrugomSmeru(boolean x, boolean y){
        if(x)
            brzina.x = -brzina.x;
        if(y)
            brzina.y = -brzina.y;
    }
}
