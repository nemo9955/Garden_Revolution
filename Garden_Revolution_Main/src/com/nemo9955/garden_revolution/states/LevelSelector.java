package com.nemo9955.garden_revolution.states;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.nemo9955.garden_revolution.Garden_Revolution;
import com.nemo9955.garden_revolution.utility.Assets;


public class LevelSelector implements Screen {

    private Stage            stage;
    private Skin             skin;
    private Table            table;

    public static FileHandle nivelLoc;
    public static boolean    internal = false;

    private FileHandle       lvlLoc;
    private String           toAcces;

    public LevelSelector() {
        skin = Garden_Revolution.manager.get( Assets.SKIN_JSON.path() );
        stage = new Stage();
        table = new Table( skin );
    }

    @Override
    public void show() {
        table.clear();
        stage.clear();

        if ( internal )
            nivelLoc = Gdx.files.internal( "harti/nivele" );
        else
            nivelLoc = Gdx.files.local( "harti/nivele" );

        if ( Gdx.app.getType() ==ApplicationType.Desktop &&internal )
            lvlLoc = Gdx.files.internal( "./bin/" +nivelLoc.path() );
        else if ( Gdx.app.getType() ==ApplicationType.Android &&!internal )
            lvlLoc = Gdx.files.internal( "./bin/" +nivelLoc.path() );
        else
            lvlLoc = nivelLoc;


        FileHandle nivele[] = lvlLoc.list();
        String harti[] = new String[nivele.length];

        for (int i = 0 ; i <harti.length ; i ++ )
            harti[i] = nivele[i].nameWithoutExtension();

        final TextButton play = new TextButton( "Play", skin );

        List elem = new List( harti, skin );
        SplitPane lista = new SplitPane( elem, table, false, skin );
        lista.setFillParent( true );
        lista.setMaxSplitAmount( 0.5f );
        lista.setMinSplitAmount( 0.3f );
        lista.setSplitAmount( 0.4f );

       // table.setFillParent( true );
        table.setHeight( stage.getHeight() );

        elem.addListener( new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toAcces = ( (List) actor ).getSelection();
            }
        } );


        toAcces = elem.getSelection();

        play.addListener( new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Garden_Revolution.game.setScreen( Garden_Revolution.gameplay.init( lvlLoc.child( toAcces +".xml" ) ) );
            }
        } );

        TextButton back = new TextButton( "Back", skin );
        back.addListener( new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Garden_Revolution.game.setScreen( Garden_Revolution.meniu );
            }
        } );

        // table.add( lista ).expand().left().fill();
        table.add( "Select a LEVEL" ).expand().top().row();
        table.add( play ).expand().row();
        table.add( back ).bottom().expand().right();

        stage.addActor( lista );

        Gdx.input.setInputProcessor( stage );
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        stage.act( delta );
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor( null );
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
