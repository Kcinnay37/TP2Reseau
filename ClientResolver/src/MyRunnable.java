import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class MyRunnable implements Runnable
{
    private boolean isRunning = true;

    @Override
    public void run()
    {
        while(isRunning)
        {
            Start();
            while(Update())
            {

            }
            End();
        }
    }

    //synchronized est un genre de lock, modifi la valer seulement si un autre thread y accede pas
    public synchronized void stop()
    {
        isRunning = false;
    }

    public abstract void Start();

    public abstract boolean Update();

    public abstract void End();
}
