package Library;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by josh on 5/26/17.
 * Provides context for declaring classes and schedules
 * Provides ability to save itself to disk
 */
public class School implements Serializable {

    private String name;

    // Necessary for serialization - basically a "version number" for the class blueprint
    // This is because serialization was designed to be used over far far distances where it would be common not to
    // have the exact same class file at the serialization and deserialization
    protected static final long serialVersionUID = 42L;

    public int getRotationalDays() {
        return rotationalDays;
    }

    public void setRotationalDays(int rotationalDays) {
        this.rotationalDays = rotationalDays;
    }

    public int getDailyClasses() {
        return dailyClasses;
    }

    public void setDailyClasses(int dailyClasses) {
        this.dailyClasses = dailyClasses;
    }

    // Stored in integer counting (this has created some headaches but it works)
    private int rotationalDays;
    private int dailyClasses;

    public School(String name, int rotationalDays, int dailyClasses) {
        this.name = name;
        this.rotationalDays = rotationalDays;
        this.dailyClasses = dailyClasses;
        courseList = new ArrayList<>();
        teacherList = new ArrayList<>();
        studentList = new ArrayList<>();
    }

    private ArrayList<Course> courseList;

    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    public void addStudent(Student student) {
        studentList.add(student);
    }

    public ArrayList<Teacher> getTeacherList() {
        return teacherList;
    }

    public ArrayList<Student> getStudentList() {
        return studentList;
    }

    private ArrayList<Teacher> teacherList;
    private ArrayList<Student> studentList;

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return this.name;
    }
}
