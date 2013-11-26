package com.nemo9955.garden_revolution.game.mediu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.nemo9955.garden_revolution.game.World;


public class Arma implements Disposable {

    private Array<Disposable>  toDispose = new Array<Disposable>( false, 1 );
    private static Vector3     tmp       = new Vector3();

    private ModelInstance      model;
    public AnimationController animation;
    public Vector3             poz;

    public Arma(Vector3 poz) {
        this.poz = poz;
        ModelBuilder build = new ModelBuilder();
        Model sfera = build.createSphere( 2, 2, 2, 12, 12, new Material( ColorAttribute.createDiffuse( Color.WHITE ) ), Usage.Position |Usage.Normal |Usage.TextureCoordinates );
        toDispose.add( sfera );
        model = new ModelInstance( sfera, poz );
        animation = new AnimationController( model );
    }


    public void update(float delta) {
        animation.update( delta );
    }

    public void fireMain(World world, Ray ray) {
        float distance = -ray.origin.y /ray.direction.y;
        distance = Math.abs( distance );
        tmp = ray.getEndPoint( new Vector3(), distance );
        world.addShot( poz, tmp.sub( poz ).nor() );
    }

    public void render(ModelBatch modelBatch) {
        modelBatch.render( model );
    }

    public void render(ModelBatch modelBatch, Environment light) {
        modelBatch.render( model, light );
    }

    public void render(ModelBatch modelBatch, Shader shader) {
        modelBatch.render( model, shader );
    }


    @Override
    public void dispose() {

        for (Disposable dis : toDispose )
            dis.dispose();

        toDispose.clear();
    }

}