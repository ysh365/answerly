package org.buaa.project.toolkit;

public class CustomIdGenerator {

    private static final long SEQUENCE_BITS = 4;  // (最大支持 16 个 ID)
    private static final long MACHINE_ID_BITS = 1;  // (最大支持 2 台机器)

    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;

    // 自定义起始时间 (从 2024 年开始生成 ID，避免当前时间戳过大)
    private static final long CUSTOM_EPOCH = 1704067200000L; // 2024-01-01 00:00:00

    private static long lastTimestamp = -1L;
    private static long sequence = 0L;  // 序列号，防止同一毫秒内生成重复ID
    private static long machineId = 1L;

    public static synchronized long getId() {
        long timestamp = System.currentTimeMillis() - CUSTOM_EPOCH; // 计算相对时间戳

        if (timestamp == lastTimestamp) {
            // 同一毫秒内生成多个ID
            sequence = (sequence + 1) & ((1 << SEQUENCE_BITS) - 1);
            if (sequence == 0) {
                // 如果序列号已满，等待下一毫秒
                timestamp = waitForNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;  // 重置序列号
        }

        lastTimestamp = timestamp;

        return (timestamp << TIMESTAMP_SHIFT) | (machineId << MACHINE_ID_SHIFT) | sequence;
    }

    private static long waitForNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis() - CUSTOM_EPOCH;
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis() - CUSTOM_EPOCH;
        }
        return timestamp;
    }

}
