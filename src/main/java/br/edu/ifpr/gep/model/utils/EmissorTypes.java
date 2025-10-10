package br.edu.ifpr.gep.model.utils;

import br.edu.ifpr.gep.model.repository.EmissorRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa os emissores de portarias do IFPR,
 * com suporte a persistência em arquivo JSON.
 */
public class EmissorTypes {
    private static final List<EmissorTypes> VALUES = new ArrayList<>();
    private static boolean initialized = false;

    private final int index;
    private final String nome;

    @JsonCreator
    public EmissorTypes(@JsonProperty("index") int index, @JsonProperty("nome") String nome) {
        this.index = index;
        this.nome = nome;
    }

    /** Inicializa os emissores padrão e carrega do JSON */
    public static void initialize() {
        if (initialized) return;
        initialized = true;

        // Carrega emissores do arquivo JSON
        List<EmissorTypes> loaded = EmissorRepository.INSTANCE.load();
        if (loaded.isEmpty()) {
            // Se não existir arquivo, usa a lista padrão inicial
            carregarPadrao();
            salvar();
        } else {
            VALUES.addAll(loaded);
        }
    }

    private static void carregarPadrao() {
        VALUES.clear();
        VALUES.add(new EmissorTypes(1, "Reitoria"));
        VALUES.add(new EmissorTypes(2, "Pró-Reitoria de Ensino"));
        VALUES.add(new EmissorTypes(3, "Pró-Reitoria de Administração"));
        VALUES.add(new EmissorTypes(4, "Pró-Reitoria de Extensão, Pesquisa, Pós-Graduação e Inovação"));
        VALUES.add(new EmissorTypes(5, "Pró-Reitoria de Pessoas"));
        VALUES.add(new EmissorTypes(6, "Pró-Reitoria de Planejamento e Desenvolvimento Institucional"));
        VALUES.add(new EmissorTypes(7, "Campus Arapongas (DG)"));
        VALUES.add(new EmissorTypes(0, "Campus Assis Chateaubriand (DG)"));
        VALUES.add(new EmissorTypes(8, "Campus Astorga (DG)"));
        VALUES.add(new EmissorTypes(9, "Campus Barracão (DG)"));
        VALUES.add(new EmissorTypes(10, "Campus Campo Largo (DG)"));
        VALUES.add(new EmissorTypes(11, "Campus Capanema (DG)"));
        VALUES.add(new EmissorTypes(12, "Campus Cascavel (DG)"));
        VALUES.add(new EmissorTypes(13, "Campus Colombo (DG)"));
        VALUES.add(new EmissorTypes(14, "Campus Coronel Vivida (DG)"));
        VALUES.add(new EmissorTypes(15, "Campus Curitiba (DG)"));
        VALUES.add(new EmissorTypes(16, "Campus Foz do Iguaçu (DG)"));
        VALUES.add(new EmissorTypes(17, "Campus Goioerê (DG)"));
        VALUES.add(new EmissorTypes(18, "Campus Irati (DG)"));
        VALUES.add(new EmissorTypes(19, "Campus Ivaiporã (DG)"));
        VALUES.add(new EmissorTypes(20, "Campus Jacarezinho (DG)"));
        VALUES.add(new EmissorTypes(21, "Campus Jaguariaíva (DG)"));
        VALUES.add(new EmissorTypes(22, "Campus Londrina (DG)"));
        VALUES.add(new EmissorTypes(23, "Campus Palmas (DG)"));
        VALUES.add(new EmissorTypes(24, "Campus Paranaguá (DG)"));
        VALUES.add(new EmissorTypes(25, "Campus Paranavaí (DG)"));
        VALUES.add(new EmissorTypes(26, "Campus Pinhais (DG)"));
        VALUES.add(new EmissorTypes(27, "Campus Pitanga (DG)"));
        VALUES.add(new EmissorTypes(28, "Campus Ponta Grossa (DG)"));
        VALUES.add(new EmissorTypes(29, "Campus Quedas do Iguaçu (DG)"));
        VALUES.add(new EmissorTypes(30, "Campus Telêmaco Borba (DG)"));
        VALUES.add(new EmissorTypes(31, "Campus Toledo (DG)"));
        VALUES.add(new EmissorTypes(32, "Campus Umuarama (DG)"));
        VALUES.add(new EmissorTypes(33, "Campus União da Vitória (DG)"));
    }

    public static void salvar() {
        EmissorRepository.INSTANCE.save(VALUES);
    }

    public static EmissorTypes add(String nome) {
        initialize(); // garante que está carregado
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do emissor não pode ser vazio.");
        }
        int nextIndex = VALUES.stream().mapToInt(e -> e.index).max().orElse(0) + 1;
        EmissorTypes newEmissor = new EmissorTypes(nextIndex, nome.trim());
        VALUES.add(newEmissor);
        salvar();
        return newEmissor;
    }

    public static List<EmissorTypes> values() {
        initialize();
        return new ArrayList<>(VALUES);
    }

    public static EmissorTypes fromValue(int index) {
        initialize();
        return VALUES.stream()
                .filter(type -> type.index == index)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nenhum emissor para índice " + index));
    }

    public static EmissorTypes fromName(String name) {
        initialize();
        if (name == null || name.trim().isEmpty()) return null;
        String normName = normalize(name);
        for (EmissorTypes type : VALUES) {
            String normType = normalize(type.nome);
            if (normType.equals(normName)) {
                return type;
            }
        }
        return null;
    }

    private static String normalize(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized.toLowerCase().trim();
    }

    public int getIndex() { return index; }
    public String getNome() { return nome; }

    @Override
    public String toString() {
        return nome + " (" + index + ")";
    }

    public int compareTo(EmissorTypes emissor) {
        return 0;
    }
}
