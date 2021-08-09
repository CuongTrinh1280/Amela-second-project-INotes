package com.assignment.edu.annotation;

import com.assignment.edu.repository.ICategoryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.List;

@Transactional
public class UniqueConstraintValidator implements ConstraintValidator<UniqueConstraint, String> {

    private static final Logger logger = Logger.getLogger(UniqueConstraintValidator.class);
    Session session;

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    private Class<?> entityClass;
    private String uniqueField;

    @Override
    public void initialize(UniqueConstraint unique) {
        entityClass = unique.entity();
        uniqueField = unique.property();

    }

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public boolean isValid(String type, ConstraintValidatorContext context) {
        return categoryRepository.findByType(type) == null;
    }

    @SuppressWarnings("rawtypes")
    public boolean isValid(Serializable property, ConstraintValidatorContext context) {

        String query = String.format("from %s where %s = :field ", entityClass.getName(), uniqueField);
        List list = getSession().createQuery(query).setParameter("field", property).list();
        return list != null && list.size() == 0;
    }
}
