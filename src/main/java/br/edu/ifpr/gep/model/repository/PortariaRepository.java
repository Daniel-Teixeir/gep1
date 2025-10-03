package br.edu.ifpr.gep.model.repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.edu.ifpr.gep.model.Portaria;
import br.edu.ifpr.gep.model.utils.EmissorTypes;

/**
 * Repositório em memória para gerenciar objetos do tipo Portaria.
 * Permite inserção, busca e listagem com filtros.
 * Carrega e salva dados em JSON.
 */
public class PortariaRepository {

    public static final PortariaRepository INSTANCE = new PortariaRepository();

    private final Map<PortariaPK, Portaria> portarias = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File dataFile = new File("portarias.json");

    /**
     * Construtor privado para singleton.
     * Configura Jackson e carrega dados do JSON.
     */
    private PortariaRepository() {
        // Registrar KeyDeserializer para PortariaPK
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(PortariaPK.class, new PortariaPKKeyDeserializer());
        objectMapper.registerModule(module);

        // Registrar módulo para LocalDate
        objectMapper.registerModule(new JavaTimeModule());

        // Configurar para serializar LocalDate corretamente
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        System.out.println("Iniciando repositório... Arquivo JSON: " + dataFile.getAbsolutePath());
        loadData();
        System.out.println("Dados carregados: " + portarias.size() + " portarias.");
    }

    /**
     * Carrega dados do arquivo JSON para o mapa em memória.
     */
    private void loadData() {
        if (!dataFile.exists()) {
            System.out.println("Arquivo JSON não encontrado. Iniciando vazio.");
            return;
        }

        try {
            // Carrega como Map<String, Map<String, Object>> raw para parsing manual
            TypeReference<Map<String, Map<String, Object>>> typeRef = new TypeReference<Map<String, Map<String, Object>>>() {};
            Map<String, Map<String, Object>> rawData = objectMapper.readValue(dataFile, typeRef);

            int loadedCount = 0;
            for (Map.Entry<String, Map<String, Object>> entry : rawData.entrySet()) {
                String keyStr = entry.getKey();
                Map<String, Object> rawPortaria = entry.getValue();

                try {
                    // Parse key to PK
                    PortariaPK pk = (PortariaPK) new PortariaPKKeyDeserializer().deserializeKey(keyStr, null);

                    // Parse Portaria
                    Object emissorObj = rawPortaria.get("emissor");
                    Integer emissorIndex;
                    if (emissorObj instanceof String) {
                        String emissorStr = (String) emissorObj;
                        emissorIndex = mapLegacyEmissor(emissorStr);
                    } else if (emissorObj instanceof Integer) {
                        emissorIndex = (Integer) emissorObj;
                    } else {
                        System.err.println("Tipo de emissor inválido em " + keyStr + ": " + emissorObj);
                        continue;
                    }

                    Integer numero = (Integer) rawPortaria.get("numero");
                    if (numero == null) {
                        System.err.println("Número ausente em " + keyStr);
                        continue;
                    }

                    Object pubObj = rawPortaria.get("publicacao");
                    LocalDate publicacao;
                    if (pubObj instanceof String) {
                        String pubStr = ((String) pubObj).trim();
                        if (pubStr.isEmpty()) {
                            System.err.println("Data de publicação vazia em " + keyStr + ", pulando.");
                            continue;
                        }
                        publicacao = LocalDate.parse(pubStr);
                    } else {
                        System.err.println("Formato de data inválido em " + keyStr + ": " + pubObj);
                        continue;
                    }

                    String membro = (String) rawPortaria.get("membro");
                    if (membro == null || membro.trim().isEmpty()) {
                        System.err.println("Membro ausente ou vazio em " + keyStr);
                        continue;
                    }

                    Portaria portaria = new Portaria(emissorIndex, numero, publicacao, membro.trim());
                    portarias.put(pk, portaria);
                    loadedCount++;
                } catch (Exception e) {
                    System.err.println("Erro ao processar entrada " + keyStr + ": " + e.getMessage());
                }
            }
            System.out.println("Carregadas " + loadedCount + " portarias válidas de " + rawData.size() + " entradas totais.");
        } catch (IOException e) {
            System.err.println("Erro ao ler JSON: " + e.getMessage());
            portarias.clear();
        }
    }

