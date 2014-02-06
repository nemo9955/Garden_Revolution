package com.nemo9955.garden_revolution.game.entitati;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector3;
import com.nemo9955.garden_revolution.game.World;
import com.nemo9955.garden_revolution.game.enumTypes.EnemyType;
import com.nemo9955.garden_revolution.utility.Vars;


public class Enemy extends LifeForm {


    public Path<Vector3>      path;
    public float              percent;
    public static final float STEP   = 1f /50f;
    public Vector3            flag;
    private Vector3           offset;
    private EnemyType         type;

    public boolean            paused = false;

    public Enemy(World world) {
        super( world );
        offset = new Vector3();
        flag = new Vector3();
    }

    public Enemy create(CatmullRomSpline<Vector3> path, EnemyType type, float x, float y, float z) {
        this.type = type;

        super.init( x, y, z );

        this.life = type.life;

        offset.set( MathUtils.random( -30, 30 ), 0, MathUtils.random( -30, 30 ) ).scl( 0.1f );
        this.path = path;
        percent = -STEP;

        lookAt( path.valueAt( flag, percent +STEP ) );
        flag.add( offset );

        return this;
    }

    @Override
    public void reset() {
        super.reset();
        paused = false;
    }

    @Override
    protected ModelInstance getModel(float x, float y, float z) {
        return type.getModel( x, y, z );
    }

    @Override
    public void update(float delta) {
        super.update( delta );
        if ( !paused && ( Vars.updateUave ||Gdx.input.isKeyPressed( Keys.F12 ) ) )
            advance( delta );
    }

    private void advance(float delta) {
        direction.set( flag ).sub( poz ).nor().scl( type.speed *delta );
        move( direction );
        if ( flag.epsilonEquals( poz, 1f ) ) {
            percent += STEP;
            if ( percent >=1 ) {
                world.addViata( -type.strenght );
                setDead( true );
            }
            path.valueAt( flag, percent +STEP );
            lookAt( path.valueAt( flag, percent +STEP ) );
            flag.add( offset );
        }
    }

    private long lastAtack = 0;

    public void atack(Ally ally) {
        if ( System.currentTimeMillis() -lastAtack >Vars.enemyAttackInterval ) {
            ally.damage( type.strenght );
            lastAtack = System.currentTimeMillis();
        }
    }

    @Override
    public void setDead(boolean dead) {
        super.setDead( dead );


        if ( isDead() ) {
            world.inamicPool.free( this );
            world.getEnemy().removeValue( this, false );
        }

    }

}
