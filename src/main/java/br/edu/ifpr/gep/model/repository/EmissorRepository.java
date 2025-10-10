package br.edu.ifpr.gep.model.repository;

import br.edu.ifpr.gep.model.utils.EmissorTypes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public enum EmissorRepository {
    INSTANCE;

    private static final String FILE_PATH = "emissores.json";
    private final ObjectMapper mapper = new ObjectMapper();

    public List<EmissorTypes> load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(file, new TypeReference<List<EmissorTypes>>() {});
        } catch (IOException e) {
            System.err.println("Erro ao carregar emissores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void save(List<EmissorTypes> emissores) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), emissores);
        } catch (IOException e) {
            System.err.println("Erro ao salvar emissores: " + e.getMessage());
        }
    }
}
