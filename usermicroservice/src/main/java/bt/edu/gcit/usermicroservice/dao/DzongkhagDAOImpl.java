package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Dzongkhag;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import jakarta.persistence.TypedQuery;

@Repository
public class DzongkhagDAOImpl implements DzongkhagDAO {
    private EntityManager entityManager;

    public DzongkhagDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Dzongkhag findByID(int theId) {
        return entityManager.find(Dzongkhag.class, theId);
    }

    @Override
    public Dzongkhag save(Dzongkhag dzongkhag) {
        return entityManager.merge(dzongkhag);
    }

    @Override
    public Dzongkhag findByNameDzongkhag(String dzongkhagName) {
        TypedQuery<Dzongkhag> query = entityManager.createQuery(
                "SELECT d FROM Dzongkhag d WHERE d.dzongkhagName = :name", Dzongkhag.class);
        query.setParameter("name", dzongkhagName);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null; // Return null if no Dzongkhag with the given name is found
        }
    }

    @Override
    public List<Dzongkhag> findAll() {
        return entityManager.createQuery("SELECT d FROM Dzongkhag d", Dzongkhag.class)
                .getResultList();
    }

    @Override
    public List<Dzongkhag> findAllByOrderByNameAsc() {
        return entityManager.createQuery("SELECT d FROM Dzongkhag d ORDER BY d.dzongkhagName ASC", Dzongkhag.class)
                .getResultList();
    }

    @Override
    public Dzongkhag update(Dzongkhag dzongkhag) {
        return entityManager.merge(dzongkhag);
    }

    @Override
    public void deleteById(int theId) {
        Dzongkhag dzongkhag = entityManager.find(Dzongkhag.class, theId);
        entityManager.remove(dzongkhag);
    }

}
