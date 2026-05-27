package lab7.common.dto;

import java.io.Serializable;
import java.util.UUID;

public class Chunk implements Serializable {
    public UUID id;
    public int index;
    public int total;
    public byte[] data;

    public Chunk(UUID id, int index, int total, byte[] data) {
        this.id = id;
        this.index = index;
        this.total = total;
        this.data = data;
    }

    public UUID getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public int getTotal() {
        return total;
    }

    public byte[] getData() {
        return data;
    }
}
