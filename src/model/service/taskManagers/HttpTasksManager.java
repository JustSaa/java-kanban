package model.service.taskManagers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.clients.KVTaskClient;
import model.utils.Converter;
import model.model.Subtask;
import model.model.Epic;
import model.model.Task;

import java.io.File;
import java.util.List;

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

        TypeToken<List<Task>> taskType = new TypeToken<List<Task>>() {
        };

        TypeToken<List<Epic>> epicType = new TypeToken<List<Epic>>() {
        };

        TypeToken<List<Subtask>> subtaskType = new TypeToken<List<Subtask>>() {
        };

        List<Task> prioritizedTasks = loadElements("tasks", prioritizedTaskType);
        getPrioritizedTasks().addAll(prioritizedTasks);

        List<Task> history = loadElements("tasks/history", historyType);
        getHistory().addAll(history);

        List<Task> tasks = loadElements("tasks/task", taskType);
        for (Task t : tasks) {
            tasksMap.put(t.getId(), t);
        }

        List<Epic> epics = loadElements("tasks/epic", epicType);
        for (Epic e : epics) {
            epicsMap.put(e.getId(), e);
        }

        List<Subtask> subtask = loadElements("tasks/subtask", subtaskType);
        for (Subtask s : subtask) {
            subtasksMap.put(s.getId(), s);
        }
    }
    private <T> List<T> loadElements(String key, TypeToken<List<T>> typeToken) {
        String json = getClient().load(key);
        return gson.fromJson(json, typeToken.getType());
    }
}