/*
 * Copyright 2014 Higher Frequency Trading
 *
 * http://www.higherfrequencytrading.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.openhft.chronicle.tcp;

import net.openhft.lang.Maths;
import net.openhft.lang.io.IOTools;
import net.openhft.lang.model.constraints.NotNull;
import sun.nio.ch.DirectBuffer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ChronicleTcp {
    public static final long BUSY_WAIT_TIME_NS = 100 * 1000;
    public static final int HEADER_SIZE = 12;
    public static final int INITIAL_BUFFER_SIZE = 64 * 1024;
    public static final int IN_SYNC_LEN = -128;
    public static final int PADDED_LEN = -127;
    public static final int SYNC_IDX_LEN = -126;
    public static final int ACK_LEN = -129;
    public static final long ACTION_SUBSCRIBE   = 1;
    public static final long ACTION_UNSUBSCRIBE = 2;
    public static final long ACTION_QUERY       = 10;
    public static final long ACTION_DATA        = 20;
    public static final long IDX_NONE           = 0L;
    public static final long IDX_TO_START       = -1;
    public static final long IDX_TO_END         = -2;
    public static final long IDX_ACK            = -3;

    public static ByteBuffer createBufferOfSize(int size) {
        return createBufferOfSize(size,  ByteOrder.nativeOrder());
    }

    public static ByteBuffer createBufferOfNextPow2Size(int size) {
        return createBufferOfNextPow2Size(size, ByteOrder.nativeOrder());
    }

    public static ByteBuffer createBufferOfSize(int size, ByteOrder byteOrder) {
        return ByteBuffer.allocateDirect(size).order(byteOrder);
    }

    public static ByteBuffer createBufferOfNextPow2Size(int size, ByteOrder byteOrder) {
        return ByteBuffer.allocateDirect(Maths.nextPower2(size,size)).order(byteOrder);
    }

    public static ByteBuffer createBuffer(int minSize) {
        return createBuffer(minSize, ByteOrder.nativeOrder());
    }

    public static ByteBuffer createBuffer(int minSize, ByteOrder byteOrder) {
        int newSize = (minSize + INITIAL_BUFFER_SIZE - 1) / INITIAL_BUFFER_SIZE * INITIAL_BUFFER_SIZE;
        return createBufferOfSize(newSize, byteOrder);
    }

    public static String connectionName(String name, final InetSocketAddress bindAddress, final InetSocketAddress connectAddress) {
        StringBuilder sb = new StringBuilder(name);
        if (bindAddress != null && connectAddress != null) {
            sb.append("[");
            sb.append(bindAddress.toString());
            sb.append(" -> ");
            sb.append(connectAddress.toString());
            sb.append("]");
        } else if (bindAddress != null) {
            sb.append("[");
            sb.append(bindAddress.toString());
            sb.append("]");
        } else if (connectAddress != null) {
            sb.append("[");
            sb.append(connectAddress.toString());
            sb.append("]");
        }

        return sb.toString();
    }

    public static int nextPower2(int size) {
        return Maths.nextPower2(size, size);
    }

    public static long nextPower2(long size) {
        return Maths.nextPower2(size, size);
    }

    public static long address(@NotNull ByteBuffer buffer) {
        return ((DirectBuffer) buffer).address();
    }

    public static void clean(@NotNull ByteBuffer buffer) {
        IOTools.clean(buffer);
    }
}

