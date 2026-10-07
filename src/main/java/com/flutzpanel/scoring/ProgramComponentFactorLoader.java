package com.flutzpanel.scoring;

import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ProgramComponentFactorLoader {

    public List<ProgramComponentFactor> load() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("scoring/2026-27/program-component-factors.json")) {

            return objectMapper.readValue(
                    inputStream,
                    objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, ProgramComponentFactor.class)
            );
        }
    }
}