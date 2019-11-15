package dao;

import models.Dog;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class DogDao {

    public Dog findById (int id) {
        Session session =  HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Dog dogDB = session.get(Dog.class, id);
        session.close();
        return dogDB;
    }

    public void save (Dog dogDB) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction ts = session.beginTransaction();
        session.save(dogDB);
        ts.commit();
        session.close();
    }

    public void update (Dog dogDB) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction ts = session.beginTransaction();
        session.update(dogDB);
        ts.commit();
        session.close();
    }

    public void delete (Dog dogDB) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction ts = session.beginTransaction();
        session.delete(dogDB);
        ts.commit();
        session.close();
    }

    public List<Dog> findAll() {
        Query<Dog> query = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from dog d", Dog.class);
        return query.getResultList();
    }
}
