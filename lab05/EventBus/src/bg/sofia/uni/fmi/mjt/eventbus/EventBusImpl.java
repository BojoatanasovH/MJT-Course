package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBusImpl implements EventBus {
    Map<Class<? extends Event<?>>, List<Subscriber<? extends Event<?>>>> subscribers;
    Map<Class<? extends Event<?>>, List<Event<?>>> eventLogs;

    public EventBusImpl() {
        this.subscribers = new HashMap<>();
        this.eventLogs = new HashMap<>();
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type or subscriber cannot be null");
        }
        List<Subscriber<?>> eventSubscribers = subscribers.get(eventType);
        if (eventSubscribers == null) {
            eventSubscribers = new ArrayList<>();
            subscribers.put(eventType, eventSubscribers);
        }

        if (!eventSubscribers.contains(subscriber)) {
            eventSubscribers.add(subscriber);
        }
    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
        throws MissingSubscriptionException {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type or subscriber cannot be null");
        }

        List<Subscriber<? extends Event<?>>> subs = subscribers.get(eventType);
        if (subs == null || !subs.remove(subscriber)) {
            throw new MissingSubscriptionException("Subscriber not found for event type");
        }
    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }
        List<Subscriber<?>> subs = subscribers.get(event.getClass());
        if (subs != null) {
            for (Subscriber<?> subscriber : subs) {
                ((Subscriber<T>) subscriber).onEvent(event);  // Проверка дали събитието е добавено
            }
        }
        List<Event<?>> logs = eventLogs.get(event.getClass());
        if (logs == null) {
            logs = new ArrayList<>();
            eventLogs.put((Class<? extends Event<?>>) event.getClass(), logs);
        }
        logs.add(event);
    }

    <T extends Event<?>> void notifySubscriber(Subscriber<?> subscriber, T event) {
        Subscriber<T> castedSubscriber = (Subscriber<T>) subscriber;
        castedSubscriber.onEvent(event);
    }

    @Override
    public void clear() {
        subscribers.clear();
        eventLogs.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        if (eventType == null || from == null || to == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        List<? extends Event<?>> logs = eventLogs.get(eventType);
        if (logs == null) {
            return Collections.emptyList();
        }

        List<Event<?>> filteredLogs = new ArrayList<>();
        for (Event<?> event : logs) {
            if (!event.getTimestamp().isBefore(from) && event.getTimestamp().isBefore(to)) {
                filteredLogs.add(event);
            }
        }

        filteredLogs.sort(new Comparator<Event<?>>() {
            @Override
            public int compare(Event<?> e1, Event<?> e2) {
                int timestampComparison = e1.getTimestamp().compareTo(e2.getTimestamp());
                if (timestampComparison != 0) {
                    return timestampComparison;
                }
                return Integer.compare(e1.getPriority(), e2.getPriority());
            }
        });

        return Collections.unmodifiableList(filteredLogs);
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }

        List<Subscriber<?>> eventSubscribers = subscribers.get(eventType);
        if (eventSubscribers == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(eventSubscribers);
    }
}
