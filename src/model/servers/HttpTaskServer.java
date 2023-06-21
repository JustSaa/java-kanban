package model.servers;

import model.service.Managers;
import model.service.taskManagers.TaskManager;
import model.servers.handlers.HomepageHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.utils.Converter;
import com.google.gson.Gson;
import model.model.Subtask;
import model.model.Epic;
import model.model.Task;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.io.OutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class HttpTaskServer {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = Converter.createGson();

    private byte[] response = new byte[0];
    private static final int PORT = 8080;
    private final TaskManager manager;
    private HttpExchange httpExchange;
    private final HttpServer server;
    private int responseCode = 404;
    private String getPath;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        server = HttpServer.create();

        server.bind(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks", new TasksHandler());
        server.createContext("/", new HomepageHandler());
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен на " + PORT + " порту!");
    }


    class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            ArrayList<String> regexRoots = new ArrayList<>();
            regexRoots.add("^/tasks$");
            regexRoots.add("^/tasks/task$");
            regexRoots.add("^/tasks/epic$");
            regexRoots.add("^/tasks/subtask$");
            regexRoots.add("^/tasks/task/\\d+$");
            regexRoots.add("^/tasks/history$");
            regexRoots.add("^/tasks/subtask/epic/\\d+$");
            regexRoots.add("^/tasks/task/\\d+$");

            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            httpExchange = exchange;
            getPath = path;

            try (exchange) {
                switch (method) {
                    case "GET" -> {
                        if (Pattern.matches(regexRoots.get(0), path))
                            getPrioritizedTasks();
                        if (Pattern.matches(regexRoots.get(1), path))
                            getTasks();
                        if (Pattern.matches(regexRoots.get(2), path))
                            getEpics();
                        if (Pattern.matches(regexRoots.get(3), path))
                            getSubtasks();
                        if (Pattern.matches(regexRoots.get(4), path))
                            getTasksByID();
                        if (Pattern.matches(regexRoots.get(5), path))
                            getHistory();
                        if (Pattern.matches(regexRoots.get(6), path))
                            getSubtasksByID();
                    }
                    case "POST" -> {
                        if (Pattern.matches(regexRoots.get(1), path))
                            createTask();
                        if (Pattern.matches(regexRoots.get(2), path))
                            createEpic();
                        if (Pattern.matches(regexRoots.get(3), path))
                            createSubtask();
                    }
                    case "DELETE" -> {
                        if (Pattern.matches(regexRoots.get(1), path))
                            deleteAllTasksEpicsSubtasks();
                        if (Pattern.matches(regexRoots.get(7), path))
                            deleteByID();
                    }
                    default -> System.out.println("Метод " + method + " не поддерживается.");
                }
                exchange.sendResponseHeaders(responseCode, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            } catch (IOException e) {
                System.out.println("Ошибка выполнения запроса: " + e.getMessage());
            }
        }

        private void getPrioritizedTasks() {
            System.out.println("GET: началась обработка /tasks запроса от клиента.\n");
            String prioritizedTasksToJson = gson.toJson(manager.getListOfTasksSortedByTime());

            if (!prioritizedTasksToJson.isEmpty()) {
                response = prioritizedTasksToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void getTasks() {
            System.out.println("GET: началась обработка /tasks/task запроса от клиента.\n");
            String tasksToJson = gson.toJson(manager.getTasks());

            if (!tasksToJson.isEmpty()) {
                response = tasksToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void getEpics() {
            System.out.println("GET: началась обработка /tasks/epic запроса от клиента.\n");
            String epicsToJson = gson.toJson(manager.getEpics());

            if (!epicsToJson.isEmpty()) {
                response = epicsToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void getSubtasks() {
            System.out.println("GET: началась обработка /tasks/subtask запроса от клиента.\n");
            String subtasksToJson = gson.toJson(manager.getSubtasks());

            if (!subtasksToJson.isEmpty()) {
                response = subtasksToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void getTasksByID() {
            System.out.println("GET: началась обработка /tasks/task/d+ запроса от клиента.\n");
            List<Task> allTasks = new ArrayList<>();
            allTasks.addAll(manager.getTasks());
            allTasks.addAll(manager.getEpics());
            allTasks.addAll(manager.getSubtasks());

            String taskByIDToJson = null;
            int id = Integer.parseInt(getPath.replaceFirst("/tasks/task/", ""));

            for (Task t : allTasks) {
                if (t.getId() == id) {
                    taskByIDToJson = gson.toJson(t);
                }
            }

            if (!(taskByIDToJson ==null)) {
                response = taskByIDToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void getHistory() {
            System.out.println("GET: началась обработка /tasks/history запроса от клиента.\n");
            String historyToJson = gson.toJson(manager.getHistory());

            if (!historyToJson.isEmpty()) {
                response = historyToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void getSubtasksByID() {
            System.out.println("GET: началась обработка /tasks/subtask/epic/d+ запроса от клиента.\n");
            int id = Integer.parseInt(getPath.replaceFirst("/tasks/subtask/epic/", ""));
            String subtaskEpicToJson = gson.toJson(manager.getSubtasks().get(id));

            if (!subtaskEpicToJson.isEmpty()) {
                response = subtaskEpicToJson.getBytes(DEFAULT_CHARSET);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                responseCode = 200;
            } else
                responseCode = 400;
        }

        private void createTask() throws IOException {
            System.out.println("POST: началась обработка /tasks/task запроса от клиента.\n");
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Task task = gson.fromJson(body, Task.class);
            responseCode = 200;

            if (!manager.getTasks().contains(task)) {
                manager.createTask(task);
                System.out.println("Задача #" + task.getId() + " создана.\n" + body);
            } else {
                manager.updateTask(task);
                System.out.println("Задача #" + task.getId() + " обновлена.\n" + body);
            }
        }

        private void createEpic() throws IOException {
            System.out.println("POST: началась обработка /tasks/epic запроса от клиента.\n");
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Epic epic = gson.fromJson(body, Epic.class);
            responseCode = 200;

            if (!manager.getEpics().contains(epic)) {
                manager.createEpic(epic);
                System.out.println("Задача #" + epic.getId() + " создана.\n" + body);
            } else {
                manager.updateEpic(epic);
                System.out.println("Задача #" + epic.getId() + " обновлена.\n" + body);
            }
        }

        private void createSubtask() throws IOException {
            System.out.println("POST: началась обработка /tasks/subtask запроса от клиента.\n");
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            responseCode = 200;

            if (!manager.getSubtasks().contains(subtask)) {
                manager.createSubtask(subtask);
                System.out.println("Задача #" + subtask.getId() + " создана.\n" + body);
            } else {
                manager.updateSubtask(subtask);
                System.out.println("Задача #" + subtask.getId() + " создана.\n" + body);
            }
        }

        private void deleteAllTasksEpicsSubtasks() {
            System.out.println("DELETE: началась обработка /tasks/task запроса от клиента.\n");
            manager.deleteTasks();
            manager.deleteEpics();
            manager.deleteSubtasks();
            System.out.println("Все таски, эпики и сабтаски удалены.");
            responseCode = 200;
        }

        private void deleteByID() {
            System.out.println("DELETE: началась обработка /tasks/task/d+ запроса от клиента.\n");
            int id = Integer.parseInt(getPath.replaceFirst("/tasks/task/", ""));
            if (manager.getTask(id) != null) {
                manager.removeTask(id);
                System.out.println("Задача #" + id + " удалена.\n");
            }
            if (manager.getEpic(id) != null) {
                manager.removeEpic(id);
                System.out.println("Задача #" + id + " удалена.\n");
            }
            if (manager.getSubtask(id) != null) {
                manager.removeSubtasks(id);
                System.out.println("Задача #" + id + " удалена.\n");
            }
            responseCode = 200;
        }
    }
}