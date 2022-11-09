import java.util.ArrayList;
import java.util.HashMap;

public class Root extends Server
{
    public Root()
    {
        cache = new HashMap<String, String>();
    }

    @Override
    public void CmdReceive(String cmd)
    {

    }
}
