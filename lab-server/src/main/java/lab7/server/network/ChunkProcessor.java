package lab7.server.network;

import lab7.common.dto.Chunk;
import lab7.common.network.ChunkBuffer;
import lab7.common.network.ChunkUtils;
import lab7.common.utils.Serializer;

import java.io.IOException;
import java.util.List;

public class ChunkProcessor {

    private final ChunkBuffer buffer = new ChunkBuffer();

    public byte[] onChunk(byte[] data) throws IOException, ClassNotFoundException {

        Chunk chunk = Serializer.deserialize(data);

        buffer.add(chunk);

        if (!buffer.isComplete(chunk.getId())) {
            return null;
        }

        List<Chunk> chunks = buffer.take(chunk.getId());

        return ChunkUtils.assemble(chunks);
    }
}
