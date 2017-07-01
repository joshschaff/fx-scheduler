package Elements;

import Library.School;
import Library.Student;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;
import java.util.*;
import java.util.List;



/**
 * Created by josh on 6/5/17.
 * This is the parent class of all of my input screens (2 on requirements);
 * This class has three parts. First there is the list manager, which manages a collection of items of the paramterized
 * type in order to obtain a "selected" item. This item, or instance, is what the class is meant to display. The second
 * part of the class is the fields. These take the form of editable and noneditable, and are represented via labledFields,
 * which represent either an Integer, a String, or a teacher. The final part of this class is coined the "special", and
 * is unique to each subclass, but is some control referencing other data objects.
 */
public abstract class SchoolMemberForm<T> extends Dialog {

    private T selected; // The selected T

    private Field[] fields; // Actual fields of the T class

    private Field[] nonEditableFields;

    private List<LabeledField> labeledFields;

    private String[] acceptedFieldNames;

    private String[] nonEditableFieldNames;

    private ListManager listManager;

    private School school; // similar to parent

    // This is some special control specific to the sub class implementation. For schedule-holders, it is a visual
    // representation of their schedule, via schedule grid. For classes, it is a student-list comboBox, via listManager
    private Parent special; // GRIDLAYOUT for SCHEDULES

    // This vbox will hold all of the fields as well as the selector and the special
    private VBox vbox;

    protected Class classT;

    public ArrayList options; // options for selected

    private boolean newItem; // used for determining whther to add to list or not



    /**
     * It is assumed that you would extend this class, and in the class header the parameterized T would be specified.
     * Then, preferably as a constant instance variable, an array of accepted field names the form would be responsible
     * for would be passed to this constructor, in the first line of the new constructor. Then all of the type specific
     * code would follow. Simple :)
     * @param
     */


    /*public SchoolMemberForm(School school, String[] acceptedFieldNames, ArrayList options, T selected, String[] nonEditableFieldNames) {
        this(school, acceptedFieldNames, options, selected);
        this.nonEditableFieldNames = nonEditableFieldNames;

        System.out.println("we passed noneditable fields...");
        for (String f : nonEditableFieldNames) {
            System.out.println("nonEditableField: "+ f);
        }
    }*/

    @SuppressWarnings("unchecked")
    public SchoolMemberForm(School school, String[] acceptedFieldNames, ArrayList options, T selected, String[] nonEditableFieldNames) {
        this.school = school;
        this.acceptedFieldNames = acceptedFieldNames;
        this.options=options;
        this.nonEditableFieldNames = nonEditableFieldNames;

        newItem = false;
        labeledFields = new ArrayList<>();
        instantiateListManager(options);

        vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.setFillWidth(true);

        vbox.getChildren().add(listManager);
        instantiateSpecial();



        //this.classT = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getClass();




        getDialogPane().setContent(vbox);
        getDialogPane().getButtonTypes().addAll(new ButtonType[]{ButtonType.OK, ButtonType.CANCEL});


        setResultConverter(button -> {
            // Returns either the current selected object (on OK) or null (on CANCEL)
            return (button == ButtonType.OK)? onSuccess() : onCancel();
        });

        // Sets classT
        /*
        try {
            //classT = this.getClass().getField("options").getClass();
            classT = options.get(0).getClass();
            System.out.println("classT = " + classT.getName());
        } catch (Exception ex) {
            System.out.println("NoSuch Field");
        }*/
        setClassT();

        //instantiateSpecial();

        //buildGUI();
        setSelected(selected);
    }

    protected abstract void setClassT();

    private void setSelected(T selected) {
        this.selected = selected;
        updateGUI();
        //System.out.println("selected is " + selected);
        newItem = (selected == null) ? false : selected.toString().equals("New") ;

    }

    private T onCancel() {
        //System.out.println("ONCANCEL");
        if (newItem == true) {
            if (options.contains(selected)) {
                options.remove(selected);
            }
        }
        return null;
    }

    private void instantiateListManager(ArrayList options) {
        listManager = new ListManager(options, "+");
        listManager.setButtonAction(event -> newAction());
        listManager.setSelectAction(event -> selectAction());
    }

    private void newAction() {
        T newInstance = getInstanceOfT();
        setSelected(newInstance);
        /*Optional <Field> fieldResult = Arrays.stream(school.getClass().getDeclaredFields()).filter(it -> it.getType() == Collection.class).findFirst();
        if (fieldResult.isPresent()) {
            System.out.println("We made it: Found matching list");
            Field f = fieldResult.get();
            f.setAccessible(true);
            try {
                System.out.println("We made it: Trying to add to list");
                ((List<T>) (f.get(school))).add(newInstance);
            } catch (Exception ex) {

            }
        }*/
        //System.out.println("newItem ="  + newItem);
        //System.out.println(options);
        //options.add(newInstance);
        addSelected();
        //System.out.println(options);
        //System.out.println("options contians new item =" + options.contains(selected));
    }

    protected abstract void addSelected();

    private void selectAction() {
        onSuccess();
        setSelected((T)listManager.getComboBox().getValue());
        if (selected instanceof Student) {
            //System.out.println(((Student) selected).getName());
        }
        //buildGUI();
    }

    // This will return a default instantiation of the form's current
    abstract <X extends T> X getInstanceOfT();


