package model.service;

import model.tasks.Epic;
import model.tasks.Subtask;
import model.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    //Для генерации идентификатора
    private int taskId = 1;
    //Хранение задач
    HashMap<Integer, Task> tasksMap = new HashMap<>();
    HashMap<Integer, Epic> epicsMap = new HashMap<>();
    HashMap<Integer, Subtask> subtasksMap = new HashMap<>();

    //Создание задачи
    public void createTask(Task task) {
        task.setId(generateId());
        tasksMap.put(task.getId(), task);
    }

    //Вывод всех задач
    public void getAllTasks() {
        for (Integer id : tasksMap.keySet()) {
            System.out.println(id + " - " + tasksMap.get(id));
        }
    }

    //Создание эпика
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epicsMap.put(epic.getId(), epic);
        epic.setEpicStatus(subtasksMap);
    }

    //Вывод всех эпиков и его подзадач
    public void getAllEpics() {
        for (Integer id : epicsMap.keySet()) {
            Epic epic = epicsMap.get(id);
            System.out.println(id + " - " + epic);
            getEpicSubtasks(id);
        }
    }

    //Вывод всех подзадач
    public void getAllSubtasks() {
        for (Integer id : subtasksMap.keySet()) {
            System.out.println(id + " - " + subtasksMap.get(id));
        }
    }

    //Создание подзадачи
    public void createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasksMap.put(subtask.getId(), subtask);
        Epic epic = epicsMap.get(subtask.getEpicId());
        epic.setSubtask(subtask);
        epic.setEpicStatus(subtasksMap);
    }

    //Удаление всех задач
    public void deleteAllTasks() {
        tasksMap.clear();
    }

    //Удаление всех эпиков
    public void deleteAllEpics() {
        epicsMap.clear();
    }

    //Удаление все подзадач
    public void deleteAllSubtasks() {
        subtasksMap.clear();
    }

    //Генерация идентификатора
    public int generateId() {
        return this.taskId++;
    }

    //Получение задачи по идентификатору
    public void getTask(int id) {
        if (tasksMap.get(id) != null) {
            System.out.println(tasksMap.get(id));
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    //Получение эпика по идентификатору
    public void getEpic(int id) {
        if (epicsMap.get(id) != null) {
            System.out.println(epicsMap.get(id));
        } else {
            System.out.println("Такого эпика - задачи нет");
        }
    }

    //Получение подзадачи по идентификатору
    public void getSubtask(int id) {
        if (subtasksMap.get(id) != null) {
            System.out.println(subtasksMap.get(id));
        } else {
            System.out.println("Такого подзадачи - нет");
        }
    }

    //Получение всех подзадач по идентификатору эпика
    public void getEpicSubtasks(int id) {
        ArrayList<Integer> epicsSubs = epicsMap.get(id).getSubtasks();
        if (epicsSubs == null) {
            System.out.println("Список подзадач пуст");
        } else {
            for (int idSubs : epicsSubs) {
                System.out.println(subtasksMap.get(idSubs));
            }
        }
    }

    //Обновление задачи по сущности
    public void updateTask(Task updateTask) {
        for (Task task : tasksMap.values()) {
            if (task.getId() == updateTask.getId()) {
                tasksMap.put(task.getId(), updateTask);
            }
        }
    }

    //Обновление задачи по сущности + id
    public void updateTask(Task updateTask, int id) {
        for (Task task : tasksMap.values()) {
            if (task.getId() == id) {
                updateTask.setId(id);
                tasksMap.put(id, updateTask);
            }
        }
    }

    //Обновление эпика по сущности
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
    public void removeTask(int id) {
        if (tasksMap.get(id) != null) {
            tasksMap.remove(id);
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    //Удаление эпика по идентификатору
    public void removeEpic(int id) {
        if (epicsMap.get(id) != null) {
            Epic epicToRemove = epicsMap.get(id);
            ArrayList<Integer> epicSubtasks = epicToRemove.getSubtasks();
            for (int idSub : epicSubtasks) {
                subtasksMap.remove(idSub);
            }
            epicsMap.remove(id);
        } else {
            System.out.println("Такого эпика в задачах нет");
        }
    }

    //Удаление подзадачи по идентификатору
    public void removeSubtasks(int id) {
        if (subtasksMap.get(id) != null) {
            int subtaskEpicId = subtasksMap.get(id).getEpicId();
            subtasksMap.remove(id);
            Epic epic = epicsMap.get(subtaskEpicId);
            epic.removeSubtask(id);
            //Обнвление статуса
            epicsMap.get(subtaskEpicId).setEpicStatus(subtasksMap);
        } else {
            System.out.println("Такой подзадачи нет");
        }
    }
}
