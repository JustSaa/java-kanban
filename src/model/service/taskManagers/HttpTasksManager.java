package model.service.taskManagers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.clients.KVTaskClient;
import model.utils.Converter;
import model.model.Subtask;
import model.model.Epic;
import model.model.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpTasksManager extends FileBackedTasksManager {
    protected KVTaskClient client;
    private final Gson gson;

    public HttpTasksManager() {
        super(new File("resources/task.csv"));
        gson = Converter.createGson();
        client = new KVTaskClient();
    }

    public KVTaskClient getClient() {
        return client;
    }

    @Override
    public void save() {
        client.save("tasks", gson.toJson(getPrioritizedTasks()));
        client.save("tasks/history", gson.toJson(getHistory()));
        client.save("tasks/task", gson.toJson(getTasks()));
        client.save("tasks/epic", gson.toJson(getEpics()));
        client.save("tasks/subtask", gson.toJson(getSubtasks()));
    }

    public void load() {
        TypeToken<List<Task>> prioritizedTaskType = new TypeToken<List<Task>>() {
        };
        TypeToken<List<Task>> historyType = new TypeToken<List<Task>>() {
        };

        TypeToken<ArrayList<Task>> taskType = new TypeToken<ArrayList<Task>>() {
        };

        TypeToken<ArrayList<Epic>> epicType = new TypeToken<ArrayList<Epic>>() {
        };

        TypeToken<ArrayList<Subtask>> subtaskType = new TypeToken<ArrayList<Subtask>>() {
        };

        List<Task> prioritizedTasks = gson.fromJson(getClient().load("tasks"), prioritizedTaskType.getType());
        getPrioritizedTasks().addAll(prioritizedTasks);

        List<Task> history = gson.fromJson(getClient().load("tasks/history"), historyType.getType());
        getHistory().addAll(history);


        String tasksFromJson = getClient().load("tasks/task");
        List<Task> tasks = gson.fromJson(tasksFromJson, taskType.getType());
        for (Task t : tasks) {
            tasksMap.put(t.getId(), t);
        }

        String epicsFromJson = getClient().load("tasks/epic");
        List<Epic> epics = gson.fromJson(epicsFromJson, epicType.getType());
        for (Epic e : epics) {
            epicsMap.put(e.getId(), e);
        }

        String subtaskFromJson = getClient().load("tasks/subtask");
        List<Subtask> subtask = gson.fromJson(subtaskFromJson, subtaskType.getType());
        for (Subtask s : subtask) {
            subtasksMap.put(s.getId(), s);
        }
    }
}