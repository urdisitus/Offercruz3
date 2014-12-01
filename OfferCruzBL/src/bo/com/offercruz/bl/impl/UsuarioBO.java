/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.com.offercruz.bl.impl;


import bo.com.offercruz.bl.contratos.IUsuarioBO;
import bo.com.offercruz.bl.excepticiones.BusinessExceptionMessage;
import bo.com.offercruz.dal.contrato.IUsuarioDAO;
import bo.com.offercruz.entidades.Usuario;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Olvinho
 */
public class UsuarioBO extends ObjetoNegocioGenerico<Usuario, Integer, IUsuarioDAO> implements IUsuarioBO {

    //algoritmos
    public static final String MD2 = "MD2";
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA384 = "SHA-384";
    public static final String SHA512 = "SHA-512";

    //Validar Email
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

//    @Override
//    protected int IdPermisoInsertar() {
//        return 20001;
//    }

//    @Override
//    protected int IdPermisoActualizar() {
//        return 20002;
//    }

    @Override
    IUsuarioDAO getObjetoDAO() {
        return getDaoManager().getUsuarioDAO();
    }

    @Override
    public Integer getIdUsuarioPorEmail(String email) {
        final String x = email;
        return ejecutarEnTransaccion(() -> getObjetoDAO().getIdUsuarioPorCorreoElectronico(x));
    }

    @Override
    public Usuario loguear(final String usuario, final String pass) {
        return ejecutarEnTransaccion(() -> {
            Usuario user = getObjetoDAO().logear(usuario, pass);
            //if (user.getRol() != null){
            //    user.getRol().getDescripcion();
            //}
            return user;
        });
    }

    @Override
    protected void validar(Usuario entity) {

        //Nombre
        boolean nombreValido = true;
        if (isNullOrEmpty(entity.getLogin())) {
            appendException(new BusinessExceptionMessage("El login es un campo requerido", "login"));
            nombreValido = false;
        } else if (entity.getLogin().length() > 30) {
            appendException(new BusinessExceptionMessage("El login no puede tener más de 30 carácteres", "login"));
            nombreValido = false;
        }

        if (nombreValido) {
            if (entity.getId() == null) {
                //Inserccion
                Integer id = getObjetoDAO().getIdPorLogin(entity.getLogin());
                if (id != null) {
                    appendException(new BusinessExceptionMessage("El login '" + entity.getLogin() + "' ya existe", "login"));
                }
            } else {
                //Actualizacion
                if (!getObjetoDAO().checkId(entity.getId())) {
                    appendException(new BusinessExceptionMessage("El usuario con Id  '" + entity.getId() + "' no existe", "id"));
                } else {
                    Usuario actual = getObjetoDAO().obtenerPorId(entity.getId());
                    if (!actual.getLogin().equals(entity.getLogin())) {
                        //El codigo cambio verificamos si existe
                        if (getObjetoDAO().getIdPorLogin(entity.getLogin()) != null) {
                            appendException(new BusinessExceptionMessage("El login '" + entity.getLogin() + "' ya existe", "login"));
                        }
                    }
                    entity.setPassword(actual.getPassword());
                    entity.setFechaCreacion(actual.getFechaCreacion());
                    entity.setTipo(actual.getTipo());
                    entity.setEstado(actual.getEstado());
                }
            }
        }

        //Email
        boolean emailValido = true;
        if (isNullOrEmpty(entity.getCorreoElectronico())) {
            appendException(new BusinessExceptionMessage("El email es un campo requerido", "email"));
            emailValido = false;
        } else if (entity.getCorreoElectronico().length() > 50) {
            appendException(new BusinessExceptionMessage("El email no puede tener más de 50 carácteres", "email"));
            emailValido = false;
        } else if (!validarEmail(entity.getCorreoElectronico())) {
            appendException(new BusinessExceptionMessage("El email no es válido", "email"));
            emailValido = false;
        }

        if (emailValido) {
            if (entity.getId() == null) {
                if ((getObjetoDAO().getIdUsuarioPorCorreoElectronico(entity.getCorreoElectronico()) != null)) {
                    appendException(new BusinessExceptionMessage("El email " + entity.getCorreoElectronico() + " ya esta registrado", "email"));
                }
            } else if (getObjetoDAO().checkId(entity.getId())) {
                Usuario actual = getObjetoDAO().obtenerPorId(entity.getId());
                if (!actual.getCorreoElectronico().equalsIgnoreCase(entity.getCorreoElectronico())) {
                    if ((getObjetoDAO().getIdUsuarioPorCorreoElectronico(entity.getCorreoElectronico()) != null)) {
                        appendException(new BusinessExceptionMessage("El email " + entity.getCorreoElectronico() + " ya esta registrado", "email"));
                    }
                }
            }
        }

        //Perfil
        if (entity.getPerfil() == null) {
            appendException(new BusinessExceptionMessage("El perfil es un campo requerido", "perfil"));
        } else {
            if (entity.getPerfil().getId() != null) {
                if (!(getDaoManager().getPerfilDAO().checkId(entity.getPerfil().getId()))) {
                    appendException(new BusinessExceptionMessage("El perfil '" + entity.getPerfil().getId() + "' no existe", "perfil"));
                }
            } else {
                //Buscamos por Nombre
                if (isNullOrEmpty(entity.getPerfil().getNombre())) {
                    appendException(new BusinessExceptionMessage("El perfil es un campo requerido", "perfil"));
                } else {
                    entity.getPerfil().setId(getDaoManager().getPerfilDAO().getIdPorNombre(entity.getPerfil().getNombre()));
                    if (entity.getPerfil().getId() == null) {
                        appendException(new BusinessExceptionMessage("El perfil '" + entity.getPerfil().getNombre() + "' no existe", "perfil"));
                    }
                }
            }
        }

    }

