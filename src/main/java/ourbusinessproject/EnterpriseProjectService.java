package ourbusinessproject;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EnterpriseProjectService {

    @PersistenceContext
    private EntityManager entityManager;

    public Project saveProjectForEnterprise(Project project, Enterprise enterprise) {
        saveEnterprise(enterprise);
        project.setEnterprise(enterprise);
        enterprise.addProject(project);
        entityManager.persist(project);
        
        /** *
         * Question 1.2 )
         * La méthode flush permet d'exécuter des instructions directement sans passer par 
         * le cache SQL ou attendre un commit.
         * Dans notre cas, elle permet d'envoyer les données des projets pour les utliser 
         * de suite après pour les entreprises.
         ** */
        entityManager.flush();
        return project;
    }

    public Enterprise saveEnterprise(Enterprise enterprise) {
        
        entityManager.flush();
        return entityManager.merge(enterprise);
    }

    public Project findProjectById(Long id) {
        return entityManager.find(Project.class, id);
    }

    public Enterprise findEnterpriseById(Long id) {
        return entityManager.find(Enterprise.class, id);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Project> findAllProjects() {
        TypedQuery<Project> query = entityManager.createQuery("select p from Project p join fetch p.enterprise order by p.title", Project.class);
        return query.getResultList();
    }
}
