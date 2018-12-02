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
    private static Queue<Pair<Date, CopyOnWriteArrayList<Event>>> event_queue =
            new ConcurrentLinkedQueue<>();

    public EventLogger()
    {
    }

    public static synchronized int get_slot()
    {
        int slot = slot_counter.getAndIncrement();
        return slot;
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
                    event_queue.add(new Pair<>(date,events));
                }
            }
        }
    }

}
