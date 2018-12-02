package DataExporter.EventLogger;

import Common.Date;
import Common.Pair;
import DataExporter.EventLogger.Events.Event;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class EventLogger
{

    private static AtomicInteger slot_counter = new AtomicInteger(0);
    private static ConcurrentHashMap<Date, CopyOnWriteArrayList<Event>> Event_Data = new ConcurrentHashMap<>();
    public static Queue<Pair<Date, CopyOnWriteArrayList<Event>>> event_queue =
            new ConcurrentLinkedQueue<>();

    private static CopyOnWriteArrayList<Date> added_dates =
            new CopyOnWriteArrayList<>();

    public EventLogger()
    {
    }

    public static synchronized int get_slot()
    {
        int slot = slot_counter.getAndIncrement();
        return slot;
    }

    private static boolean row_check(Date date)
    {
        if(added_dates.contains(date))
        {
            return false;
        }
        CopyOnWriteArrayList<Event> events = Event_Data.get(date);
        for (Event event : events)
        {
            if (event == null)
            {
                return false;
            }
        }
        return true;
    }

    public static void register_event(Event event, Date date, int slot)
    {
        if (Event_Data.containsKey(date))
        {
            CopyOnWriteArrayList<Event> events = Event_Data.get(date);
            events.set(slot, event);
        } else
        {
            synchronized (EventLogger.class)
            {
                if (!Event_Data.containsKey(date))
                {
                    CopyOnWriteArrayList<Event> events =
                            new CopyOnWriteArrayList<>();

                    for (int i = 0; i < slot_counter.get(); ++i)
                    {
                        events.add(null);
                    }

                    events.set(slot, event);
                    Event_Data.put(date, events);
                }
                else
                {
                    CopyOnWriteArrayList<Event> events = Event_Data.get(date);
                    events.set(slot, event);
                }
            }
        }
        if (row_check(date))
        {
            synchronized (EventLogger.class)
            {
                event_queue.add(new Pair<>(date, Event_Data.get(date)));
                added_dates.add(date);
            }
        }
    }

    public static Queue<Pair<Date, CopyOnWriteArrayList<Event>>> getEvent_queue()
    {
        return event_queue;
    }

}
