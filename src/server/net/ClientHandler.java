package server.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import protocol.*;
import server.app.ComplexNumberFactory;
import server.app.DoubleNumberFactory;
import server.app.PolinomService;
import server.domain.ComplexNumber;
import server.domain.DoubleNumber;
import server.domain.Numberic;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread{
    private PolinomService<ComplexNumber> complexService = new PolinomService<>(new ComplexNumberFactory());
    private PolinomService<DoubleNumber> doubleService = new PolinomService<>(new DoubleNumberFactory());

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
            String numberType = payload.get("numberType").getAsString();
            switch (message.getType())
            {
                case INIT -> {
                    if ("complex".equals(numberType)) {
                        handleInit(payload, complexService);
                        response.setResult(handleAsText(payload, complexService));
                    }
                    else {
                        handleInit(payload, doubleService);
                        response.setResult(handleAsText(payload, doubleService));
                    }
                    response.setOk(true);

                }
                case CHANGE_COEFF -> {
                    if ("complex".equals(numberType)) {
                        handleChangeCoeff(payload, complexService);
                        response.setResult(handleAsText(payload, complexService));
                    } else {
                        handleChangeCoeff(payload, doubleService);
                        response.setResult(handleAsText(payload, doubleService));
                    }
                    response.setOk(true);
                }
                case CHANGE_ROOT -> {
                    if ("complex".equals(numberType))
                    {
                        handleChangeRoot(payload, complexService);
                        response.setResult(handleAsText(payload, complexService));
                    }
                    else {
                        handleChangeRoot(payload, doubleService);
                        response.setResult(handleAsText(payload, doubleService));
                    }
                    response.setOk(true);
                }
                case EVAL -> {
                    if ("complex".equals(numberType)) {
                        response.setResult(handleEval(payload, complexService));
                    }else {
                        response.setResult(handleEval(payload, doubleService));
                    }
                    response.setOk(true);
                }
                case RESIZE -> {
                    if ("complex".equals(numberType)) {
                        handleResize(payload, complexService);
                        response.setResult(handleAsText(payload, complexService));
                    } else {
                        handleResize(payload, doubleService);
                        response.setResult(handleAsText(payload, doubleService));
                    }
                    response.setOk(true);
                } case AS_TEXT -> {
                    if ("complex".equals(numberType)) {
                        response.setResult(handleAsText(payload, complexService));
                    } else {
                        response.setResult(handleAsText(payload, doubleService));
                    }
                    response.setOk(true);
            } case CHANGE_TYPE -> {
                    if ("complex".equals(numberType)) {
                        complexService.resetToDefault();
                        response.setResult(handleAsText(payload, complexService));
                    } else {
                        doubleService.resetToDefault();
                        response.setResult(handleAsText(payload, doubleService));
                    }
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

    private <T extends Numberic> void handleInit(JsonObject payload, PolinomService<T> service)
    {
        String leadCoeff = payload.get("leadCoeff").getAsString();
        JsonArray rootsArray = payload.getAsJsonArray("roots");

        T lead = service.parseNumber(leadCoeff);
        List<T> roots = new ArrayList<>();
        for (int i = 0; i < rootsArray.size(); i++) {
            roots.add(service.parseNumber(rootsArray.get(i).getAsString()));
        }
        service.initPolinom(lead, roots);
    }

    private <T extends Numberic> void handleChangeCoeff(JsonObject payload, PolinomService<T> service)
    {
        String leadCoeff = payload.get("leadCoeff").getAsString();
        T lead = service.parseNumber(leadCoeff);
        service.changeLeadCoeef(lead);

    }

    private <T extends Numberic> void handleChangeRoot(JsonObject payload, PolinomService<T> service)
    {
        String indexStr = payload.get("index").getAsString();
        String rootStr = payload.get("root").getAsString();

        int index = Integer.parseInt(indexStr);
        T root = service.parseNumber(rootStr);

        service.changeRoot(index, root);

    }

    private <T extends Numberic> JsonObject handleEval(JsonObject payload, PolinomService<T> service)
    {
        String xStr = payload.get("x").getAsString();
        T x = service.parseNumber(xStr);
        T result = service.evaluate(x);

        JsonObject res = new JsonObject();
        res.addProperty("result", result.toString());

        return res;
    }

    private <T extends Numberic> void handleResize(JsonObject payload, PolinomService<T> service)
    {
        String newSizeStr = payload.get("newSize").getAsString();
        int newSize = Integer.parseInt(newSizeStr);
        service.resize(newSize);
    }

    private <T extends Numberic> JsonObject handleAsText(JsonObject payload, PolinomService<T> service)
    {
        JsonObject asText = new JsonObject();
        boolean withBrackets = payload.get("format").getAsString().equals("Со скобками");
        String text = service.getAsText(withBrackets);
        asText.addProperty("polinom", text);
        return asText;
    }
}