    /**
     * Mapeia emissores legacy (strings) para índices do enum.
     */
    private Integer mapLegacyEmissor(String emissorStr) {
        if (emissorStr == null) return 1;
        String upper = emissorStr.toUpperCase().trim();
        try {
            EmissorTypes e = EmissorTypes.valueOf(upper);
            return e.index();
        } catch (IllegalArgumentException e) {
            // Fallback para códigos curtos legacy
            switch (upper) {
                case "MPF":
                    return 5;
                case "MEC":
                    return 1;
                case "IFPR":
                    return 1;
                case "MJ":
                    return 1;
                case "MINC":
                    return 3;
                case "UFPR":
                    return 1;
                case "UTFPR":
                    return 1;
                case "UNICAMP":
                    return 1;
                case "UEL":
                    return 1;
                case "UFRGS":
                    return 1;
                default:
                    System.err.println("Emissor desconhecido '" + emissorStr + "', usando 1 (Reitoria)");
                    return 1;
            }
        }
    }

    /**
     * Salva dados do mapa para o arquivo JSON.
     * Chamado automaticamente em operações de modificação, se necessário.
     */
    private void saveData() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, portarias);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados no JSON: " + e.getMessage());
        }
    }

    /**
     * Insere uma nova portaria no repositório.
     * Evita duplicidade de chave (emissor + número + ano).
     * Salva no JSON após inserção.
     */
    public boolean insert(Portaria portaria) {
        PortariaPK pk = new PortariaPK(
                portaria.getEmissor().nome(), // usa o nome amigável como chave
                portaria.getNumero(),
                portaria.getPublicacao().getYear()
        );

        if (portarias.containsKey(pk)) {
            return false; // Já existe
        }

        portarias.put(pk, portaria);
        saveData(); // Salva após inserção
        return true;
    }

    /** Atualiza uma portaria existente */
    public boolean update(Portaria portaria) {
        PortariaPK pk = new PortariaPK(
                portaria.getEmissor().nome(),
                portaria.getNumero(),
                portaria.getPublicacao().getYear()
        );
        if (portarias.containsKey(pk)) {
            portarias.put(pk, portaria);
            saveData(); // Salva após atualização
            return true;
        }
        return false;
    }

    /** Deleta uma portaria pela chave */
    public boolean delete(String emissor, Integer numero, Integer ano) {
        boolean removed = portarias.remove(new PortariaPK(emissor, numero, ano)) != null;
        if (removed) {
            saveData(); // Salva após deleção
        }
        return removed;
    }

    /** Deleta todas as portarias */
    public int deleteAll() {
        int size = portarias.size();
        portarias.clear();
        saveData(); // Salva após limpar
        return size;
    }

    /** Busca uma portaria pela chave */
    public Optional<Portaria> findPortaria(String emissor, Integer numero, Integer ano) {
        return Optional.ofNullable(portarias.get(new PortariaPK(emissor, numero, ano)));
    }

    /** Retorna todas as portarias */
    public List<Portaria> findAll() {
        return new ArrayList<>(portarias.values());
    }

    /** Busca por emissor */
    public List<Portaria> findByEmissor(String emissor, boolean strict) {
        return portarias.values().stream()
                .filter(p -> search(p.getEmissor().nome(), emissor, strict))
                .collect(Collectors.toList());
    }

    /** Busca por ano */
    public List<Portaria> findByAno(Integer ano) {
        return portarias.values().stream()
                .filter(p -> p.getPublicacao().getYear() == ano)
                .collect(Collectors.toList());
    }

    /** Busca por membro */
    public List<Portaria> findByMembro(String membro, boolean strict) {
        return portarias.values().stream()
                .filter(p -> search(p.getMembro(), membro, strict))
                .collect(Collectors.toList());
    }

    /** Busca por número */
    public List<Portaria> findByNumero(Integer numero) {
        return portarias.values().stream()
                .filter(p -> p.getNumero().equals(numero))
                .collect(Collectors.toList());
    }

    /** Busca por data de publicação exata */
    public List<Portaria> findByPublicacao(LocalDate data) {
        return portarias.values().stream()
                .filter(p -> p.getPublicacao().equals(data))
                .collect(Collectors.toList());
    }

    /** Busca por período (inclusive) */
    public List<Portaria> findByPeriodo(LocalDate start, LocalDate end) {
        return portarias.values().stream()
                .filter(p -> !p.getPublicacao().isBefore(start) && !p.getPublicacao().isAfter(end))
                .collect(Collectors.toList());
    }

    private boolean search(String field, String value, boolean strict) {
        if (field == null || value == null) return false;
        return strict ? field.equalsIgnoreCase(value) : field.toLowerCase().contains(value.toLowerCase());
    }
}