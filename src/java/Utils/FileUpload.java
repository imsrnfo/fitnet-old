
package Utils;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLConnection;
import javax.servlet.http.Part;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;

public class FileUpload implements Serializable{
    
    private final String homeDir;
    private final String separator;
    private byte[] buffer;
    private String nombreArchivo;
    private String extensionArchivo;
    private String directorioAbsolutoArchivo;
    private String directorioRelativoArchivo;
    
    public FileUpload() {
        homeDir = System.getProperty("user.home");
        separator = System.getProperty("file.separator");
        buffer = new byte[1024];
    }
    
    public FileUpload cargarImagenEnBuffer(byte[] arregloBytes) throws IOException{
        this.buffer = arregloBytes;
        InputStream is = new ByteArrayInputStream(arregloBytes);
        //Obtener extension
        String mimeType = null;
        String fileExtension = null;
        mimeType = URLConnection.guessContentTypeFromStream(is); //mimeType is something like "image/jpeg"
        String delimiter = "[/]";
        String[] tokens = mimeType.split(delimiter);
        fileExtension = tokens[1];
        extensionArchivo =fileExtension;
        return this;
    }
    
    public FileUpload cargarImagenEnBuffer(UploadedFile archivo) throws IOException{
        if (archivo==null) return null; // corrige el error al ingresar cliente y volver sin ingresar foto
        InputStream input = archivo.getInputstream();
        buffer = IOUtils.toByteArray(input);
        extensionArchivo = FilenameUtils.getExtension(archivo.getFileName());
        return this;
    }
    
    public FileUpload cargarImagenEnBuffer(Part archivo) throws IOException{
        if (archivo==null) return null; // corrige el error al ingresar cliente y volver sin ingresar foto
        InputStream input = archivo.getInputStream();
        buffer = IOUtils.toByteArray(input);
        extensionArchivo = FilenameUtils.getExtension(archivo.getSubmittedFileName());
        return this;
    }

    public FileUpload setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo + "." + extensionArchivo;
        return this;
    }

    public FileUpload setDirectorioArchivo(String directorioArchivo) {
        this.directorioAbsolutoArchivo =  homeDir+separator+"fitnet"+separator+directorioArchivo+separator+nombreArchivo;
        this.directorioRelativoArchivo = "/images/"+directorioArchivo+"/"+nombreArchivo;
        return this;
    }
    
    public String guardarImagen() throws FileNotFoundException, IOException{
        try (FileOutputStream fos = new FileOutputStream(directorioAbsolutoArchivo)) {
            fos.write(buffer);
        }
        return directorioRelativoArchivo;
    }
    
    public String guardarImagenPerfil() throws FileNotFoundException, IOException{
        this.directorioAbsolutoArchivo =  homeDir+separator+"fitnet"+separator+"profilePicture"+separator+nombreArchivo;
        this.directorioRelativoArchivo = "/images/"+"profilePicture"+"/"+nombreArchivo;
        try (FileOutputStream fos = new FileOutputStream(directorioAbsolutoArchivo)) {
            fos.write(buffer);
        }
        return directorioRelativoArchivo;
    }
    
    public String guardarLogo() throws FileNotFoundException, IOException{
        this.directorioAbsolutoArchivo =  homeDir+separator+"fitnet"+separator+"gimnasio"+separator+nombreArchivo;
        this.directorioRelativoArchivo = "/images/"+"gimnasio"+"/"+nombreArchivo;
        try (FileOutputStream fos = new FileOutputStream(directorioAbsolutoArchivo)) {
            fos.write(buffer);
        }
        return directorioRelativoArchivo;
    }
    
}