public class Root  extends Server
{

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

        // prend la reponse et lenvoie -----------------------------------------------
        String value = GetValueMasterFile(path, lastDomain);

        cmd = cmd.substring(0, idLenght) + 1 + cmd.substring(idLenght + 1);

        m_RequestHost.print(cmd + "\r\n");

        //longeur du domaine name
        String rangeDn = String.valueOf(domainName.length());
        if(rangeDn.length() == 1)
        {
            rangeDn = '0' + rangeDn;
        }

        m_RequestHost.print(rangeDn + "\r\n");
        m_RequestHost.print(domainName + "\r\n");

        String QType = cmd.substring(cmd.length() - 33, cmd.length() - 17);

        m_RequestHost.print(QType + "\r\n");
        m_RequestHost.print("0000000000000001\r\n");
        m_RequestHost.print("00000000000000000000000000000000\r\n");

        String rdLength = String.valueOf(value.length());
        while (rdLength.length() < 16)
        {
            rdLength = '0' + rdLength;
        }
        m_RequestHost.print(rdLength + "\r\n");
        m_RequestHost.print(value + "\r\n");
        m_RequestHost.print("end\r\n");
        m_RequestHost.flush();
        // --------------------------------------------------------------------------
    }

    @Override
    public void Put(String cmd)
    {

    }
}
