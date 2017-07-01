package Library;

/**
 * Created by josh on 5/27/17.
 */
public class Tester {
    public static void main(String[] args) {
        FileManager<School> hi = new FileManager<>(".school");
        System.out.println(hi.getDirectory());

        //hi.serialize(new School(), "testSchool");
        //hi.updateAllObjects();
        System.out.println(hi.getObjectList().size());
    }
}
