package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Dzongkhag;
import bt.edu.gcit.usermicroservice.entity.TopDestination;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import jakarta.persistence.TypedQuery;

@Repository
public class TopDestinationDAOImpl implements TopDestinationDAO {
    private EntityManager entityManager;

    public TopDestinationDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<TopDestination> findByDzongkhagOrderByNameAsc(Dzongkhag dzongkhag) {
        return entityManager
                .createQuery("select t from TopDestination t where t.dzongkhag = :dzongkhag order by t.name asc",
                        TopDestination.class)
                .setParameter("dzongkhag", dzongkhag).getResultList();
    }

    @Override
    public List<TopDestination> findAll() {
        return entityManager.createQuery("select t from TopDestination t", TopDestination.class).getResultList();
    }

    @Override
    public TopDestination findById(int theId) {
        return entityManager.find(TopDestination.class, theId);
    }

    @Override
    public TopDestination save(TopDestination theDestination, String dzongkhagName) {
        return entityManager.merge(theDestination);
    }

    @Override
    public void deleteById(int theId) {
        Query query = entityManager.createQuery(
                "DELETE FROM TopDestination t WHERE t.id = :id");
        query.setParameter("id", theId);
        query.executeUpdate();
    }


    @Override
    public List<TopDestination> listTopDestinationByDzongkhag(String theDzongkhag) {
        TypedQuery<TopDestination> query = entityManager.createQuery(
                "SELECT t FROM TopDestination t WHERE t.dzongkhag.dzongkhagName = :theDzongkhag", TopDestination.class);
        query.setParameter("theDzongkhag", theDzongkhag);
        return query.getResultList();
    }

    @Override
    public TopDestination update(TopDestination destination) {
        return entityManager.merge(destination);
    }


}
