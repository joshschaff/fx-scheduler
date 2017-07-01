package Library;

import java.beans.*;
import java.io.Serializable;

/**
 * Created by josh on 5/26/17.
 */
public abstract class Schedule implements Serializable{

    protected static final long serialVersionUID = 42L;

    private Course[][] courses;

    public Course[][] getCourses() {
        return courses;
    }

    public void setClassrooms(Course[][] classrooms) {
        this.courses = classrooms;
    }

    public String getName() {
        return firstName;
    }

    public void setName(String name) {
        this.firstName = name;
    }

    private String firstName;

    private String lastName;

    private PropertyChangeSupport mPcs =
            new PropertyChangeSupport(this);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;


    public Schedule(String firstName, String lastName, int passedDays, int passedPeriods) {
        this.firstName = firstName;
        this.lastName = lastName;
        courses = new Course[passedDays][passedPeriods];
        //System.out.println(passedDays + " " + passedPeriods);
    }

    // missing access modifier to allow for public by student and protected by teacher
    abstract boolean setPeriod(int passedDay, int passedPeriod, Course course);

    // Overriden in order to display name in comboBoxes
    @Override
    public String toString() {

        return (firstName != null && lastName != null )? this.firstName + " " + this.lastName : "New";
    }
}
