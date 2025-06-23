package com.eduardamaia;
import org.hibernate.Session;

import com.eduardamaia.entities.Medico;
import com.eduardamaia.util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Medico medico = new Medico();
        medico.setNome("Carlos Silva");
        medico.setCpf("12345678900");
        medico.setEndereco("Rua A, 100");
        medico.setValorConsulta(200.0);
        medico.setCodigoConselho("CRM12345");

        session.persist(medico);

        session.getTransaction().commit();
        session.close();

        HibernateUtil.shutdown();
    }
}
