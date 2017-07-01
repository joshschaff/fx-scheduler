package Library;

import java.util.ArrayList;

/**
 * Created by josh on 6/2/17.
 * This has become a sad and pointless class
 */
public class Roster<Student> extends ArrayList<Student> {
    public Course parent;


    public Roster(Course parent) {
        this.parent = parent;
    }


    @Override
    public boolean add(Student obj) {
        super.add(obj);
        return true;
    }

}


