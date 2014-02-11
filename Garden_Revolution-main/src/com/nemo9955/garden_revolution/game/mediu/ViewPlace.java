package com.nemo9955.garden_revolution.game.mediu;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.nemo9955.garden_revolution.game.enumTypes.TowerType;
import com.nemo9955.garden_revolution.game.enumTypes.WeaponType;
import com.nemo9955.garden_revolution.game.world.WorldWrapper;


public class ViewPlace extends Tower {

    @SuppressWarnings("deprecation")
    public ViewPlace(ModelInstance baza, WorldWrapper world, Vector3 poz, int ID) {
        super( baza, world, poz, ID );
        addToTowerColiders( new BoundingBox( poz.tmp().add( 1 ), poz.tmp().sub( 1 ) ) );
    }

    @Override
    public boolean changeWeapon(WeaponType toChange) {
        return false;
    }

    @Override
    public boolean upgradeTower(TowerType upgrade) {
        return false;
    }

    @Override
    public void update(float delta) {
    }
}
