 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruzmail.imp;

import bo.com.offercruz.bl.contratos.IGenericoBO;
import bo.com.offercruz.bl.excepticiones.BusinessException;
import bo.com.offercruz.bl.excepticiones.BusinessExceptionMessage;
import bo.com.offercruz.bl.excepticiones.PermisosInsuficientesException;
import bo.com.offercruzmail.LectorBandejaCorreo;
import bo.com.offercruzmail.contrato.IInterpretadorMensaje;
import bo.com.offercruzmail.utils.HojaExcelHelper;
import bo.com.offercruzmail.utils.UtilitariosMensajes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 *
 * @author Olvinho
 * @param <T> Clase entidad
 * @param <ID> Clase que representa el id
 * @param <BO> Clase de negocio
 */
public abstract class InterpretadorMensajeGenerico<T, ID extends Serializable, BO extends IGenericoBO<T, ID>>
        implements IInterpretadorMensaje {

    private static final ThreadLocal<Map<String, IInterpretadorMensaje>> caja = new ThreadLocal<>();

    public static Map<String, IInterpretadorMensaje> getMapaObjetos() {
        Map<String, IInterpretadorMensaje> mapa = caja.get();
        if (mapa == null) {
            mapa = new HashMap<>();
//            mapa.put("area", new InterpretadorMensajeArea());
//            mapa.put("faja", new InterpretadorMensajeFaja());
//            mapa.put("especie", new InterpretadorMensajeEspecie());
//            mapa.put("calidad", new IntepretadorMensajeCalidad());
//            mapa.put("carga", new InterpretadorMensajeCarga());
//            mapa.put("patio", new InterpretardorMensajePatio());
//
//            mapa.put("censo", new InterpretadorMensajeCenso());
//            mapa.put("tala", new InterpretadorMensajeCorta());
//            mapa.put("extraccion", new InterpretadorMensajeExtraccion());
//            mapa.put("despacho", new InterpretadorMensajeMovimiento());
//            mapa.put("dasometrico", new InterpretarPlantillaFormulario());
            mapa.put("oferta", new InterpretadorMensajeOferta());
            mapa.put("perfil", new InterpretadorMensajePerfil());
            mapa.put("categoria", new InterpretadorMensajeCategoria());
            mapa.put("usuario", new InterpretadorMensajeUsuario());
            mapa.put("empresa", new InterpretadorMensajeEmpresa());
//
//            mapa.put("reportes", new InterpretadorMensajeReportes());

            caja.set(mapa);
        }
        return mapa;
    }

    protected Integer idUsuario;
    protected String parametros;
    protected String nombreEntidad;
    protected List<File> archivosTemporales = new ArrayList<>();
    protected HojaExcelHelper hojaActual;
    private BusinessException mensajesError;
    private boolean cargarPlantillaFormularios;
    protected LectorBandejaCorreo lectorBandejaCorreo;

    public InterpretadorMensajeGenerico() {
        parametros = "";
        cargarPlantillaFormularios = true;
    }

    @Override
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public boolean isCargarPlantillaFormularios() {
        return cargarPlantillaFormularios;
    }

    public void setCargarPlantillaFormularios(boolean cargarPlantillaFormularios) {
        this.cargarPlantillaFormularios = cargarPlantillaFormularios;
    }

    public void setLectorBandejaCorreo(LectorBandejaCorreo lectorBandejaCorreo) {
        this.lectorBandejaCorreo = lectorBandejaCorreo;
    }

    @Override
    public Multipart interpretar() throws MessagingException, IOException {
        if (parametros == null || "".equals(parametros)) {
            return null;
        }
        String[] params = parametros.split(UtilitariosMensajes.SEPERADOR_PARAMETROS);
        if (params.length == 0) {
            params = new String[]{parametros};
        }
        String idCargar = "";
        if (params.length > 1) {
            idCargar = params[1];
        }
        switch (params[0]) {
            case "plantilla":
                return enviarPlantilla(true, idCargar);
            case "cargar":
                return enviarPlantilla(false, idCargar);
        }
        return null;
    }

    protected void appendException(BusinessExceptionMessage message) {
        if (mensajesError == null) {
            mensajesError = new BusinessException(message);
            return;
        }
        mensajesError.getMessages().add(message);
    }

    protected Multipart enviarPlantilla(boolean plantillaNueva, String idCargar) throws MessagingException, IOException {
        String nombreArchivoOrigen;
        String nombreAdjunto;
        List<T> lista = null;
        mensajesError = null;
        T entidad = null;
        getObjetoNegocio().setIdUsuario(idUsuario);
        getObjetoNegocio().setComandoPermiso(nombreEntidad);
        try {
            if (!plantillaNueva) {
                if ("todos".equals(idCargar)) {
                    lista = getObjetoNegocio().obtenerTodos();
                    nombreArchivoOrigen = nombreEntidad + "-" + "lista";
                    nombreAdjunto = "lista_" + nombreEntidad + ".xlsx";
                } else {
                    ID id;
                    try {
                        id = convertirId(idCargar);
                    } catch (Exception ex) {
                        return FormadorMensajes.enviarIdCargarNoValido();
                    }
                    entidad = getObjetoNegocio().recuperarPorId(id);
                    if (entidad == null) {
                        return FormadorMensajes.enviarEntidadNoExiste(idCargar);
                    }
                    nombreArchivoOrigen = nombreEntidad;
                    nombreAdjunto = nombreEntidad + "_" + idCargar + ".xlsx";
                }
            } else {
                nombreArchivoOrigen = nombreEntidad;
                nombreAdjunto = "plantilla_" + nombreEntidad + ".xlsx";
//            if (this instanceof IInterpretadorFormularioDasometrico) {
//                if (cargarPlantillaFormularios) {
//                    nombreArchivoOrigen = "plantillafrm";
//                }
//            }
            }
            String nombreArchivoOriginal = "plantillas/" + nombreArchivoOrigen + ".xlsx";
            File archivoCopia = UtilitariosMensajes.reservarNombre(nombreEntidad);
            UtilitariosMensajes.copiarArchivo(new File(nombreArchivoOriginal), archivoCopia);
            archivosTemporales.add(archivoCopia);
            FileInputStream fis = null;
            OutputStream os = null;
            try {
                Workbook libro;
                fis = new FileInputStream(archivoCopia);
                libro = WorkbookFactory.create(fis);
                hojaActual = new HojaExcelHelper(libro.getSheetAt(0));
                if (plantillaNueva) {
                    preparPlantillaAntesDeEnviar(libro);
                } else {
                    if (lista != null) {
                        mostrarLista(lista);
                    } else {
                        mostrarEntidad(entidad, libro);
                    }
                }
                if (mensajesError != null) {
                    return FormadorMensajes.enviarErroresNegocio(mensajesError);
                }
                //Guardamos cambio
                os = new FileOutputStream(archivoCopia);
                libro.write(os);
            } catch (InvalidFormatException ex) {

            } finally {
                if (fis != null) {
                    fis.close();
                }
                if (os != null) {
                    os.close();
                }
            }
            String textoMensaje;
            if (plantillaNueva) {
                textoMensaje = escapeHtml4("La plantilla está adjunta a este mensaje.");
            } else if (lista != null) {
                textoMensaje = "La consulta ha devuelto " + lista.size() + " registro(s).";
            } else {
                textoMensaje = escapeHtml4("El registro solicitado está adjunto a este mensaje");
            }
            Multipart cuerpo = new MimeMultipart();
            BodyPart adjunto = new MimeBodyPart();
            DataSource origen = new FileDataSource(archivoCopia);
            adjunto.setDataHandler(new DataHandler(origen));
            adjunto.setFileName(nombreAdjunto);
            cuerpo.addBodyPart(FormadorMensajes.getBodyPartEnvuelto(textoMensaje));
            cuerpo.addBodyPart(adjunto);
            return cuerpo;
        } catch (PermisosInsuficientesException ex) {
            appendException(new BusinessExceptionMessage(ex.getMessage(), "Autentificacion"));
        }
        if (mensajesError != null) {
            return FormadorMensajes.enviarErroresNegocio(mensajesError);
        }
        return null;
    }

    @Override
    public void setNombreEntidad(String nombre) {
        this.nombreEntidad = nombre;
    }

    @Override
    public List<File> obtenerArchivoTemporalesCreados() {
        return archivosTemporales;
    }

    protected Multipart enviarInserccionExitosa(T entidad) throws MessagingException {
        return FormadorMensajes.enviarInserccionExitosa(getId(entidad));

    }

    @Override
    public Multipart interpretarHojaExcel(Sheet hojaExcel) throws MessagingException {
        this.hojaActual = new HojaExcelHelper(hojaExcel);
        mensajesError = null;
        T entidad = convertirHojaEnEntidad();
        if (mensajesError != null) {
            return FormadorMensajes.enviarErroresNegocio(mensajesError);
        }
        getObjetoNegocio().setIdUsuario(idUsuario);
        getObjetoNegocio().setComandoPermiso(nombreEntidad);
        boolean esInserccion;
        try {
            if (esNuevo(entidad)) {
                esInserccion = true;
                preInsertar(entidad);
                entidad = getObjetoNegocio().insertar(entidad);
                postInsertar(entidad);
            } else {
                esInserccion = false;
                preActualizar(entidad);
                entidad = getObjetoNegocio().actualizar(entidad);
                postActualizar(entidad);

            }
        } catch (BusinessException e) {
            return FormadorMensajes.enviarErroresNegocio(e);
        } catch (Exception e) {
            return FormadorMensajes.enviarErrorInesperado();
        }

        if (esInserccion) {
            return enviarInserccionExitosa(entidad);
        }
        return FormadorMensajes.enviarModificacionExitosa();
    }

    protected Integer convertirIdAEntero(String cadena) {
        return new Integer(cadena);
    }

    protected void preparPlantillaAntesDeEnviar(Workbook libro) {

    }

//    /**
//     * *
//     * Carga las áreas a la plantilla para solicitar un formulario dasométrico
//     */
//    protected void cargarAreasAPlantillaFormularios() {
//        List<Area> areas = FactoriaObjetosNegocio.getInstance().getAreaBO().obtenerTodos();
//        String[] codigos = new String[areas.size()];
//        for (int i = 0; i < areas.size(); i++) {
//            codigos[i] = areas.get(i).getCodigo();
//        }
//        hojaActual.agregarValidacionLista(4, 4, 2, 2, codigos, true, true);
//    }
    protected void preInsertar(T entidad) {
    }

    protected void postInsertar(T entidad) {
    }

    protected void preActualizar(T entidad) {
    }

    protected void postActualizar(T entidad) {
    }

    abstract T convertirHojaEnEntidad();

    abstract BO getObjetoNegocio();

    abstract boolean esNuevo(T entidad);

    abstract String getId(T entidad);

    abstract ID convertirId(String cadena) throws Exception;

    abstract void mostrarLista(List<T> lista);

    abstract void mostrarEntidad(T entidad, Workbook libro);

}
