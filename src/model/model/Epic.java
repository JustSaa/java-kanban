package model.model;

import model.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static model.utils.Formatter.formatter;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, String title, Status status, String description) {
        super(id, title, status, description);
    }


    public Epic(String title, String description) {
        super(title, description);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    public void setSubtask(Subtask subtask) {
        this.subtasks.add(subtask.id);
    }

    public void removeSubtask(int id) {
        for (int i = 0; i < this.subtasks.size(); i++) {
            if (id == this.subtasks.get(i)) {
                this.subtasks.remove(i);
            }
        }
    }

    public int getEpicId() {
        return this.id;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.id).append(",")
                .append(getClass().getSimpleName()).append(",")
                .append(this.title).append(",")
                .append(this.status).append(",")
                .append(this.description);

        if (startTime != null) {
            builder.append(",").append(this.startTime.format(formatter))
                    .append(",").append(this.duration).append(",");
        }
        if (endTime != null) {
            builder.append(",").append(this.endTime.format(formatter));
        }

        return builder.toString();
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Epic epic = (Epic) obj;
        return (Objects.equals(this.subtasks, epic.subtasks)
                && Objects.equals(this.id, epic.id)
                && Objects.equals(this.title, epic.title)
                && Objects.equals(this.description, epic.description)
                && Objects.equals(this.status, epic.status)
                && Objects.equals(this.duration, epic.duration)
                && Objects.equals(this.startTime, epic.startTime)
                && Objects.equals(this.endTime, epic.endTime));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.subtasks, this.id, this.title, this.description,
                this.status, getClass(), this.startTime, this.duration, this.endTime);
    }

}
