package com.nemo9955.garden_revolution.game.entitati;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;


public abstract class LifeForm extends Entity {

    public AnimationController animation;
    public final Vector3       direction = new Vector3();

    public int                 life;

    public void init(float x, float y, float z) {
        super.init( x, y, z );
        animation = new AnimationController( model );
        direction.set( Vector3.Zero );
        // update( Gdx.graphics.getDeltaTime() );
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public void update(float delta) {
        super.update( delta );
        animation.update( delta );
    }

    public void damage(int dmg) {
        life -= dmg;
        if ( life <=0 )
            setDead( true );
    }

    protected void lookAt(Vector3 look) {// FIXME they spin
        float ung = (float) Math.toDegrees( Math.atan2( poz.x -look.x, poz.z -look.z ) );
        if ( ung <0 )
            ung += 360;
        ung -= 180;
        rotate( 0, angle +ung, 0 );
    }
}