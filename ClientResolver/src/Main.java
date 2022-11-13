import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] argv)
    {
        // cree toute les cache pour les serveur qui roule sur des thread different pour pouvoir les update ------------
        Cache cacheResolver = new Cache();
        Cache cacheRoot = new Cache();
        Cache cacheTLD_Com = new Cache();
        Cache cacheTLD_Ca = new Cache();
        Cache cacheAuthoritative_Com = new Cache();
        Cache cacheAuthoritative_Ca = new Cache();
        // -------------------------------------------------------------------------------------------------------------

        //cree tout les servers et client -----------------------------------------
        Client client = new Client();
        client.SetAddressPort("192.168.2.26", 53);

        Server dnsResolver = new Resolver(cacheResolver);
        dnsResolver.SetPortHost(53);
        dnsResolver.SetPathMasterFile("MasterFile\\Resolver.txt");

        Server rootServer = new Root(cacheRoot);
        rootServer.SetPortHost(54);
        rootServer.SetPathMasterFile("MasterFile\\Root.txt");
        rootServer.SetUpdateLoop(false);

        Server tldServer_Com = new TLD(cacheTLD_Com);
        tldServer_Com.SetPortHost(55);
        tldServer_Com.SetPathMasterFile("MasterFile\\TLD_Com.txt");
        tldServer_Com.SetUpdateLoop(false);

        Server tldServer_Ca = new TLD(cacheTLD_Ca);
        tldServer_Ca.SetPortHost(56);
        tldServer_Ca.SetPathMasterFile("MasterFile\\TLD_Ca.txt");
        tldServer_Ca.SetUpdateLoop(false);

        Server authoritativeNameServer_Com = new Authoritative(cacheAuthoritative_Com);
        authoritativeNameServer_Com.SetPortHost(57);
        authoritativeNameServer_Com.SetPathMasterFile("MasterFile\\Authoritative_Com.txt");
        authoritativeNameServer_Com.SetUpdateLoop(false);

        Server authoritativeNameServer_Ca = new Authoritative(cacheAuthoritative_Ca);
        authoritativeNameServer_Ca.SetPortHost(58);
        authoritativeNameServer_Ca.SetPathMasterFile("MasterFile\\Authoritative_Ca.txt");
        authoritativeNameServer_Ca.SetUpdateLoop(false);
        //--------------------------------------------------------------

        //les associ a des thread -----------------------------------------------------------------------------
        Thread threadCacheResolver = new Thread(cacheResolver, "threadCacheResolver");
        Thread threadCacheRoot = new Thread(cacheRoot, "threadCacheRoot");
        Thread threadCacheTLD_Com = new Thread(cacheTLD_Com, "threadCacheTLD_Com");
        Thread threadCacheTLD_ca = new Thread(cacheTLD_Ca, "threadCacheTLD_Ca");
        Thread threadCacheAuthoritative_Com = new Thread(cacheAuthoritative_Com, "threadCacheAuthoritative_Com");
        Thread threadCacheAuthoritative_Ca = new Thread(cacheAuthoritative_Ca, "threadCacheAuthoritative_Ca");

        Thread threadClient = new Thread(client, "threadClient");
        Thread threadResolver = new Thread(dnsResolver, "threadResolver");
        Thread threadRoot = new Thread(rootServer, "threadRoot");
        Thread threadTld_Com = new Thread(tldServer_Com, "threadTld_Com");
        Thread threadTld_Ca = new Thread(tldServer_Ca, "threadTld_Ca");
        Thread threadAuthoritativeName_Com = new Thread(authoritativeNameServer_Com, "threadAuthoritativeName_Com");
        Thread threadAuthoritativeName_Ca = new Thread(authoritativeNameServer_Ca, "threadAuthoritativeName_Ca");
        // ----------------------------------------------------------------------------------------------------

        // Lance tout les thread ------------------------------------------------------------------------------
        threadCacheResolver.start();
        threadCacheRoot.start();
        threadCacheTLD_Com.start();
        threadCacheTLD_ca.start();
        threadCacheAuthoritative_Com.start();
        threadCacheAuthoritative_Ca.start();

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