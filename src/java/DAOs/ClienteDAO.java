package DAOs;

import DataBase.DBGenericClass;
import DataBase.HibernateUtil;
import HBMs.Bitacora;
import HBMs.Cliente;
import HBMs.Gimnasio;
import Utils.ManejadorSession;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class ClienteDAO extends DBGenericClass<Cliente>{
    
    private Session session;
    
    private static ClienteDAO instance = null;
    
    public static ClienteDAO getInstance()  throws Exception{
        if(instance == null) {
            instance = new ClienteDAO();
        }
        return instance;
    }
    
    @Override
    public void create(Cliente entity) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            
            //por el momento no se permitiran crear 2 clientes con la misma cedula independientemente del gimnasio al que pertenezcan
            Query query = session.createQuery("select c from " + Cliente.class.getName() + " c " + "where c.cedula = :cedula");
            query.setParameter("cedula", entity.getCedula());
            if (query.list().size() > 0) throw new Exception("Ya existe un cliente registrado con la cedula : " + entity.getCedula());
            
            trans = session.beginTransaction();
            session.persist(entity);
            session.flush();
            trans.commit();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
    private ClienteDAO()  throws Exception{
        super(Cliente.class);
        session=super.getSession();
    }
    
    public Cliente obtenerClientesPorCedula(String cedula) throws Exception{
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        List <Cliente> listaCliente = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select c from " + Cliente.class.getName() + " c " + "where c.cedula = :cedula and c.bitacora.usuario.gimnasio.id = :id_gimnasio");
            query.setParameter("cedula", cedula);
            query.setParameter("id_gimnasio", gym.getId());
            listaCliente = query.list();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return (listaCliente.size()>0)?listaCliente.get(0) : null;
    }
    
    public Cliente buscarPorId(int idCliente) throws Exception{
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        List <Cliente> listaCliente = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select c from " + Cliente.class.getName() + " c " + "where c.id = :id_cliente and c.bitacora.usuario.gimnasio.id = :id_gimnasio");
            query.setParameter("id_cliente", idCliente);
            query.setParameter("id_gimnasio", gym.getId());
            listaCliente = query.list();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return (listaCliente.size()>0)?listaCliente.get(0) : null;
    }
    
    public Cliente buscarPorIdConBitacora(int idCliente) throws Exception{
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        List <Cliente> listaCliente = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select c from " + Cliente.class.getName() + " c " + "where c.id = :id_cliente and c.bitacora.usuario.gimnasio.id = :id_gimnasio");
            query.setParameter("id_cliente", idCliente);
            query.setParameter("id_gimnasio", gym.getId());
            listaCliente = query.list();
            for (Cliente cliente : listaCliente){
                Hibernate.initialize(cliente.getBitacora());
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return (listaCliente.size()>0)?listaCliente.get(0) : null;
    }
    
    public List<Cliente> obtenerClientes() throws Exception{
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        List <Cliente> listaCliente = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select c from " + Cliente.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio");
            query.setParameter("id_gimnasio", gym.getId());
            listaCliente = query.list();
            for (Cliente cliente : listaCliente) {
                Hibernate.initialize(cliente.getBitacora());
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaCliente;
    }
    
    public void guardar(Cliente cliente) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            
            //por el momento no se permitiran crear 2 clientes con la misma cedula independientemente del gimnasio al que pertenezcan
            Query query = session.createQuery("select c from " + Cliente.class.getName() + " c " + "where c.cedula = :cedula");
            query.setParameter("cedula", cliente.getCedula());
            if (query.list().size() > 0) throw new Exception("Ya existe un cliente registrado con la cedula : " + cliente.getCedula());
            
            trans = session.beginTransaction();
            Bitacora bitacora = cliente.getBitacora();
            session.persist(bitacora);
            session.flush();
            session.persist(cliente);
            session.flush();
            trans.commit();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
    public void borrar(Cliente cliente) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            Bitacora bitacora = cliente.getBitacora();
            bitacora.setEstado("ELIMINADO");
            session.update(bitacora);
            trans.commit();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
    public List<Cliente> obtenerClientesREST(String key) throws Exception{
        Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
        List <Cliente> listaCliente = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select c from " + Cliente.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio");
            query.setParameter("id_gimnasio", gym.getId());
            listaCliente = query.list();
            for (Cliente cliente : listaCliente) {
                cliente.setCobros(null);
                cliente.setMarcas(null);
                cliente.setBitacora(null);
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaCliente;
    }
    
    public Cliente obtenerClienteREST(String id,String key) throws Exception{
        Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
        List <Cliente> listaCliente = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select c from " + Cliente.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio and c.id = :id");
            query.setParameter("id_gimnasio", gym.getId());
            query.setParameter("id", Integer.valueOf(id));
            listaCliente = query.list();
            for (Cliente cliente : listaCliente) {
                cliente.setCobros(null);
                cliente.setMarcas(null);
                cliente.setBitacora(null);
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return (listaCliente!=null && listaCliente.size()>0)? listaCliente.get(0) : null;
    }
    
    public void crearClienteREST(Cliente entidad) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            
            //por el momento no se permitiran crear 2 clientes con la misma cedula independientemente del gimnasio al que pertenezcan
            Query query = session.createQuery("select c from " + Cliente.class.getName() + " c " + "where c.cedula = :cedula");
            query.setParameter("cedula", entidad.getCedula());
            if (query.list().size() > 0) throw new Exception("Ya existe un cliente registrado con la cedula : " + entidad.getCedula());
            
            Bitacora bitacora = entidad.getBitacora();
            session.persist(bitacora);
            session.flush();
            session.persist(entidad);
            session.flush();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
}
