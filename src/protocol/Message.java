package protocol;

import com.google.gson.*;

public class Message {
    public enum MessageType {
        INIT,
        CHANGE_COEFF,
        CHANGE_ROOT,
        EVAL,
        RESIZE,
        AS_TEXT,
        CHANGE_TYPE
    };
    private String id;
    private MessageType type;
    private JsonObject payload;

    public Message() {}

    public Message(String id, MessageType type, JsonObject payload)
    {
        this.id = id;
        this.type = type;
        this.payload = payload;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public MessageType getType() {
        return type;
    }
    public void setType(MessageType type) {
        this.type = type;
    }

    public JsonObject getPayload() {
        return payload;
    }
    public void setPayload(JsonObject payload) {
        this.payload = payload;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
    public static Message fromJson(String json) {
        return new Gson().fromJson(json, Message.class);
    }
}
