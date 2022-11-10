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

    protected String path;

    protected String line = "";

    protected boolean updateLoop = true;

    protected HashMap<String, String> cache;

    protected int idLenght = 9;
    protected int headerLenght = 87;

    public Server()
    {
        path = "";
        cache = new HashMap<String, String>();
    }

    public void Start()
    {
        try
        {
            // connection host --------------------------------------------------------------
            m_ServerHost = new ServerSocket(portHost);

            System.out.println("En attente d'une connection");
            m_SocketHost = m_ServerHost.accept();
            //System.out.println("Serveur connecter1");

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

    public void CmdReceive(String cmd)
    {
        System.out.println(cmd);
        if(cmd.charAt(idLenght) == '0')
        {
            String opCode = cmd.substring(idLenght + 1, idLenght + 5);

            switch (opCode)
            {
                case "0001":
                    //PUT cmd
                    Put(cmd);
                    //AddValueMasterFile(path, domainName, ip, port);
                    break;
                case "0000":
                    //GET cmd
                    Get(cmd);
                    //GetValueMasterFile(path, domainName);
                    break;
                default:
                    break;
            }
        }
        m_RequestHost.print("end\r\n");
        m_RequestHost.flush();
    }

    public void SetPortHost(int port)
    {
        portHost = port;
    }
    public void SetAddressConnect(String address)
    {
        this.addressConnect = address;
    }

    public void SetPortConnect(int port)
    {
        this.portConnect = port;
    }

    public void SetPathMasterFile(String path)
    {
        this.path = path;
    }

    public void Connect()
    {
        try
        {
            // Va ce connecter au server en gardant tout les ref -----------------------------------
            m_SocketConnect = new Socket(addressConnect, portConnect);
            //System.out.println("Serveur Connecter2");

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

    public void Disconnect()
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

    public String GetServerConnectResponse()
    {
        String response = "";
        try
        {
            String responseLine = "";
            while(!(responseLine = m_ResponseConnect.readLine()).equals("end"))
            {
                response += responseLine;
            }
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        return response;
    }

    public void AddValueMasterFile(String path, String domainName, String ip, String port)
    {
        try
        {
            // ouvre le file et le cree s'il n'existe pas -----------------------------------
            File file = new File(path);
            file.createNewFile();
            // ------------------------------------------------------------------------------

            //prend les donner existant dans le file -----------------------------------------
            Scanner reader = new Scanner(file);
            String fileData = "";
            while (reader.hasNextLine())
            {
                fileData += reader.nextLine() + '\n';
            }
            reader.close();
            // -------------------------------------------------------------------------------

            // Update du data dans le file ---------------------------------------------------
            String firstDomainName = "";
            for(int i = 0; i < domainName.length(); i++)
            {
                if(domainName.charAt(i) == '.')
                {
                    break;
                }
                firstDomainName += domainName.charAt(i);
            }


            fileData = '\t' + "NS\t" + domainName + '\n' +
                        fileData +
                        firstDomainName + '\t' + 'A' + '\t' + ip + '\t' + 'P' + '\t' + port + '\n';
            // -------------------------------------------------------------------------------

            // ecrit le data dans le file ----------------------------------------------------
            FileWriter writer = new FileWriter(path);
            writer.write(fileData);
            writer.close();
            // -------------------------------------------------------------------------------

            System.out.println(fileData);
        } catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public String GetValueMasterFile(String path, String domainName)
    {
        try
        {
            // ouvre le file ----------------------------------------------------------------
            File file = new File(path);

            // prend le premier nom de domaine pour la recherche ----------------------------
            String firstDomainName = "";
            for(int i = 0; i < domainName.length(); i++)
            {
                if(domainName.charAt(i) == '.')
                {
                    break;
                }
                firstDomainName += domainName.charAt(i);
            }
            // -------------------------------------------------------------------------------

            // prend les donner existant dans le file -----------------------------------------
            Scanner reader = new Scanner(file);
            String fileData = "";
            while (reader.hasNextLine())
            {
                fileData = reader.nextLine();
                if(fileData.charAt(0) != '\t' && fileData.contains(firstDomainName))
                {
                    break;
                }
            }
            reader.close();
            // -------------------------------------------------------------------------------

            int conter = 0;
            int i;
            for(i = 0; i < fileData.length(); i++)
            {
                if(fileData.charAt(i) == '\t')
                {
                    if(conter != 0)
                    {
                        break;
                    }
                    conter++;
                }
            }
            String value = 'A' + fileData.substring(i, fileData.length());
            return value;
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        return "";
    }

    public String GetDomainName(String cmd)
    {
        // GET le domain name dans la cmd ----------------------------------------------------------
        String domainName = "";
        String rangeDomainName = "";
        for(int i = headerLenght; i < headerLenght + 2; i++)
        {
            rangeDomainName += cmd.charAt(i);
        }

        for(int i = headerLenght + 2; i < headerLenght + 2 + Integer.parseInt(rangeDomainName); i++)
        {
            domainName += cmd.charAt(i);
        }
        // -----------------------------------------------------------------------------------------
        return domainName;
    }

    public String GetIp(String cmd)
    {
        String rangeDomainName = "";
        for(int i = headerLenght; i < headerLenght + 2; i++)
        {
            rangeDomainName += cmd.charAt(i);
        }

        // GET le IP dans la cmd ----------------------------------------------------------
        String ip = "";
        String rangeIp = "";
        int currIndex = headerLenght + 2 + Integer.parseInt(rangeDomainName);
        for(int i = currIndex; i < currIndex + 2; i++)
        {
            rangeIp += cmd.charAt(i);
        }

        for(int i = currIndex + 2; i < currIndex + 2 + Integer.parseInt(rangeIp); i++)
        {
            ip += cmd.charAt(i);
        }
        // -----------------------------------------------------------------------------------------
        return ip;
    }

    public String GetPort(String cmd)
    {
        String rangeDomainName = "";
        for(int i = headerLenght; i < headerLenght + 2; i++)
        {
            rangeDomainName += cmd.charAt(i);
        }

        String rangeIp = "";
        int currIndex = headerLenght + 2 + Integer.parseInt(rangeDomainName);
        for(int i = currIndex; i < currIndex + 2; i++)
        {
            rangeIp += cmd.charAt(i);
        }

        // GET le PORT dans la cmd ----------------------------------------------------------
        String port = "";
        String rangePort = "";
        currIndex = currIndex + 2 + Integer.parseInt(rangeIp);
        for(int i = currIndex; i < currIndex + 2; i++)
        {
            rangePort += cmd.charAt(i);
        }

        for(int i = currIndex + 2; i < currIndex + 2 + Integer.parseInt(rangePort); i++)
        {
            port += cmd.charAt(i);
        }
        // -----------------------------------------------------------------------------------------
        return port;
    }

    public String SetOpCode(String cmd, String opCode)
    {
        cmd = cmd.substring(0, idLenght + 1) + opCode + cmd.substring(idLenght + 5);
        return cmd;
    }

    public int GetFirstIndexResponse(String cmd)
    {
        String rangeDomainName = "";
        for(int i = headerLenght; i < headerLenght + 2; i++)
        {
            rangeDomainName += cmd.charAt(i);
        }

        String opCode = cmd.substring(idLenght + 1, idLenght + 5);

        String rangeIp = "";
        String rangePort = "";

        if(opCode.equals("0001"))
        {
            int currIndex = headerLenght + 2 + Integer.parseInt(rangeDomainName);
            for(int i = currIndex; i < currIndex + 2; i++)
            {
                rangeIp += cmd.charAt(i);
            }

            currIndex = currIndex + 2 + Integer.parseInt(rangeIp);
            for(int i = currIndex; i < currIndex + 2; i++)
            {
                rangePort += cmd.charAt(i);
            }
        }

        int index = headerLenght + 2 + Integer.parseInt(rangeDomainName) + 32;
        if(opCode.equals("0001"))
        {
            index = index + 4 + Integer.parseInt(rangeIp) + Integer.parseInt(rangePort);
        }

        System.out.println(cmd.charAt(index - 1));

        return 0;
    }

    public void SetUpdateLoop(boolean value)
    {
        updateLoop = value;
    }

    public abstract void Get(String cmd);

    public abstract void Put(String cmd);
}