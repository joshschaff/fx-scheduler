package Elements;

import Library.Course;
import Library.Schedule;
import Library.School;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Created by josh on 6/9/17.
 */
public class ScheduleGrid extends GridPane {

    // Gaps between elements of rows and column
    // Must be static to use "before the superconstructor is called")
    //static final int GAP_SIZE = 10;

    private GridChild[][] children;

    private Schedule schedule;

    private School school;


    /** TODO: Do I wanna pass courses or Schedule children to this class?
     * @param school
     * @param schedule
     */
    public ScheduleGrid(School school, Schedule schedule) {
        setAlignment(Pos.CENTER);
        this.schedule = schedule;
        this.school = school;
        // implicit super class
        Course[][] courses = schedule.getCourses();
        children = new GridChild[courses.length][courses[0].length];
        buildChildren(courses);
        fillGrid();
    }

    private void buildChildren(Course[][] courses) {
        ColumnConstraints cc = new ColumnConstraints();
        cc.setMaxWidth(Double.MAX_VALUE);
        cc.setFillWidth(true);
        cc.setHgrow(Priority.ALWAYS);
        children = new GridChild[courses.length][courses[0].length];

        for (int day = 0; day < courses.length; day ++) {
            for (int period = 0; period < courses[day].length; period ++) {
                children[day][period] = new GridChild(courses[day][period], schedule, school, day, period, true);
                //setHgrow(children[day][period], Priority.ALWAYS);
                setFillWidth(children[day][period], true);
                children[day][period].setMaxWidth(Double.MAX_VALUE);



            }
            getColumnConstraints().add(cc);
        }
    }

    private void fillGrid() {
        for (int day = 0; day < children.length; day ++) {
            for (int period = 0; period < children[day].length; period ++) {
                add(children[day][period], day, period);
            }
        }
    }
}
