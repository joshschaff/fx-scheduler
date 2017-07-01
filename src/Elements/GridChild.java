package Elements;

import Library.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Created by josh on 6/9/17.
 * A child of the visual representation of the Courses in a schedule
 * Also... this class is the listener for the individual courses.
 * It must listen to see if the course is null, or not, and change its appearance to offer the correct functionality.
 * If the field is null, a single button will be shown in order to
 */
public class GridChild extends HBox {

    // The course this element represents, and grants the ability to edit
    private Course course;

    // The person taking this class, or owning the schedule
    private Schedule schedule;

    // Either used to edit current instance or create new instance
    private Button edit;

    // Clears the referencing course from the schedule
    private Button clear;

    private String editText = "Edit";

    private String clearText = "X";

    private boolean access;

    private int day;

    private int period;

    private School school;

    // if null, provide a plus button to add one
    // if not null, provide edit button and x
    // TODO: Could possibly replace editable with instanceof check to see if student or not, less polymorphic but eh
    public GridChild(Course course, Schedule schedule, School school, int day, int period, boolean access) {
        this.course = course ;//schedule.getCourses()[day][period];
        this.schedule = schedule;
        this.access = access;
        this.day = day;
        this.period = period;
        this.school = school;
        this.editText = ((schedule instanceof Student)?"Edit " : "View ") + (day+1) + ":" + (period+1);
        this.clearText = "X";
        edit = new Button();
        clear = new Button();
        edit.setText(editText);
        clear.setText(clearText);
        setHgrow(edit, Priority.ALWAYS);
        edit.setMaxWidth(Double.MAX_VALUE);
        buildGUI();

        //System.out.println("the Course on SchedChild " + day + ":" + period + " is " + course);

        try {
            ((Button) edit).setText(course.toString());
        } catch (Exception ex) {

        }
    }



    private void clearEvent() {
        // Actually clears the class from schedule, and clears student from Roster in method
        //System.out.println("schedule is" + schedule);
        ((Student)schedule).clearPeriod(day, period);
        //System.out.println("cleared period is now = " + schedule.getCourses()[day][period]);
        course = schedule.getCourses()[day][period];
        //System.out.println("course is now = " + course);
        // Removes the clear button because there's no more course to clear!
        getChildren().remove(clear);
        edit.setText(editText);
    }

    private void editEvent() {
        // TODO: set onaction of edit to open course edit form (pass null as selected?)
        // Must also check to see if null, and then add clear button or not

        boolean contianedPrior = (course != null)? course.getStudents().contains(schedule): false;

        CourseForm courseSelect = new CourseForm(day, period, school, course);
        Optional<Course> diagResult = courseSelect.showAndWait();
        if (schedule instanceof Student) {
            clearEvent();
            if (diagResult.isPresent()) {
                course = diagResult.get();
                ((Student) schedule).setPeriod(day, period, diagResult.get());
                //System.out.println("set was successful: " + ((Student) schedule).setPeriod(day, period, diagResult.get()));
                //System.out.println("contianed prior: " + contianedPrior);
                //System.out.println("contians now: " + course.getStudents().contains(schedule));
                schedule.getCourses()[day][period] = course;
                //course.getStudents().add(schedule);
                //System.out.println("students=" + course.getStudents());
                //((Student) schedule).setPeriod(day, period, course);
            }
        }
        buildGUI();

        //System.out.println("daCourse = " + ((Student) schedule).getCourses()[1][3]);
    }

    private void buildGUI() {
        getChildren().remove(0, getChildren().size());
        getChildren().add(edit);
        edit.setOnAction(event -> editEvent());
        if (course != null) {
            //System.out.println("setting text on" + day + period);
            edit.setText(course.getAbbrev());
            if (access == true && schedule instanceof Student) {
                getChildren().add(clear);
                clear.setOnAction(event -> clearEvent());
            }
        }
    }
}
