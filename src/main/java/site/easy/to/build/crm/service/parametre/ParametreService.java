package site.easy.to.build.crm.service.parametre;

import site.easy.to.build.crm.entity.Parametre;

import java.util.List;

public interface ParametreService {

    public List<Parametre> findAll();

    public Parametre findById(int id);

    public Parametre findByNom(String nom);

    public Parametre save(Parametre parametre);

    public void delete(Parametre parametre);

    public boolean isTauxAlerteDepasse(double depense, double budget);

}
