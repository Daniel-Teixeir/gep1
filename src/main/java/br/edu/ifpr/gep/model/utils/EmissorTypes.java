package br.edu.ifpr.gep.model.utils;

/**
 * Enum que representa os emissores de portarias do IFPR.
 * Cada emissor possui um índice numérico e um nome amigável para exibição.
 */
public enum EmissorTypes {
    REITORIA(1,"Reitoria"),
    PROENS(2,"Pró-Reitoria de Ensino"),
    PROAD(3,"Pró-Reitoria de Administração"),
    PROEPPI(4,"Pró-Reitoria de Extensão, Pesquisa, Pós-Graduação e Inovação"),
    PROGEPE(5,"Pró-Reitoria de Pessoas"),
    PROPLAN(6,"Pró-Reitoria de Planejamento e Desenvolvimento Institucional"),
    ARAPONGAS_DG(7,"Campus Arapongas (DG)"),
    ASSIS_CHATEAUBRIAND_DG(0,"Campus Assis Chateaubriand (DG)"),
    ASTORGA(8,"Campus Astorga (DG)"),
    BARRACAO(9,"Campus Barracão (DG)"),
    CAMPO_LARGO_DG(10,"Campus Campo Largo (DG)"),
    CAPANEMA_DG(11,"Campus Capanema (DG)"),
    CASCAVEL_DG(12,"Campus Cascavel (DG)"),
    COLOMBO_DG(13,"Campus Colombo (DG)"),
    CORONEL_VIVIDA_DG(14,"Campus Coronel Vivida (DG)"),
    CURITIBA_DG(15,"Campus Curitiba (DG)"),
    FOZ_IGUAÇU_DG(16,"Campus Foz do Iguaçu (DG)"),
    GOIOERÊ_DG(17,"Campus Goioerê (DG)"),
    IRATI_DG(18,"Campus Irati (DG)"),
    IVAIPORÃ_DG(19,"Campus Ivaiporã (DG)"),
    JACAREZINHO(20,"Campus Jacarezinho (DG)"),
    JAGUARIAÍVA_DG(21,"Campus Jaguariaíva (DG)"),
    LONDRINA_DG(22,"Campus Londrina (DG)"),
    PALMAS_DG(23,"Campus Palmas (DG)"),
    PARANAGUÁ_DG(24,"Campus Paranaguá (DG)"),
    PARANAVAÍ_DG(25,"Campus Paranavaí (DG)"),
    PINHAIS_DG(26,"Campus Pinhais (DG)"),
    PITANGA_DG(27,"Campus Pitanga (DG)"),
    PONTA_GROSSA_DG(28,"Campus Ponta Grossa (DG)"),
    QUEDAS_IGUAÇU_DG(29,"Campus Quedas do Iguaçu (DG)"),
    TELÊMACO_BORGA_DG(30,"Campus Telêmaco Borba (DG)"),
    TOLEDO_DG(31,"Campus Toledo (DG)"),
    UMUARAMA_DG(32,"Campus Umuarama (DG)"),
    UNIÃO_VITÓRIA_DG(33,"Campus União da Vitória (DG)");

    private final Integer index;
    private final String nome;

    EmissorTypes(Integer index, String nome) {
        this.index = index;
        this.nome  = nome;
    }

    public static EmissorTypes fromCode(EmissorTypes emissor) {
        return emissor;
    }

    /** Retorna o índice numérico associado ao emissor */
    public Integer index() { return index; }

    /** Retorna o nome completo do emissor */
    public String nome() { return nome; }

    /**
     * Localiza um emissor pelo índice numérico.
     * @param index código numérico do emissor
     * @return o enum correspondente
     * @throws IllegalArgumentException se não houver emissor associado
     */
    public static EmissorTypes fromValue(Integer index) {
        for (EmissorTypes type : values())
            if (type.index.equals(index))
                return type;

        throw new IllegalArgumentException("Nenhum emissor para índice " + index);
    }
}