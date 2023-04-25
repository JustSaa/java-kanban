package model.service;

import model.service.historyManager.HistoryManager;
import model.service.historyManager.InMemoryHistoryManager;
import model.service.taskManagers.FileBackedTasksManager;
import model.service.taskManagers.InMemoryTaskManager;
import model.service.taskManagers.TaskManager;

public class Managers {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTasksManager getDefaultFileBackedManager() {
        return new FileBackedTasksManager();
    }
}
