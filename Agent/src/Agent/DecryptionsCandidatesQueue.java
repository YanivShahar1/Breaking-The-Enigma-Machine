package Agent;

import DTO.DTOAgentDetails;
import DTO.DTODecryptionCandidate;

import java.util.LinkedList;
import java.util.Queue;

public class DecryptionsCandidatesQueue {
    private Queue<DTODecryptionCandidate> queue;

    public DecryptionsCandidatesQueue() {
        this.queue = new LinkedList<>();
    }
    public synchronized void enqueue(DTODecryptionCandidate dtoDecryptionCandidate) {
        queue.add(dtoDecryptionCandidate);
        this.notifyAll();
    }
    public synchronized DTODecryptionCandidate dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
                this.wait();
        }
        //notifyAll();
        return queue.poll();
    }
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    public void clear() {
        this.queue.clear();
    }
    public int size() {
        return this.queue.size();
    }

    public Queue<DTODecryptionCandidate> getQueue() {
        return queue;
    }
}
