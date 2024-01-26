package org.gersonchuquiej.controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTimePicker;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.gersonchuquiej.bean.Empleado;
import org.gersonchuquiej.bean.Servicio;
import org.gersonchuquiej.bean.ServicioHasEmpleado;
import org.gersonchuquiej.db.Conexion;
import org.gersonchuquiej.main.Principal;

public class ServicioHasEmpleadoController implements Initializable{

    private Principal escenarioPrincipal;
    private enum operaciones{NINGUNO,GUARDAR};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<ServicioHasEmpleado> listaServicioHasEmpleado;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empleado> listaEmpleado;
    private DatePicker fecha;

            
        @FXML
    private Button btnNuevo;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private Button btnEliminar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private GridPane grpSerHasEmpleado;

    @FXML
    private TextField txtServicioCodServicio;

    @FXML
    private ComboBox cmbCodServicio;

    @FXML
    private ComboBox cmbCodEmpleado;

    @FXML
    private JFXTimePicker jfxHoraEvento;

    @FXML
    private TextField txtLugarEvento;

    @FXML
    private TableColumn colcodShasEmpleado;

    @FXML
    private TableColumn colcodigoServicio;

    @FXML
    private TableColumn colCodigoEmpleado;

    @FXML
    private TableColumn colFecha;

    @FXML
    private TableColumn colHora;

    @FXML
    private TableColumn colLugarEvento;
    
    @FXML
    private TableView tblServiciosHasEmpleados;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
     cargarDatos();
     fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd")); //se le da el formato ala Fecha
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/gersonchuquiej/resource/TonnysKinal.css");
        grpSerHasEmpleado.add(fecha, 1, 1);
        cmbCodServicio.setItems(getServicios());
        cmbCodEmpleado.setItems(getEmpleados());
    }

    public void cargarDatos(){
        tblServiciosHasEmpleados.setItems(getServicioHasEmpleados());
        colcodShasEmpleado.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Integer>("servicios_codigoServicio"));
        colcodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Integer>("codigoEmpleado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Date>("fechaEvento"));
        colHora.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleadoController, Time>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, String> ("lugarEvento"));
    }
    
     public void nuevo() {

        switch (tipoDeOperaciones) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Guardar.png"));
                imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Cancelar.png"));
                tipoDeOperaciones = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setDisable(true);
                imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();

                break;
        }
    }
    public ObservableList<ServicioHasEmpleado> getServicioHasEmpleados(){
        ArrayList<ServicioHasEmpleado> lista = new ArrayList<ServicioHasEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Empleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServicioHasEmpleado(resultado.getInt("servicios_codigoServicio"),
                                                              resultado.getInt("codigoServicio"),
                                                              resultado.getInt("codigoEmpleado"),
                                                              resultado.getDate("fechaEvento"),
                                                              resultado.getTime("horaEvento"),
                                                              resultado.getString("lugarEvento")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicioHasEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Empleado> getEmpleados(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                                                resultado.getInt("numeroEmpleado"),
                                                resultado.getString("apellidoEmpleado"),
                                                resultado.getString("nombresEmpleado"),
                                                resultado.getString("direccionEmpleado"),
                                                resultado.getString("telefonoContacto"),
                                                resultado.getString("gradoCocinero"),
                                                resultado.getInt("codigoTipoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    
        public Empleado buscarCodigoEmpleado(int codigoEmpleado) {
        Empleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpleado(?)");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado =     new Empleado(registro.getInt("codigoEmpleado"),
                                                registro.getInt("numeroEmpleado"),
                                                registro.getString("apellidoEmpleado"),
                                                registro.getString("nombresEmpleado"),
                                                registro.getString("direccionEmpleado"),
                                                registro.getString("telefonoContacto"),
                                                registro.getString("gradoCocinero"),
                                                registro.getInt("codigoTipoEmpleado"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
        public ObservableList<Servicio> getServicios() {
        ArrayList<Servicio> lista = new ArrayList<Servicio>();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                        resultado.getTime("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("tipoServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
    }
        
    public Servicio buscarCodigoServicio(int codigoServicio) {
        Servicio resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarServicio(?)");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Servicio(registro.getInt("codigoServicio"),
                        registro.getDate("fechaServicio"),
                        registro.getTime("horaServicio"),
                        registro.getString("lugarServicio"),
                        registro.getString("tipoServicio"),
                        registro.getString("telefonoContacto"),
                        registro.getInt("codigoEmpresa"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    public void guardar(){
        ServicioHasEmpleado registro = new ServicioHasEmpleado();
            registro.setServicios_codigoServicio(Integer.parseInt(txtServicioCodServicio.getText()));
            registro.setCodigoServicio(((Servicio)cmbCodServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
            registro.setCodigoEmpleado(((Empleado)cmbCodEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
            registro.setFechaEvento(fecha.getSelectedDate());
            registro.setHoraEvento(Time.valueOf(jfxHoraEvento.getValue()));
            registro.setLugarEvento(txtLugarEvento.getText());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicios_has_Empleados (?,?,?,?,?,?)");
                procedimiento.setInt(1, registro.getServicios_codigoServicio());
                procedimiento.setInt(2, registro.getCodigoServicio());
                procedimiento.setInt(3, registro.getCodigoEmpleado());
                procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
                procedimiento.setTime(5, new java.sql.Time(registro.getHoraEvento().getTime()));
                procedimiento.setString(6, registro.getLugarEvento());
                procedimiento.execute();
                listaServicioHasEmpleado.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void seleccionarElemento(){
        txtServicioCodServicio.setText(String.valueOf(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        cmbCodServicio.getSelectionModel().select(buscarCodigoServicio(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbCodEmpleado.getSelectionModel().select(buscarCodigoEmpleado(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        fecha.selectedDateProperty().set(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
        jfxHoraEvento.setValue(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento().toLocalTime());
        txtLugarEvento.setText(String.valueOf(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento()));
    }
    
    public void activarControles(){
        txtServicioCodServicio.setEditable(true);
        cmbCodServicio.setDisable(false);
        cmbCodEmpleado.setDisable(false);
        jfxHoraEvento.setDisable(false);
        txtLugarEvento.setEditable(true);  
    }
    
    public void desactivarControles(){
        txtServicioCodServicio.setEditable(false);
        cmbCodServicio.setDisable(false);
        cmbCodEmpleado.setDisable(false);
        jfxHoraEvento.setDisable(false);
        txtLugarEvento.setEditable(false);
    }
    
    public void limpiarControles(){
        txtServicioCodServicio.clear();
        cmbCodServicio.setValue(null);
        cmbCodEmpleado.setValue(null);
        fecha.selectedDateProperty().set(null);
        jfxHoraEvento.setValue(null);
        txtLugarEvento.clear();
    }
        
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
