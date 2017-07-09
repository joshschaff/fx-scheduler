import Elements.*;
import Elements.Forms.CourseForm;
import Elements.Forms.StudentForm;
import Elements.Forms.TeacherForm;
import Library.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

import static Elements.DialogHelp.getIntDialog;
import static Elements.DialogHelp.getStringDialog;

/**
 * Created by josh on 5/25/17.
 * Will have three different sections, file /data/reports, with vertical space in between.
 * THIS IS MY MAIN CLASS! EVERYTHING ELSE COMES FROM HERE!
 */
public class GUIScheduler extends Application {

    private School school; // My working "database" field - will be serialized and deserialized
    private FileManager<School> fileManager; // This class manages all saving and loading functionality, in the background
    private final String EXTENSION = "school";

    private Stage primaryStage;

    private VBox root;

    private VBox file;
    // Instantiated by default to hold app directory
    private TextField path;
    // Displays the name of the current file, otherwise a default prompt, which launches a choiceDialog for choosing file in directory;
    private ListManager fileSelect;


    private VBox data;
    private Button students;
    private Button teachers;
    private Button classes;


    private VBox reports;
    // Prints info about the current school, the numbers of all the items, the directory, etc
    private Button generalReport;
    // prints a schedule report for all students
    private Button studentReports;




    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // save no matter what on close request
        primaryStage.setOnCloseRequest(windowEvent -> saveCurrent());
        fileManager = new FileManager<>(EXTENSION);
        path = new TextField(fileManager.getDirectory().toString());
        buildIndependentGUI();



