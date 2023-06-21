package service;

import com.google.gson.reflect.TypeToken;
import model.enums.Status;
import model.model.Epic;
import model.model.Subtask;
import model.model.Task;
import model.servers.HttpTaskServer;
import model.service.taskManagers.InMemoryTaskManager;
import model.service.taskManagers.TaskManager;
import model.utils.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Test;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTasksServerTest {

    private HttpTaskServer server;
    private Subtask subtask1;
    private Subtask subtask2;
    private Task task1;
    private Epic epic1;
    private Gson gson;

    @BeforeEach
    void loadInitialConditions() throws IOException {

        TaskManager manager = new InMemoryTaskManager();
        server = new HttpTaskServer(manager);
        gson = Converter.createGson();

        task1 = new Task("Task1", "Task1D", LocalDateTime.of(2023, 5, 1, 1, 1), 22);
        manager.createTask(task1);

        epic1 = new Epic("Epic1", "Epic1D");
        manager.createEpic(epic1);

        subtask1 = new Subtask("Subtask01", Status.IN_PROGRESS, "subId01", epic1.getId(), LocalDateTime.of(2023, 7, 1, 1, 1), 22);
        manager.createSubtask(subtask1);

        subtask2 = new Subtask("Subtask02", Status.IN_PROGRESS, "subId02", epic1.getId(), LocalDateTime.of(2022, 12, 1, 1, 1), 22);
        manager.createSubtask(subtask2);

        manager.getTask(task1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());

        server.start();
    }

    @AfterEach
    void serverStop() {
        server.stop();
    }

    @Test
    void getTasksTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        TypeToken<ArrayList<Task>> taskType = new TypeToken<ArrayList<Task>>() {
        };

        List<Task> tasks = gson.fromJson(response.body(), taskType.getType());

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
    }

    @Test
    void getEpicsTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        TypeToken<ArrayList<Epic>> epicType = new TypeToken<ArrayList<Epic>>() {
        };

        List<Epic> epics = gson.fromJson(response.body(), epicType.getType());

        assertNotNull(epics);
        assertEquals(1, epics.size());

    }

    @Test
    void getSubtasksTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        TypeToken<ArrayList<Subtask>> subtaskType = new TypeToken<ArrayList<Subtask>>() {
        };
        List<Subtask> subtasks = gson.fromJson(response.body(), subtaskType.getType());

        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
    }

    @Test
    void getTaskTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/" + task1.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type type = new TypeToken<Task>() {
        }.getType();
        Task task = gson.fromJson(response.body(), type);

        assertNotNull(task);
        assertEquals(task1, task);

    }

    @Test
    void getHistoryTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");

        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                GET().
                build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type type = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), type);

        assertNotNull(tasks);
        assertEquals(4, tasks.size());

    }

    @Test
    void getPrioritizedTasksTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type type = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), type);

        assertNotNull(tasks);
        assertEquals(4, tasks.size());

    }

    @Test
    void createTaskTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");

        Task task2 = new Task("Task1", "Task1D", LocalDateTime.of(2023, 6, 1, 1, 1), 22);

        String json = gson.toJson(task2);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

    }

    @Test
    void createEpicTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");

        Epic epic2 = new Epic("Epic1", "Epic1D");
        String json = gson.toJson(epic2);


        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

    }

    @Test
    void createSubtaskTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");

        String json = gson.toJson(subtask1);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

    }

    @Test
    void deleteTaskTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

    }

    @Test
    void deleteTasksTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        TypeToken<ArrayList<Task>> taskType = new TypeToken<ArrayList<Task>>() {
        };

        List<Task> tasks = gson.fromJson(response.body(), taskType.getType());

        assertNull(tasks);
    }
}