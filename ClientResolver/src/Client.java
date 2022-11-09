import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client extends MyRunnable
{
    private ServerSocket m_ServerSocket = null;
    private Socket m_Socket = null;

    private InputStreamReader isr = null;
    private DataInputStream in = null;

    private DataOutputStream m_Out = null;
    private BufferedReader m_Response = null;
    private PrintWriter m_Request = null;

    String address = "";
    int port = 0;

    String responseLine = "";
    String cmd = "";

    public void Start()
    {
        try
        {
            // Connection au server --------------------------------------------------------
            m_Socket = new Socket(address, port);
            System.out.println("Client Connecter");

            in = new DataInputStream(System.in);

            isr = new InputStreamReader(this.m_Socket.getInputStream());
            m_Response = new BufferedReader(this.isr);
            m_Request = new PrintWriter(this.m_Socket.getOutputStream());
            // ----------------------------------------------------------------------------
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    public boolean Update()
    {
        try
        {
            // prend les input du client ----------------------------------------
            cmd = "";
            System.out.print("entrer le type de commande : ");
            cmd = in.readLine();
            //-------------------------------------------------------------------

            //envoie la commande au server connecter
            CmdSend(cmd);

            // affiche la reponse a la console ------------------------------------
            String responseLine = "";
            while(!(responseLine = m_Response.readLine()).equals("end")) {
                System.out.println(responseLine);
            }
            System.out.println();
            // --------------------------------------------------------------------

        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        return true;
    }

    public void End()
    {
        try
        {
            m_Socket.close();
            in.close();
            isr.close();
            m_Response.close();
            m_Request.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    //HEADER ------------------------------------------------------------------------------------------------------------------
    // 100000000, 999999999: id
    // 0, 1: Query, answer
    // 0000, 0001, 0010, 0011-1111: standard query, inverse query, server status request, reserved for future use = 0000
    // 0, 1: disponible dans les response et dis si c'est la response provient de l'authoritative name server
    // 0, 1: truncated? = 0
    // 0, 1: Recursion desired? = 0
    // 0, 1: Recursion Available? = 0
    // 0, 1: Reserved for future use = 0
    // 0, 1, 2, 3, 4, 5, 6-15: 4 bit 0000, No error, Format error, Server failure, Name error, Not implemented, Refused, Reserved for future use
    // 0000000000000000, 9999999999999999: integer specifying number entries in the question section
    // 0000000000000000, 9999999999999999: integer specifying number resource records in the answer section
    // 0000000000000000, 9999999999999999: integer specifying number name server resource records in the authority records section
    // 0000000000000000, 9999999999999999: integer specifying number resource records in the additional records section
    //QUESTION ---------------------------------------------------------------------------------------------------------------
    //QNAME: nom de domaine
    //QTYPE
    //QCLASS 00: 2 octet class of the query ex: IN for internet
    //RESPONSE ---------------------------------------------------------------------------------------------------------------
    //NAME: Domaine name
    //TYPE 00: 2 octets containing RR type code
    //CLASS 00: 2 octets specify class of the data in the RDATA field
    //TTL 00000000000000000000000000000000: 32 octets integer time interval resource record
    //RDLENGTH 0000000000000000: 16 bit integer, specifies the length in octets of the RDATA field
    //RDATA variable length of octets: describes the resource
    // -----------------------------------------------------------------------------------------------------------------------
    //  Master file format
    //  \t              NS\t  nomDeDomain
    //  nomDeDomain\t   A\t   10.10.0.23

    public void CmdSend(String cmd)
    {
        //Header -------------------------------------------------------
        m_Request.print("0\r\n");
        m_Request.print("0000\r\n");
        m_Request.print("0\r\n");
        m_Request.print("0\r\n");
        m_Request.print("0\r\n");
        m_Request.print("0\r\n");
        m_Request.print("0\r\n");
        m_Request.print("0000\r\n");

        //Question -----------------------------------------------------
        String nd = "";
        try
        {
            System.out.println("Entrer le nom de domaine: ");
            nd = in.readLine();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        m_Request.print(nd + "\r\n");

        if(cmd.contains("GET"))
        {
            m_Request.print("TXT\r\n");
        }
        else if(cmd.contains("PUT"))
        {
            String add = "";
            try
            {
                System.out.println("Entrer l'adresse: ");
                add = in.readLine();
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
            m_Request.print(add + "\r\n");
            m_Request.print("A");
        }

        m_Request.print("IN\r\n");

        // fin --------------------------------------------------------
        m_Request.print("end\r\n");
        m_Request.flush();
    }

    public void SetAddressPort(String address, int port)
    {
        this.address = address;
        this.port = port;
    }
}
