package Elements;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;

/**
 * Provides an organized control of a comboBox and a button, and abstracts their action methods.
 * This class just saves a few lines, since this type of Control is reused both for SchoolMemberForm and in ClassForm
 * Created by josh on 6/9/17.
 */
public class ListManager extends HBox {

    public ComboBox getComboBox() {
        return comboBox;
    }


    public Button getButton() {
        return button;
    }


    private ComboBox comboBox;
    private Button button;
    private boolean showButton;

    public ListManager(ArrayList options) {
        showButton = false;
        setAlignment(Pos.CENTER);
        comboBox = new ComboBox(FXCollections.observableArrayList(options));
        setHgrow(comboBox, Priority.ALWAYS);
        getChildren().add(comboBox);
    }

    public ListManager(ArrayList options, String buttonText) {
        this(options);
        showButton = true;
        button = new Button(buttonText);
        this.getChildren().add(button);

    }

    public void setButtonAction(EventHandler<ActionEvent> eventHandler) {
        button.setOnAction(eventHandler);
    }

    public void setSelectAction(EventHandler<ActionEvent> eventHandler) {
        comboBox.setOnAction(eventHandler);
    }

    public boolean isButtonShown() {
        return showButton;
    }
}
