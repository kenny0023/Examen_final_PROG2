import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private Instant entryDate;
    private List<Group> groupHistory;

    public Student(int id, String firstName, String lastName, Instant entryDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.entryDate = entryDate;
        this.groupHistory = new ArrayList<>();
    }

    public void addGroup(Group group) {
        groupHistory.add(group);
    }

    public List<Group> getGroupHistory() {
        return groupHistory;
    }

    public int getId() {
        return id;
    }
}