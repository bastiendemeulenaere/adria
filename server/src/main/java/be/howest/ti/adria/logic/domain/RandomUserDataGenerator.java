package be.howest.ti.adria.logic.domain;

import be.howest.ti.adria.logic.exceptions.RepositoryException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
public class RandomUserDataGenerator {
    private static final Logger LOGGER = Logger.getLogger(RandomUserDataGenerator.class.getName());
    private final WebClient webClient;

    private final Random random = new SecureRandom();

    public RandomUserDataGenerator(Vertx vertx) {
        this.webClient = WebClient.create(vertx);
    }

    public UserCreateObject generateRandomUser(String id) {
        String firstName = getRandomDummyDataFromJsonFile("firstnames.json");
        String lastName = getRandomDummyDataFromJsonFile("lastnames.json");
        String aboutMe = getRandomDummyDataFromJsonFile("about-me.json");
        addRandomProfilePictureToUser(id);
        return new UserCreateObject(id, firstName, lastName, aboutMe);
    }

    private void addRandomProfilePictureToUser(String id) {
        webClient.getAbs(String.format("https://api.dicebear.com/7.x/adventurer/jpg?seed=%s", id))
                .send(res -> {
                    if (!res.succeeded()){
                        LOGGER.severe("Unable to fetch user avatar, using default.");
                    }
                    HttpResponse<Buffer> response = res.result();
                    if (response.statusCode() != 200){
                        LOGGER.severe("Unable to fetch user avatar, using default.");
                        return;
                    }
                    Buffer imageBuffer = response.body();
                    saveImageToFile(imageBuffer, id + ".jpg");
                });
    }
    private void saveImageToFile(Buffer imageBuffer, String fileName) {
        try {
            Path imagePath = Paths.get("static/private/users/img", fileName);
            Files.createDirectories(imagePath.getParent());
            try(
                    FileOutputStream outputStream = new FileOutputStream(imagePath.toFile());
            ){
                outputStream.write(imageBuffer.getBytes());
            }

        } catch (IOException e) {
            LOGGER.severe("Failed to save image: " + e.getMessage());
        }
    }

    private String getRandomDummyDataFromJsonFile(String fileName) {
        try (
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dummy-data/" + fileName);
        ) {
            if (inputStream == null)
                throw new RepositoryException("Could not read file: " + fileName);

            String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            String[] dataArray = objectMapper.readValue(jsonString, String[].class);

            int randomIndex = random.nextInt(dataArray.length);

            return dataArray[randomIndex];
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "failed to get random dummy data", e);
            throw new RepositoryException("failed to get random dummy data");
        }
    }
}
