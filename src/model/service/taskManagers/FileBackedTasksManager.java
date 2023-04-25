package model.service.taskManagers;

import model.enums.TaskType;
import model.model.Epic;
import model.model.Subtask;
import model.model.Task;
import model.service.taskManagers.exeptions.ManagerSaveException;
import model.utils.Converter;
import model.service.Managers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private static final String FILE_NAME = "results.csv";
    //Получение файла
    private static final Path filePath = Path.of("src/resources/" + FILE_NAME);
    private static File file = null;

    public FileBackedTasksManager() {
        file = filePath.toFile();
    }


    //Сохранение в файл
    protected void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath.toFile()));
             BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {

            if (br.readLine() == null) {
                String header = "id,type,name,status,description,epicId" + "\n";
                bw.write(header);
            }
            String values = Converter.toString(this)
                    + "\n"
                    + Converter.historyToString(historyManager);
            bw.write(values);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    // Загрузка из файла
    public static FileBackedTasksManager load(Path filePath) {
        FileBackedTasksManager fileBackedTasksManager = Managers.getDefaultFileBackedManager();
        boolean itsDelimiter = false;
        List<Integer> history;
        Map<Integer, Task> mapForHistory = new HashMap<>();

        try {
            String fileName = Files.readString(filePath);
            String[] lines = fileName.split("\n");

            for (int i = 1; i < lines.length; i++) {
                if (lines[i].isEmpty()) {
                    itsDelimiter = true;
                    continue;
                }
                if (!itsDelimiter) {
                    Task task = Converter.fromString(lines[i]);
                    TaskType type = TaskType.valueOf(lines[i].split(",")[1]);

                    switch (type) {
                        case TASK -> {
                            fileBackedTasksManager.createTask(task);
                            mapForHistory.put(task.getId(), task);
                        }
                        case EPIC -> {
                            Epic epic = (Epic) task;
                            fileBackedTasksManager.createEpic(epic);
                            mapForHistory.put(epic.getId(), epic);
                        }
                        case SUBTASK -> {
                            Subtask subtask = (Subtask) task;
                            fileBackedTasksManager.createSubtask(subtask);
                            mapForHistory.put(subtask.getId(), subtask);
                        }
                    }
                } else {
                    history = Converter.historyFromString(lines[i]);
                    for (Integer id : history) {
                        Task task = mapForHistory.get(id);
                        switch (task.getType()) {
                            case TASK ->
                                    fileBackedTasksManager.historyManager.addToHistory(fileBackedTasksManager.getTask(id));
                            case EPIC ->
                                    fileBackedTasksManager.historyManager.addToHistory(fileBackedTasksManager.getEpic(id));
                            case SUBTASK ->
                                    fileBackedTasksManager.historyManager.addToHistory(fileBackedTasksManager.getSubtask(id));
                        }
                    }
                }
            }
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
