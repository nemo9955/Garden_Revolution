package com.nemo9955.garden_revolution.utility;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.nemo9955.garden_revolution.Garden_Revolution;
import com.nemo9955.garden_revolution.game.mediu.Turnuri;
import com.nemo9955.garden_revolution.states.Gameplay;


public class StageUtils {


    public static Stage makeGamePlayStage(Stage stage, final Gameplay gameplay) {
        stage = new Stage();

        Skin skin = Garden_Revolution.manager.get( Assets.SKIN_JSON.path() );
        final ImageButton pauseBut = new ImageButton( skin, "IGpause" );
        final ImageButton camLeft = new ImageButton( skin, "camLeft" );
        final ImageButton camRight = new ImageButton( skin, "camRight" );
        final TextButton resumeBut = new TextButton( "Resume play", skin );
        final TextButton meniuBut = new TextButton( "Main menu", skin );

        final TextButton optBut = new TextButton( "Options", skin );

        final Board hud = new Board();
        final Table pauseIG = new Table( skin );

        pauseIG.setVisible( false );
        pauseIG.addAction( Actions.alpha( 0 ) );
        pauseIG.setBackground( "pix30" );
        pauseIG.setFillParent( true );
        pauseIG.add( "PAUSE", "big-green" ).padBottom( stage.getHeight() *0.1f );
        pauseIG.row();
        pauseIG.add( resumeBut ).padBottom( stage.getHeight() *0.07f );
        pauseIG.row();
        pauseIG.add( optBut ).padBottom( stage.getHeight() *0.07f );
        pauseIG.row();
        pauseIG.add( meniuBut );

        final Table optContinut = new Table();
        final ScrollPane optIG = new ScrollPane( optContinut, skin );

        final TextButton backBut = new TextButton( "Back", skin, "demon" );
        final TextButton updWaves = new TextButton( "Activate Wave", skin );
        final TextButton debug = new TextButton( "Hide Debug", skin );

        optIG.addAction( Actions.alpha( 0 ) );
        optIG.setVisible( false );
        optIG.setBounds( stage.getHeight() *0.1f, stage.getHeight() *0.1f, stage.getWidth() -stage.getHeight() *0.2f, stage.getHeight() *0.8f );

        optContinut.setFillParent( true );
        optContinut.defaults().space( 70 *Mod.densitate );
        optContinut.add( updWaves ).row();
        optContinut.add( debug ).row();
        optContinut.add( backBut ).row();

        final TextButton backTowe = new TextButton( "Back", skin );
        final TextButton basicT = new TextButton( "BASIC", skin );

        final ImageButton turnIG = new ImageButton( skin, "towerUpgrade" );
        final HorizontalGroup upgrades = new HorizontalGroup();
        final VerticalGroup selecter = new VerticalGroup();


        selecter.addActor( backTowe );
        selecter.addActor( basicT );
        final SplitPane panel = new SplitPane( selecter, upgrades, false, skin );

        panel.setFillParent( true );
        panel.setVisible( false );
        panel.addAction( Actions.alpha( 0 ) );
        panel.setMaxSplitAmount( 0.4f );
        panel.setMinSplitAmount( 0.2f );
        panel.setSplitAmount( 0.3f );


        Image tinta = new Image( skin, "tinta" );
        gameplay.viataTurn = new Label( "Life ", skin );
        gameplay.mover = new Touchpad( 1, skin );
        gameplay.mover.setVisible( true );

        turnIG.setScale( Mod.densitate );
        // turnIG.setSize( turnIG.getWidth() *Mod.densitate, turnIG.getHeight() *Mod.densitate );
        turnIG.setPosition( stage.getWidth() -turnIG.getWidth(), 0 );

        gameplay.viataTurn.setScale( Mod.densitate );
        // gameplay.viataTurn.setSize( gameplay.viataTurn.getWidth() *Mod.densitate , gameplay.viataTurn.getHeight() *Mod.densitate );
        gameplay.viataTurn.setPosition( 0, stage.getHeight() -gameplay.viataTurn.getHeight() );

        pauseBut.setScale( Mod.densitate );
        // pauseBut.setSize( pauseBut.getWidth() *Mod.densitate, pauseBut.getHeight() *Mod.densitate );
        pauseBut.setPosition( stage.getWidth() -pauseBut.getWidth(), stage.getHeight() -pauseBut.getHeight() );

        camLeft.setScale( Mod.densitate );
        // camLeft.setSize( camLeft.getWidth() *Mod.densitate, camLeft.getHeight() *Mod.densitate );
        camLeft.setPosition( 0, stage.getHeight() /2 -camLeft.getHeight() /2 );

        camRight.setScale( Mod.densitate );
        // camRight.setSize( camRight.getWidth() *Mod.densitate, camRight.getHeight() *Mod.densitate );
        camRight.setPosition( stage.getWidth() -camRight.getWidth(), stage.getHeight() /2 -camRight.getHeight() /2 );

        // gameplay.mover.setScale( Mod.densitate );
        gameplay.mover.setSize( gameplay.mover.getWidth() *Mod.densitate, gameplay.mover.getHeight() *Mod.densitate );
        gameplay.mover.setPosition( stage.getWidth() *0.02f, stage.getWidth() *0.02f );

        tinta.setScale( Mod.densitate );
        // tinta.setSize( tinta.getWidth() *Mod.densitate, tinta.getHeight() *Mod.densitate );
        tinta.setPosition( stage.getWidth() /2 -tinta.getWidth() /2, stage.getHeight() /2 -tinta.getHeight() /2 );

        hud.addActor( gameplay.viataTurn );
        hud.addActor( pauseBut );
        hud.addActor( camLeft );
        hud.addActor( camRight );
        hud.addActor( tinta );
        hud.addActor( gameplay.mover );
        hud.addActor( turnIG );

        // pentru tot ce tine de upgradarea turnurilor
        ChangeListener turnButons = new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if ( backTowe.isPressed() ) {
                    panel.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ) ) );
                    hud.addAction( Actions.sequence( Actions.alpha( 0 ), Actions.visible( true ), Actions.delay( 0.2f ), Actions.alpha( 1, 0.5f ) ) );
                    gameplay.toUpdate = 0;
                }
                else if ( basicT.isPressed() &&!gameplay.world.overview ) {// TODO
                    gameplay.world.getCurrentTower().upgradeTower( Turnuri.BASIC );
                }
            }
        };


        // pentru elementele din HUD
        ChangeListener hudButons = new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if ( pauseBut.isPressed() ) {
                    hud.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ) ) );
                    pauseIG.addAction( Actions.sequence( Actions.visible( true ), Actions.alpha( 0 ), Actions.delay( 0.2f ), Actions.alpha( 1, 0.5f ) ) );
                    gameplay.toUpdate = 1;
                }
                else if ( camLeft.isPressed() ) {
                    gameplay.world.prevCamera();
                }
                else if ( camRight.isPressed() ) {
                    gameplay.world.nextCamera();
                }
                else if ( turnIG.isPressed() ) {
                    hud.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ) ) );
                    panel.addAction( Actions.sequence( Actions.visible( true ), Actions.alpha( 0 ), Actions.delay( 0.2f ), Actions.alpha( 1, 0.5f ) ) );
                    gameplay.toUpdate = 1;
                }

            }
        };

        // pentru elementele din optiuni
        ChangeListener optButons = new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if ( backBut.isPressed() ) {
                    optIG.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ) ) );
                    pauseIG.addAction( Actions.sequence( Actions.alpha( 0 ), Actions.visible( true ), Actions.delay( 0.2f ), Actions.alpha( 1, 0.5f ) ) );
                }
                else if ( updWaves.isPressed() ) {
                    if ( updWaves.isChecked() )
                        updWaves.setText( "Dezactivate Wave" );
                    else
                        updWaves.setText( "Activate Wave" );
                    updWaves.pack();
                    Mod.updateUave = !Mod.updateUave;
                }
                else if ( debug.isPressed() ) {
                    if ( debug.isChecked() )
                        debug.setText( "Show Debug" );
                    else
                        debug.setText( "Hide Debug" );
                    debug.pack();
                    Mod.showDebug = !Mod.showDebug;
                }
            }
        };

        // pentru butoanele din pause
        ChangeListener pauseButons = new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if ( optBut.isPressed() ) {
                    pauseIG.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ) ) );
                    optIG.addAction( Actions.sequence( Actions.visible( true ), Actions.alpha( 0 ), Actions.delay( 0.2f ), Actions.alpha( 1, 0.5f ) ) );
                    gameplay.toUpdate = 1;
                }
                else if ( resumeBut.isPressed() ) {
                    pauseIG.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ) ) );
                    hud.addAction( Actions.sequence( Actions.alpha( 0 ), Actions.visible( true ), Actions.delay( 0.2f ), Actions.alpha( 1, 0.5f ) ) );
                    gameplay.toUpdate = 0;
                }
                else if ( meniuBut.isPressed() ) {
                    hud.setVisible( true );
                    hud.addAction( Actions.alpha( 1 ) );
                    pauseIG.addAction( Actions.sequence( Actions.alpha( 0, 0.5f ), Actions.visible( false ), Actions.run( new Runnable() {

                        @Override
                        public void run() {
                            Garden_Revolution.game.setScreen( Garden_Revolution.meniu );
                        }
                    } ) ) );
                }
            }
        };

        gameplay.mover.addListener( new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameplay.movex = Mod.invertPadX *gameplay.mover.getKnobPercentX() *Mod.modCamSpeedX;
                gameplay.movey = Mod.invertPadY *gameplay.mover.getKnobPercentY() *Mod.modCamSpeedY;
                gameplay.touchPadTimmer = Mod.tPadAlphaDellay;
                gameplay.mover.addAction( Actions.alpha( 1 ) );
            }
        } );

        optIG.setWidget( optContinut );


        panel.addListener( turnButons );
        hud.addListener( hudButons );
        pauseIG.addListener( pauseButons );
        optIG.addListener( optButons );
        stage.addActor( panel );
        stage.addActor( hud );
        stage.addActor( optIG );
        stage.addActor( pauseIG );

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
