public class Subtask extends Task {
    private int epicId;
    public Subtask(String title, String description, String status, int epicId) {
        super(title, description, status);
        this.epicId=epicId;
    }

    @Override
    public String toString(){
        return "Subtask{title='"+this.title
                +"', description='"+this.description
                +"', status='"+this.status
                +"', id='"+this.id
                +"', epicId='"+this.epicId+"'";
    }

    public int getEpicId(){
        return this.epicId;
    }
}
