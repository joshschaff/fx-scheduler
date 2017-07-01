package Elements;

import Library.School;
import Library.Teacher;

import java.util.ArrayList;

/**
 * Created by josh on 6/12/17.
 */
public class TeacherForm<Teacher> extends SchoolMemberForm<Teacher> {
    static final String[] acceptedFields = {"firstName", "lastName"};

    public TeacherForm(School school, Teacher selected){
        super(school, acceptedFields, school.getTeacherList(), selected, null);
    }

    protected Library.Teacher getInstanceOfT() {
        return new Library.Teacher(null, null, getSchool().getRotationalDays()+1, getSchool().getDailyClasses()+1);
    }

    // TODO: Call this after a new object is selected
    protected void instantiateSpecial() {
        if (getSelected() != null) {
            Library.Teacher localSelected = (Library.Teacher)getSelected();
            ScheduleGrid test = new ScheduleGrid(getSchool(), localSelected);
            setSpecial(test);
        }
    }

    protected void setClassT() {
        classT = Library.Teacher.class;
    }

    protected void addSelected() {
        getSchool().getTeacherList().add((Library.Teacher)getSelected());
    }
}
