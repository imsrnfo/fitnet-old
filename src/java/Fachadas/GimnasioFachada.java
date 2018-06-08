package Fachadas;

import DAOs.GimnasioDAO;
import DAOs.UsuarioDAO;
import HBMs.Gimnasio;
import HBMs.PermisoUsuario;
import HBMs.Usuario;
import Utils.FileUpload;
import Utils.Seguridad;
import java.util.HashSet;
import java.util.Set;

public class GimnasioFachada{

    private static GimnasioFachada instance = null;
    
    public static GimnasioFachada getInstance() throws Exception {
        if(instance == null) {
            instance = new GimnasioFachada();
        }
        return instance;
    }
    
    private GimnasioFachada() throws Exception {}
    
    public void guardar(Gimnasio gimnasio, FileUpload logo, FileUpload foto, Usuario usuario) throws Exception{
        
        if (gimnasio.getId()>0) GimnasioDAO.getInstance().update(gimnasio);
        
        if (gimnasio.getId()==0){
            logo.setNombreArchivo(String.valueOf(gimnasio.getId()));
            gimnasio.setImagen(logo.guardarLogo());
            GimnasioDAO.getInstance().create(gimnasio);
            usuario.setGimnasio(gimnasio);
            foto.setNombreArchivo(String.valueOf(usuario.getUsername()));
            usuario.setImagen(foto.guardarImagenPerfil());
            usuario.setHashkey(new Seguridad().getPasswordSeguro(usuario.getUsername(), usuario.getPassword()));
            
            PermisoUsuario pu = new PermisoUsuario();
            pu.setPermiso(UsuarioDAO.getInstance().obtenerPermisoPorId(9));
            pu.setUsuario(usuario);
            
            Set<PermisoUsuario> permisosUsuario = new HashSet<>();
            permisosUsuario.add(pu);
            
            usuario.setPermisoUsuarios(permisosUsuario);
            UsuarioDAO.getInstance().crearUsuarioConPermisos(usuario);
        }
        
    }
    
}
