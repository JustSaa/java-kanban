package model.service.taskManagers;

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
    private int taskId = 1;
    //Хранение задач
    HashMap<Integer, Task> tasksMap = new HashMap<>();
    HashMap<Integer, Epic> epicsMap = new HashMap<>();
    HashMap<Integer, Subtask> subtasksMap = new HashMap<>();

    HistoryManager historyManager = Managers.getDefaultHistory();

    //Создание задачи
    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        tasksMap.put(task.getId(), task);
    }

    //Вывод всех задач
    @Override
    public HashMap<Integer, Task> getAllTasks() {
        return this.tasksMap;
    }

    //Создание эпика
    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epicsMap.put(epic.getId(), epic);
        epic.setEpicStatus(subtasksMap);
    }

    //Вывод всех эпиков и его подзадач
    @Override
    public HashMap<Integer, Epic> getAllEpics() {
        return this.epicsMap;
    }

    //Вывод всех подзадач
    @Override
    public HashMap<Integer, Subtask> getAllSubtasks() {
        return this.subtasksMap;
    }

    //Создание подзадачи
    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasksMap.put(subtask.getId(), subtask);
        Epic epic = epicsMap.get(subtask.getEpicId());
        epic.setSubtask(subtask);
        epic.setEpicStatus(subtasksMap);
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
    @Override
    public int generateId() {
        return this.taskId++;
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
    public HashMap<Integer, Subtask> getEpicSubtasks(int id) {
        ArrayList<Integer> epicSubsId = epicsMap.get(id).getSubtasks();
        HashMap<Integer, Subtask> epicSubs = new HashMap<>();
        if (epicSubsId == null) {
            System.out.println("Список подзадач пуст");
            return null;
        } else {
            for (int idSubs : epicSubsId) {
                epicSubs.put(idSubs, subtasksMap.get(idSubs));
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
                updateEpic.setEpicStatus(subtasksMap);
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
                updateEpic.setEpicStatus(subtasksMap);
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
                epicsMap.get(updateSubtask.getEpicId()).setEpicStatus(subtasksMap);
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
                epicsMap.get(updateSubtask.getEpicId()).setEpicStatus(subtasksMap);
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
            epicsMap.get(subtaskEpicId).setEpicStatus(subtasksMap);
        } else {
            System.out.println("Такой подзадачи нет");
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
