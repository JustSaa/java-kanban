package model.service.taskManagers;

import model.enums.Status;
import model.service.Managers;
import model.service.historyManager.HistoryManager;
import model.model.Epic;
import model.model.Subtask;
import model.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    //Для генерации идентификатора
    private static int taskId = 1;
    //Хранение задач
    private final HashMap<Integer, Task> tasksMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicsMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasksMap = new HashMap<>();

    HistoryManager historyManager = Managers.getDefaultHistory();

    //Создание задачи
    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        tasksMap.put(task.getId(), task);
    }

    //Вывод всех задач
    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>(tasksMap.values());
        return tasks;
    }

    //Создание эпика
    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epicsMap.put(epic.getId(), epic);
        setEpicStatus(subtasksMap, epic);
    }

    //Вывод всех эпиков и его подзадач
    @Override
    public List<Epic> getAllEpics() {
        List<Epic> epics = new ArrayList<>(epicsMap.values());
        return epics;
    }

    //Вывод всех подзадач
    @Override
    public List<Subtask> getAllSubtasks() {
        List<Subtask> subs = new ArrayList<>(subtasksMap.values());
        return subs;
    }

    //Создание подзадачи
    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasksMap.put(subtask.getId(), subtask);
        Epic epic = epicsMap.get(subtask.getEpicId());
        epic.setSubtask(subtask);
        setEpicStatus(subtasksMap, epic);
    }

    //Удаление всех задач
    @Override
    public void deleteAllTasks() {
        tasksMap.clear();
    }

    //Удаление всех эпиков
    @Override
    public void deleteAllEpics() {
        epicsMap.clear();
    }

    //Удаление все подзадач
    @Override
    public void deleteAllSubtasks() {
        subtasksMap.clear();
    }

    //Генерация идентификатора
    private int generateId() {
        return InMemoryTaskManager.taskId++;
    }

    //Получение задачи по идентификатору
    @Override
    public Task getTask(int id) {
        if (tasksMap.get(id) != null) {
            //Добавление задачи в историю при просмотре
            historyManager.addTaskToHistory(tasksMap.get(id));
            return this.tasksMap.get(id);
        } else {
            System.out.println("Такой задачи нет");
            return null;
        }
    }

    //Получение эпика по идентификатору
    @Override
    public Epic getEpic(int id) {
        if (epicsMap.get(id) != null) {
            //Добавление задачи в историю при просмотре
            historyManager.addTaskToHistory(epicsMap.get(id));
            return this.epicsMap.get(id);
        } else {
            System.out.println("Такого эпика - задачи нет");
            return null;
        }
    }

    //Получение подзадачи по идентификатору
    @Override
    public Subtask getSubtask(int id) {
        if (subtasksMap.get(id) != null) {
            //Добавление задачи в историю при просмотре
            historyManager.addTaskToHistory(subtasksMap.get(id));
            return this.subtasksMap.get(id);
        } else {
            System.out.println("Такого подзадачи - нет");
            return null;
        }
    }

    //Получение всех подзадач по идентификатору эпика
    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        ArrayList<Integer> epicSubsId = epicsMap.get(id).getSubtasks();
        List<Subtask> epicSubs = new ArrayList<>();
        if (epicSubsId == null) {
            System.out.println("Список подзадач пуст");
            return null;
        } else {
            for (int idSubs : epicSubsId) {
                epicSubs.add(subtasksMap.get(idSubs));
                historyManager.addTaskToHistory(subtasksMap.get(idSubs));
            }
            return epicSubs;
        }
    }

    //Обновление задачи по сущности
    @Override
    public void updateTask(Task updateTask) {
        for (Task task : tasksMap.values()) {
            if (task.getId() == updateTask.getId()) {
                tasksMap.put(task.getId(), updateTask);
            }
        }
    }

    //Обновление задачи по сущности + id
    @Override
    public void updateTask(Task updateTask, int id) {
        for (Task task : tasksMap.values()) {
            if (task.getId() == id) {
                updateTask.setId(id);
                tasksMap.put(id, updateTask);
            }
        }
    }

    //Обновление эпика по сущности
    @Override
    public void updateEpic(Epic updateEpic) {
        for (Epic epic : epicsMap.values()) {
            if (epic.getId() == updateEpic.getId()) {
                updateEpic.setSubtasks(epic.getSubtasks());
                epicsMap.put(epic.getId(), updateEpic);
                setEpicStatus(subtasksMap, updateEpic);
            }
        }
    }

    //Обновление эпика по сущности + id
    @Override
    public void updateEpic(Epic updateEpic, int id) {
        for (Epic epic : epicsMap.values()) {
            if (epic.getId() == id) {
                updateEpic.setSubtasks(epic.getSubtasks());
                updateEpic.setId(id);
                epicsMap.put(id, updateEpic);
                setEpicStatus(subtasksMap, updateEpic);
            }
        }
    }

    //Обновление подзадачи по сущности
    @Override
    public void updateSubtask(Subtask updateSubtask) {
        for (Subtask subtask : subtasksMap.values()) {
            if (subtask.getId() == updateSubtask.getId()) {
                subtasksMap.put(subtask.getId(), updateSubtask);
                //Обновление статуса Epic
                setEpicStatus(subtasksMap, epicsMap.get(updateSubtask.getEpicId()));
            }
        }
    }

    //Обновление подзадачи по сущности + id
    @Override
    public void updateSubtask(Subtask updateSubtask, int id) {
        for (Subtask subtask : subtasksMap.values()) {
            if (subtask.getId() == id) {
                updateSubtask.setId(id);
                subtasksMap.put(subtask.getId(), updateSubtask);
                setEpicStatus(subtasksMap, epicsMap.get(updateSubtask.getEpicId()));
            }
        }
    }

    //Удаление задачи по идентификатору
    @Override
    public void removeTask(int id) {
        if (tasksMap.get(id) != null) {
            tasksMap.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    //Удаление эпика по идентификатору
    @Override
    public void removeEpic(int id) {
        if (epicsMap.get(id) != null) {
            Epic epicToRemove = epicsMap.get(id);
            ArrayList<Integer> epicSubtasks = epicToRemove.getSubtasks();
            for (int idSub : epicSubtasks) {
                subtasksMap.remove(idSub);
                historyManager.remove(idSub);
            }
            epicsMap.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Такого эпика в задачах нет");
        }
    }

    //Удаление подзадачи по идентификатору
    @Override
    public void removeSubtasks(int id) {
        if (subtasksMap.get(id) != null) {
            int subtaskEpicId = subtasksMap.get(id).getEpicId();
            subtasksMap.remove(id);
            historyManager.remove(id);
            Epic epic = epicsMap.get(subtaskEpicId);
            epic.removeSubtask(id);
            //Обнвление статуса
            setEpicStatus(subtasksMap, epic);
        } else {
            System.out.println("Такой подзадачи нет");
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //Присвоение статуса задаче
    private void setEpicStatus(HashMap<Integer, Subtask> subs, Epic epic) {
        int isNew = 0;
        int isDone = 0;
        for (int id : epic.getSubtasks()) {
            if (subs.get(id) == null || subs.get(id).getStatus().equals(Status.NEW)) {
                isNew++;
            } else if (subs.get(id).getStatus().equals(Status.DONE)) {
                isDone++;
            }
        }
        if (epic.getSubtasks().size() == isNew) {
            epic.setStatus(Status.NEW);
        } else if (epic.getSubtasks().size() == isDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

}
