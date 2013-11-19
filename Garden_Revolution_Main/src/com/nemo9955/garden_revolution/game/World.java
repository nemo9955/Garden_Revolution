package com.nemo9955.garden_revolution.game;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.nemo9955.garden_revolution.Garden_Revolution;
import com.nemo9955.garden_revolution.game.entitati.Inamici;
import com.nemo9955.garden_revolution.game.mediu.Turn;
import com.nemo9955.garden_revolution.utility.IndexedObject;


public class World extends GestureAdapter implements Disposable {

    private Array<Disposable>               toDispose  = new Array<Disposable>( false, 1 );
    public GestureDetector                  gestures;

    private Array<ModelInstance>            nori       = new Array<ModelInstance>( false, 10 );
    public Array<ModelInstance>             mediu      = new Array<ModelInstance>( false, 10 );

    public Array<Inamic>                    foe        = new Array<Inamic>( false, 10 );
    public Array<Aliat>                     ally       = new Array<Aliat>( false, 10 );
    public Array<Shot>                      shot       = new Array<Shot>( false, 10 );

    public Array<BoundingBox>               colide     = new Array<BoundingBox>( false, 10 );
    public Array<CatmullRomSpline<Vector3>> paths;
    private int                             viata;


    private PerspectiveCamera               cam;
    private Vector3                         tmp        = new Vector3();
    public boolean                          overview   = false;
    private int                             curentTurn = -1;

    private Turn[]                          turnuri;
    public int                              curentCam  = 0;

    public Waves                            waves;

    public World(FileHandle location, PerspectiveCamera cam) {
        this.cam = cam;
        gestures = new GestureDetector( this );
        gestures.setLongPressSeconds( 1f );
        makeNori();
        populateWorld( location );
        readData( location );
    }

    public void update(float delta) {

        if ( waves.finishedWaves() )
            waves.update( delta );// TODO

        for (Inamic fo : foe ) {
            fo.update( delta );
            if ( fo.dead ) {
                inamicPool.free( fo );
                foe.removeValue( fo, false );
            }
        }
        for (Aliat al : ally ) {
            al.update( delta );
            if ( al.dead ) {
                aliatPool.free( al );
                ally.removeValue( al, false );
            }
        }
        for (Shot sh : shot ) {
            sh.update( delta );
            if ( sh.dead ) {
                shotPool.free( sh );
                shot.removeValue( sh, false );
            }
            for (Entitate fo : foe ) {
                if ( fo.box.intersects( sh.box ) ) {
                    fo.damage( sh );
                    sh.dead = true;
                }
            }
            for (BoundingBox col : colide ) {
                if ( col.intersects( sh.box ) )
                    sh.dead = true;
            }
        }

    }

    public void render(ModelBatch modelBatch, Environment light, Shader shader) {
        for (ModelInstance nor : nori )
            modelBatch.render( nor, shader );
        for (ModelInstance e : mediu )
            modelBatch.render( e, light );

        for (Entitate e : foe )
            e.render( modelBatch, light );
        for (Entitate e : ally )
            e.render( modelBatch, light );
        for (Entitate e : shot )
            e.render( modelBatch );
        for (Turn turn : turnuri )
            turn.render( modelBatch, light );
    }

