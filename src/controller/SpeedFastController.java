package controller;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;

import dao.impl.EntregaDAOImpl;
import dao.impl.PedidoDAOImpl;
import dao.impl.RepartidorDAOImpl;

import model.Entrega;
import model.Pedido;
import model.Repartidor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SpeedFastController {

    private final RepartidorDAO repartidorDAO;
    private final PedidoDAO pedidoDAO;
    private final EntregaDAO entregaDAO;

    public SpeedFastController() {

        repartidorDAO = new RepartidorDAOImpl();
        pedidoDAO = new PedidoDAOImpl();
        entregaDAO = new EntregaDAOImpl();
    }

    // repartidores

    public boolean registrarRepartidor(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        Repartidor repartidor =
                new Repartidor(nombre.trim());

        return repartidorDAO.create(repartidor);
    }

    public List<Repartidor> listarRepartidores() {
        return repartidorDAO.readAll();
    }

    public boolean actualizarRepartidor(
            int id,
            String nombre
    ) {

        if (id <= 0 ||
                nombre == null ||
                nombre.trim().isEmpty()) {

            return false;
        }

        Repartidor repartidor =
                new Repartidor(
                        id,
                        nombre.trim()
                );

        return repartidorDAO.update(repartidor);
    }

    public boolean eliminarRepartidor(int id) {

        if (id <= 0) {
            return false;
        }

        return repartidorDAO.delete(id);
    }

    // pedidos

    public boolean registrarPedido(
            String direccion,
            String tipo,
            String estado
    ) {

        if (!validarPedido(
                direccion,
                tipo,
                estado
        )) {

            return false;
        }

        Pedido pedido =
                new Pedido(
                        direccion.trim(),
                        tipo,
                        estado
                );

        return pedidoDAO.create(pedido);
    }

    public List<Pedido> listarPedidos() {
        return pedidoDAO.readAll();
    }

    public boolean actualizarPedido(
            int id,
            String direccion,
            String tipo,
            String estado
    ) {

        if (id <= 0 ||
                !validarPedido(
                        direccion,
                        tipo,
                        estado
                )) {

            return false;
        }

        Pedido pedido =
                new Pedido(
                        id,
                        direccion.trim(),
                        tipo,
                        estado
                );

        return pedidoDAO.update(pedido);
    }

    public boolean eliminarPedido(int id) {

        if (id <= 0) {
            return false;
        }

        return pedidoDAO.delete(id);
    }

    // entregas

    public boolean registrarEntrega(
            int idPedido,
            int idRepartidor,
            LocalDate fecha,
            LocalTime hora
    ) {

        if (!validarEntrega(
                idPedido,
                idRepartidor,
                fecha,
                hora
        )) {

            return false;
        }

        if (entregaDAO.existePedido(idPedido)) {
            return false;
        }

        Entrega entrega =
                new Entrega(
                        idPedido,
                        idRepartidor,
                        fecha,
                        hora
                );

        return entregaDAO.create(entrega);
    }

    public List<Entrega> listarEntregas() {
        return entregaDAO.readAll();
    }

    public boolean actualizarEntrega(
            int id,
            int idPedido,
            int idRepartidor,
            LocalDate fecha,
            LocalTime hora
    ) {

        if (id <= 0 ||
                !validarEntrega(
                        idPedido,
                        idRepartidor,
                        fecha,
                        hora
                )) {

            return false;
        }

        Entrega entrega =
                new Entrega(
                        id,
                        idPedido,
                        idRepartidor,
                        fecha,
                        hora
                );

        return entregaDAO.update(entrega);
    }

    public boolean eliminarEntrega(int id) {

        if (id <= 0) {
            return false;
        }

        return entregaDAO.delete(id);
    }

    // validaciones

    private boolean validarPedido(
            String direccion,
            String tipo,
            String estado
    ) {

        if (direccion == null ||
                direccion.trim().isEmpty()) {

            return false;
        }

        if (tipo == null ||
                !(tipo.equals("COMIDA")
                        || tipo.equals("ENCOMIENDA")
                        || tipo.equals("EXPRESS"))) {

            return false;
        }

        return estado != null &&
                (estado.equals("PENDIENTE")
                        || estado.equals("EN_REPARTO")
                        || estado.equals("ENTREGADO"));
    }

    private boolean validarEntrega(
            int idPedido,
            int idRepartidor,
            LocalDate fecha,
            LocalTime hora
    ) {

        return idPedido > 0
                && idRepartidor > 0
                && fecha != null
                && hora != null;
    }
}