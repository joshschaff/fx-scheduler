package Elements.Forms;

import Elements.ListManager;
import Library.School;
import Library.Student;

import java.util.*;

import static Elements.DialogHelp.getIntDialog;

/**
 * Created by josh on 6/11/17.
 */
public class CourseForm<Course> extends SchoolMemberForm<Course> {

    static final String[] acceptedFieldNames = {"name", "abbrev", "room", "capacity", "teacher"};

    static final String[] nonEditableFieldNames = {"day", "period"};

    private int day;

    private int period;

    // Access to just courses during a designated period
    public CourseForm(int day, int period, School school, Course selected) {
        super(school, acceptedFieldNames, school.getCourseList().stream()
                .filter(c -> c.getDay() == day && c.getPeriod() == period)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll),
                selected, nonEditableFieldNames);
        this.day = day;
        this.period = period;
    }


    // Access to all fo the courses during all of the periods
    public CourseForm(School school, Course selected){
        super(school, acceptedFieldNames, school.getCourseList(), selected, nonEditableFieldNames);
        this.day = -1;
        this.period = -1;
    }

    protected Library.Course getInstanceOfT() {
        if (day > -1 && period > -1) {
            return new Library.Course(day, period, null, null, null, 0);
        }
        // Must add one to convert from integer counting to natural counting
        int localDay = getIntDialog(getSchool().getRotationalDays()+1,
                "What day does this class take place?",
                "Please select a day.",
                "Day:");

        int localPeriod = getIntDialog(getSchool().getDailyClasses()+1,
                "What period does this class take place?",
                "Please select a period.",
                "Period:");

        return new Library.Course(localDay, localPeriod, null, null, null, 0);
    }

    // TODO: Call this after a new object is selected
    protected void instantiateSpecial() {
        if (getSelected() != null) {
            //System.out.println("students=" + ((Library.Course)getSelected()).getStudents());
            setSpecial(new ListManager(((Library.Course)getSelected()).getStudents(), "View"));

            ((ListManager)getSpecial()).setButtonAction(event ->  {
                Student selectedStud = (Student) ((ListManager) getSpecial()).getComboBox().getValue();
                if (selectedStud != null) {
                    new StudentForm(getSchool(), selectedStud).showAndWait();


                    /*Optional<Student> result = new StudentForm(getSchool(), selectedStud).showAndWait();
                    if (result.isPresent()) {
                        //if (((Library.Course) getSelected()).getStudents().contains((Student)result.get())) {
                        if (selectedStud.getCourses()[day][period] != null) {
                            System.out.println("Still got it");
                        } else {
                            System.out.println("It's gone");
                        }
                    }*/
                }
                setSpecial(new ListManager(((Library.Course)getSelected()).getStudents(), "View"));
                getVbox().getChildren().remove(getVbox().getChildren().size()-1);
                getVbox().getChildren().add(getSpecial());
            });

        }
    }


    protected void setClassT() {
        classT = Library.Course.class;
    }

    protected void addSelected() {
        getSchool().getCourseList().add((Library.Course)getSelected());
    }

}