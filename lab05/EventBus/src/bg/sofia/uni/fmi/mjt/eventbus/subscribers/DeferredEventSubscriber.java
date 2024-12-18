package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

public class DeferredEventSubscriber<T extends Event<?>> implements Subscriber<T>, Iterable<T> {

    private final Queue<T> events;

    public DeferredEventSubscriber() {
        this.events = new PriorityQueue<>(new Comparator<T>() {
            @Override
            public int compare(T e1, T e2) {
                int priorityComparison = Integer.compare(e1.getPriority(), e2.getPriority());
                if (priorityComparison != 0) {
                    return priorityComparison;
                }
                return e1.getTimestamp().compareTo(e2.getTimestamp());
            }
        });
    }

    /**
     * Store an event for processing at a later time.
     *
     * @param event the event to be processed
     * @throws IllegalArgumentException if the event is null
     */
    @Override
    public void onEvent(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        events.offer(event);
    }

    /**
     * Get an iterator for the unprocessed events. The iterator should provide the events sorted by
     * their priority in descending order. Events with equal priority are ordered in ascending order
     * of their timestamps.
     *
     * @return an iterator for the unprocessed events
     */
    @Override
    public Iterator<T> iterator() {
        return new PriorityQueue<>(events).iterator();
    }

    /**
     * Check if there are unprocessed events.
     *
     * @return true if there are unprocessed events, false otherwise
     */
    public boolean isEmpty() {
        return events.isEmpty();
    }
}