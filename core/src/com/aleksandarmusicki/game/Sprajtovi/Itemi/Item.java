package com.aleksandarmusicki.game.Sprajtovi.Itemi;

import com.aleksandarmusicki.game.Sprajtovi.Mario;
import com.aleksandarmusicki.game.SuperMario;
import com.aleksandarmusicki.game.Ekrani.EkranIgre;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public abstract class Item extends Sprite {
    protected EkranIgre ekran;
    protected World svet;
    protected Vector2 brzina;
    protected boolean unistiti;
    protected boolean unisteno;
    protected Body body;

    public Item(EkranIgre ekran, float x, float y){
        this.ekran = ekran;
        this.svet = ekran.getSvet();
        unistiti = false;
        unisteno = false;

        setPosition(x, y);
        setBounds(getX(), getY(), 16 / SuperMario.PPM, 16 / SuperMario.PPM);
        definisiItem();
    }

    public abstract void definisiItem();
    public abstract void koristi(Mario mario);

    public void update(float dt){
        if(unistiti && !unisteno){
            svet.destroyBody(body);
            unisteno = true;
        }
    }

    public void draw(Batch batch){
        if(!unisteno)
            super.draw(batch);
    }

    public void destroy(){
        unistiti = true;
    }
    public void kretnjaUDrugomSmeru(boolean x, boolean y){
        if(x)
            brzina.x = -brzina.x;
        if(y)
            brzina.y = -brzina.y;
    }
}
