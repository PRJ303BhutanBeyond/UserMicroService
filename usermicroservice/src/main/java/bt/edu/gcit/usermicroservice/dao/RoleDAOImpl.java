package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Role;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;

@Repository
public class RoleDAOImpl implements RoleDAO {
    private EntityManager entityManager;

    public RoleDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addRole(Role role) {
        // TODO Auto-generated method
        entityManager.persist(role);
    }
}
