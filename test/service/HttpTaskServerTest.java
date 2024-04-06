package service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private TaskManager manager;
    private HttpTaskServer httpTaskServer;
    Duration duration = Duration.ofMinutes(30);
    LocalDateTime startTime = LocalDateTime.now();
    Task task = new Task(1, "Переезд", "Нужно собрать вещи", duration, startTime);
    SubTask subTask = new SubTask(3, "Подзадача","///", duration, startTime, 2);
    ArrayList<Integer> subTasksID = (ArrayList<Integer>) List.of(subTask.getId());
    Epic epic = new Epic(subTasksID,1, "Эпик 1", "///", duration, startTime );

    Task task2 = new Task(4, "Переезд", "Нужно собрать вещи", duration, startTime);
    SubTask subTask2 = new SubTask(5, "Подзадача","///", duration, startTime, 4);
    Epic epic2 = new Epic(subTasksID,4, "Эпик 1", "///", duration, startTime );
    private static final Gson gson = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    @BeforeEach
    public void startServers() throws IOException {
        manager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
    }

    @Test
    void shouldPOSTTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(task.toString(), manager.getTaskById(task.getId()).toString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void shouldGETTask() throws IOException, InterruptedException {
        manager.createTask(task);
        manager.createTask(task2);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        JsonElement jsonElement = JsonParser.parseString(response.body());
        HashMap<Integer, Task> tasksFromJson =
                gson.fromJson(jsonElement, new TypeToken<HashMap<Integer, Task>>(){}.getType());

        assertEquals(200, response.statusCode());
        assertEquals(2, tasksFromJson.size());
    }

    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {
        int id = task.getId();
        manager.createTask(task);
        URI url = URI.create("http://localhost:8080/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int jsonId = jsonObject.get("id").getAsInt();
        String jsonDescription = jsonObject.get("description").getAsString();

        assertEquals(id, jsonId);
        assertTrue(jsonElement.isJsonObject(), "Incorrect JSON");
        assertEquals(task.getDescription(), jsonDescription);
    }

    @Test
    void shouldDELETETask() throws IOException, InterruptedException {
        manager.createTask(task);
        manager.createTask(task2);
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());
        assertEquals("Все задачи удалены", response.body());
    }

    @Test
    void shouldPOSTEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(epic.toString(), manager.getEpicById(epic.getId()).toString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void shouldGETEpic() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createEpic(epic2);
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        JsonElement jsonElement = JsonParser.parseString(response.body());
        HashMap<Integer, Task> tasksFromJson =
                gson.fromJson(jsonElement, new TypeToken<HashMap<Integer, Epic>>(){}.getType());

        assertEquals(200, response.statusCode());
        assertEquals(2, tasksFromJson.size());
    }

    @Test
    void shouldDELETEEpic() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createEpic(epic2);
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());
        assertEquals("Все эпики удалены", response.body());
    }

    @Test
    void shouldPOSTSubtask() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(subTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(subTask.toString(), manager.getSubTaskById(subTask.getId()).toString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void shouldGETSubtask() throws IOException, InterruptedException {
        manager.createEpic(epic);
        manager.createSubTask(subTask);
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        JsonElement jsonElement = JsonParser.parseString(response.body());
        HashMap<Integer, SubTask> tasksFromJson =
                gson.fromJson(jsonElement, new TypeToken<HashMap<Integer, SubTask>>(){}.getType());

        assertEquals(200, response.statusCode());
        assertEquals(1, tasksFromJson.size());
    }

    @Test
    void shouldDELETESubtask() throws IOException, InterruptedException {
        manager.createSubTask(subTask);
        manager.createSubTask(subTask2);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());
        assertEquals("Все подазадчи удалены", response.body());
    }

    @Test
    void shouldGETHistory() throws IOException, InterruptedException {
        manager.createTask(task);
        manager.createTask(task2);
        manager.getTaskById(task.getId());
        manager.getTaskById(task2.getId());
        URI url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertEquals(200, response.statusCode());
        assertTrue(jsonElement.isJsonArray(), "Некорректное преобразование в JSON");
        assertEquals(2, jsonArray.size());
    }


    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
    }
}