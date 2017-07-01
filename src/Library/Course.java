package Library;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by josh on 5/26/17.
 * A note on naming of this class:
 * Logically, this class should be called "class". It serves
 * to collect the relationships between students and teachers
 * in a classroom. However, due to the fact that "class" is
 * obviously a java keyword, not only would it raise questions
 * of compilation, but documentation would get really confusing
 * really fast.
 *
 * For this purpose, this class has been named "Classroom".
 * While there are issues with this as well (namely the fact
 * that this class doesn't represent the physical location
 * but rather the gatherings hosted there), it will have to do.
 */
public class Course implements Serializable {

    protected static final long serialVersionUID = 42L;

    public int getDay() {
        return day;
    }

    public int getPeriod() {
        return period;
    }

    // When the class takes place
    private int day;
    private int period;

    //Unlike courses and students, the teacher reference is managed by the course class itself
    private Teacher teacher;
    private int room;
    private String name;
    private Roster students; // At this point this is essentially an arraylist

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }


    // Stands for abbreviation - used to represent the class on small buttons and fields
    private String abbrev;



    public Course(int day, int period, String name, String abbrev, Teacher teacher, int room) {
        this.name = name;
        this.abbrev = abbrev;
        this.day = day;
        this.period = period;
        this.teacher = teacher;
        this.room = room;
        students = new Roster(this);
    }

    public Teacher getTeacher() {
        return teacher;
    }

    /**
     * So I made a rule and I broke it. I said that the
     * reference relationships would be managed by calling
     * the people's scheduling methods from outside of the
     * library's context. This makes a lot of sense for
     * students, as their scheduling decisions are decided
     * by the needs of the student.
     *
     * However, in the case of teachers, their scheduling
     * needs are more of a macro issue. A teacher doesn't
     * always get to pick and chose what they want to teach
     * (sadly). But besides an argument about morale
     * inefficiencies from lack of self-determination, it
     * makes more sense that the classrooms should have the
     * role of assigning teachers apt to them.
     *
     * This will make more sense in terms of implementation
     * into a GUI later on (I hope), as the GUi will focus
     * more on students and classes as the main entities
     * rather than teachers, hence it is logical to bundle
     * functionality there.
     */
    public boolean setTeacher(Teacher target) {
        // If target teacher has this period free
        if (target.setPeriod(day, period, this)) {
            // remove class from current teacher's schedule
            if (teacher != null) {
                Course c = null;
                teacher.setPeriod(day, period, c);
            }
            teacher = target;
            return true;
        }
        return false;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    /**
     * Protected modifier is for very intentional purpose.
     * The structure of the library requires the specific
     * maintenance of references in multiple places, i.e.
     * people holding classrooms and classrooms holding
     * people. This is very intentional with the idea of
     * sorting and searching in mind.
     *
     * Namely, assume the functionality of retrieving all
     * the students in a class and the functionality of
     * retrieving all the classes in a student (schedule).
     * By only opting to place classes in people or people
     * in classes, one of thee two algorithms would become
     * very inefficient by requiring the filtering of an
     * entire list. This inefficiency would exponentially
     * increase with large sets of students and classes.
     *
     * Instead, I opted for efficiently managed compute time
     * and scalability at the cost of maintaining complex
     * reference pointer relationships. The protected modifier
     * ensures that these relationships are managed exclusively
     * by methods within the library's package, namely
     * those owned by people modifying their schedules.
     *
     * I felt this allocation of responsibility most
     * applicable considering this is a student scheduling
     * system, so it is the ___Period methods that are
     * called on students (and teachers) from a context
     * outside the library.
     */
    protected boolean addStudent(Student passedStudent) {
        return students.add(passedStudent);
    }

    public Roster getStudents() {
        return students;
    }

    @Override
    public String toString() {
        return (this.name== null)? "New" : this.name;
    }
}