    public void renderLines(ImmediateModeRenderer20 renderer) {
        Vector3 corn[] = new Vector3[8];

        for (BoundingBox box : colide ) {
            corn = box.getCorners();
            for (Vector3 crn : corn ) {
                renderer.color( 1, 0.5f, 0, 1 );
                renderer.vertex( crn.x, crn.y, crn.z );
            }
        }
        for (Turn turn : turnuri ) {
            for (BoundingBox box : turn.coliders ) {
                corn = box.getCorners();
                for (Vector3 crn : corn ) {
                    renderer.color( 0.3f, 0.9f, 0.2f, 1 );
                    renderer.vertex( crn.x, crn.y, crn.z );
                }
            }
        }

        for (Turn turn : turnuri ) {
            corn = turn.baza.getCorners();
            for (Vector3 crn : corn ) {
                renderer.color( 0.5f, 0, 0.5f, 1 );
                renderer.vertex( crn.x, crn.y, crn.z );
            }
        }

        for (Entitate e : foe ) {
            corn = e.box.getCorners();
            for (Vector3 crn : corn ) {
                renderer.color( 1, 0, 0, 1 );
                renderer.vertex( crn.x, crn.y, crn.z );
            }
        }

        for (Entitate e : ally ) {
            corn = e.box.getCorners();
            for (Vector3 crn : corn ) {
                renderer.color( 0, 0, 1, 1 );
                renderer.vertex( crn.x, crn.y, crn.z );
            }
        }

        for (Entitate e : shot ) {
            corn = e.box.getCorners();
            for (Vector3 crn : corn ) {
                renderer.color( 0, 1, 1, 1 );
                renderer.vertex( crn.x, crn.y, crn.z );
            }
        }
        int pts = paths.size;
        for (int i = 0 ; i <pts ; i ++ ) {
            float val = 0;
            while ( val <=1f ) {
                paths.get( i ).valueAt( tmp, val );
                renderer.color( i +3 /pts, i +1 /pts, i +2 /pts, 1f );
                renderer.vertex( tmp.x, tmp.y, tmp.z );
                val += 1f /100f;
            }
        }

    }

    private void makeNori() {
        nori.clear();
        ModelBuilder build = new ModelBuilder();
        Model sfera = build.createSphere( 5, 5, 5, 12, 12, new Material( ColorAttribute.createDiffuse( Color.WHITE ) ), Usage.Position |Usage.Normal |Usage.TextureCoordinates );
        ModelInstance nor;
        toDispose.add( sfera );

        int norx, norz;

        for (int i = 0 ; i <20 ; i ++ ) {
            norx = MathUtils.random( -100, 100 );
            norz = MathUtils.random( -100, 100 );
            for (int j = 1 ; j <=5 ; j ++ ) {
                nor = new ModelInstance( sfera );
                nor.transform.translate( norx +MathUtils.random( 0f, 7f ), 50, norz +MathUtils.random( 0f, 7f ) );
                nori.add( nor );
            }
        }
    }

