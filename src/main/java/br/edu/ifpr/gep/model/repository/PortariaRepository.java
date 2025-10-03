package br.edu.ifpr.gep.model.repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.edu.ifpr.gep.model.Portaria;
import br.edu.ifpr.gep.model.StringSearch;

public enum PortariaRepository {
    INSTANCE;

    private final Map<PortariaPK, Portaria> portarias = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File dataFile = new File("portarias.json");

    PortariaRepository() {
        // Registrar KeyDeserializer para PortariaPK
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(PortariaPK.class, new PortariaPKKeyDeserializer());
        objectMapper.registerModule(module);

        // Registrar módulo para LocalDate
        objectMapper.registerModule(new JavaTimeModule());

        System.out.println("Iniciando repositório... Arquivo JSON: " + dataFile.getAbsolutePath());
        loadData();
        System.out.println("Dados carregados: " + portarias.size() + " portarias.");
    }

    private void loadData() {
        if (dataFile.exists()) {
            System.out.println("JSON existe, tentando carregar...");
            try {
                Map<PortariaPK, Portaria> loaded = objectMapper.readValue(dataFile,
                        objectMapper.getTypeFactory().constructMapType(Map.class, PortariaPK.class, Portaria.class));
                portarias.putAll(loaded);
                System.out.println("Carregamento OK: " + loaded.size() + " itens.");
            } catch (IOException e) {
                System.err.println("Erro ao carregar dados: " + e.getMessage());
                e.printStackTrace();
                portarias.clear();
            }
        } else {
            System.out.println("JSON não existe, iniciando vazio.");
        }
    }

    private void saveData() {
        try {
            // Estou pedindo para ele formatar o json no formato Pretty-print
            // Pretty-print: formata com quebras de linha e indentação
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, portarias);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public boolean insert(Portaria portaria) {
        if (findPortaria(portaria.getEmissor(), portaria.getNumero(), portaria.getPublicacao().getYear()).isPresent()) {
            return false;
        }

        PortariaPK pk = new PortariaPK(portaria.getEmissor(), portaria.getNumero(), portaria.getPublicacao().getYear());
        boolean inserted = portarias.put(pk, portaria) == null;
        if (inserted) saveData();
        return inserted;
    }

    public boolean update(Portaria portaria) {
        Optional<Portaria> temp = findPortaria(portaria.getEmissor(), portaria.getNumero(),
                portaria.getPublicacao().getYear());

        if (temp.isPresent()) {
            Portaria p = temp.get();
            p.setMembro(portaria.getMembro());
            saveData();
            return true;
        }
        return false;
    }

    public boolean delete(String emissor, Integer numero, Integer ano) {
        PortariaPK pk = new PortariaPK(emissor, numero, ano);
        boolean deleted = portarias.remove(pk) != null;
        if (deleted) saveData();
        return deleted;
    }

    public int delete() {
        int total = portarias.size();
        portarias.clear();
        saveData();
        return total;
    }

    public List<Portaria> findByEmissor(String emissor, StringSearch searchMode) {
        if (emissor == null || emissor.isEmpty()) return Collections.emptyList();
        return portarias.entrySet().stream()
                .filter(p -> search(p.getValue().getEmissor(), emissor, searchMode))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Portaria> findByNumero(Integer number) {
        if (number == null) return Collections.emptyList();
        return portarias.entrySet().stream()
                .filter(p -> p.getValue().getNumero().equals(number))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Portaria> findByPeriodo(LocalDate start, LocalDate end) {
        if (start == null || end == null) return Collections.emptyList();
        return portarias.entrySet().stream()
                .filter(p -> {
                    LocalDate date = p.getValue().getPublicacao();
                    return (date.isEqual(start) || date.isAfter(start)) &&
                           (date.isEqual(end) || date.isBefore(end));
                })
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Portaria> findByPublicacao(LocalDate start) {
        if (start == null) return Collections.emptyList();
        return portarias.entrySet().stream()
                .filter(p -> p.getValue().getPublicacao().isEqual(start))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Portaria> findByMembro(String nome, StringSearch searchMode) {
        if (nome == null || nome.isEmpty()) return Collections.emptyList();
        return portarias.entrySet().stream()
                .filter(p -> search(p.getValue().getMembro(), nome, searchMode))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public Optional<Portaria> findPortaria(String emissor, Integer numero, Integer ano) {
        PortariaPK pk = new PortariaPK(emissor, numero, ano);
        return portarias.entrySet().stream()
                .filter(p -> p.getKey().equals(pk))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public List<Portaria> findAll() {
        return new ArrayList<>(portarias.values());
    }

    private Boolean search(String str1, String str2, StringSearch searchMode) {
        switch (searchMode) {
            case EXACT: return str1.equals(str2);
            case EXACT_CASE_INSENSITIVE: return str1.equalsIgnoreCase(str2);
            case PARTIAL: return str1.contains(str2);
            case PARTIAL_CASE_INSENSITIVE: return str1.toLowerCase().contains(str2.toLowerCase());
            default: return false;
        }
    }
}
