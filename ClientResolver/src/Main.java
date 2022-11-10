import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] argv)
    {
        //cree tout les servers et client -----------------------------------------
        Client client = new Client();
        client.SetAddressPort("192.168.0.186", 53);

        Server dnsResolver = new Resolver();
        dnsResolver.SetPortHost(53);
        dnsResolver.SetPathMasterFile("MasterFile\\Resolver.txt");

        Server rootServer = new Root();
        rootServer.SetPortHost(54);
        rootServer.SetPathMasterFile("MasterFile\\Root.txt");
        rootServer.SetUpdateLoop(false);

        Server tldServer_Com = new TLD();
        tldServer_Com.SetPortHost(55);
        tldServer_Com.SetPathMasterFile("MasterFile\\TLD_Com.txt");
        tldServer_Com.SetUpdateLoop(false);

        Server tldServer_Ca = new TLD();
        tldServer_Ca.SetPortHost(56);
        tldServer_Ca.SetPathMasterFile("MasterFile\\TLD_Ca.txt");
        tldServer_Ca.SetUpdateLoop(false);

        Server authoritativeNameServer_Com = new Authoritative();
        authoritativeNameServer_Com.SetPortHost(57);
        authoritativeNameServer_Com.SetPathMasterFile("MasterFile\\Authoritative_Com.txt");
        authoritativeNameServer_Com.SetUpdateLoop(false);

        Server authoritativeNameServer_Ca = new Authoritative();
        authoritativeNameServer_Ca.SetPortHost(58);
        authoritativeNameServer_Ca.SetPathMasterFile("MasterFile\\Authoritative_Ca.txt");
        authoritativeNameServer_Ca.SetUpdateLoop(false);
        //--------------------------------------------------------------

        //les associ a des thread -----------------------------------------------------------------------------
        Thread threadClient = new Thread(client, "threadClient");
        Thread threadResolver = new Thread(dnsResolver, "threadResolver");
        Thread threadRoot = new Thread(rootServer, "threadRoot");
        Thread threadTld_Com = new Thread(tldServer_Com, "threadTld_Com");
        Thread threadTld_Ca = new Thread(tldServer_Ca, "threadTld_Ca");
        Thread threadAuthoritativeName_Com = new Thread(authoritativeNameServer_Com, "threadAuthoritativeName_Com");
        Thread threadAuthoritativeName_Ca = new Thread(authoritativeNameServer_Ca, "threadAuthoritativeName_Ca");
        // ----------------------------------------------------------------------------------------------------

        // Lance tout les thread ------------------------------------------------------------------------------
        threadClient.start();
        threadResolver.start();
        threadRoot.start();
        threadTld_Com.start();
        threadTld_Ca.start();
        threadAuthoritativeName_Com.start();
        threadAuthoritativeName_Ca.start();
        //-----------------------------------------------------------------------------------------------------
    }
}