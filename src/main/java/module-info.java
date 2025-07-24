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

    // IMPORTANT: Changed from 'java.persistence' to 'jakarta.persistence'
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.slf4j;

    exports com.eduardamaia.clinica.projetopooclinica.entities;
    exports com.eduardamaia.clinica.projetopooclinica.util;

    // These opens directives are correct for Hibernate 6
    opens com.eduardamaia.clinica.projetopooclinica.entities to org.hibernate.orm.core;
    opens com.eduardamaia.clinica.projetopooclinica.util to org.hibernate.orm.core;

    opens com.eduardamaia.clinica.projetopooclinica to javafx.fxml;
    exports com.eduardamaia.clinica.projetopooclinica;
    exports com.eduardamaia.clinica.projetopooclinica.controller;
    opens com.eduardamaia.clinica.projetopooclinica.controller to javafx.fxml;

    requires java.naming; // Keep this, as Hibernate often uses JNDI internally
}