package client.gui;

import client.net.NetworkClient;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ApplicationGUI extends Application
{
    private NetworkClient client;
    private BorderPane root;
    private VBox buttonsPane;
    private StackPane centerPane;
    private Label statusLabel;
    ComboBox<String> formats = new ComboBox<>();

    @Override
    public void start(Stage stage) throws IOException
    {
        client = new NetworkClient(new Socket("127.0.0.1", 8081));

        root = new BorderPane();
        // кнопки (слева)
        buttonsPane = buildButtonsPane();
        root.setLeft(buttonsPane);

        // центр (весь контент)
        centerPane = new StackPane();
        centerPane.setPadding(new Insets(16));
        root.setCenter(centerPane);

        // режимы вывода (сверху)
        root.setTop(buildTopBar());

        // статусная строка (снизу)
        root.setBottom(buildStatusLabel());

        // сцена
        stage.setTitle("Полиномыч");
        stage.setScene(new Scene(root, 1080, 520));
        stage.show();
    }

    private void updateStatus(String text)
    {
        statusLabel.setText(text);
        Tooltip.install(statusLabel, new Tooltip(text));
    }

    private ToolBar buildTopBar()
    {
        Label modeLabel = new Label("Режим вывода: ");
        formats.getItems().addAll("Со скобками", "В раскрытом виде");
        formats.getSelectionModel().selectFirst();

        formats.valueProperty().addListener(((observableValue, s, t1) -> {
            try {
                updateStatus(client.asText(t1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

        return new ToolBar(modeLabel, formats);
    }

    private Node buildStatusLabel() throws IOException {
        statusLabel = new Label();
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setFont(Font.font(20));
        statusLabel.setTextOverrun(OverrunStyle.ELLIPSIS);
        HBox.setHgrow(statusLabel, Priority.ALWAYS);

        Tooltip tip = new Tooltip();
        statusLabel.textProperty().addListener((obs, oldText, newText) -> tip.setText(newText));
        statusLabel.setTooltip(tip);
        statusLabel.setText(client.asText(formats.getValue()));

        HBox bar = new HBox(12, statusLabel);
        bar.setPadding(new Insets(8,12,8,12));
        return bar;
    }

    private VBox buildButtonsPane()
    {
        Button inputNewPolinomButton = new Button("Ввести новый полином");
        Button changeLeadCoeffButton = new Button("Изменить старший коэффициент");
        Button changeRootButton = new Button("Изменить корень");
        Button resizeButton = new Button("Изменить размерность");
        Button evaluateButton = new Button("Вычислить в точке х");

        inputNewPolinomButton.setMaxWidth(Double.MAX_VALUE);
        changeLeadCoeffButton.setMaxWidth(Double.MAX_VALUE);
        changeRootButton.setMaxWidth(Double.MAX_VALUE);
        resizeButton.setMaxWidth(Double.MAX_VALUE);
        evaluateButton.setMaxWidth(Double.MAX_VALUE);

        inputNewPolinomButton.setOnAction(e -> inputNewPolinom());
        changeLeadCoeffButton.setOnAction(e -> changeLeadCoeff());
        changeRootButton.setOnAction(e -> changeRoot());
        resizeButton.setOnAction(e -> resize());
        evaluateButton.setOnAction(e -> evaluate());

        VBox box = new VBox(5, inputNewPolinomButton,
                changeLeadCoeffButton, changeRootButton, resizeButton, evaluateButton);
        box.setPadding(new Insets(20));
        box.setPrefWidth(260);
        box.setPrefHeight(400);
        box.setFillWidth(true);
        return box;
    }

    private void inputNewPolinom ()
    {
        Label coeffLabel = new Label("Введите старший коэффициент:");
        Label rootsLabel = new Label("Вводите корни (1 или 2 числа на каждую строчку)");
        TextField coeffField = new TextField();
        TextArea rootsArea = new TextArea();
        Button applyBtnYes = new Button("Ок");

        applyBtnYes.setOnAction(e ->
            {
                try {
                    String leadCoeff = coeffField.getText();
                    List<String> roots = new ArrayList<>();
                    for (String line : rootsArea.getText().split("\\R")) {
                        if (!line.trim().isEmpty()) {
                            roots.add(line.trim());
                        };
                    }
                    String result = client.initPolinom(leadCoeff, roots, formats.getValue());
                    updateStatus(result);
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                }
            });
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);

        pane.addRow(0, coeffLabel, coeffField);
        pane.addRow(1, rootsLabel, rootsArea);
        pane.addRow(2, applyBtnYes);

        centerPane.getChildren().setAll(pane);
    }

    private void changeLeadCoeff ()
    {
        Label label = new Label("Введите новый коэффициент");
        TextField field = new TextField();
        Button applyBtn = new Button("Ок");
        GridPane pane = new GridPane();

        applyBtn.setOnAction(e ->
            {
                try {
                    String coeff = field.getText();
                    String res = client.changeLead(coeff, formats.getValue());
                    updateStatus(res);
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                }
            });
        pane.setHgap(10);
        pane.setVgap(10);
        pane.addRow(0, label, field, applyBtn);
        centerPane.getChildren().setAll(pane);
    }

    private void changeRoot ()
    {
        Label rootLabel = new Label("Введите корень: ");
        Label indexLabel = new Label("Введите индекс корня:");
        TextField rootField = new TextField();
        TextField indexField = new TextField();
        Button applyBtn = new Button("Ок");

        applyBtn.setOnAction(e ->
            {
                try {
                    int index = Integer.parseInt(indexField.getText());
                    String root = rootField.getText();
                    updateStatus(client.changeRoot(index, root, formats.getValue()));
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                }
            });

        GridPane pane = new GridPane();
        pane.addRow(0, indexLabel, indexField);
        pane.addRow(1, rootLabel, rootField);
        pane.addRow(2, applyBtn);
        pane.setHgap(10);
        pane.setVgap(10);
        centerPane.getChildren().setAll(pane);
    }

    private void resize ()
    {
        Label label = new Label("Введите новую размерность:");
        TextField field = new TextField();
        Button applyBtn = new Button("Применить");

        applyBtn.setOnAction(e ->
            {
                try {
                    int size = Integer.parseInt(field.getText());
                    updateStatus(client.resize(size, formats.getValue()));

                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                }
            });

        GridPane pane = new GridPane();
        pane.addRow(0, label, field, applyBtn);
        pane.setVgap(10);
        pane.setHgap(10);
        centerPane.getChildren().setAll(pane);
    }

    private void evaluate()
    {
        Label label = new Label("Введите х");
        TextField field = new TextField();
        Button applyBtn = new Button("Ок");
        Label res = new Label("");

        applyBtn.setOnAction(e ->
            {
                try {
                    String x = field.getText();
                    String resValue = client.evaluate(x);
                    res.setText("Значение полинома в данной точке P(" + x + ") = " + resValue);
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                }
            });
        GridPane pane = new GridPane();
        pane.addRow(0, label, field, applyBtn);
        pane.addRow(1, res);
        pane.setHgap(10);
        pane.setVgap(10);
        centerPane.getChildren().setAll(pane);
    }

}
