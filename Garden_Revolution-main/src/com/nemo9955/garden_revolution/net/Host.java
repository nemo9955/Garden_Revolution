package com.nemo9955.garden_revolution.net;

import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.nemo9955.garden_revolution.net.Packets.MapOfServer;
import com.nemo9955.garden_revolution.states.Gameplay;
import com.nemo9955.garden_revolution.utility.Functions;
import com.nemo9955.garden_revolution.utility.Vars;


public class Host extends Listener {

    public Server    server;

    private Gameplay gp;

    public Host(Gameplay gp) {
        this.gp = gp;
        server = new Server();
        server.start();
        try {
            server.bind( Vars.TCPport, Vars.UDPport );
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println( "[H]Connection failed" );
        }
        server.addListener( this );

        Functions.setSerializedClasses( server.getKryo() );
    }

    public void brodcast(String mesaj) {
        server.sendToAllTCP( mesaj );
    }

    @Override
    public void connected(Connection connection) {
        gp.showMessage( "Someone connected" );
    }

    @Override
    public void received(Connection connection, Object object) {
        if ( object instanceof String ) {
            gp.showMessage( "[H] : " +object.toString() );
        }
        else if ( object instanceof MapOfServer ) {
            FileHandle theMap = new FileHandle( gp.mapLoc.path() );
            gp.showMessage( "[S] sending map to client " );
            connection.sendTCP( theMap );
        }
    }

    public void stopHost() {
        server.stop();
    }

}
