public class Authoritative  extends Server
{

    @Override
    public void Get(String cmd)
    {
        System.out.println("chui dans le Authoritative");
    }

    @Override
    public void Put(String cmd)
    {
        //AddValueMasterFile(path, domainName, ip, port);
    }
}
