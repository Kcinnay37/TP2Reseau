import javax.swing.text.Style;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.Clock;

public class Cache extends MyRunnable
{
    private HashMap<String, String> cacheMemory;
    private HashMap<String, Float> cachTimer;

    private ArrayList<String> keysToDelete;

    long lastTime = System.currentTimeMillis() / 1000;

    private boolean canAccesHim = true;
    private boolean canAccesOther = false;

    public void Start()
    {
        cacheMemory = new HashMap<String, String>();
        cachTimer = new HashMap<String, Float>();
        keysToDelete = new ArrayList<String>();
    }

    public boolean Update()
    {
        while (!canAccesHim)
        {
            System.out.print("");
        }
        canAccesOther = false;

        long currTime = System.currentTimeMillis() / 1000;
        float dt = (currTime - lastTime);
        lastTime = currTime;

        cachTimer.forEach((key, value)
                        -> UpdateTimer(key, value, dt));

        for(int i = 0; i < keysToDelete.size(); i++)
        {
            cachTimer.remove(keysToDelete.get(i));
            cacheMemory.remove(keysToDelete.get(i));
        }
        keysToDelete.clear();

        canAccesOther = true;
        return true;
    }

    public void End()
    {
        cachTimer.clear();
        cacheMemory.clear();
    }

    public void PutInCache(String key, String value, float time)
    {
        while (!canAccesOther)
        {
            System.out.print("");
        }
        canAccesHim = false;

        cacheMemory.put(key, value);
        cachTimer.put(key, time);

        canAccesHim = true;
    }

    public String GetInCache(String key)
    {
        while (!canAccesOther)
        {
            System.out.print("");
        }
        canAccesHim = false;

        if(cacheMemory.containsKey(key))
        {
            canAccesHim = true;

            return cacheMemory.get(key);
        }

        canAccesHim = true;
        return "";
    }

    public void UpdateTimer(String key, float value, float dt)
    {
        //System.out.println("yo");
        value -= dt;
        if(value <= 0)
        {
            keysToDelete.add(key);
        }
        cachTimer.put(key, value);
    }
}
