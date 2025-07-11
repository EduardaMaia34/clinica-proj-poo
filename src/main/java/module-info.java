module com.eduardamaia.clinica.projetopooclinica {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires java.persistence;      // Para o javax.persistence-api 2.2
    requires org.hibernate.orm.core; // Para o hibernate-core 5.6.15.Final
    requires java.xml.bind;
    requires java.activation;
    requires org.slf4j;

    exports com.eduardamaia.clinica.projetopooclinica.entities;
    exports com.eduardamaia.clinica.projetopooclinica.util;

    opens com.eduardamaia.clinica.projetopooclinica.entities to org.hibernate.orm.core;
    opens com.eduardamaia.clinica.projetopooclinica.util to org.hibernate.orm.core;

    opens com.eduardamaia.clinica.projetopooclinica to javafx.fxml;
    exports com.eduardamaia.clinica.projetopooclinica;

    requires java.naming;
}
