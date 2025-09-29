package protocol;

import com.google.gson.*;

public class Response {
    private String id;
    private boolean isOk;
    private JsonObject result;

    public Response() {}

    public Response(String id, boolean isOk, JsonObject result)
    {
        this.id = id;
        this.isOk = isOk;
        this.result = result;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public boolean isOk() {
        return isOk;
    }
    public void setOk(boolean ok) {
        isOk = ok;
    }

    public JsonObject getResult() {
        return result;
    }
    public void setResult(JsonObject result) {
        this.result = result;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
    public static Response fromJson(String json) {
        return new Gson().fromJson(json, Response.class);
    }
}
