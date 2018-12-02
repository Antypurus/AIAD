package DataExporter.EventLogger;

import Common.Date;
import DataExporter.EventLogger.Events.Event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class EventLogger
{

    private static AtomicInteger slot_counter = new AtomicInteger(0);
    private static ConcurrentHashMap<Date, CopyOnWriteArrayList<Event>> Event_Data = new ConcurrentHashMap<>();

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
                }
            }
        }
    }

}
