package Elements;

import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

/**
 * Created by josh on 6/8/17.
 * A GUI element which will
 */
public class InputField<T> extends HBox{

    public TextField textField;
    public Label label;
    public Field field;
    public Object object;



    public InputField(Field field, Object obj) {
        // Shouldn't leave this in "production code" but oh well
        // gotta make sure developers implementing this (me) aren't really dumb.
        //assert ((T) new Object()).getClass() == field.getType();

        this.field = field;
        this.object = obj;
        field.setAccessible(true);


            // Null check to allow the selected obj to be empty in SchoolMemberForm
            if (obj == null) {
                textField = new TextField();
            } else {
                try {
                    Object fieldVal = field.get(obj);
                    // Checks whether or not the actual value stored in the field is null, and if so assign blank string
                    textField = new TextField((fieldVal == null)? "" : fieldVal.toString());
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
            //textField = (obj == null)? new TextField() : new TextField((String)field.get(obj));
            //textField = new TextField();
            label = new Label(field.getName());

        //textField.setAlignment(Pos.CENTER_RIGHT);
        //label.setAlignment(Pos.CENTER_LEFT);
        setHgrow(textField, Priority.ALWAYS);

        getChildren().addAll(label, textField);
    }

    // Saves the text in the textField back to the referencing instance variable
    // There's probably a polymorphic way to do this in some elegant one-liner. This works though.
    // Throws exception so that you can yell at the user when then enter words instead of numbers
    public void save() throws ClassCastException {
        // A poor man's switch statement
        if (object != null) {
            System.out.println("object isnt null yay");
            try {
                if (field.getType() == Integer.class) {
                    field.setInt(object, Integer.parseInt(textField.getText()));
                } else if (field.getType() == String.class) {
                    System.out.println("object is " + object);

                    field.set(object, textField.getText());
                }
            } catch (IllegalAccessException e) {
                // (famous last words) this will never be thrown
            }
        } else {
            System.out.println("trying to save to null object");
        }
    }
}
