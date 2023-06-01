package model.service.taskManagers;

import model.enums.Status;
import model.service.Managers;
import model.service.historyManager.HistoryManager;
import model.model.Epic;
import model.model.Subtask;
import model.model.Task;

import java.util.*;
import java.time.LocalDateTime;
import java.time.Duration;

public class InMemoryTaskManager implements TaskManager {
    //Для генерации идентификатора
    protected static int taskId = 1;
    //Хранение задач
    protected final HashMap<Integer, Task> tasksMap = new HashMap<>();
    protected final HashMap<Integer, Epic> epicsMap = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasksMap = new HashMap<>();

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final TreeSet<Task> sortedTasks = new TreeSet<>((o1, o2) -> {
        if (o1.getStartTime() == null && o2.getStartTime() == null) {
            // Если оба времени начала равны null, сравниваем по другому критерию
            return 1;
        } else if (o1.getStartTime() == null) {
            return 1;
        } else if (o2.getStartTime() == null) {
            return -1;
        }

        return o1.getStartTime().compareTo(o2.getStartTime());
    });

    public TreeSet<Task> getListOfTasksSortedByTime() {
        return sortedTasks;
    }

    //Создание задачи
    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        tasksMap.put(task.getId(), task);
        checkTheTaskCompletionTime(task);
        sortedTasks.add(task);
    }

    //Создание эпика
    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epicsMap.put(epic.getId(), epic);
        setEpicStatus(epic);
        checkTheTaskCompletionTime(epic);
        sortedTasks.add(epic);
    }

    //Создание подзадачи
    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasksMap.put(subtask.getId(), subtask);
        Epic epic = epicsMap.get(subtask.getEpicId());
        epic.setSubtask(subtask);
        setEpicStatus(epic);
        checkTheTaskCompletionTime(subtask);
        sortedTasks.add(subtask);
        startTimeForEpic(subtask.getEpicId());
        setEndTimeForEpic(subtask.getEpicId());
        sumOfDuration(subtask.getEpicId());


    }

    //Вывод всех задач
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    //Вывод всех эпиков и его подзадач
    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epicsMap.values());
    }

    //Вывод всех подзадач
    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasksMap.values());
    }

    //Удаление всех задач
    @Override
    public void deleteTasks() {
        for (Integer idTasks : tasksMap.keySet()) {
            historyManager.remove(idTasks);
            sortedTasks.remove(tasksMap.get(idTasks));
        }
        tasksMap.clear();
    }

    //Удаление всех эпиков
    @Override
    public void deleteEpics() {
        for (Integer idSub : subtasksMap.keySet()) {
            historyManager.remove(idSub);
            sortedTasks.remove(subtasksMap.get(idSub));
        }
        for (Integer idEpics : epicsMap.keySet()) {
            historyManager.remove(idEpics);
            sortedTasks.remove(epicsMap.get(idEpics));
        }
        epicsMap.clear();
        subtasksMap.clear();
    }

    //Удаление все подзадач
    @Override
    public void deleteSubtasks() {
        for (Integer idSubtasks : subtasksMap.keySet()) {
            historyManager.remove(idSubtasks);
            sortedTasks.remove(subtasksMap.get(idSubtasks));
        }
        subtasksMap.clear();
    }

    public ArrayList<Integer> getSubTaskList(int epicId) {    //Нахождение по id Epic'а всех id subTask'ов
        if (epicsMap.get(epicId) != null) {
            return epicsMap.get(epicId).getSubtasks();
        } else {
            return null;
        }
    }

    //Генерация идентификатора
    private int generateId() {
        return taskId++;
    }

    //Получение задачи по идентификатору
    @Override
    public Task getTask(int id) {
        if (tasksMap.get(id) != null) {
            //Добавление задачи в историю при просмотре
            historyManager.addToHistory(tasksMap.get(id));
            return tasksMap.get(id);
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
            historyManager.addToHistory(epicsMap.get(id));
            return epicsMap.get(id);
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
            historyManager.addToHistory(subtasksMap.get(id));
            return subtasksMap.get(id);
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
                historyManager.addToHistory(subtasksMap.get(idSubs));
            }
            return epicSubs;
        }
    }

    //Обновление задачи по сущности
    @Override
    public void updateTask(Task updateTask) {
        for (Task task : tasksMap.values()) {
            if (task.getId() == updateTask.getId()) {
                sortedTasks.remove(task);
                tasksMap.put(task.getId(), updateTask);
                checkTheTaskCompletionTime(updateTask);
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
                setEpicStatus(updateEpic);
                sortedTasks.remove(epic);
                checkTheTaskCompletionTime(updateEpic);
            }
        }
    }

    //Обновление подзадачи по сущности
    @Override
    public void updateSubtask(Subtask updateSubtask) {
        for (Subtask subtask : subtasksMap.values()) {
            if (subtask.getId() == updateSubtask.getId()) {
                sortedTasks.remove(subtask);
                subtasksMap.put(subtask.getId(), updateSubtask);
                //Обновление статуса Epic
                setEpicStatus(epicsMap.get(updateSubtask.getEpicId()));
                //Should update check interval

                checkTheTaskCompletionTime(updateSubtask);
                startTimeForEpic(updateSubtask.getEpicId());
                sumOfDuration(updateSubtask.getEpicId());
                setEndTimeForEpic(updateSubtask.getEpicId());
            }
        }
    }

    //Удаление задачи по идентификатору
    @Override
    public void removeTask(int id) {
        if (tasksMap.get(id) != null) {
            sortedTasks.remove(tasksMap.get(id));
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
            sortedTasks.remove(epicsMap.get(id));
            for (int idSub : epicSubtasks) {
                sortedTasks.remove(subtasksMap.get(idSub));
                subtasksMap.remove(idSub);
                historyManager.remove(idSub);
            }
            sortedTasks.remove(epicsMap.get(id));
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
            sortedTasks.remove(subtasksMap.get(id));
            subtasksMap.remove(id);
            historyManager.remove(id);
            Epic epic = epicsMap.get(subtaskEpicId);
            epic.removeSubtask(id);
            //Обновление статуса
            setEpicStatus(epic);
        } else {
            System.out.println("Такой подзадачи нет");
        }
    }

    private void startTimeForEpic(int epicId) {
        getEpicForMethod(epicId).getSubtasks()
                .stream()
                .map(this::getSubtaskForMethod)
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .ifPresent(getEpicForMethod(epicId)::setStartTime);
    }

    private void setEndTimeForEpic(int epicId) {
        getEpicForMethod(epicId).getSubtasks()
                .stream()
                .map(this::getSubtaskForMethod)
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .ifPresent(getEpicForMethod(epicId)::setEndTime);
    }

    private void sumOfDuration(int epicId) {
        List<Integer> subTasks = getSubTaskList(epicId);
        Duration duration = Duration.ZERO;
        for (Integer subTask : subTasks) {
            Duration durationSubTask;
            if (getSubtask(subTask).getDuration() == null) {
                continue;
            }
            durationSubTask = getSubtask(subTask).getDuration();

            duration = duration.plus(durationSubTask);
        }
        epicsMap.get(epicId).setDuration(duration);
    }


    private void checkTheTaskCompletionTime(Task task) throws RuntimeException {
        if (sortedTasks.isEmpty() || task.getStartTime() == null) {
            return;
        }
        LocalDateTime prevEndDateTime = null;
        for (Task t : sortedTasks) {

            if(t.getStartTime()==null){
                return;
            }
            if (prevEndDateTime == null && task.getEndTime().isBefore(t.getStartTime())) {
                return;
            } else {
                if (prevEndDateTime == null) {
                    prevEndDateTime = t.getEndTime();
                    continue;
                }

                if (prevEndDateTime.isBefore(task.getStartTime()) && task.getEndTime().isBefore(t.getStartTime())) {
                    return;
                } else {
                    prevEndDateTime = t.getEndTime();
                }
            }
        }
        if (prevEndDateTime.isBefore(task.getStartTime())) {
            return;
        }

        throw new RuntimeException("Это время уже занято. Выберите другое время");
    }

    private Epic getEpicForMethod(int id) {
        return epicsMap.get(id);
    }

    private Subtask getSubtaskForMethod(int id) {
        return subtasksMap.get(id);
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<Task>(sortedTasks);
    }


    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //Присвоение статуса задаче
    private void setEpicStatus(Epic epic) {
        int isNew = 0;
        int isDone = 0;

        for (int id : epic.getSubtasks()) {
            if (subtasksMap.get(id) == null || subtasksMap.get(id).getStatus().equals(Status.NEW)) {
                isNew++;
            } else if (subtasksMap.get(id).getStatus().equals(Status.DONE)) {
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
