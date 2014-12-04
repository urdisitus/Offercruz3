/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruzmail.imp;
import bo.com.offercruz.bl.excepticiones.BusinessException;
import bo.com.offercruz.bl.excepticiones.BusinessExceptionMessage;
import bo.com.offercruz.bl.excepticiones.PermisosInsuficientesException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import static  org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 *
 * @author Olvinho
 */
public class FormadorMensajes {

    private static String plantillaGeneral;
    public static String tablaHtmlAyuda;
    private static String tablaExcepciones;
    private static String filaExcepcion;
    private static String credenciales;

    static {
        try {
            plantillaGeneral = readFile("html/contenedor.html", Charset.defaultCharset());
            tablaHtmlAyuda = readFile("html/tabla_ayuda.html", Charset.defaultCharset());
            tablaExcepciones = readFile("html/tabla_excepciones.html", Charset.defaultCharset());
            filaExcepcion = readFile("html/fila_excepcion.html", Charset.defaultCharset());
            credenciales = readFile("html/credenciales.html", Charset.defaultCharset());
        } catch (IOException ex) {
            Logger.getLogger(FormadorMensajes.class.getName()).log(Level.SEVERE, null, ex);
           
        }
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static String envolverMensaje(String texto) {
        String aux = plantillaGeneral;
        return aux.replace("{texto}", texto);
    }

    public static String envolverFilaExcepcion(String fila, String mensaje) {
        return filaExcepcion.replace("{fila}", fila).replace("{mensaje}", mensaje);
    }

    public static String envolverTablaExcepcion(String filas) {
        return tablaExcepciones.replace("{filas}", filas);
    }

    public static BodyPart getBodyPartEnvuelto(String texto) throws MessagingException {
        BodyPart cuerpo = new MimeBodyPart();
        cuerpo.setContent(plantillaGeneral.replace("{texto}", texto), "text/html");
        return cuerpo;
    }

    public static BodyPart getBodyPartCredenciales(String usuario, String contrasena) throws MessagingException {
        return getBodyPartEnvuelto(credenciales.replace("{usuario}", usuario).replace("{pass}", contrasena));
    }

    public static Multipart enviarErroresNegocio(BusinessException errores) throws MessagingException {
        Multipart cuerpo = new MimeMultipart();
        StringBuilder mensaje = new StringBuilder();
        if (errores instanceof PermisosInsuficientesException) {
            mensaje.append(errores.getMessage());
        } else {
            mensaje.append(escapeHtml4("No se pudo completar la petición debido a los siguiente errores:"));
            mensaje.append("<br /><br />");
            StringBuilder filas = new StringBuilder();
            for (BusinessExceptionMessage error : errores.getMessages()) {
                if (error.getIndex() > 0) {
                    filas.append(FormadorMensajes.envolverFilaExcepcion(error.getIndex() + "", escapeHtml4(error.getMessage())));
                } else {
                    filas.append(FormadorMensajes.envolverFilaExcepcion("-",  escapeHtml4(error.getMessage())));
                }
                
            }
            mensaje.append(FormadorMensajes.envolverTablaExcepcion(filas.toString()));
        }
        cuerpo.addBodyPart(FormadorMensajes.getBodyPartEnvuelto(mensaje.toString()));
        return cuerpo;
    }

    public static Multipart enviarErrorInesperado() throws MessagingException {
        Multipart cuerpo = new MimeMultipart();
        cuerpo.addBodyPart(FormadorMensajes.getBodyPartEnvuelto("Ha ocurrido un error inesperado, por favor intentelo nuevamente"));
        return cuerpo;
    }

    public static Multipart enviarEntidadNoExiste(String id) throws MessagingException {
        Multipart cuerpo = new MimeMultipart();
        cuerpo.addBodyPart(FormadorMensajes.getBodyPartEnvuelto("El regitro con Id <b>" + id + "</b> no existe"));
        return cuerpo;
    }

    public static Multipart enviarIdCargarNoValido() throws MessagingException {
        Multipart cuerpo = new MimeMultipart();
        StringBuilder texto = new StringBuilder();
        texto.append(escapeHtml4("No se pudo recuperar el Id especificado, debe enviar un número válido o la cadena ")); 
        texto.append("<b>todos</b> para recuperar todos los registros.");
        cuerpo.addBodyPart(FormadorMensajes.getBodyPartEnvuelto(texto.toString()));
        return cuerpo;

    }

    public static Multipart enviarModificacionExitosa() throws MessagingException {
        Multipart cuerpo = new MimeMultipart();
        cuerpo.addBodyPart(FormadorMensajes.getBodyPartEnvuelto(
                escapeHtml4("Se ha efectuado la actualización exitosamente")));
        return cuerpo;
    }

    public static Multipart enviarInserccionExitosa(String id) throws MessagingException {
        Multipart cuerpo = new MimeMultipart();
        StringBuilder texto =  new StringBuilder();
         texto.append(escapeHtml4("La insercción se ha efectuado exitosamente. El identificador del registro es "));
         texto.append("<b>").append(id).append("<\b>");
        cuerpo.addBodyPart(FormadorMensajes.getBodyPartEnvuelto(texto.toString()));
        return cuerpo;

    }
    
    public static Multipart getMensajeUsuarioNoRegistrado() throws MessagingException {
        Multipart multiPartes = new MimeMultipart();
        multiPartes.addBodyPart(FormadorMensajes.
                getBodyPartEnvuelto(
                        escapeHtml4("Lo siento no está registrado para poder usar este sistema")));
        return multiPartes;
    }

    public static Multipart getMensajeUsuarioAyuda(boolean comandoReconocido) throws MessagingException {
        Multipart multiPartes = new MimeMultipart();
        StringBuilder mensaje = new StringBuilder();
        if (comandoReconocido) {
            //Es ayuda
            mensaje.append("Manual de comandos para el sistema de lector de bandeja Kibo. <br /><br />");
        } else {
            mensaje.append(
                    escapeHtml4("El mensaje no ha sido reconocido, consulte el manual para enviar un mensaje válido."));
            mensaje.append("<br /><br />");
        }
        mensaje.append(FormadorMensajes.tablaHtmlAyuda);
        multiPartes.addBodyPart(FormadorMensajes.getBodyPartEnvuelto(mensaje.toString()));
        return multiPartes;
    }

    public static Multipart getMensajeUsuarioAyuda() throws MessagingException {
        return getMensajeUsuarioAyuda(false);
    }

}
