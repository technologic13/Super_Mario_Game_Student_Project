package com.aleksandarmusicki.game.Sprajtovi.TileObjekti;

import com.aleksandarmusicki.game.Sprajtovi.Mario;
import com.aleksandarmusicki.game.SuperMario;
import com.aleksandarmusicki.game.Ekrani.EkranIgre;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public abstract class InteraktivniTileObject {
    protected World svet;
    protected TiledMap mapa;
    protected Rectangle granice;
    protected Body body;
    protected EkranIgre ekran;
    protected MapObject objekat;

    protected Fixture fixture;

    public InteraktivniTileObject(EkranIgre ekran, MapObject objekat){
        this.objekat = objekat;
        this.ekran = ekran;
        this.svet = ekran.getSvet();
        this.mapa = ekran.getMapa();
        this.granice = ((RectangleMapObject) objekat).getRectangle();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape oblik = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((granice.getX() + granice.getWidth() / 2) / SuperMario.PPM, (granice.getY() + granice.getHeight() / 2) / SuperMario.PPM);

        body = svet.createBody(bdef);

        oblik.setAsBox(granice.getWidth() / 2 / SuperMario.PPM, granice.getHeight() / 2 / SuperMario.PPM);
        fdef.shape = oblik;
        fixture = body.createFixture(fdef);

    }

    public abstract void udaracGlavom(Mario mario);
    public void setFilterKategorije(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) mapa.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * SuperMario.PPM / 16),
                (int)(body.getPosition().y * SuperMario.PPM / 16));
    }


}
