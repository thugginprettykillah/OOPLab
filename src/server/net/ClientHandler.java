package server.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import protocol.*;
import server.app.PolinomService;
import server.domain.ComplexNumber;
import server.domain.Numberic;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread{
    private PolinomService service = new PolinomService();
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket) throws IOException
    {
        this.socket = socket;
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        start();
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                String messageJson = in.readLine();
                if (messageJson == null) break;
                System.out.println(">>> Получено от клиента: " + messageJson);

                Message message = Message.fromJson(messageJson);
                Response response = responseMessage(message);
                System.out.println("<<< Отправка клиенту: " + response.toJson());

                out.write(response.toJson() +"\n");
                out.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Response responseMessage(Message message)
    {
        Response response = new Response();
        response.setId(message.getId());
        try
        {
            JsonObject payload = message.getPayload();

            switch (message.getType())
            {
                case INIT -> {
                    handleInit(payload);
                    response.setOk(true);
                    response.setResult(handleAsText(payload));
                }
                case CHANGE_COEFF -> {
                    handleChangeCoeff(payload);
                    response.setOk(true);
                    response.setResult(handleAsText(payload));
                }
                case CHANGE_ROOT -> {
                    handleChangeRoot(payload);
                    response.setOk(true);
                    response.setResult(handleAsText(payload));
                }
                case EVAL -> {
                    response.setResult(handleEval(payload));
                    response.setOk(true);
                }
                case RESIZE -> {
                    handleResize(payload);
                    response.setOk(true);
                    response.setResult(handleAsText(payload));
                } case AS_TEXT -> {
                    response.setResult(handleAsText(payload));
                    response.setOk(true);
                }
            }
        } catch (Exception e) {
            response.setOk(false);
            JsonObject error = new JsonObject();
            error.addProperty("error", e.getMessage());
            response.setResult(error);
        }
        return response;
    }

    private void handleInit(JsonObject payload)
    {
        String leadCoeff = payload.get("leadCoeff").getAsString();
        JsonArray rootsArray = payload.getAsJsonArray("roots");

        Numberic lead = parseNumber(leadCoeff);
        List<Numberic> roots = new ArrayList<>();
        for (int i = 0; i < rootsArray.size(); i++) {
            roots.add(parseNumber(rootsArray.get(i).getAsString()));
        }
        service.initPolinom(lead, roots);
    }

    private void handleChangeCoeff(JsonObject payload)
    {
        String leadCoeff = payload.get("leadCoeff").getAsString();
        Numberic lead = parseNumber(leadCoeff);
        service.changeLeadCoeef(lead);

    }

    private void handleChangeRoot(JsonObject payload)
    {
        String indexStr = payload.get("index").getAsString();
        String rootStr = payload.get("root").getAsString();

        int index = Integer.parseInt(indexStr);
        Numberic root = parseNumber(rootStr);

        service.changeRoot(index, root);

    }

    private JsonObject handleEval(JsonObject payload)
    {
        String xStr = payload.get("x").getAsString();
        Numberic x = parseNumber(xStr);
        Numberic result = service.evaluate(x);

        JsonObject res = new JsonObject();
        res.addProperty("result", result.toString());

        return res;
    }

    private void handleResize(JsonObject payload)
    {
        String newSizeStr = payload.get("newSize").getAsString();
        int newSize = Integer.parseInt(newSizeStr);
        service.resize(newSize);
    }

    private JsonObject handleAsText(JsonObject payload)
    {
        JsonObject asText = new JsonObject();
        boolean withBrackets = payload.get("format").getAsString().equals("Со скобками");
        String text = service.getAsText(withBrackets);
        asText.addProperty("polinom", text);
        return asText;
    }

    private Numberic parseNumber(String str)
    {
        String[] parts = str.trim().split("\\s+");
        if (parts.length == 1) {
            return new ComplexNumber(Double.parseDouble(parts[0]), 0);
        } else if (parts.length == 2) {
            return new ComplexNumber(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
        } else throw new IllegalArgumentException("Ожидается ввод 1 или 2 вещественных числа");
    }
}
