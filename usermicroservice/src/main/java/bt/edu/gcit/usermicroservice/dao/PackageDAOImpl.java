package bt.edu.gcit.usermicroservice.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import bt.edu.gcit.usermicroservice.entity.TourPackage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class PackageDAOImpl implements PackageDAO {

    private EntityManager entityManager;

    public PackageDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public TourPackage save(TourPackage TourPackage) {
        return entityManager.merge(TourPackage);
    }

    @Override
    public TourPackage updatePackage(TourPackage tourPackage) {
        return entityManager.merge(tourPackage);
    }


    @Override
    public TourPackage findByID(int id) {
        return entityManager.find(TourPackage.class, id);
    }

    @Override
    public void deletePackage(long id) {
        TourPackage tourPackage = entityManager.find(TourPackage.class, id);
        if (tourPackage != null) {
            entityManager.remove(tourPackage);
        }
    }

    @Override
    public List<TourPackage> getAllTourPackages() {
        TypedQuery<TourPackage> query = entityManager.createQuery(
                "SELECT t FROM TourPackage t", TourPackage.class);
        return query.getResultList();
    }

}
