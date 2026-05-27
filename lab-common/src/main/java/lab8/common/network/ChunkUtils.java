package lab8.common.network;

import lab8.common.dto.Chunk;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ChunkUtils {

    private static final int MAX_SIZE = 40000;

    public static List<Chunk> split(byte[] data, UUID id) {
        List<Chunk> chunks = new ArrayList<>();

        int total = (int) Math.ceil(data.length / (double) MAX_SIZE);

        for (int i = 0; i < total; i++) {
            int start = i * MAX_SIZE;
            int end = Math.min(data.length, start + MAX_SIZE);

            byte[] part = new byte[end - start];
            System.arraycopy(data, start, part, 0, part.length);

            chunks.add(new Chunk(id, i, total, part));
        }

        return chunks;
    }

    public static byte[] assemble(List<Chunk> chunks) {
        chunks.sort(Comparator.comparing(Chunk::getIndex));

        int size = 0;
        for (Chunk c : chunks) {
            size += c.getData().length;
        }

        byte[] result = new byte[size];

        int offset = 0;
        for (Chunk c : chunks) {
            System.arraycopy(c.getData(), 0, result, offset, c.getData().length);
            offset += c.getData().length;
        }

        return result;
    }
}
