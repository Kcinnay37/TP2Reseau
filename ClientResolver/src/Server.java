import java.net.*;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

public abstract class Server extends MyRunnable
{
    //recoi et envoi les info
    protected Socket m_SocketHost = null;
    protected Socket m_SocketConnect = null;

    //le socket qui est ouvert qui permet douvrir la connection et permettre decouter si quelqun
    //veux ce connecter a la connection c'est aussi lui qui va accepter si quelqun demande de ce connecter
    protected ServerSocket m_ServerHost = null;
    protected DataInputStream in = null;
    protected DataOutputStream out = null;

    protected InputStreamReader isrHost = null;
    protected BufferedReader m_ResponseHost = null;
    protected PrintWriter m_RequestHost = null;;

    protected InputStreamReader isrConnect = null;
    protected BufferedReader m_ResponseConnect = null;
    protected PrintWriter m_RequestConnect = null;;

    protected int portHost = 0;
    protected int portConnect = 0;
    protected String addressConnect = "";

    protected String line = "";

    protected boolean updateLoop = true;

    protected HashMap<String, String> cache;

    public void Start()
    {
        try
        {
            // connection host --------------------------------------------------------------
            m_ServerHost = new ServerSocket(portHost);

            System.out.println("En attente d'une connection");
            m_SocketHost = m_ServerHost.accept();
            System.out.println("Serveur connecter1");

            isrHost = new InputStreamReader(m_SocketHost.getInputStream());
            m_ResponseHost = new BufferedReader(isrHost);
            m_RequestHost = new PrintWriter(m_SocketHost.getOutputStream());
            // -----------------------------------------------------------------------------
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public boolean Update()
    {
        try
        {
            // prend la commande qu'il recois -------------------------------------------
            line = "";
            String currLine = "";
            while(!(currLine = m_ResponseHost.readLine()).equals("end"))
            {
                line += currLine;
            }
            // -------------------------------------------------------------------------

            //va traiter la commande
            line += '\n';

            Random rand = new Random();
            int id = rand.nextInt(100000000, 999999999);
            line = String.valueOf(id) + line;

            CmdReceive(line);
        }
        catch (IOException i)
        {
            System.out.println(i);
        }
        return updateLoop;
    }

    public void End()
    {
        try
        {
            isrHost.close();
            m_RequestHost.close();
            m_ResponseHost.close();
            m_SocketHost.close();
            m_ServerHost.close();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public abstract void CmdReceive(String cmd);

    public void SetPortHost(int port)
    {
        portHost = port;
    }
    public void SetAddressConnect(String address)
    {
        this.addressConnect = address;
    }

    public void Connect()
    {
        try
        {
            // Va ce connecter au server en gardant tout les ref -----------------------------------
            m_SocketConnect = new Socket(addressConnect, portConnect);
            System.out.println("Serveur Connecter2");

            isrConnect = new InputStreamReader(m_SocketConnect.getInputStream());
            m_ResponseConnect = new BufferedReader(isrConnect);
            m_RequestConnect = new PrintWriter(m_SocketConnect.getOutputStream(), true);
            // -------------------------------------------------------------------------------------
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void disconnect()
    {
        try
        {
            isrConnect.close();
            m_RequestConnect.close();
            m_ResponseConnect.close();
            m_SocketConnect.close();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }
}