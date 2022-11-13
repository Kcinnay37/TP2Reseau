public class Resolver  extends Server
{
    public Resolver(Cache cache)
    {
        super(cache);
    }

    @Override
    public void Get(String cmd) {
        String domaineName = GetDomainName(cmd);
        String getInCache = cache.GetInCache(domaineName);

        if (getInCache.equals(""))
        {
            String response = GetAuthoritativeServer(cmd);
            m_RequestHost.print(response + "\r\n");
            cache.PutInCache(domaineName, response, 120);
        }
        else
        {
            System.out.println("Get in cache resolver");
            m_RequestHost.print(getInCache + "\r\n");
        }
    }

    @Override
    public void Put(String cmd)
    {
        String response = GetAuthoritativeServer(cmd);
        m_RequestHost.print(response + "\r\n");
    }

    public String GetAuthoritativeServer(String cmd)
    {
        System.out.println("Root");
        String rootResponse = GetServerResponse(cmd, GetValueMasterFile(path, "root"));
        String rootValue = GetServerResponseInfo(rootResponse);
        System.out.println("Response: " + rootResponse);

        System.out.println();

        System.out.println("TLD");
        String TLDResponse = GetServerResponse(cmd, rootValue);
        String TLDValue = GetServerResponseInfo(TLDResponse);
        System.out.println("Response: " + TLDResponse);

        System.out.println();

        System.out.println("Authoritative");
        String authoritativeResponse = GetServerResponse(cmd, TLDValue);
        System.out.println("Response: " + authoritativeResponse);
        System.out.println();
        return authoritativeResponse ;
    }

    public String GetServerResponse(String cmd, String masterFileValue)
    {
        // prend le id et le port du server -------------------------------------------------------
        String ipRoot = "";
        int i = 2;
        while(masterFileValue.charAt(i) != '\t')
        {
            ipRoot += masterFileValue.charAt(i++);
        }
        i += 3;
        String portRoot = "";
        for(; i < masterFileValue.length(); i++)
        {
            portRoot += masterFileValue.charAt(i);
        }
        // ---------------------------------------------------------------------------------------------

        // ce connect au root server et lui envoie la requete et recois la response --------------------
        SetAddressConnect(ipRoot);
        SetPortConnect(Integer.parseInt(portRoot));
        Connect();
        m_RequestConnect.print(cmd + "\r\n");
        m_RequestConnect.print("end\r\n");
        m_RequestConnect.flush();

        String responseServer = GetServerConnectResponse();
        Disconnect();
        // --------------------------------------------------------------------------------------------
        return responseServer;
    }

    public String GetServerResponseInfo(String responseServer)
    {
        // Va chercher le RDATA dans la reponse et le retourne ----------------------------------------
        int indexResponse = GetFirstIndexResponse(responseServer);
        String rangeDomainName = "";
        for(int index = indexResponse; index <= indexResponse + 1; index++)
        {
            rangeDomainName += responseServer.charAt(index);
        }

        int indexValueStart = indexResponse + Integer.parseInt(rangeDomainName) + 2 + 16 + 16 + 32;

        String rangeValue = "";
        for(int index = indexValueStart; index < indexValueStart + 16; index++)
        {
            rangeValue += responseServer.charAt(index);
        }
        indexValueStart += 16;

        String value = "";
        for(int index = indexValueStart; index < indexValueStart + Integer.parseInt(rangeValue); index++)
        {
            value += responseServer.charAt(index);
        }

        return value;
        // --------------------------------------------------------------------------------------------
    }
}
