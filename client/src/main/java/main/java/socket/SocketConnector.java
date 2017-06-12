package main.java.socket;

import com.google.gson.Gson;
import main.java.SessionConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by filip on 01/06/2017.
 */
public class SocketConnector
{
    private ServerSocket listeningServer;
    private final List<ISocketObserver> observersList;

    public SocketConnector(List<ISocketObserver> observers)
    {
        System.out.println("Initializing " + this.getClass().getCanonicalName());

        observersList = observers;
        try
        {
            //create listener
            listeningServer = new ServerSocket(0);
            //update session config
            SessionConfig.getInstance().LISTENER_ADDR = listeningServer.getInetAddress().getHostAddress();
            SessionConfig.getInstance().LISTENER_PORT = listeningServer.getLocalPort();

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //Input Side
    public boolean startListener() //syncronous op.
    {
        //wait on client
        System.out.println(String.format("Listening on %s:%d", getListenerAddress(), getListenerPort()));
        while (true)
        {
            try {
                Socket client = listeningServer.accept();
                //set up listener
                SocketListenerRunner runner = new SocketListenerRunner(client, observersList);
                //create thread
                Thread listenerThread = new Thread(runner);
                //start thread
                listenerThread.start();

            }catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }
    }

    //Output Side
    public boolean sendMessage(RingMessage message, List<InetAddress> destinations) //TODO implementare invio messaggi socket
    {
        return false;
    }

    public int getListenerPort()
    {
        return listeningServer.getLocalPort();
    }
    public InetAddress getListenerAddress()
    {
        return listeningServer.getInetAddress();
    }
}


class SocketListenerRunner implements Runnable
{
    private final List<ISocketObserver> observersList; //not concurrent access
    private final Socket client;

    public SocketListenerRunner(Socket client, List<ISocketObserver> observers) //set up listening thread
    {
        this.client = client;
        observersList = observers;
    }

    @Override
    public void run()
    {
        try
        {
            runListener(); //listening loop
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void runListener() throws Exception
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

        while (!client.isClosed())
        {
            //read input message
            String input = reader.readLine();

            //json parsing
            RingMessage message = new Gson().fromJson(input, RingMessage.class);

            //dispatch message to observers
            for (ISocketObserver obs : observersList)
                obs.pushMessage(message);
        }

        //dispose reader
        reader.close();
    }
}
