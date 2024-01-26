package org.gersonchuquiej.bean;

public class ProductoHasPlatos {
    private int producos_codigoProductos;
    private int codigoPlato;
    private int codigoProducto;

    public ProductoHasPlatos() {
    }

    public ProductoHasPlatos(int producos_codigoProductos, int codigoPlato, int codigoProducto) {
        this.producos_codigoProductos = producos_codigoProductos;
        this.codigoPlato = codigoPlato;
        this.codigoProducto = codigoProducto;
    }

    
    public int getProducos_codigoProductos() {
        return producos_codigoProductos;
    }

    public void setProducos_codigoProductos(int producos_codigoProductos) {
        this.producos_codigoProductos = producos_codigoProductos;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }
    
    
}
