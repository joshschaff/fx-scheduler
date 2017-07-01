package Library;

import java.io.Serializable;

/**
 * Created by josh on 5/26/17.
 */
public class Student extends Schedule implements Serializable{

    protected static final long serialVersionUID = 42L;

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    private int grade;

    /*
     * A note on student class relations:
     * Maybe there is an object missing from the design scheme. Students should still be able to access the class.
     * Classes should instead hold a roster object. That roster object will
     */


    public Student(String firstName, String lastName, int grade, int passedDays, int passedPeriods) {
        super(firstName, lastName, passedDays, passedPeriods);
        this.grade = grade;
    }

    public boolean setPeriod(int passedDay, int passedPeriod, Course classroom) {
        Course target = getCourses()[passedDay][passedPeriod];
        // checks if schedule slot is empty
        if (target == null) {
            // checks if we're under capacity
            if (classroom.addStudent(this)) {
                target = classroom;
                return true;
            }
        }
        return false;
    }


    /**
     * PRECONDITION: Passed period is not null in schedule
     * @param passedDay
     * @param passedPeriod
     * @return
     */
    public boolean clearPeriod(int passedDay, int passedPeriod) {
        Course removed = getCourses()[passedDay][passedPeriod];
        if (removed != null) {
            removed.getStudents().remove(this);
            getCourses()[passedDay][passedPeriod] = null;
        }
        return true;
    }
}
