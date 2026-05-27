package lab8.common.network;

import lab8.common.dto.Chunk;

import java.util.*;

public class ChunkBuffer {

    private final Map<UUID, List<Chunk>> map = new HashMap<>();

    public void add(Chunk chunk) {
        map.computeIfAbsent(chunk.getId(), k -> new ArrayList<>()).add(chunk);
    }

    public boolean isComplete(UUID id) {
        List<Chunk> list = map.get(id);
        if (list == null || list.isEmpty()) return false;

        return list.size() == list.get(0).getTotal();
    }

    public List<Chunk> take(UUID id) {
        return map.remove(id);
    }
}
