package com.kb.challenge.deadline;


import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class DeadlineEngineImpl implements DeadlineEngine{

    private volatile AtomicLong counter;
    private final Queue<Pair<Long, Long>> deadlineQueue ;
    private final Map<Long,Pair<Long,Long>> idMap ;

    public DeadlineEngineImpl(){
        counter = new AtomicLong(1);
        deadlineQueue = new PriorityQueue<>((first,second) -> (int) (first.deadline-second.deadline));
        idMap = new ConcurrentHashMap<>();
    }

    @Override
    public long schedule(long deadlineMs) {
        long id = counter.getAndAdd(1);
        Pair<Long,Long> p = new Pair<>(deadlineMs, id);
        deadlineQueue.add(p);
        idMap.put(id,p);
        return id;
    }

    @Override
    public boolean cancel(long requestId) {
        if(idMap.containsKey(requestId)){
            Pair<Long,Long> p = idMap.get(requestId);
            if(deadlineQueue.remove(p)){
                return idMap.remove(requestId) != null;
            }
        }
        return false;
    }

    @Override
    public int poll(long nowMs, Consumer<Long> handler, int maxPoll) {
        int expired = 0;
        while(!deadlineQueue.isEmpty() && deadlineQueue.peek().deadline <= nowMs && expired<maxPoll){
            Pair<Long,Long> top = deadlineQueue.peek();
            Long requestId = top.requestId;
            handler.accept(requestId);
            if(this.cancel(requestId)){
               expired++;
            }
        }
        return expired;
    }

    @Override
    public int size() {
        return deadlineQueue.size();
    }

    private static class Pair<L extends Number, L1 extends Number> {
        public long deadline;
        public long requestId;

        public Pair(final long deadline, final long requestId){
            this.deadline = deadline;
            this.requestId = requestId;
        }
    }
}
