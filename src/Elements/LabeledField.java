package Elements;

import Elements.Forms.TeacherForm;
import Library.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Created by josh on 6/11/17.
 * C is the type of special control used to edit - either a button or a textfield
 * Automatically choses the right control to represent a field
 */
public class LabeledField extends HBox {

    // label holding the name of the field
    private Label label;

    // The variable control that edits the value of the field
    private Control edit;

    // The field being accessed
    private Field field;

    // Actual value stored in field by instance, boxed
    public Object fieldVal;

    // The instance holding the field
    private Object instance;

    private School school;

    private boolean editable;

    public LabeledField(Field field, Object instance, School school, boolean editable) {
        field.setAccessible(true);

        this.field = field;
        this.instance = instance;
        this.school = school;
        this.editable = editable;

        Type fieldType = field.getType();

        // Null check to allow the selected obj to be empty in SchoolMemberForm
        if (fieldType == String.class) {
            edit = new TextField();
            ((TextField)edit).setEditable(editable);
        } else if (fieldType.getClass().isPrimitive() || fieldType == Integer.TYPE) {
            edit = new TextField();
            ((TextField)edit).setEditable(editable);
        } else {
            edit = new Button();
            ((Button) edit).setOnAction(event -> onClick());
        }

        if (instance != null) {
            try {
                fieldVal = field.get(instance);
                // Checks whether or not the actual value stored in the field is null, and if so assign blank string
                setText(edit, (fieldVal == null)? "" : (field.getName().equals("day") || field.getName().equals("period"))? Integer.toString((int)fieldVal +1) : fieldVal.toString());
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        //textField = (obj == null)? new TextField() : new TextField((String)field.get(obj));
        //textField = new TextField();
        label = new Label(field.getName());

        //textField.setAlignment(Pos.CENTER_RIGHT);
        //label.setAlignment(Pos.CENTER_LEFT);
        setHgrow(edit, Priority.ALWAYS);
        setMaxWidth(Double.MAX_VALUE);
        edit.setMaxWidth(Double.MAX_VALUE);

        getChildren().addAll(label, edit);
    }

    private void onClick() {
        TeacherForm teachSelect = new TeacherForm(school, (Teacher)fieldVal);
        Optional<Teacher> diaResult = teachSelect.showAndWait();
        if (diaResult.isPresent()) {
            if (fieldVal instanceof Teacher) {
                ((Teacher) fieldVal).getCourses()[((Course) instance).getDay()][((Course) instance).getPeriod()] = null;
            }
            fieldVal = diaResult.get();
            //System.out.println("set was successful: " + ((Student) schedule).setPeriod(day, period, diagResult.get()));
            //schedule.getCourses()[day][period] = course;
        }
        if (fieldVal != null) {
            setText(edit, ((Teacher)fieldVal).toString());
        }
        save();
        // TODO: do the whole result check thingy, then implement in save();
    }



    public void save() throws ClassCastException {
        // A poor man's switch statement
        if (instance != null && editable) {
            //System.out.println("object isnt null yay");
            try {
                if (field.getType().getClass().isPrimitive() || field.getType() == Integer.TYPE) {
                    /*
                    if (field.getName().equals("day") || field.getName().equals("period")) {
                        field.setInt(instance, Integer.parseInt(((TextField)edit).getText()) -1);
                        System.out.println("special short field");
                    } else {
                    }*/
                    TextField tf = (TextField) edit;
                    //String text = new String();
                    //((TextField) edit).getText().chars().filter(it -> Character.isDigit(it)).mapToObj(i -> new Character((char) i)).forEach(it -> text.concat(it.toString()));
                    tf.setText(tf.getText().replaceAll("[^\\d.]", ""));
                    field.setInt(instance, Integer.parseInt(tf.getText()));
                } else if (field.getType() == String.class) {
                    field.set(instance, ((TextField)edit).getText());
                } else {
                    if (fieldVal instanceof Teacher) {
                        if (((Teacher) fieldVal).getCourses()[((Course) instance).getDay()][((Course) instance).getPeriod()] != null) {
                            ((Teacher) fieldVal).getCourses()[((Course) instance).getDay()][((Course) instance).getPeriod()] = null;
                        }
                        field.set(instance, fieldVal);
                        //System.out.println("We're saving a teacher");
                        ((Course) instance).setTeacher((Teacher) fieldVal);
                        ((Teacher) fieldVal).getCourses()[((Course) instance).getDay()][((Course) instance).getPeriod()] = (Course) instance;
                    }
                }
            } catch (IllegalAccessException e) {
                // (famous last words) this will never be thrown
            }
        } else {
            //System.out.println("trying to save to null object");
        }
    }


    private static boolean setText(Control c, String s) {
        if (c instanceof Button) {
            ((Button) c).setText(s);
            return true;
        } else if (c instanceof TextField) {
            ((TextField) c).setText(s);
            return true;
        }
        return false;
    }


}