    private void populateWorld(FileHandle location) {
        Array<Array<IndexedObject<Vector3>>> cp = null;
        String[] sect;

        Element map = null;
        try {
            map = new XmlReader().parse( location );
            turnuri = new Turn[map.getInt( "turnuri" )];
            paths = new Array<CatmullRomSpline<Vector3>>( map.getInt( "drumuri" ) );
            cp = new Array<Array<IndexedObject<Vector3>>>( 1 );

            for (int k = 0 ; k <map.getInt( "drumuri" ) ; k ++ )
                cp.add( new Array<IndexedObject<Vector3>>( false, 1, IndexedObject.class ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Model scena = new G3dModelLoader( new UBJsonReader() ).loadModel( location.parent().parent().child( "maps" ).child( map.get( "map" ) ) );

        toDispose.add( scena );

        for (int i = 0 ; i <scena.nodes.size ; i ++ ) {
            String id = scena.nodes.get( i ).id;
            ModelInstance instance = new ModelInstance( scena, id );
            // Node node = instance.getNode( id );
            // instance.transform.set( node.globalTransform );
            // node.translation.set( 0, 0, 0 );
            // node.scale.set( 1, 1, 1 );
            // node.rotation.idt();
            // instance.calculateTransforms();

            if ( id.startsWith( "turn" ) ) {
                sect = id.split( "_" );
                turnuri[Integer.parseInt( sect[1] ) -1] = new Turn( instance, scena.nodes.get( i ).translation );
            }
            else if ( id.startsWith( "path" ) ) {
                sect = id.split( "_" );
                int pat = Integer.parseInt( sect[1] ) -1;
                int pct = Integer.parseInt( sect[2] );
                cp.get( pat ).add( new IndexedObject<Vector3>( scena.nodes.get( i ).translation, pct ) );
            }
            else if ( id.startsWith( "colider" ) ) {
                BoundingBox box = new BoundingBox();
                instance.calculateBoundingBox( box );
                colide.add( box );
            }
            else if ( id.endsWith( "solid" ) ) {
                BoundingBox box = new BoundingBox();
                instance.calculateBoundingBox( box );
                colide.add( box );
                addMediu( instance );
            }
            else if ( id.startsWith( "camera" ) ) {
                cam.position.set( scena.nodes.get( i ).translation );
                cam.lookAt( Vector3.Zero );
                cam.update();
                overview = true;
            }
            else {
                addMediu( instance );
            }

        }

        for (Array<IndexedObject<Vector3>> pat : cp )
            pat.sort();

        for (Array<IndexedObject<Vector3>> pat : cp ) {

            Vector3 cps[] = new Vector3[pat.size +2];
            for (int j = 0 ; j <pat.size ; j ++ )
                cps[j +1] = pat.get( j ).object;
            cps[0] = cps[1];
            cps[cps.length -1] = cps[cps.length -2];

            paths.add( new CatmullRomSpline<Vector3>( cps, false ) );
        }

        if ( !overview &&turnuri.length !=0 )
            setCamera( 0 );

    }

    private void readData(FileHandle location) {

        Element map = null;
        try {
            map = new XmlReader().parse( location );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        waves = new Waves( this );

        setViata( map.getInt( "viata", 100 ) );

        Array<Element> tmpWaves = map.getChildrenByName( "wave" );
        tmpWaves.shrink();
        Array<IndexedObject<Element>> sortedWaves = new Array<IndexedObject<Element>>( tmpWaves.size );
        for (int i = 0 ; i <tmpWaves.size ; i ++ )
            sortedWaves.add( new IndexedObject<Element>( tmpWaves.get( i ), tmpWaves.get( i ).getInt( "index" ) -1 ) );
        sortedWaves.sort();
        tmpWaves.clear();

        for (IndexedObject<Element> wav : sortedWaves ) {
            waves.addWave( wav.object.getFloat( "delay", 5 ), wav.object.getFloat( "interval", 0.5f ) );
            Array<Element> tmpPaths = wav.object.getChildrenByName( "path" );

            for (Element pat : tmpPaths ) {

                int numar = pat.getInt( "nr" );
                for (int i = 0 ; i <pat.getChildCount() ; i ++ ) {

                    Element monstru = pat.getChild( i );
                    waves.populate( numar -1, Inamici.valueOf( monstru.getName().toUpperCase() ), monstru.getInt( "amount" ) );

                }
            }
        }
        sortedWaves.clear();
    }


    public int getViata() {
        return viata;
    }


    public void setViata(int viata) {
        this.viata = viata;
        Garden_Revolution.gameplay.viataTurn.setText( "Life " +viata );
    }

    public void addViata(int amount) {
        this.viata += amount;
        Garden_Revolution.gameplay.viataTurn.setText( "Life " +viata );
    }

    public void setCamera(int nr) {

        nr = MathUtils.clamp( nr, 0, turnuri.length -1 );
        Ray ray = cam.getPickRay( Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() /2 );
        float distance = -ray.origin.y /ray.direction.y;
        Vector3 look = ray.getEndPoint( new Vector3(), distance );

        cam.position.set( turnuri[nr].place );// TODO
        curentTurn = nr;

        cam.lookAt( look );

        cam.up.set( Vector3.Y );
        curentCam = nr;
        overview = false;
        cam.update();
    }

    public Turn getCurrentTower() {
        if ( turnuri.length ==0 )
            return null;
        return turnuri[curentTurn];
    }

    public void nextCamera() {
        curentCam ++;
        if ( curentCam >=turnuri.length )
            curentCam = 0;
        setCamera( curentCam );
    }

    public void prevCamera() {
        curentCam --;
        if ( curentCam <0 )
            curentCam = turnuri.length -1;
        setCamera( curentCam );
    }

    public CatmullRomSpline<Vector3> closestPath(final Vector3 location) {
        CatmullRomSpline<Vector3> closest = null;
        float dist = Float.MAX_VALUE;
        for (CatmullRomSpline<Vector3> path : paths ) {
            tmp = path.controlPoints[0].cpy();
            if ( location.dst2( tmp ) <dist ) {
                dist = location.dst2( tmp );
                closest = path;
            }
        }
        return closest;
    }

    public Inamic addFoe(Inamici type, CatmullRomSpline<Vector3> path, float x, float y, float z) {
        Inamic inamicTemp = inamicPool.obtain().create( path, type, x, y, z );
        foe.add( inamicTemp );
        return inamicTemp;
    }

    public Inamic addFoe(Inamici type, float x, float y, float z) {
        Inamic inamicTemp = inamicPool.obtain().create( closestPath( tmp.set( x, y, z ) ), type, x, y, z );
        foe.add( inamicTemp );
        return inamicTemp;
    }

    public Aliat addAlly(float x, float y, float z) {
        Aliat aliatTemp = aliatPool.obtain().create( x, y, z );
        ally.add( aliatTemp );
        return aliatTemp;
    }

    public Shot addShot(Vector3 position, Vector3 direction) {
        Shot shotTemp = shotPool.obtain().create( position, direction );
        shot.add( shotTemp );
        return shotTemp;
    }

    private void addMediu(ModelInstance med) {
        mediu.add( med );
    }

    private Pool<Inamic> inamicPool = new Pool<Inamic>() {

                                        @Override
                                        protected Inamic newObject() {
                                            return new Inamic( World.this );
                                        }
                                    };

    private Pool<Aliat>  aliatPool  = new Pool<Aliat>() {

                                        @Override
                                        protected Aliat newObject() {
                                            return new Aliat( World.this );
                                        }
                                    };

    private Pool<Shot>   shotPool   = new Pool<Shot>() {

                                        @Override
                                        protected Shot newObject() {
                                            return new Shot( World.this );
                                        }
                                    };


    @Override
    public boolean tap(float x, float y, int count, int button) {
        if ( !overview ) {
            Ray ray = cam.getPickRay( x, y );
            addShot( ray.origin, ray.direction );
        }
        else {
            Ray ray = cam.getPickRay( x, y );
            float distance = -ray.origin.y /ray.direction.y;
            Vector3 poz = ray.getEndPoint( new Vector3(), distance );
            for (int i = 0 ; i <turnuri.length ; i ++ )
                if ( turnuri[i].baza.contains( poz ) )
                    setCamera( i );
        }

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {

        if ( !overview &&Math.abs( Gdx.input.getX() -x ) <20 &&Math.abs( Gdx.input.getY() -y ) <20 ) {
            Ray ray = cam.getPickRay( x, y );
            float distance = -ray.origin.y /ray.direction.y;
            Vector3 poz = ray.getEndPoint( new Vector3(), distance );
            if ( Gdx.input.isButtonPressed( Buttons.RIGHT ) )
                addAlly( poz.x, poz.y, poz.z );
            else if ( Gdx.input.isButtonPressed( Buttons.MIDDLE ) )
                addFoe( Inamici.ROSIE, poz.x, poz.y, poz.z );
            else
                addFoe( Inamici.MORCOV, poz.x, poz.y, poz.z );
            gestures.invalidateTapSquare();
        }
        return false;
    }


    @Override
    public void dispose() {

        for (Disposable dis : toDispose )
            dis.dispose();

        toDispose.clear();
        foe.clear();
        ally.clear();
        shot.clear();
        mediu.clear();
        nori.clear();

    }

}
