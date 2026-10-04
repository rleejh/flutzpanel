package com.flutzpanel.scoring;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;

public class ScaleOfValuesLoader {

    private ObjectMapper objectMapper;


    public ScaleOfValuesLoader() {
        this.objectMapper = new ObjectMapper();
    }

    public ScaleOfValues load() throws IOException {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream("scoring/2026-27/scale-of-values.json")){

            if (inputStream == null) {
                throw new IOException("Could not find scale-of-values.json");
            }

            return objectMapper.readValue(inputStream, ScaleOfValues.class);
        }
    }
}
