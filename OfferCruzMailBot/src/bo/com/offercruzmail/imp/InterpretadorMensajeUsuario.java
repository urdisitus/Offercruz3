/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruzmail.imp;

import bo.com.offercruz.bl.contratos.IPerfilBO;
import bo.com.offercruz.bl.contratos.IUsuarioBO;
import bo.com.offercruz.bl.impl.control.FactoriaObjetosNegocio;
import bo.com.offercruz.dal.contrato.IPerfilDAO;
import bo.com.offercruz.entidades.Perfil;
import bo.com.offercruz.entidades.Usuario;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Ernesto
 */
public class InterpretadorMensajeUsuario
        extends InterpretadorMensajeGenerico<Usuario, Integer, IUsuarioBO> {

    @Override
    Usuario convertirHojaEnEntidad() {
        Usuario entidad = new Usuario();
        Cell celda;
        //Id
        celda = hojaActual.getCelda(3, 2);
        if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            entidad.setId((int) celda.getNumericCellValue());
        }
        //Nombre
        celda = hojaActual.getCelda(4, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setLogin(hojaActual.getValorCeldaCadena(celda));
        }
        //Perfil
        celda = hojaActual.getCelda(5, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setPerfil(new Perfil());
            entidad.getPerfil().setNombre(hojaActual.getValorCeldaCadena(celda));            
        }
        //Email
        celda = hojaActual.getCelda(6, 2);
        if (celda.getCellType() != Cell.CELL_TYPE_BLANK) {
            entidad.setCorreoElectronico(hojaActual.getValorCeldaCadena(celda));
        }
        entidad.setTipo(0);
        entidad.setEstado(1);
        return entidad;
    }

    @Override
    protected void preparPlantillaAntesDeEnviar(Workbook libro) {
        IPerfilBO perfilBO = FactoriaObjetosNegocio.getInstance().getIPerfilBO();
        perfilBO.setComandoPermiso(nombreEntidad);
        perfilBO.setIdUsuario(idUsuario);
        List<Perfil> perfiles = perfilBO.obtenerTodos();
        
        String[] descripciones = new String[perfiles.size()];
        for (int i = 0; i < perfiles.size(); i++) {
            descripciones[i] = perfiles.get(i).getNombre();
        }
        hojaActual.agregarValidacionLista(5, 5, 2, 2, descripciones, true, true);
    }

    @Override
    IUsuarioBO getObjetoNegocio() {
        return FactoriaObjetosNegocio.getInstance().getIUsuarioBO();
    }

    @Override
    boolean esNuevo(Usuario entidad) {
        return (entidad.getId() == null);
    }

    @Override
    String getId(Usuario entidad) {
        return entidad.getId().toString();
    }

    @Override
    Integer convertirId(String cadena) throws Exception {
        return convertirIdAEntero(cadena);
    }

    @Override
    void mostrarLista(List<Usuario> lista) {
        int i = 5;
        for (Usuario u : lista) {
            hojaActual.setValorCelda(i, 1, u.getId());
            hojaActual.setValorCelda(i, 2, u.getLogin());
            if (u.getPerfil() != null) {
                hojaActual.setValorCelda(i, 3, u.getPerfil().getNombre());
            }
            hojaActual.setValorCelda(i, 4, u.getCorreoElectronico());
            i++;
        }
    }

    @Override
    void mostrarEntidad(Usuario entidad, Workbook libro) {
        preparPlantillaAntesDeEnviar(libro);
        hojaActual.setValorCelda(3, 2, entidad.getId());
        hojaActual.setValorCelda(4, 2, entidad.getLogin());
        if (entidad.getPerfil() != null){
            hojaActual.setValorCelda(5, 2, entidad.getPerfil().getNombre());
        }
        hojaActual.setValorCelda(6, 2, entidad.getCorreoElectronico());
    }

    @Override
    protected void postInsertar(Usuario entidad) {
        //Enviar email al usuario
        String datosUsuario = "Usuario: " + entidad.getLogin() + "\n Contrasena: " + entidad.getPasswordInicial();
        System.out.println(datosUsuario);
        if (lectorBandejaCorreo != null){
            Message mensaje = new MimeMessage(lectorBandejaCorreo.getSesion());
            try {
                lectorBandejaCorreo.notificarEvento("....Enviando credenciales a nuevo usuario");
                mensaje.setFrom(new InternetAddress(lectorBandejaCorreo.getEmail()));
                mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(entidad.getCorreoElectronico()));
                mensaje.setSubject("Credenciales KiboMailSystem");
                Multipart cuerpo = new MimeMultipart();
                cuerpo.addBodyPart(FormadorMensajes.getBodyPartCredenciales(entidad.getLogin(), entidad.getPasswordInicial()));
                mensaje.setContent(cuerpo);
                Transport.send(mensaje);
                lectorBandejaCorreo.notificarEvento("....Credenciales enviadas");
            } catch (AddressException ex) {
                Logger.getLogger(InterpretadorMensajeUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MessagingException ex) {
                Logger.getLogger(InterpretadorMensajeUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
