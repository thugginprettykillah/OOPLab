package client.net;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import protocol.Message;
import protocol.Response;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class NetworkClient {
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private long reqId;
    private Gson gson;

    public NetworkClient(Socket socket) throws IOException
    {
        this.socket = socket;
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.reqId = 0;
        this.gson = new Gson();
    }

    public String initPolinom(String leadCoeff, List<String> roots, String format) throws IOException
    {
        JsonObject payload = new JsonObject();
        payload.addProperty("leadCoeff", leadCoeff);
        payload.addProperty("format", format);

        JsonArray rootsArr = new JsonArray();
        for (String root : roots) {
            rootsArr.add(root);
        }
        payload.add("roots", rootsArr);

        Message message = new Message(Long.toString(++reqId), Message.MessageType.INIT, payload);
        Response response = sendMessage(message);

        JsonObject res = response.getResult();
        return res.get("polinom").getAsString();
    }

    public String changeLead(String leadCoeff, String format) throws IOException
    {
        JsonObject payload = new JsonObject();
        payload.addProperty("leadCoeff", leadCoeff);
        payload.addProperty("format", format);

        Message message = new Message(Long.toString(++reqId), Message.MessageType.CHANGE_COEFF, payload);
        Response response = sendMessage(message);

        JsonObject res = response.getResult();
        return res.get("polinom").getAsString();
    }

    public String changeRoot(int index, String root, String format) throws IOException
    {
        JsonObject payload = new JsonObject();
        payload.addProperty("index", index);
        payload.addProperty("root", root);
        payload.addProperty("format", format);

        Message message = new Message(Long.toString(++reqId), Message.MessageType.CHANGE_ROOT, payload);
        Response response = sendMessage(message);

        JsonObject res = response.getResult();
        return res.get("polinom").getAsString();
    }

    public String resize(int newSize, String format) throws IOException
    {
        JsonObject payload = new JsonObject();
        payload.addProperty("newSize", newSize);
        payload.addProperty("format", format);

        Message message = new Message(Long.toString(++reqId), Message.MessageType.RESIZE, payload);
        Response response = sendMessage(message);

        JsonObject res = response.getResult();
        return res.get("polinom").getAsString();
    }

    public String evaluate(String x) throws IOException
    {
        JsonObject payload = new JsonObject();
        payload.addProperty("x", x);

        Message message = new Message(Long.toString(++reqId), Message.MessageType.EVAL, payload);
        Response response = sendMessage(message);

        JsonObject res = response.getResult();
        return res.get("result").getAsString();
    }

    public Response sendMessage(Message message) throws IOException
    {
        String requestJson = message.toJson();
        out.write(requestJson + "\n");
        out.flush();

        String responseJson = in.readLine();
        Response response = Response.fromJson(responseJson);

        if (!response.isOk()) throw new IOException("Ошибка на сервере: " + response.getResult().get("error").getAsString());

        return response;
    }

    public String asText(String format) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("format", format);

        Message message = new Message(Long.toString(++reqId), Message.MessageType.AS_TEXT, payload);
        Response response = sendMessage(message);

        JsonObject res = response.getResult();
        return res.get("polinom").getAsString();
    }

}
