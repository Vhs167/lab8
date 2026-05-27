package lab8.common.dto;


import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private static final long serialVersionUID = 2L;
    private final List<?> collection;
    private final String message;

    public Response(List<?> collection, String message) {
        this.collection = collection;
        this.message = message;
    }

    public List<?> getCollection() {
        return collection;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEmpty() {
        return collection == null || collection.isEmpty();
    }
}
