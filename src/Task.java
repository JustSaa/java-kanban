public class Task {
    protected String title;
    protected String description;
    protected String status;
    protected int id;

    public Task(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{title='" + this.title
                + "', description='" + this.description
                + "', status='" + this.status
                + "', id='" + this.id + "'";
    }
}
