package DataExporter.Exporter;

import Common.Pair;
import DataExporter.EventLogger.EventLogger;
import DataExporter.EventLogger.Events.Event;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataExporter implements Runnable
{

    FileOutputStream outputStream;
    boolean innited = false;

    public DataExporter()
    {
        DateFormat format = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss");
        Date date = new Date();
        String filename = "./Data/"+format.format(date)+".csv";

        File file = new File(filename);
        try
        {
            file.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            this.outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        while(true)
        {
            Queue<Pair<Common.Date, CopyOnWriteArrayList<Event>>> event_queue =
                    EventLogger.getEvent_queue();

            while (!event_queue.isEmpty())
            {
                Pair<Common.Date, CopyOnWriteArrayList<Event>> elements =
                        event_queue.poll();
                if(!innited)
                {

                    String header = "";
                    for(Event event:elements.right)
                    {
                        header+=event.get_csv_header()+"\n";
                        break;
                    }


                    try
                    {
                        this.outputStream.write(header.getBytes());
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    innited = true;
                }
                String data = "";
                for(Event event:elements.right)
                {
                    data+=/*elements.left+","*/event.get_csv_data()+"\n";
                }

                try
                {
                    this.outputStream.write(data.getBytes());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
