package main.java;

import com.fv.sdp.model.Match;
import com.fv.sdp.model.Player;
import main.java.rest.RESTConfig;
import main.java.rest.RESTConnector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by filip on 24/05/2017.
 */
public class ClientCore
{
    //private PlayerProfile playerProfile;

    public static void main(String[] args)
    {
        //RESTConnector restConnector = new RESTConnector(new RESTConfig());
        PlayerProfile playerProfile = new PlayerProfile();
    }
}

class PlayerProfile
{
    private Player playerInfo;
    private Match matchInfo;

    public PlayerProfile()
    {
        playerInfo = new Player();
        matchInfo = new Match();

        //nickname
        setPlayerNickname();
        //match
        setMatch();
    }
    public PlayerProfile(String nickname)
    {
        playerInfo = new Player();
        matchInfo = new Match();

        //nickname
        playerInfo.setId(nickname);
        //match
        setMatch();
    }

    //insert nickname
    private void setPlayerNickname()
    {
        System.out.println("Set your nickname: ");
        Scanner consoleReader = new Scanner(System.in);
        String nick = consoleReader.nextLine();

        playerInfo.setId(nick);
        System.out.println("Saved");
    }

    //choose match
    private void setMatch()
    {
        //get matches list
        RESTConnector connector = new RESTConnector(new RESTConfig());
        ArrayList<Match> matchesList = connector.requestMatchesList();
        //display list
        System.out.println("Available matches");
        for(Match m : matchesList)
        {
            System.out.println(String.format("#- %s [size: %d, points: %d, players: %d]", m.getId(), m.getEdgeLength(), m.getVictoryPoints(), m.getPlayers()));
        }
        //choose match
        System.out.println("Choose a match by name");
        Scanner consoleReader = new Scanner(System.in);
        String choosenMatchId = consoleReader.nextLine();

        //set match
        matchInfo.setId(choosenMatchId);
        System.out.println("Saved");
    }
}

