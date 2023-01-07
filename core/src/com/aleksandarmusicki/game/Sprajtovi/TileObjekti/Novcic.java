package com.aleksandarmusicki.game.Sprajtovi.TileObjekti;
import com.aleksandarmusicki.game.Sprajtovi.Itemi.ItemDef;
import com.aleksandarmusicki.game.Sprajtovi.Itemi.Pecurka;
import com.aleksandarmusicki.game.Sprajtovi.Mario;
import com.aleksandarmusicki.game.SuperMario;
import com.aleksandarmusicki.game.Scene.Hud;
import com.aleksandarmusicki.game.Ekrani.EkranIgre;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public class Novcic extends InteraktivniTileObject {
    private static TiledMapTileSet tileSet;
    private final int PRAZAN_NOVCIC = 28;

    public Novcic(EkranIgre ekran, MapObject object){
        super(ekran, object);
        tileSet = mapa.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setFilterKategorije(SuperMario.NOVCIC_BIT);
    }

    @Override
    public void udaracGlavom(Mario mario) {
        if(getCell().getTile().getId() == PRAZAN_NOVCIC)
            SuperMario.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else {
            if(objekat.getProperties().containsKey("mushroom")) {
                ekran.stvoriItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / SuperMario.PPM),
                        Pecurka.class));
                SuperMario.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            }
            else
                SuperMario.manager.get("audio/sounds/coin.wav", Sound.class).play();
            getCell().setTile(tileSet.getTile(PRAZAN_NOVCIC));
            Hud.dodajSkor(100);
        }
    }


}
