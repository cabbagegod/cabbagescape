package com.cabbagegod.cabbagescape.util;

public class ThreadingUtil {
    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted");
        }
    }
}
