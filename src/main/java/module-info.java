module gep {  // Ou o nome do seu módulo
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens br.edu.ifpr.gep.aplicacao to javafx.fxml;
    opens br.edu.ifpr.gep.panel to javafx.fxml;
    opens br.edu.ifpr.gep.panel.dados to javafx.fxml;
    opens br.edu.ifpr.gep.panel.dados.alterar to javafx.fxml;
    opens br.edu.ifpr.gep.panel.consulta to javafx.fxml;
    opens br.edu.ifpr.gep.model.repository;
    opens br.edu.ifpr.gep.model to com.fasterxml.jackson.databind, javafx.base;  // Para TableView reflection

    exports br.edu.ifpr.gep.aplicacao;
    exports br.edu.ifpr.gep.panel;
    exports br.edu.ifpr.gep.panel.dados;
    exports br.edu.ifpr.gep.panel.consulta;
}