        /*school = new School(2, 4);
        school.addStudent(new Student("Josh", "Schaffer", 11, 2, 4));
        //System.out.println(school.getStudentList().size());
        school.getCourseList().add(new Course(1, 3, "AP Computer Science", "APCS", null, 305));*/
    }

    private void setSchool(School school) {
        if (this.school != null) {
            saveCurrent();
        }
        this.school = school;
        //System.out.println(this.school);
        if (!fileManager.getObjectList().contains(school)) {
            fileManager.getObjectList().add(school);
            //fileSelect.getComboBox().getItems().add(school);
            //System.out.println(fileManager.getObjectList());
        }
        buildDependentGUI();
        fileSelect.getComboBox().setValue(school);
        if (!fileSelect.getComboBox().getItems().contains(school)) {
            fileSelect.getComboBox().getItems().add(school);
        }
        //updateFileSelect();
    }

    private void buildIndependentGUI() {

        path.setOnAction(event -> {
            File test = new File(path.getText());
            if (!test.isDirectory()) {
                path.setText(fileManager.getDirectory().toString());
            } else {
                saveCurrent();
                fileManager = new FileManager(EXTENSION, test);
                school = null;
                buildIndependentGUI();
            }
        });

        updateFileSelect();

        file = new VBox(5);
        file.setAlignment(Pos.CENTER);
        file.getChildren().addAll(path, fileSelect);

        root = new VBox(25, file);
        root.setAlignment(Pos.CENTER);
        root.setFillWidth(true);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        buildDependentGUI();
        primaryStage.sizeToScene();
    }

    private void updateFileSelect() {
        fileSelect = new ListManager(fileManager.getObjectList(), "+");
        fileSelect.setButtonAction(event -> setSchool(getNewSchool()));
        fileSelect.setSelectAction(event -> setSchool((School)fileSelect.getComboBox().getValue()));
    }

    private void buildDependentGUI() {
        root.getChildren().removeAll(data, reports);

        if (school != null) {

            students = new Button("Edit Students");
            students.setOnAction(event -> new StudentForm(school, null).showAndWait());
            teachers = new Button("Edit Teachers");
            teachers.setOnAction(event -> new TeacherForm(school, null).showAndWait());
            classes = new Button("Edit Classes");
            classes.setOnAction(event -> new CourseForm(school, null).showAndWait());
            data = new VBox(5);
            data.setAlignment(Pos.CENTER);
            data.getChildren().addAll(students, teachers, classes);

            generalReport = new Button("Generate School Report");
            generalReport.setOnAction(event -> generateSchoolReport());
            studentReports = new Button("Generate Student Reports");
            studentReports.setOnAction(event -> generateStudentReports());
            reports = new VBox(5);
            reports.setAlignment(Pos.CENTER);
            reports.getChildren().addAll(generalReport, studentReports);

            root.getChildren().addAll(data, reports);
        }
        primaryStage.sizeToScene();
    }

    private void buildGUI() {

        path = new TextField(fileManager.getDirectory().toString());
        path.setOnAction(event -> {
            File test = new File(path.getText());
            if (!test.isDirectory()) {
                path.setText(fileManager.getDirectory().toString());
            } else {
                fileManager = new FileManager(EXTENSION);
            }
        });
        fileSelect = new ListManager(fileManager.getObjectList(), "+");
        fileSelect.setButtonAction(event -> setSchool(getNewSchool()));
        fileSelect.setSelectAction(event -> setSchool((School)fileSelect.getComboBox().getValue()));

        file = new VBox(5);
        file.setAlignment(Pos.CENTER);
        file.getChildren().addAll(path, fileSelect);

        root = new VBox(25, file);
        root.setAlignment(Pos.CENTER);
        root.setFillWidth(true);

        //System.out.println("school is " + school);
        if (school != null) {

            students = new Button("Edit Students");
            students.setOnAction(event -> new StudentForm(school, null).showAndWait());
            teachers = new Button("Edit Teachers");
            teachers.setOnAction(event -> new TeacherForm(school, null).showAndWait());
            classes = new Button("Edit Classes");
            classes.setOnAction(event -> new CourseForm(school, null).showAndWait());
            data = new VBox(5);
            data.setAlignment(Pos.CENTER);
            data.getChildren().addAll(students, teachers, classes);

            generalReport = new Button("Generate School Report");
            generalReport.setOnAction(event -> generateSchoolReport());
            studentReports = new Button("Generate Student Reports");
            studentReports.setOnAction(event -> generateStudentReports());
            reports = new VBox(5);
            reports.setAlignment(Pos.CENTER);
            reports.getChildren().addAll(generalReport, studentReports);

            root.getChildren().addAll(data, reports);
        }

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private School getNewSchool() {
        String name = getStringDialog("school","What would you like to name the school?",
                "Please enter a name.", "School name: ");
        int day = getIntDialog(10, "How many rotational days does the school have?",
                "Please enter the number of rotational days", "Rotational days: ");
        int period = getIntDialog(10, "How many periods are there per day?",
                "Please enter the number of periods", "Periods: ");
        return new School(name, day, period);
    }

    private void saveCurrent() {
        if (school != null) {
            int i = 1;
            String name = school.getName();
            while (!fileManager.safelySerialize(school, school.getName())) {
                name = name + i;
                i += 1;
            }
        }
    }


    /**
     * This is for 4B! - all properties of each instance of Student
     */
    private void generateSchoolReport() {
        String p = new String();
        p += school.toString() + " (" + (school.getRotationalDays()+1) + ":" + (school.getDailyClasses()+1) + ")\n";
        p += "\nThere are " + school.getStudentList().size() + " students:\n";
        for (Student s : school.getStudentList()) {
            p += "\t" + s + " (" + s.getGrade() +")\n";
        }
        p += "\nThere are " + school.getTeacherList().size() + " teachers:\n";
        for (Teacher t : school.getTeacherList()) {
            p += "\t" + t +"\n";
        }
        p += "\nThere are " + school.getCourseList().size() + " classes:\n";
        for (Course course : school.getCourseList()) {
            p += "\t" + course + "(" + course.getRoom() + ", " + course.getTeacher() + ")" +"\n";
        }
        System.out.println(p);
    }

    /**
     * This is for 4A! - all properties of each instance of Student
     */
    private void generateStudentReports() {
        String p = new String();
        for (Student s : school.getStudentList()) {
            p += s + " (" + s.getGrade() +")\n"; // Prints student's name
            for (int d =0; d < school.getRotationalDays() + 1; d++) {
                for (int c =0; c < school.getDailyClasses() +1; c++) {
                    Course course = s.getCourses()[d][c];
                    // Print couse with day, period, name, room, and teacher
                    p += "\t" + (d+1) + ":" + (c+1) + " " + ((course != null)? course + "(" + course.getRoom() + ", " + course.getTeacher() + ")" : "Free") + "\n";
                }
                p += "\n";
            }
            p += "\n\n";
        }
        System.out.println(p);
    }
}
