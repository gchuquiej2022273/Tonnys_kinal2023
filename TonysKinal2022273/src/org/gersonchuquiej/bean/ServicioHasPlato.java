package org.gersonchuquiej.bean;

public class ServicioHasPlato {

private int servicios_codigoServio;
private int codigoPlato;
private int codigoServicio;

    public ServicioHasPlato() {
    }


    public ServicioHasPlato(int servicios_codigoServio, int codigoPlato, int codigoServicio) {
        this.servicios_codigoServio = servicios_codigoServio;
        this.codigoPlato = codigoPlato;
        this.codigoServicio = codigoServicio;
    }

    
    public int getServicios_codigoServio() {
        return servicios_codigoServio;
    }

    public void setServicios_codigoServio(int servicios_codigoServio) {
        this.servicios_codigoServio = servicios_codigoServio;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

}
