package com.aleksandarmusicki.game.Alatke;

import com.aleksandarmusicki.game.SuperMario;
import com.aleksandarmusicki.game.Ekrani.EkranIgre;
import com.aleksandarmusicki.game.Sprajtovi.Neprijatelji.Neprijatelj;
import com.aleksandarmusicki.game.Sprajtovi.Neprijatelji.Kornjaca;
import com.aleksandarmusicki.game.Sprajtovi.TileObjekti.Cigla;
import com.aleksandarmusicki.game.Sprajtovi.TileObjekti.Novcic;
import com.aleksandarmusicki.game.Sprajtovi.Neprijatelji.Gumba;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Kod napisao Aleksandar Musicki 23/8/21.
 */

public class B2KreatorSveta {
    private Array<Gumba> gumbe;
    private Array<Kornjaca> kornjace;

    public B2KreatorSveta(EkranIgre ekran){
        World svet = ekran.getSvet();
        TiledMap mapa = ekran.getMapa();
        //kreiranje body-a i fixture varijabli
        BodyDef bdef = new BodyDef();
        PolygonShape oblik = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //kreiranje zemlje
        for(MapObject object : mapa.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / SuperMario.PPM, (rect.getY() + rect.getHeight() / 2) / SuperMario.PPM);

            body = svet.createBody(bdef);

            oblik.setAsBox(rect.getWidth() / 2 / SuperMario.PPM, rect.getHeight() / 2 / SuperMario.PPM);
            fdef.shape = oblik;
            body.createFixture(fdef);
        }

        //kreiranje cevi
        for(MapObject object : mapa.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / SuperMario.PPM, (rect.getY() + rect.getHeight() / 2) / SuperMario.PPM);

            body = svet.createBody(bdef);

            oblik.setAsBox(rect.getWidth() / 2 / SuperMario.PPM, rect.getHeight() / 2 / SuperMario.PPM);
            fdef.shape = oblik;
            fdef.filter.categoryBits = SuperMario.OBJEKAT_BIT;
            body.createFixture(fdef);
        }

        //kreiranje cigli
        for(MapObject object : mapa.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            new Cigla(ekran, object);
        }

        //kreiranje novcica
        for(MapObject object : mapa.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

            new Novcic(ekran, object);
        }

        //kreiranje svih gumbi
        gumbe = new Array<Gumba>();
        for(MapObject object : mapa.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            gumbe.add(new Gumba(ekran, rect.getX() / SuperMario.PPM, rect.getY() / SuperMario.PPM));
        }
        kornjace = new Array<Kornjaca>();
        for(MapObject object : mapa.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            kornjace.add(new Kornjaca(ekran, rect.getX() / SuperMario.PPM, rect.getY() / SuperMario.PPM));
        }
    }


    public Array<Neprijatelj> getNeprijatelje(){
        Array<Neprijatelj> neprijatelji = new Array<Neprijatelj>();
        neprijatelji.addAll(gumbe);
        neprijatelji.addAll(kornjace);
        return neprijatelji;
    }
}
