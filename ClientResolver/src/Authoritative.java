public class Authoritative  extends Server
{

    @Override
    public void Get(String cmd, String path, String domainName)
    {

    }

    @Override
    public void Put(String cmd, String path, String domainName, String ip, String port)
    {
        AddValueMasterFile(path, domainName, ip, port);
    }
}
