package com.aleksandarmusicki.game.Sprajtovi.Itemi;

import com.badlogic.gdx.math.Vector2;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public class ItemDef {
    public Vector2 pozicija;
    public Class<?> tip;
    public ItemDef(Vector2 pozicija, Class<?> tip){
        this.pozicija = pozicija;
        this.tip = tip;
    }
}
