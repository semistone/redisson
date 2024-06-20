/**
 * Copyright (c) 2013-2024 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.redisson.misc;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author Nikita Koksharov
 *
 */
public class AsyncSemaphore {

    private final AtomicInteger counter;
    private final Queue<CompletableFuture<Runnable>> listeners = new ConcurrentLinkedQueue<>();

    public AsyncSemaphore(int permits) {
        counter = new AtomicInteger(permits);
    }
    
    public int queueSize() {
        return listeners.size();
    }
    
    public void removeListeners() {
        listeners.clear();
    }

    public CompletableFuture<Runnable> acquire() {
        CompletableFuture<Runnable> future = new CompletableFuture<>();
        listeners.add(future);
        tryRun();
        return future;
    }

    private void tryRun() {
        while (true) {
            if (counter.decrementAndGet() >= 0) {
                CompletableFuture<Runnable> future = listeners.poll();
                if (future == null) {
                    counter.incrementAndGet();
                    return;
                }

                if (!future.isDone()) {
                    AtomicBoolean once = new AtomicBoolean();
                    if (future.complete(() -> {
                        if (once.compareAndSet(false, true)) {
                            release();
                        }
                    })) {
                        return;
                    }
                }
            }

            if (counter.incrementAndGet() <= 0) {
                return;
            }
        }
    }

    public int getCounter() {
        return counter.get();
    }

    public void release() {
        counter.incrementAndGet();
        tryRun();
    }

    @Override
    public String toString() {
        return "value:" + counter + ":queue:" + queueSize();
    }
    
    
    
}
