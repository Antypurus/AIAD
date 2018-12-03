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

    private static AtomicInteger company_slot_counter = new AtomicInteger(0);
    private static AtomicInteger investor_slot_counter = new AtomicInteger(0);
    private static AtomicInteger manager_slot_counter = new AtomicInteger(0);

    private static ConcurrentHashMap<Date, CopyOnWriteArrayList<Event>> Event_Data = new ConcurrentHashMap<>();
    public static Queue<Pair<Date, CopyOnWriteArrayList<Event>>> event_queue =
            new ConcurrentLinkedQueue<>();

    private static CopyOnWriteArrayList<Date> added_dates =
            new CopyOnWriteArrayList<>();

    public EventLogger()
    {
    }

    public static synchronized int get_slot(SlotType slotType)
    {
        int slot = Integer.MAX_VALUE;
        switch (slotType)
        {
            case CompanySlot:
            {
                slot = company_slot_counter.getAndIncrement();
                break;
            }

            case ManagerSlot:
            {
                slot = manager_slot_counter.getAndIncrement();
                break;
            }

            case InvestorSlot:
            {
                slot = investor_slot_counter.getAndIncrement();
                break;
            }
        }
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

    private static void set_event(CopyOnWriteArrayList events, Event event,
                             int slot, SlotType slotType)
    {
        switch (slotType)
        {
            case CompanySlot:
            {
                events.set(slot, event);
                break;
            }

            case ManagerSlot:
            {
                events.set(slot + company_slot_counter.get() + manager_slot_counter.get(), event);
                break;
            }

            case InvestorSlot:
            {
                events.set(slot + company_slot_counter.get(), event);
                break;
            }
        }
    }

    public static void register_event(Event event, Date date, int slot,
                                      SlotType slotType)
    {
        if (Event_Data.containsKey(date))
        {
            CopyOnWriteArrayList<Event> events = Event_Data.get(date);
            set_event(events,event,slot,slotType);

        } else
        {
            synchronized (EventLogger.class)
            {
                if (!Event_Data.containsKey(date))
                {
                    CopyOnWriteArrayList<Event> events =
                            new CopyOnWriteArrayList<>();

                    int slot_counter =
                            company_slot_counter.get() + investor_slot_counter.get() + manager_slot_counter.get();
                    for (int i = 0; i < slot_counter; ++i)
                    {
                        events.add(null);
                    }

                    set_event(events,event,slot,slotType);
                    Event_Data.put(date, events);
                }
                else
                {
                    CopyOnWriteArrayList<Event> events = Event_Data.get(date);
                    set_event(events,event,slot,slotType);
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
