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
import com.badlogic.gdx.utils.Disposable;
import com.nemo9955.garden_revolution.game.World;


public abstract class Weapon implements Disposable {

    protected static Vector3   tmp        = new Vector3();

    private ModelInstance      model;
    public AnimationController animation;
    public Vector3             poz;

    protected int              fireDellay = 100;
    protected long             fireTime;

    public String              name       = "weapon name";
    public String              details    = "weapon description";


    public Weapon(Vector3 poz) {
        this.poz = poz;

        ModelBuilder build = new ModelBuilder();
        Model sfera = build.createSphere( 2, 2, 2, 12, 12, new Material( ColorAttribute.createDiffuse( Color.WHITE ) ), Usage.Position |Usage.Normal |Usage.TextureCoordinates );
        World.toDispose.add( sfera );

        model = getModelInstance( poz );
        animation = new AnimationController( model );
    }

    protected abstract ModelInstance getModelInstance(Vector3 poz2);

    public void update(float delta) {
        animation.update( delta );
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


    public int getFireDellay() {
        return fireDellay;
    }

    public void setFireDellay(int fireDellay) {
        this.fireDellay = fireDellay;
    }

    @Override
    public void dispose() {

    }

    public static interface FireCharged {

        public void fireCharged(World world, Ray ray, float charged);
    }

    public static interface FireHold {

        public void fireHold(World world, Ray ray);
    }
}