    private String contrasena;

    @Override
    protected void preActualizar(Usuario entidad) {
        entidad.setFechaModificacion(new Date());
    }      

    @Override
    protected void preInsertar(Usuario entidad) {
        //Generamos la conseña
        entidad.setFechaCreacion(new Date());
        entidad.setFechaModificacion(new Date());
        entidad.setPasswordInicial(cadenaAleatoria(15));
        entidad.setPassword(encriptar(entidad.getPasswordInicial()));
        contrasena = entidad.getPasswordInicial();
    }

    @Override
    protected void postInsertar(Usuario entidad) {
        entidad.setPasswordInicial(contrasena);
    }

    public boolean validarEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String cadenaAleatoria(int longitud) {
        String cadenaAleatoria = "";
        long milis = new java.util.GregorianCalendar().getTimeInMillis();
        Random r = new Random(milis);
        int i = 0;
        while (i < longitud) {
            char c = (char) r.nextInt(255);
            if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z')) {
                cadenaAleatoria += c;
                i++;
            }
        }
        return cadenaAleatoria;
    }

    @Override
    public String encriptar(String texto) {
        return getStringMessageDigest(texto, SHA256);
    }

    /**
     * *
     * Convierte un arreglo de bytes a String usando valores hexadecimales
     *
     * @param digest arreglo de bytes a convertir
     * @return String creado a partir de <code>digest</code>
     */
    private static String toHexadecimal(byte[] digest) {
        String hash = "";
        for (byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) {
                hash += "0";
            }
            hash += Integer.toHexString(b);
        }
        return hash;
    }

    /**
     * *
     * Encripta un mensaje de texto mediante algoritmo de resumen de mensaje.
     *
     * @param message texto a encriptar
     * @param algorithm algoritmo de encriptacion, puede ser: MD2, MD5, SHA-1,
     * SHA-256, SHA-384, SHA-512
     * @return mensaje encriptado
     */
    public static String getStringMessageDigest(String message, String algorithm) {
        byte[] digest = null;
        byte[] buffer = message.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            messageDigest.update(buffer);
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error creando Digest");
        }
        return toHexadecimal(digest);
    }

}
