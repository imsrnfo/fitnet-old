package DataBase;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.metadata.ClassMetadata;

public abstract class DBGenericClass<T>{
    
    private Session session;
    protected Transaction trans;
    private Class<T> entityClass;
    
    public DBGenericClass(Class<T> entityClass) throws Exception {
        try{
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.getCurrentSession();
            this.entityClass = entityClass;
        }catch(Exception e){
            //TODO: persistir el error
            throw e;
        }
    }
    
    public Session getSession() throws Exception{
        return this.session;
    }
    
    public void create(T entity) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            session.save(entity);
            trans.commit();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
    public void update(T entity) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            session.update(entity);
            trans.commit();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
    public void delete(T entity) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            session.delete(entity);
            trans.commit();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
    public T findById(int id) throws Exception{
        T object;
        try{
        session = HibernateUtil.getSessionFactory().openSession();
        object = (T)session.get(entityClass, id);
        }catch(Exception e){
            throw e;
        }finally{
            session.close();
        }
        return object;
    }
    
    public List<T> findAll() throws Exception {
        List<T> list = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria cri = session.createCriteria(entityClass);
            list = cri.list();
        }catch(Exception e){
            throw e;
        }finally{
            session.close();
        }
        return list;
    }
    
    public boolean existenRegistros() throws Exception {
        List<T> list = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria cri = session.createCriteria(entityClass);
            list = cri.list();
        }catch(Exception e){
            throw e;
        }finally{
            session.close();
        }
        if((list!=null) && (!list.isEmpty())){
            return true;
        }else{
            return false;
        }
    }
    
    public int count()  throws Exception{
        Long result;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            result = (long)session.createCriteria(entityClass).setProjection(Projections.rowCount()).uniqueResult();
        }catch(Exception e){
            throw e;
        }finally{
            session.close();
        }
        return result.intValue();
    }
    
    public Object getIdentifier(Object entity) throws Exception{
        Object result;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            ClassMetadata cmd = session.getSessionFactory().getClassMetadata(entityClass);
            result = cmd.getIdentifier(entity);
        }catch(Exception e){
            throw e;
        }finally{
            session.close();
        }
        return result;
    }
}