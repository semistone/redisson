package org.redisson.misc;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DebugCounter {

    public static final AtomicInteger lock = new AtomicInteger();

    public static final AtomicBoolean sent = new AtomicBoolean();
    public static final AtomicInteger afterLock = new AtomicInteger();
    public static final AtomicInteger connected = new AtomicInteger();
    public static final AtomicInteger attemptComplete = new AtomicInteger();
    public static final AtomicInteger send = new AtomicInteger();
    public static final AtomicInteger exeRelease = new AtomicInteger();
    public static final AtomicInteger pollCon = new AtomicInteger();
    public static final AtomicInteger beforeConnected2 = new AtomicInteger();
    public static final AtomicInteger release = new AtomicInteger();
    public static final AtomicInteger poolResult = new AtomicInteger();
    public static final AtomicInteger connectComplete = new AtomicInteger();
    public static final AtomicInteger step8 = new AtomicInteger();
    public static final AtomicInteger step9 = new AtomicInteger();

    static {
        new Thread((() -> monitor())).start();
    }

    private static void monitor() {
        while(true) {
            try {
                Thread.sleep(1000);
                System.out.println("lock:" + (lock.get() -1) + " afterLock:" + afterLock.get() + " pollConn:" + pollCon.get() + " beforeConnect:"+ (beforeConnected2.get() -2)
                        + " connected:" +  (connected.get() -2)
                        + " poolresult:" + poolResult.get()
                        +" connectComplete:" + connectComplete.get()
                        +" send:" + send.get() + " sent:" + sent.get()+ " exeRelease:" + exeRelease.get() + " released:" + (release.get() -1 )
                        + ":" + step8.get()
                        + " attemptComplete:" + attemptComplete.get()
                );
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }
}
