package model.utils;

import model.enums.Status;
import model.enums.TaskType;
import model.model.Epic;
import model.model.Subtask;
import model.model.Task;
import model.service.taskManagers.InMemoryTaskManager;
import model.service.historyManager.HistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static model.utils.Formatter.formatter;

public class Converter {

    // преобразование истории в строку
    public static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();

        for (Task task : manager.getHistory())
            sb.append(task.getId()).append(",");

        return sb.toString();

    }

    // преобразование истории из строки
    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();

        for (String line : value.split(","))
            history.add(Integer.parseInt(line));
        return history;

    }

    //Сохранения задачи в строку
    public static String toString(InMemoryTaskManager taskManager) {
        StringBuilder sb = new StringBuilder();
        List<Task> allTasks = new ArrayList<>();

        allTasks.addAll(taskManager.getTasks());
        allTasks.addAll(taskManager.getEpics());
        allTasks.addAll(taskManager.getSubtasks());
        for (Task t : allTasks) {
            sb.append(t.toString()).append("\n");
        }
        return sb.toString();
    }

    //Метод создания задачи из строки
    public static Task fromString(String value) {
        try {
            String[] values = value.split(",");
            int epicId = 0;
            int id = Integer.parseInt(values[0]);
            String type = values[1];
            String title = values[2];
            Status status = Status.valueOf(values[3]);
            String description = values[4];
            if (description.equals("null")) {
                description = null;
            }
            LocalDateTime startTime = null;
            if (values[5] != null && !values[4].equals("null")) {
                startTime = LocalDateTime.parse(values[5], formatter);
            }
            long duration = 0;
            if (values.length > 6 && values[6] != null && !values[6].equals("null")) {
                duration = Long.parseLong(values[6]);
            }

            if (type.equals("Task")) {
                return new Task(id, title, status, description, startTime, duration);
            } else if (type.equals("Epic")) {
                return new Epic(id, title, status, description);
            } else if (type.equals("Subtask")) {
                epicId = Integer.parseInt(values[8]);
                return new Subtask(id, title, status, description, epicId, startTime, duration);
            } else {
                throw new IllegalArgumentException("Введенный формат задачи не поддерживается");
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            // Обработка исключений при некорректных значениях или формате строки
            throw new IllegalArgumentException("Ошибка при парсинге строки задачи: " + value, e);
        }
    }
}
