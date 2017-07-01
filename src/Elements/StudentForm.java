package Elements;

import Library.School;
import javafx.scene.control.Control;

import java.util.ArrayList;

/**
 * Created by josh on 6/10/17.
 */
public class StudentForm<Student> extends SchoolMemberForm<Student> {


    static final String[] acceptedFields = {"firstName", "lastName", "grade", "id"};

    public StudentForm(School school, Student selected){
        super(school, acceptedFields, school.getStudentList(), selected, null);
    }

    protected Library.Student getInstanceOfT() {
        return new Library.Student(null, null, 0, getSchool().getRotationalDays()+1, getSchool().getDailyClasses()+1);
    }

    // TODO: Call this after a new object is selected
    protected void instantiateSpecial() {
        if (getSelected() != null) {
            Library.Student localSelected = (Library.Student)getSelected();
            ScheduleGrid test = new ScheduleGrid(getSchool(), localSelected);
            setSpecial(test);
        }
    }

    protected void setClassT() {
        classT = Library.Student.class;
    }

    protected void addSelected() {
        getSchool().getStudentList().add((Library.Student)getSelected());
    }


}
