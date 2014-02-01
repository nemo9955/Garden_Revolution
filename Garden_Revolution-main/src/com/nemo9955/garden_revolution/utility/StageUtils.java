package com.nemo9955.garden_revolution.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.nemo9955.garden_revolution.Garden_Revolution;
import com.nemo9955.garden_revolution.game.enumTypes.TowerType;
import com.nemo9955.garden_revolution.game.mediu.Cannon;
import com.nemo9955.garden_revolution.game.mediu.MiniGun;
import com.nemo9955.garden_revolution.states.Gameplay;


public class StageUtils {


    public static Stage makeGamePlayStage(Stage stage, final Gameplay gameplay) {
        stage = new Stage( Gdx.graphics.getWidth() *1.5f /Vars.densitate, Gdx.graphics.getHeight() *1.5f /Vars.densitate, true );
        Skin skin = Garden_Revolution.manager.get( Assets.SKIN_JSON.path(), Skin.class );

        gameplay.weaponCharger = new Image( skin, "mover-knob" );
        gameplay.weaponCharger.setVisible( false );

        gameplay.fps = new Label( "FPS: ", skin );
        gameplay.fps.setScale( Vars.densitate );
        gameplay.fps.setPosition( stage.getWidth() /2, stage.getHeight() -gameplay.fps.getHeight() );

        final Board hud = new Board(); // aici e tot ce e legat de HUD ------------------------------------------------------------------------------
        final ImageButton pauseBut = new ImageButton( skin, "IGpause" );
        final ImageButton turnIG = new ImageButton( skin, "towerUpgrade" );

        Image tinta = new Image( skin, "tinta" );
        gameplay.viataTurn = new Label( "Life ", skin );
        gameplay.mover = new Touchpad( 1, skin );
        gameplay.mover.setVisible( true );

        turnIG.setPosition( stage.getWidth() -turnIG.getWidth(), 0 );
        gameplay.viataTurn.setFontScale( 0.6f );
        gameplay.viataTurn.setPosition( 10 *Vars.densitate, stage.getHeight() -gameplay.viataTurn.getHeight() -10 *Vars.densitate );
        pauseBut.setPosition( stage.getWidth() -pauseBut.getWidth(), stage.getHeight() -pauseBut.getHeight() );

        gameplay.mover.setPosition( stage.getWidth() *0.02f, stage.getWidth() *0.02f );
        gameplay.mover.addAction( Actions.alpha( Vars.tPadMinAlpha ) );
        tinta.setPosition( stage.getWidth() /2 -tinta.getWidth() /2, stage.getHeight() /2 -tinta.getHeight() /2 );

        hud.addActor( gameplay.viataTurn );
        hud.addActor( pauseBut );
        hud.addActor( tinta );
        hud.addActor( gameplay.mover );
        hud.addActor( turnIG );
        hud.addActor( gameplay.weaponCharger );


        final Table pauseIG = new Table( skin ); // aici e tot ce are legatura cu meniul de pauza --------------------------------------------------------------------
        final TextButton resumeBut = new TextButton( "Resume play", skin );
        final TextButton optBut = new TextButton( "Options", skin );
        final TextButton meniuBut = new TextButton( "Main menu", skin );

        resumeBut.setName( "resume" );

        pauseIG.setVisible( false );
        pauseIG.setFillParent( true );
        pauseIG.setBackground( "pix30" );
        pauseIG.add( "PAUSE", "big-green" ).padBottom( stage.getHeight() *0.1f );
        pauseIG.row();
        pauseIG.add( resumeBut ).padBottom( stage.getHeight() *0.07f );
        pauseIG.row();
        pauseIG.add( optBut ).padBottom( stage.getHeight() *0.07f );
        pauseIG.row();
        pauseIG.add( meniuBut );


        final Table optContinut = new Table();// aici e tot ce tine de optiunile in-game ---------------------------------------------------------------------------------
        final ScrollPane optiuni = new ScrollPane( optContinut, skin );
        final TextButton backBut = new TextButton( "Back", skin, "demon" );
        final TextButton updWaves = new TextButton( "Activate Wave", skin );
        final TextButton debug = new TextButton( "Hide Debug", skin );

        optiuni.setVisible( false );
        optiuni.setWidget( optContinut );
        optiuni.setBounds( stage.getHeight() *0.1f, stage.getHeight() *0.1f, stage.getWidth() -stage.getHeight() *0.2f, stage.getHeight() *0.8f );

        optContinut.setFillParent( true );
        optContinut.defaults().space( 70 *Vars.densitate );
        optContinut.add( updWaves ).row();
        optContinut.add( debug ).row();
        optContinut.add( backBut ).row();


        final Group upgradeTower = new Group();// aici e tot ce tine de meniul de upgradare a turnurilor ------------------------------------------------------------------
        final TextButton backTowe1 = new TextButton( "Bk", skin );// FIXME repara afiseara butonului
        final TextButton backTowe2 = new TextButton( "Bk", skin );
        final TextButton basicT = new TextButton( "BASIC", skin );
        final ImageButton miniGun = new ImageButton( IconType.SAGETI.getAsDrawable( skin, 70, 70 ) );
        final ImageButton cannon = new ImageButton( IconType.TUN.getAsDrawable( skin, 70, 70 ) );
        final CircularGroup mainUpgrades = new CircularGroup( gameplay.shape );

        float freeSpace = 25 *Vars.densitate;

        backTowe1.setPosition( 15 *Vars.densitate, stage.getHeight() /2 -backTowe1.getHeight() /2 );
        backTowe2.setPosition( 15 *Vars.densitate, stage.getHeight() /2 -backTowe2.getHeight() /2 );

        mainUpgrades.setDraggable( true );
        mainUpgrades.setAsCircle( 1000, 70 );
        mainUpgrades.setPosition( -mainUpgrades.getRadius() + ( freeSpace *2.1f ) +backTowe1.getWidth(), stage.getHeight() /2 );
        // mainUpgrades.setPosition( stage.getWidth() /2, stage.getHeight() /2 );
        float i, treapta = CircularGroup.aprxOptAngl( mainUpgrades.getRadius(), basicT.getHeight() );
        Rectangle zon = new Rectangle( 0, 0, stage.getWidth(), stage.getHeight() );
        i = mainUpgrades.minAngleInZon( zon, treapta, 2 );
        mainUpgrades.setActivInterval( i, 360 -i, true, treapta *1.4f, true );
        // mainUpgrades.setActivInterval( 30, -30, true, 30, false );


        mainUpgrades.addActor( basicT );
        mainUpgrades.addActor( miniGun );
        mainUpgrades.addActor( cannon );


        upgradeTower.addActor( backTowe1 );
        upgradeTower.addActor( mainUpgrades );
        upgradeTower.addActor( backTowe2 );
        upgradeTower.setVisible( false );

        // pentru elementele din HUD +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        ChangeListener hudButons = new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if ( pauseBut.isPressed() ) {
                    hud.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ) ) );
                    pauseIG.addAction( Actions.sequence( Actions.alpha( 0 ), Actions.visible( true ), Actions.delay( 0.2f ), Actions.alpha( 1, 0.5f ) ) );
                    gameplay.updWorld = false;
                }
                else if ( turnIG.isPressed() ) {
                    hud.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ) ) );
                    upgradeTower.addAction( Actions.sequence( Actions.alpha( 0 ), Actions.visible( true ), Actions.delay( 0.2f ), Actions.alpha( 1, 0.5f ) ) );
                    gameplay.updWorld = false;
                }

            }
        };


        // pentru butoanele din pause
        ChangeListener pauseButons = new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if ( optBut.isPressed() ) {
                    pauseIG.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ) ) );
                    optiuni.addAction( Actions.sequence( Actions.alpha( 0 ), Actions.visible( true ), Actions.delay( 0.2f ), Actions.alpha( 1, 0.5f ) ) );
                    gameplay.updWorld = false;
                }
                else if ( resumeBut.isPressed() ) {
                    pauseIG.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ) ) );
                    hud.addAction( Actions.sequence( Actions.alpha( 0 ), Actions.visible( true ), Actions.delay( 0.2f ), Actions.alpha( 1, 0.5f ) ) );
                    gameplay.updWorld = true;
                }
                else if ( meniuBut.isPressed() ) {
                    hud.setVisible( true );
                    hud.addAction( Actions.alpha( 1 ) );
                    pauseIG.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ), Actions.run( new Runnable() {

                        @Override
                        public void run() {
                            Garden_Revolution.game.setScreen( Garden_Revolution.menu );
                        }
                    } ) ) );
                }
            }
        };

        // pentru tot ce tine de upgradarea turnurilor
        ChangeListener turnButons = new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if ( backTowe1.isPressed() ||backTowe2.isPressed() ) {
                    upgradeTower.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ) ) );
                    hud.addAction( Actions.sequence( Actions.alpha( 0 ), Actions.visible( true ), Actions.delay( 0.2f ), Actions.alpha( 1, 0.5f ) ) );
                    gameplay.updWorld = true;
                }
                else if ( basicT.isPressed() )
                    gameplay.player.upgradeCurentTower( TowerType.BASIC );
                else if ( miniGun.isPressed() )
                    gameplay.player.changeCurrentWeapon( MiniGun.class );
                else if ( cannon.isPressed() )
                    gameplay.player.changeCurrentWeapon( Cannon.class );

            }
        };


        // pentru elementele din optiuni
        ChangeListener optButons = new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if ( backBut.isPressed() ) {
                    optiuni.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ) ) );
                    pauseIG.addAction( Actions.sequence( Actions.alpha( 0 ), Actions.visible( true ), Actions.delay( 0.2f ), Actions.alpha( 1, 0.5f ) ) );
                }
                else if ( updWaves.isPressed() ) {
                    if ( updWaves.isChecked() )
                        updWaves.setText( "Dezactivate Wave" );
                    else
                        updWaves.setText( "Activate Wave" );
                    updWaves.pack();
                    Vars.updateUave = !Vars.updateUave;
                }
                else if ( debug.isPressed() ) {
                    if ( debug.isChecked() )
                        debug.setText( "Show Debug" );
                    else
                        debug.setText( "Hide Debug" );
                    debug.pack();
                    Vars.showDebug = !Vars.showDebug;
                }
            }
        };

        gameplay.mover.addListener( new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameplay.movex = Vars.invertPadX *gameplay.mover.getKnobPercentX() *Vars.modCamSpeedX;
                gameplay.movey = Vars.invertPadY *gameplay.mover.getKnobPercentY() *Vars.modCamSpeedY;
                gameplay.touchPadTimmer = Vars.tPadAlphaDellay;
                gameplay.mover.addAction( Actions.alpha( 1 ) );
            }
        } );


        upgradeTower.addListener( turnButons );
        hud.addListener( hudButons );
        pauseIG.addListener( pauseButons );
        optiuni.addListener( optButons );

        hud.setName( "HUD" );
        upgradeTower.setName( "Tower Upgrade" );
        optiuni.setName( "Optiuni" );
        pauseIG.setName( "Pause" );

        stage.addActor( hud );
        stage.addActor( upgradeTower );
        stage.addActor( optiuni );
        stage.addActor( pauseIG );
        stage.addActor( gameplay.fps );


        return stage;
    }

    /**
     * https://bitbucket.org/dermetfan/somelibgdxtests/src/28080ff7dd7bd6d000ec8ba7f9514e177bb03e17/SomeLibgdxTests/src/net/dermetfan/someLibgdxTests/screens/TabsLeftTest.java?at=default
     * 
     * @author dermetfan
     * 
     */
    public static class Board extends Group {

        public void pack() {
            float width = Float.NEGATIVE_INFINITY, height = Float.NEGATIVE_INFINITY, childXandWidth, childYandHeight;
            for (Actor child : getChildren() ) {
                if ( ( childXandWidth = child.getX() +child.getWidth() ) >width )
                    width = childXandWidth;

                if ( ( childYandHeight = child.getY() +child.getHeight() ) >height )
                    height = childYandHeight;
            }

            setSize( width, height );
        }

    }
}
