package fpt.aptech.trackmentalhealth.configs;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class RedisCircuitBreaker {
    private enum State { CLOSED, OPEN, HALF_OPEN }

    private State state = State.CLOSED;
    private final int failureThreshold = 3;
    private final long openTimeoutMs = 5000; // 5s
    private AtomicInteger failureCount = new AtomicInteger(0);
    private Instant lastFailureTime;

    public void reset() {

        this.state = State.CLOSED;
        this.failureCount = new AtomicInteger(0);
        // ...
        System.out.println("Circuit Breaker has been reset.");
    }
    public synchronized boolean allowRequest() {
        if (state == State.OPEN) {
            if (Instant.now().isAfter(lastFailureTime.plusMillis(openTimeoutMs))) {
                state = State.HALF_OPEN;
                return true;
            }
            return false;
        }
        return true;
    }

    public synchronized void recordSuccess() {
        state = State.CLOSED;
        failureCount.set(0);
    }

    public synchronized void recordFailure() {
        int failures = failureCount.incrementAndGet();
        lastFailureTime = Instant.now();
        if (failures >= failureThreshold) {
            state = State.OPEN;
            System.err.println("🚨 Redis Circuit Open: chuyển sang DB");
        }
    }
}
