package org.gersonchuquiej.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
import org.gersonchuquiej.bean.Plato;
import org.gersonchuquiej.bean.Servicio;
import org.gersonchuquiej.bean.ServicioHasPlato;
import org.gersonchuquiej.db.Conexion;
import org.gersonchuquiej.main.Principal;

public class ServicioHasPlatoController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        NINGUNO, GUARDAR
    };
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<ServicioHasPlato> listaServicioHasPlato;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Plato> listaPlato;

    @FXML
    private Button btnNuevo;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private Button btnEliminar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private TableView tblServiciohasPlato;

    @FXML
    private TextField txtCodigoServicioHasPlato;

    @FXML
    private ComboBox cmbCodigoPlato;
    @FXML
    private ComboBox cmbCodigoServicio;

    @FXML
    private TableColumn colServicioHasPlato;
    @FXML
    private TableColumn colCodigoPlato;
    @FXML
    private TableColumn colCodgoServicio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoServicio.setItems(getServicios());
        cmbCodigoPlato.setItems(getPlatos());
    }

    public void cargarDatos() {
        tblServiciohasPlato.setItems(getServicioHasPlatos());
        colServicioHasPlato.setCellValueFactory(new PropertyValueFactory<ServicioHasPlato, Integer>("servicios_codigoServio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServicioHasPlato, Integer>("codigoPlato"));
        colCodgoServicio.setCellValueFactory(new PropertyValueFactory<ServicioHasPlato, Integer>("codigoServicio"));
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

    public ObservableList<ServicioHasPlato> getServicioHasPlatos() {
        ArrayList<ServicioHasPlato> lista = new ArrayList<ServicioHasPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_platos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServicioHasPlato(resultado.getInt("servicios_codigoServio"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoServicio")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicioHasPlato = FXCollections.observableArrayList(lista);
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

    
    public ObservableList<Plato> getPlatos() {
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableList(lista);
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
        }
        return resultado;
    }

    public Plato buscarCodigoPlato(int codigoPlato) {
        Plato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPlato(?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Plato(registro.getInt("codigoPlato"),
                        registro.getInt("cantidad"),
                        registro.getString("nombrePlato"),
                        registro.getString("descripcionPlato"),
                        registro.getDouble("precioPlato"),
                        registro.getInt("codigoTipoPlato"));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return resultado;
    }

    public void guardar() {
        ServicioHasPlato registro = new ServicioHasPlato();
        registro.setServicios_codigoServio(Integer.parseInt(txtCodigoServicioHasPlato.getText()));
        registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicios_has_platos(?,?,?)");
            procedimiento.setInt(1, registro.getServicios_codigoServio());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoServicio());
            procedimiento.execute();
            listaServicioHasPlato.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void seleccionarElemento() {
        txtCodigoServicioHasPlato.setText(String.valueOf(((ServicioHasPlato) tblServiciohasPlato.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbCodigoPlato.getSelectionModel().select(buscarCodigoPlato(((ServicioHasPlato) tblServiciohasPlato.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        cmbCodigoServicio.getSelectionModel().select(buscarCodigoServicio(((ServicioHasPlato) tblServiciohasPlato.getSelectionModel().getSelectedItem()).getCodigoServicio()));
    }

    public void activarControles() {
        cmbCodigoPlato.setDisable(false);
        cmbCodigoServicio.setDisable(false);
        txtCodigoServicioHasPlato.setEditable(true);
    }

    public void desactivarControles() {
        cmbCodigoPlato.setDisable(false);
        cmbCodigoServicio.setDisable(false);
        txtCodigoServicioHasPlato.setEditable(false);
    }

    public void limpiarControles() {
        txtCodigoServicioHasPlato.clear();
        cmbCodigoPlato.setValue(null);
        cmbCodigoServicio.setValue(null);
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
