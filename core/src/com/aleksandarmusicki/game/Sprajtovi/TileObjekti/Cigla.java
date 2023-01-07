package com.aleksandarmusicki.game.Sprajtovi.TileObjekti;

import com.aleksandarmusicki.game.Sprajtovi.Mario;
import com.aleksandarmusicki.game.SuperMario;
import com.aleksandarmusicki.game.Scene.Hud;
import com.aleksandarmusicki.game.Ekrani.EkranIgre;
import com.badlogic.gdx.audio.Sound;

import com.badlogic.gdx.maps.MapObject;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public class Cigla extends InteraktivniTileObject {
    public Cigla(EkranIgre ekran, MapObject object){
        super(ekran, object);
        fixture.setUserData(this);
        setFilterKategorije(SuperMario.CIGLA_BIT);
    }

    @Override
    public void udaracGlavom(Mario mario) {
        if(mario.jeVelik()) {
            setFilterKategorije(SuperMario.UNISTEN_BIT);
            getCell().setTile(null);
            Hud.dodajSkor(200);
            SuperMario.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        }
        SuperMario.manager.get("audio/sounds/bump.wav", Sound.class).play();
    }

}
