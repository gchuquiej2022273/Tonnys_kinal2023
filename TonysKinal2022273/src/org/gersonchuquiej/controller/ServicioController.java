package org.gersonchuquiej.controller;

import com.jfoenix.controls.JFXTimePicker;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.JOptionPane;
import org.gersonchuquiej.bean.Empresa;
import org.gersonchuquiej.bean.Servicio;
import org.gersonchuquiej.db.Conexion;
import org.gersonchuquiej.main.Principal;

public class ServicioController implements Initializable {

    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empresa>ListaEmpresa;
    private DatePicker fecha;

    @FXML
    private TextField txtCodigoServicio;
    @FXML
    private TextField txtTipoServicio;
    @FXML
    private TextField txtLigarServicio;
    @FXML
    private TextField txtTelefono;

    @FXML
    private ComboBox cmbCodigoEmpresa;

    @FXML
    private GridPane grpServicios;

    @FXML
    private TableView tblServicios;
    
    @FXML private JFXTimePicker jfxHoraServicio;
    
    @FXML
    private TableColumn colCodServicio;
    @FXML
    private TableColumn colTipoServicio;
    @FXML
    private TableColumn colHoraServicio;
    @FXML
    private TableColumn colFechaServicio;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colLugarServicio;
    @FXML
    private TableColumn ColCodigoEmpresa;

    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;

    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        
        cmbCodigoEmpresa.setItems(getEmpresa());
    }

    public void cargarDatos() {
        tblServicios.setItems(getServicios());
        colCodServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Time>("horaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio , String>("lugarServicio"));
        ColCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
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
    
    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),    
                            resultado.getString("nombreEmpresa"),
                            resultado.getString("direccion"),
                            resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ListaEmpresa =   FXCollections.observableList(lista);
    }

    public Empresa bucarEmpresa(int codigoEmpresa){
        
        Empresa resultado = null;
        try{
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
            
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                registro.getString("nombreEmpresa"),
                                registro.getString("direccion"),
                                registro.getString("telefono"));
                
            }
            
        }catch(Exception e){
        
            e.printStackTrace();
            
        }
        
        return resultado;
    }
    
    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                cargarDatos();
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Guardar.png"));
                imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar el registo", "Eliminar Empleados", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicio(?)");
                            procedimiento.setInt(1, ((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServicio.remove(tblServicios.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/gersonchuquiej/image/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/gersonchuquiej/image/Cancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "debe seleccionar un elemento");
                }

                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("reporte");
                imgEditar.setImage(new Image("/org/gersonchuquiej/image/Editar.png"));
                imgReporte.setImage(new Image("/org/gersonchuquiej/image/Reporte.png"));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

        public void reporte(){
        switch (tipoDeOperacion) {
            case ACTUALIZAR:
                    actualizar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    imgEditar.setImage(new Image("/org/gersonchuquiej/image/Editar.png"));
                    imgReporte.setImage(new Image("/org/gersonchuquiej/image/Reporte.png"));
                    cargarDatos();
                    tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    public void guardar() {
        Servicio registro = new Servicio();
            
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setHoraServicio(Time.valueOf(jfxHoraServicio.getValue()));
            registro.setLugarServicio(txtLigarServicio.getText());
            registro.setTelefonoContacto(txtTelefono.getText());
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            
            
            try{
                
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicios(?,?,?,?,?,?);");
                
                procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
                procedimiento.setString(2, registro.getTipoServicio());
                procedimiento.setTime(3, new java.sql.Time(registro.getHoraServicio().getTime()));
                procedimiento.setString(4, registro.getLugarServicio());
                procedimiento.setString(5, registro.getTelefonoContacto());
                procedimiento.setInt(6, registro.getCodigoEmpresa());      
                procedimiento.execute();
                listaServicio.equals(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
    
    }

    public void actualizar() {
        try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarServicio(?,?,?,?,?,?,?)");           
                Servicio registro = (Servicio)tblServicios.getSelectionModel().getSelectedItem(); 
                registro.setFechaServicio(fecha.getSelectedDate());
                registro.setTipoServicio(txtTipoServicio.getText());
                registro.setHoraServicio(Time.valueOf(jfxHoraServicio.getValue()));
                registro.setLugarServicio(txtLigarServicio.getText());
                registro.setTelefonoContacto(txtTelefono.getText());
                registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());   
                procedimiento.setInt(1, registro.getCodigoServicio());
                procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
                procedimiento.setString(3, registro.getTipoServicio());
                procedimiento.setTime(4, registro.getHoraServicio());
                procedimiento.setString(5, registro.getLugarServicio());
                procedimiento.setString(6, registro.getTelefonoContacto());
                procedimiento.setInt(7, registro.getCodigoEmpresa());
                procedimiento.execute();
                
            }catch(Exception e){
                e.printStackTrace();
            }
    }

    public void seleccionarElemento(){
        txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
        txtTipoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio()));
        jfxHoraServicio.setValue((((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio()).toLocalTime());
        txtLigarServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio()));
        txtTelefono.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
        cmbCodigoEmpresa.getSelectionModel().select(bucarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
    }
    public void limpiarControles() {
        txtCodigoServicio.clear();
        txtTipoServicio.clear();
        txtLigarServicio.clear();
        txtTelefono.clear();
        cmbCodigoEmpresa.setValue(null);
        jfxHoraServicio.setValue(null);
        fecha.selectedDateProperty().set(null);
    }

    public void activarControles() {
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(true);
        jfxHoraServicio.setEditable(true);
        txtLigarServicio.setEditable(true);
        txtTelefono.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
    }

    public void desactivarControles() {
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(false);
        jfxHoraServicio.setDisable(false);
        txtLigarServicio.setEditable(false);
        txtTelefono.setEditable(false);
        cmbCodigoEmpresa.setDisable(false);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

}
