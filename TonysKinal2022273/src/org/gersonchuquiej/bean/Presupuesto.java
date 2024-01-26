package org.gersonchuquiej.bean;

import java.util.Date;

public class Presupuesto {
    private int codigoPresupuesto;
    private Date fechaSolucitud;
    private double cantidadPresupuesto;
    private int codigoEmpresa;

    public Presupuesto() {
    }

    public Presupuesto(int codigoPresupuesto, Date fechaSolucitud, double cantidadPresupuesto, int codigoEmpresa) {
        this.codigoPresupuesto = codigoPresupuesto;
        this.fechaSolucitud = fechaSolucitud;
        this.cantidadPresupuesto = cantidadPresupuesto;
        this.codigoEmpresa = codigoEmpresa;
    }

    
    public int getCodigoPresupuesto() {
        return codigoPresupuesto;
    }

    public void setCodigoPresupuesto(int codigoPresupuesto) {
        this.codigoPresupuesto = codigoPresupuesto;
    }

    public Date getFechaSolucitud() {
        return fechaSolucitud;
    }

    public void setFechaSolucitud(Date fechaSolucitud) {
        this.fechaSolucitud = fechaSolucitud;
    }

    public double getCantidadPresupuesto() {
        return cantidadPresupuesto;
    }

    public void setCantidadPresupuesto(double cantidadPresupuesto) {
        this.cantidadPresupuesto = cantidadPresupuesto;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

   
    
}
