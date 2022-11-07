package DecryptionManager;

import DTO.DTOMission;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MissionsQueue {
    private final int QUEUE_CAPACITY = 1000;
    private BlockingQueue<DTOMission> queue;

    public MissionsQueue() {
        this.queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    }
    public synchronized void enqueue(DTOMission dtoMission) throws InterruptedException {

        if (queue.size() == QUEUE_CAPACITY) {
            wait();
        }
        queue.put(dtoMission);
        notifyAll();
    }
    public synchronized DTOMission dequeue() throws InterruptedException {
        DTOMission d = queue.poll();
        notifyAll();
        return d;
    }
    public synchronized void notifyDMThread() {
        notifyAll();
    }
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    public synchronized void clear() {
        this.queue.clear();
    }
    public int size() {
        return this.queue.size();
    }
}