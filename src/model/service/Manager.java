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
        epic.setSubtasks(subtask);
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
    public Task getTask(int id) {
        return tasksMap.get(id);
    }

    //Получение эпика по идентификатору
    public Epic getEpic(int id) {
        return epicsMap.get(id);
    }

    //Получение подзадачи по идентификатору
    public Subtask getSubtask(int id) {
        return subtasksMap.get(id);
    }

    //Получение всех подзадач по идентификатору эпика
    public void getEpicSubtasks(int id) {
        ArrayList<Integer> epicsSubs = epicsMap.get(id).getSubtasks();

        if (epicsSubs == null) {
            System.out.println("Список подзадач пуст");
        } else {
            for (int idSubs : epicsSubs
            ) {
                System.out.println(subtasksMap.get(idSubs));
            }
        }
    }

    //Обновдение задачи
    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    //Обновдение эпика
    public void updateEpic(Epic epic) {
        epicsMap.put(epic.getId(), epic);
        epic.setEpicStatus(subtasksMap);
    }

    //Обновление подзадачи
    public void updateSubtask(Subtask subtask) {
        subtasksMap.put(subtask.getId(), subtask);
        epicsMap.get(subtask.getEpicId()).setEpicStatus(subtasksMap);
    }

    //Удаление задачи по идентификатору
    public void removeTask(int id) {
        tasksMap.remove(id);
    }

    //Удаление эпика по идентификатору
    public void removeEpic(int id) {
        Epic epicToRemove = epicsMap.get(id);
        ArrayList<Integer> epicSubtasks = epicToRemove.getSubtasks();
        for (int idSub : epicSubtasks
        ) {
            subtasksMap.remove(idSub);
        }
        epicsMap.remove(id);

    }

    //Удаление подзадачи по идентификатору
    public void removeSubtasks(int id) {
        int subtaskEpicId = subtasksMap.get(id).getEpicId();
        subtasksMap.remove(id);
        Epic epic = epicsMap.get(subtaskEpicId);
        epic.removeSubtask(id);
        epicsMap.get(subtaskEpicId).setEpicStatus(subtasksMap);
    }
}
