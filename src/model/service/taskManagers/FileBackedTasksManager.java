package model.service.taskManagers;

import model.enums.TaskType;
import model.model.Epic;
import model.model.Subtask;
import model.model.Task;
import model.service.taskManagers.exeptions.ManagerSaveException;
import model.utils.Converter;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }


    //Сохранение в файл
    protected void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            String header = "id,type,name,status,description,epicId" + "\n";
            String values = Converter.toString(this)
                    + "\n"
                    + Converter.historyToString(historyManager);

            bw.write(header);
            bw.write(values);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    // Загрузка из файла
    public static TaskManager load(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        boolean itsDelimiter = false;
        List<Integer> history;
        Map<Integer, Task> mapForHistory = new HashMap<>();
        int initId=0;
        try {
            String fileName = Files.readString(file.toPath());
            String[] lines = fileName.split("\n");

            for (int i = 1; i < lines.length; i++) {
                if (lines[i].isEmpty()) {
                    itsDelimiter = true;
                    continue;
                }
                if (!itsDelimiter) {
                    Task task = Converter.fromString(lines[i]);
                    TaskType type = TaskType.valueOf(lines[i].split(",")[1]);
                    //Получение самого большого id
                    if (task.getId()>initId) {
                        initId=task.getId();
                    }

                    switch (type) {
                        case TASK -> {
                            fileBackedTasksManager.tasksMap.put(task.getId(), task);
                            mapForHistory.put(task.getId(), task);
                        }
                        case EPIC -> {
                            Epic epic = (Epic) task;
                            fileBackedTasksManager.epicsMap.put(task.getId(), epic);
                            mapForHistory.put(epic.getId(), epic);
                        }
                        case SUBTASK -> {
                            Subtask subtask = (Subtask) task;
                            fileBackedTasksManager.subtasksMap.put(task.getId(), subtask);
                            mapForHistory.put(subtask.getId(), subtask);
                        }
                    }
                } else {
                    history = Converter.historyFromString(lines[i]);
                    for (Integer id : history) {
                        Task task = mapForHistory.get(id);
                        switch (task.getType()) {
                            case TASK, SUBTASK, EPIC -> fileBackedTasksManager.historyManager
                                    .addToHistory(task);
                        }
                    }
                }
            }
            taskId=initId+1;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки из файла");
        }
        return fileBackedTasksManager;
    }

    @Override
    //Создание задачи
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    //Создание эпика
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    //Создание подзадачи
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    //Удаление всех задач
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    //Удаление всех эпиков
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    //Удаление все подзадач
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    //Получение задачи по идентификатору
    public Task getTask(int id) {
        Task taskById = super.getTask(id);
        save();
        return taskById;
    }

    @Override
    //Получение эпика по идентификатору
    public Epic getEpic(int id) {
        Epic epicById = super.getEpic(id);
        save();
        return epicById;
    }

    @Override
    //Получение подзадачи по идентификатору
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    //Обновление задачи по сущности
    public void updateTask(Task updateTask) {
        super.updateTask(updateTask);
        save();
    }

    @Override
    //Обновление эпика по сущности
    public void updateEpic(Epic updateEpic) {
        super.updateEpic(updateEpic);
        save();
    }

    @Override
    //Обновление подзадачи по сущности
    public void updateSubtask(Subtask updateSubtask) {
        super.updateSubtask(updateSubtask);
        save();
    }

    @Override
    //Удаление задачи по идентификатору
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    //Удаление эпика по идентификатору
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    //Удаление подзадачи по идентификатору
    public void removeSubtasks(int id) {
        super.removeSubtasks(id);
        save();
    }

}