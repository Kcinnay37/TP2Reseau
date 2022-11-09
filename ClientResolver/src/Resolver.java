import java.util.ArrayList;
import java.util.HashMap;

public class Resolver extends Server
{
    public Resolver()
    {
        cache = new HashMap<String, String>();
    }

    @Override
    public void CmdReceive(String cmd)
    {
//        String cmdName = "";
//
//        int i = 0;
//        while(cmd.charAt(i) != ' ')
//        {
//            cmdName += cmd.charAt(i++);
//        }
//        i++;
//
//        ArrayList<String> domaineNames = new ArrayList<String>();
//        String currDomaineName = "";
//
//        while(cmd.charAt(i) != '\n')
//        {
//            if(cmd.charAt(i) == '.')
//            {
//                domaineNames.add(currDomaineName);
//                i++;
//                currDomaineName = "";
//                continue;
//            }
//            else
//            {
//                currDomaineName += cmd.charAt(i++);
//            }
//        }
//        domaineNames.add(currDomaineName);
//
//        System.out.println(cmd);
//        switch (cmdName)
//        {
//            case "GET":
//                m_RequestHost.print("ca Get bordel\r\n");
//                break;
//            case "PUT":
//
//                break;
//        }

        System.out.println(cmd);

        m_RequestHost.print("end\r\n");
        m_RequestHost.flush();
    }
}