    /**
     * POSTCONDITION: Must actuall add these fields to the GUI!
     */
    private void updateInputFields() {
        // I don't think you need to call this because acceptedfields would never change...?
        // Have to call it at least first time?
        updateFields();
        // Builds labeledFields based upon the fields

        // removes old input fields from GUI
        if (vbox.getChildren().size() > 1) {
            //System.out.println("there are this many labeled fields" + labeledFields.size());
            vbox.getChildren().remove(1, labeledFields.size() + 1);
        }

        //System.out.println("children after removing: " + vbox.getChildren());

        labeledFields = new ArrayList<>();

        //Arrays.stream(fields).forEach(it -> System.out.println(it));

        for (Field f : fields) {
            labeledFields.add(new LabeledField(f, selected, school, true));
        }

        if (nonEditableFields != null) {
            for (Field f : nonEditableFields) {
                //System.out.println("nonEditableField: "+nonEditableFields);
                labeledFields.add(new LabeledField(f, selected, school, false));
            }
        }

        //labeledFields = Arrays.stream(fields).map(it -> new InputField(it, selected)).collect(Collectors.toList());
        //System.out.println("There are this many inputfields: " + labeledFields.size());
    }

    private void updateFields() {
        /*
        // Grabs all of the fields of a class, and filters based upon whether or not the name were in the accepted fields.
        fields = getInheritedPrivateFields(classT).stream()
                .filter(it -> Arrays.asList(acceptedFields).contains(it.getName())).toArray(Field[]::new);
        // This is necessary in order to modify private instance variables. That sounds scary, but the developer would
        // have already delegated access to these fields by including the field name in $acceptableFields.
        // TODO: Is setting fields accessible a property of the scope or metadata within the Field object?
        Arrays.stream(fields).forEach(it -> it.setAccessible(true)); */
        List<Field> allFields = getInheritedPrivateFields(classT);

        //System.out.println("allFields = "+ allFields);

        List<String> acceptedFieldsList = Arrays.asList(acceptedFieldNames);


        fields = allFields.stream().filter(it -> acceptedFieldsList.contains(it.getName())).toArray(Field[]::new);

        if (nonEditableFieldNames != null) {
            List<String> nonEditableFieldsList = Arrays.asList(nonEditableFieldNames);
            nonEditableFields = allFields.stream().filter(it -> nonEditableFieldsList.contains(it.getName())).toArray(Field[]::new);
        }

        //System.out.println(acceptedFieldsList);
        //System.out.println(allFields);
        //System.out.println(fields);

        //System.out.println("foundFields = " + fields.length);
    }

    /**
     * Saves all of the
     * @return The selected object of this form
     */
    private T onSuccess() {
        if (selected!= null ) {
            labeledFields.stream().forEach(f -> f.save());
        }
        return selected;
    }

    abstract void instantiateSpecial();


    protected void setSpecial(Parent c) {
        this.special = c;
    }

    protected Parent getSpecial() {
        return this.special;
    }

    public School getSchool() {
        return this.school;
    }

    public T getSelected() {
        return this.selected;
    }

    private void buildGUI() {

        vbox.getChildren().add(listManager);

        //List<Field> allFields = getFieldsUpTo(classT, Object.class);

        //System.out.println(classT);


        updateGUI();
    }

    private void updateGUI() {
        // Saves all of the current fields
        onSuccess();

        listManager.getComboBox().setValue(selected);

        // Regenerates all inputfields for the current object
        updateInputFields();


        // InstantiateSpecial is abstract so this goes here as you must remove the current special on every selected update
        vbox.getChildren().remove(special);
        instantiateSpecial();


        /* OLD CODE
        List<Field> allFields = getInheritedPrivateFields(classT);

        List<String> acceptedFieldsList = Arrays.asList(acceptedFields);

        Field[] foundFields =
                allFields.stream().filter(it -> acceptedFieldsList.contains(it.getName())).toArray(Field[]::new);

        System.out.println(acceptedFieldsList);
        System.out.println(allFields);
        System.out.println(foundFields);

        System.out.println("foundFields = " + foundFields.length);

        for (Field f : foundFields) {
            labeledFields.add(new InputField(f, selected));
        } */
        if (selected != null) {
            for (LabeledField ipf : labeledFields) {
                vbox.getChildren().add(ipf);
            }
        }

        if (special != null) {
            vbox.getChildren().add(special);
        }
    }


    /**
     * Stolen straight from stackoverflow!
     * https://stackoverflow.com/questions/16966629/what-is-the-difference-between-getfields-and-getdeclaredfields-in-java-reflectio#16966699
     * @param startClass
     * @param exclusiveParent
     * @return
     */
    public static List<Field> getFieldsUpTo(Class<?> startClass,
                                                @Nullable Class<?> exclusiveParent) {

        List<Field> currentClassFields = Arrays.asList(startClass.getDeclaredFields());
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null &&
                (exclusiveParent == null || !(parentClass.equals(exclusiveParent)))) {
            List<Field> parentClassFields =
                    (List<Field>) getFieldsUpTo(parentClass, exclusiveParent);
            currentClassFields.addAll(parentClassFields);
        }

        return currentClassFields;
    }

    private List<Field> getInheritedPrivateFields(Class<?> type) {
        List<Field> result = new ArrayList<Field>();

        Class<?> i = type;
        while (i != null && i != Object.class) {
            Collections.addAll(result, i.getDeclaredFields());
            i = i.getSuperclass();
        }

        return result;
    }


    protected VBox getVbox() {
        return this.vbox;
    }
}
