public class Resolver  extends Server
{

    @Override
    public void Get(String cmd)
    {
        GetFirstIndexResponse(cmd);
        //GetAuthoritativeServer(cmd);
    }

    @Override
    public void Put(String cmd)
    {
        GetFirstIndexResponse(cmd);
        //String tempCmd = SetOpCode(cmd, "0000");

        //GetAuthoritativeServer(tempCmd);
    }

    public String GetAuthoritativeServer(String cmd)
    {
        // prend le id et le port du root server ---------------------------------
        String root = GetValueMasterFile(path, "root");

        String ipRoot = "";
        int i = 2;
        while(root.charAt(i) != '\t')
        {
            ipRoot += root.charAt(i++);
        }
        i += 3;
        String portRoot = "";
        for(; i < root.length(); i++)
        {
            portRoot += root.charAt(i);
        }
        // -----------------------------------------------------------------------

        // ce connect au root server et lui envoie la requete et recois la response --------------------
        SetAddressConnect(ipRoot);
        SetPortConnect(Integer.parseInt(portRoot));
        Connect();
        m_RequestConnect.print(cmd + "\r\n");
        m_RequestConnect.print("end\r\n");
        m_RequestConnect.flush();

        String responseRoot = GetServerConnectResponse();
        Disconnect();
        System.out.println(responseRoot);
        // ----------------------------------------------------------------------- --------------------

        return "";
    }
}
