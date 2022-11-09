import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] argv)
    {
        //cree tout les servers et client -----------------------------------------
        Client client = new Client();
        client.SetAddressPort("192.168.2.26", 500);

        Server dnsResolver = new Resolver();
        dnsResolver.SetPortHost(500);

        Server rootServer = new Root();
        rootServer.SetPortHost(501);

        Server tldServer = new TLD();
        tldServer.SetPortHost(502);

        Server authoritativeNameServer = new Autoritative();
        authoritativeNameServer.SetPortHost(503);
        //--------------------------------------------------------------

        //les associ a des thread -----------------------------------------------------------------------------
        Thread threadClient = new Thread(client, "threadClient");
        Thread threadResolver = new Thread(dnsResolver, "threadResolver");
        Thread threadRoot = new Thread(rootServer, "threadRoot");
        Thread threadTld = new Thread(tldServer, "threadTld");
        Thread threadAuthoritativeName = new Thread(authoritativeNameServer, "threadAuthoritativeName");
        // ----------------------------------------------------------------------------------------------------

        // Lance tout les thread ------------------------------------------------------------------------------
        threadClient.start();
        threadResolver.start();
        threadRoot.start();
        threadTld.start();
        threadAuthoritativeName.start();
        //-----------------------------------------------------------------------------------------------------
    }
}