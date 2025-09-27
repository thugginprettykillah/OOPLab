import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ApplicationGUI extends Application
{

    private BorderPane root;
    private VBox buttonsPane;
    private StackPane centerPane;
    private Label statusLabel;
    ComboBox<String> formats = new ComboBox<>();

    @Override
    public void start(Stage stage) throws Exception
    {
        MyArray roots = new MyArray(2, new ComplexNumber(1, 0));
        roots.set(1, new ComplexNumber(3, 0));
        Polinom polinom = new Polinom(new ComplexNumber(2, 0), roots);

        root = new BorderPane();
        // кнопки (слева)
        buttonsPane = buildButtonsPane(polinom);
        root.setLeft(buttonsPane);

        // центр (весь контент)
        centerPane = new StackPane();
        centerPane.setPadding(new Insets(16));
        root.setCenter(centerPane);

        // режимы вывода (сверху)
        root.setTop(buildTopBar(polinom));

        // статусная строка (снизу)
        root.setBottom(buildStatusLabel(polinom));

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

    private ToolBar buildTopBar(Polinom polinom)
    {
        Label modeLabel = new Label("Режим вывода: ");
        formats.getItems().addAll("Со скобками", "В раскрытом виде");
        formats.getSelectionModel().selectFirst();

        formats.valueProperty().addListener((obs, oldV, newV) ->
            {
                String text = "Со скобками".equals(newV) ?
                        polinom.toStringWithBrackets() : polinom.toStringWithDegree();
                updateStatus(text);
            });

        return new ToolBar(modeLabel, formats);
    }

    private Node buildStatusLabel(Polinom polinom)
    {
        statusLabel = new Label(polinom.toStringWithBrackets());
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setFont(Font.font(20));
        statusLabel.setTextOverrun(OverrunStyle.ELLIPSIS);
        HBox.setHgrow(statusLabel, Priority.ALWAYS);

        Tooltip tip = new Tooltip();
        statusLabel.textProperty().addListener((obs, oldText, newText) -> tip.setText(newText));
        statusLabel.setTooltip(tip);

        HBox bar = new HBox(12, statusLabel);
        bar.setPadding(new Insets(8,12,8,12));
        return bar;
    }

    private VBox buildButtonsPane(Polinom polinom)
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

        inputNewPolinomButton.setOnAction(e -> inputNewPolinom(polinom));
        changeLeadCoeffButton.setOnAction(e -> changeLeadCoeff(polinom));
        changeRootButton.setOnAction(e -> changeRoot(polinom));
        resizeButton.setOnAction(e -> resize(polinom));
        evaluateButton.setOnAction(e -> evaluate(polinom));

        VBox box = new VBox(5, inputNewPolinomButton,
                changeLeadCoeffButton, changeRootButton, resizeButton, evaluateButton);
        box.setPadding(new Insets(20));
        box.setPrefWidth(260);
        box.setPrefHeight(400);
        box.setFillWidth(true);
        return box;
    }

    private void inputNewPolinom (Polinom polinom)
    {
        Label coeffLabel = new Label("Введите старший коэффициент:");
        Label rootsLabel = new Label("Вводите корни (1 или 2 числа на каждую строчку)");
        TextField coeffField = new TextField();
        TextArea rootsArea = new TextArea();
        Button applyBtnYes = new Button("Ок");

        applyBtnYes.setOnAction(e ->
            {
                try {
                    Number leadCoeff = parseNumber(coeffField.getText());
                    MyArray roots = new MyArray(0, null);
                    for (String line : rootsArea.getText().split("\\R")) {
                        if (!line.trim().isEmpty()) roots.add(parseNumber(line));
                    }
                    polinom.setLeadCoeff(leadCoeff);
                    polinom.setRoots(roots);
                    updateStatus(asText(polinom));

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

    private void changeLeadCoeff (Polinom polinom)
    {
        Label label = new Label("Введите новый коэффициент");
        TextField field = new TextField();
        Button applyBtn = new Button("Ок");
        GridPane pane = new GridPane();

        applyBtn.setOnAction(e ->
            {
                try {
                    Number coeff = parseNumber(field.getText());
                    polinom.setLeadCoeff(coeff);
                    updateStatus(asText(polinom));
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                }
            });
        pane.setHgap(10);
        pane.setVgap(10);
        pane.addRow(0, label, field, applyBtn);
        centerPane.getChildren().setAll(pane);
    }

    private void changeRoot (Polinom polinom)
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
                    Number root = parseNumber(rootField.getText());
                    polinom.setRoot(index, root);
                    updateStatus(asText(polinom));
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

    private void resize (Polinom polinom)
    {
        Label label = new Label("Введите новую размерность:");
        TextField field = new TextField();
        Button applyBtn = new Button("Применить");

        applyBtn.setOnAction(e ->
            {
                try {
                    int size = Integer.parseInt(field.getText());
                    polinom.resizeRoots(size);
                    updateStatus(asText(polinom));
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

    private void evaluate(Polinom polinom)
    {
        Label label = new Label("Введите х");
        TextField field = new TextField();
        Button applyBtn = new Button("Ок");
        Label res = new Label("");

        applyBtn.setOnAction(e ->
            {
                try {
                    Number x = parseNumber(field.getText());
                    Number result = polinom.evaluate(x);
                    res.setText("Значение полинома в данной точке P(" + x.toString() + ") = " + result.toString());
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

    private Number parseNumber(String s)
    {
        String[] strings = s.trim().split("\\s+");
        int len = strings.length;
        if (len == 1) {
            return new ComplexNumber(Double.parseDouble(strings[0]), 0);
        } else if (len == 2) {
            double re = Double.parseDouble(strings[0]);
            double im = Double.parseDouble(strings[1]);
            return new ComplexNumber(re, im);
        } else throw new IllegalArgumentException("Ожидается 1 или 2 вещественных числа");
    }

    private String asText(Polinom polinom)
    {
        return "Со скобками".equals(formats.getValue()) ?
                polinom.toStringWithBrackets() :
                polinom.toStringWithDegree();
    }

}
