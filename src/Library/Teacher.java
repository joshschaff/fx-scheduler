package Library;

import java.io.Serializable;

/**
 * Created by josh on 5/26/17.
 */
public class Teacher extends Schedule implements Serializable{

    protected static final long serialVersionUID = 42L;

    public Teacher(String firstName, String lastName, int passedDays, int passedPeriods) {
        super(firstName, lastName, passedDays, passedPeriods);
    }

    /**
     * Precondition: called from instance of Classroom
     * @param passedDay
     * @param passedPeriod
     * @param classroom
     * @return
     */
    protected boolean setPeriod(int passedDay, int passedPeriod, Course classroom) {
        Course target = getCourses()[passedDay][passedPeriod];
        if (target != null) {
            target = classroom;
            return true;
        }
        return false;
    }
}
