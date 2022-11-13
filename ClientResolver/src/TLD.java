public class TLD  extends Server
{
    public TLD(Cache cache)
    {
        super(cache);
    }

    @Override
    public void Get(String cmd)
    {
        // prend la derniere parti dans le nom de domaine -----------------------------
        String domainName = GetDomainName(cmd);
        String lastDomain = "";
        for(int i = 0; i < domainName.length(); i++)
        {
            if(domainName.charAt(i) == '.')
            {
                lastDomain = "";
                continue;
            }
            lastDomain += domainName.charAt(i);
        }
        // ---------------------------------------------------------------------------

        String getInCache = cache.GetInCache(lastDomain);

        if (getInCache.equals(""))
        {
            // prend la reponse et lenvoie -----------------------------------------------
            String value = GetValueMasterFile(path, lastDomain);

            cmd = cmd.substring(0, idLenght) + 1 + cmd.substring(idLenght + 1);

            String response = "";

            response += cmd.substring(0, cmd.length() - 1);

            //longeur du domaine name
            String rangeDn = String.valueOf(domainName.length());
            if(rangeDn.length() == 1)
            {
                rangeDn = '0' + rangeDn;
            }

            response += rangeDn;
            response += domainName;

            String QType = cmd.substring(cmd.length() - 33, cmd.length() - 17);

            response += QType;
            response += "0000000000000001";
            response += "00000000000000000000000000000000";

            String rdLength = String.valueOf(value.length());
            while (rdLength.length() < 16)
            {
                rdLength = '0' + rdLength;
            }

            response += rdLength;
            response += value;

            m_RequestHost.print(response + "\r\n");
            cache.PutInCache(lastDomain, response, 30);
            // --------------------------------------------------------------------------
        }
        else
        {
            System.out.println("Get in cache TLD");
            m_RequestHost.print(getInCache + "\r\n");
        }

        m_RequestHost.print("end\r\n");
        m_RequestHost.flush();
    }

    @Override
    public void Put(String cmd)
    {
        Get(cmd);
    }
}
