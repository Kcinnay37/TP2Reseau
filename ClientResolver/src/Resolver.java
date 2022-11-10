public class Resolver  extends Server
{

    @Override
    public void Get(String cmd, String path, String domainName)
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
        System.out.println(ipRoot);
        System.out.println(portRoot);
    }

    @Override
    public void Put(String cmd, String path, String domainName, String ip, String port)
    {

    }
}
