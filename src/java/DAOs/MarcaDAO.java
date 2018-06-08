package DAOs;

import DataBase.DBGenericClass;
import DataBase.HibernateUtil;
import HBMs.Bitacora;
import HBMs.Cliente;
import HBMs.Cobro;
import HBMs.Gimnasio;
import HBMs.Marca;
import Utils.ManejadorSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class MarcaDAO extends DBGenericClass<Marca>{
    
    private Session session;
    
    private static MarcaDAO instance = null;
    
    public static MarcaDAO getInstance() throws Exception {
        if(instance == null) {
            instance = new MarcaDAO();
        }
        return instance;
    }
    
    private MarcaDAO()  throws Exception{
        super(Marca.class);
        session=super.getSession();
    }
    
//    public boolean validarMarcaDeClienteCobroFechaPersonalizada(Cliente cli) throws Exception{
//        List <Cobro> listaCobro = new ArrayList<>();
//        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
//        try{
//            session = HibernateUtil.getSessionFactory().openSession();
//            Query query = session.createQuery("select c from " + Cobro.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio and c.fdesde <= current_date and c.fhasta >= current_date and c.cliente.id = :id_cli");
//            query.setParameter("id_cli", cli.getId());
//            query.setParameter("id_gimnasio", gym.getId());
//            listaCobro = query.list();
//        }catch(HibernateException e){
//            throw e;
//        }finally{
//            session.close();
//        }
//        return listaCobro.size()>0;
//    }
    
    //PARA HACER ESTO SE REQUIERE QUE LA MARCA TENGA UNA ACTIVIDAD ASOCIADA, PERO EL TEMA ES A LA HORA DE ENTRAR A QUE ACTIVIDAD ENTRA
//    public int obtenerCantidadMarcasPorActividad(int idActividad) throws Exception{
//        Marca result = null;
//        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
//        try{
//            session = HibernateUtil.getSessionFactory().openSession();
//            Query query = session.createQuery("select m from " + Marca.class.getName() + " m " + "where m.bitacora.usuario.gimnasio.id = :id_gimnasio and  ");
//            query.setParameter("idActividad", idActividad);
//            query.setParameter("id_gimnasio", gym.getId());
//            if (query.list().size()>0) result = (Marca) query.list().get(0);
//        }catch(HibernateException e){
//            throw e;
//        }finally{
//            session.close();
//        }
//        return result;
//    }
    
    public Marca obtenerMarcaClientePorDia(Cliente cli, Date fecha) throws Exception{
        Marca result = null;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select m from " + Marca.class.getName() + " m " + "where m.bitacora.usuario.gimnasio.id = :id_gimnasio and  DAY(m.fecha) = DAY(:fecha) and MONTH(m.fecha) = MONTH(:fecha) and YEAR(m.fecha) = YEAR(:fecha) and m.cliente.id = :id_cli");
            query.setParameter("id_cli", cli.getId());
            query.setParameter("fecha", fecha);
            query.setParameter("id_gimnasio", gym.getId());
            if (query.list().size()>0) result = (Marca) query.list().get(0);
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result;
    }
    
    public List<Marca> obtenerMarcas() throws Exception{
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        List <Marca> listaMarca = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select m from " + Marca.class.getName() + " m " + "where m.bitacora.usuario.gimnasio.id = :id_gimnasio");
            query.setParameter("id_gimnasio", gym.getId());
            listaMarca = query.list();
            for (Marca marca : listaMarca){
                Hibernate.initialize(marca.getCliente());
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaMarca;
    }
    
    public void guardar(Marca marca) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            Bitacora bitacora = marca.getBitacora();
            session.persist(bitacora);
            session.flush();
            session.persist(marca);
            session.flush();
            trans.commit();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
    public List<Marca> obtenerMarcasREST(String key) throws Exception{
        Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
        List <Marca> listaMarca = new ArrayList<>();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select m from " + Marca.class.getName() + " m " + "where m.bitacora.usuario.gimnasio.id = :id_gimnasio");
            query.setParameter("id_gimnasio", gym.getId());
            listaMarca = query.list();
            for (Marca marca : listaMarca){
                Hibernate.initialize(marca.getCliente());
                Cliente cliente = marca.getCliente();
                cliente.setCobros(null);
                cliente.setMarcas(null);
                cliente.setBitacora(null);
                marca.setBitacora(null);
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaMarca;
    }
    
    public void crearMarcaREST(Marca marca) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Bitacora bitacora = marca.getBitacora();
            session.persist(bitacora);
            session.flush();
            session.persist(marca);
            session.flush();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
    public List<Marca> obtenerAccesosHoy() throws Exception{
        List<Marca> result = null;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select m from " + Marca.class.getName() + " m " + "where m.bitacora.usuario.gimnasio.id = :id_gimnasio and "
                    + "DATE(m.fecha) = STR_TO_DATE(:fechaInicio, '%d/%m/%Y')");
            query.setParameter("fechaInicio", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
             query.setParameter("id_gimnasio", gym.getId());
            result = query.list();
            for (Marca marca : result){
                Hibernate.initialize(marca.getCliente());   
            }
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result!=null?result:new ArrayList<>();
    }
}
