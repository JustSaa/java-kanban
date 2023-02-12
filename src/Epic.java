import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();
    private String status;


    public Epic(String title, String description) {
        super(title, description);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Subtask subtask) {
        this.subtasks.add(subtask.id);
    }

    public int getEpicId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Epic{title='" + this.title
                + "', description='" + this.description
                + "', status='" + this.status
                + "', id='" + this.id + "'";
    }

    public void setEpicStatus(HashMap<Integer, Subtask> subs) {
        int isNew = 0;
        int isDone = 0;

        for (int id : getSubtasks()
        ) {
            if (subs.get(id)==null ||subs.get(id).status.equals("NEW")){
                isNew++;
            } else if (subs.get(id).status.equals("DONE")) {
                isDone++;
            }
        }
        if (subtasks.size() == isNew) {
            this.status = "NEW";
        } else if (subtasks.size() == isDone) {
            this.status = "DONE";
        } else {
            this.status = "IN_PROGRESS";
        }

    }
}
