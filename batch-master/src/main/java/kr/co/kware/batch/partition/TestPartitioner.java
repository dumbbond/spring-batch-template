package kr.co.kware.batch.partition;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class TestPartitioner implements Partitioner {
    public static long DEFAULT_TEST_VALUE_SIZE = 10L;

    private long testValueSize = DEFAULT_TEST_VALUE_SIZE;

    public TestPartitioner(long testValueSize) {
        this.testValueSize = testValueSize;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        long targetSize = testValueSize / gridSize;

        Map<String, ExecutionContext> result = new HashMap<>();
        for (int i = 0; i < gridSize; i++) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + i, value);

            value.putLong("minValue", i * targetSize);
            value.putLong("maxValue", (i + 1) * targetSize -1);
        }

        return result;
    }
}
