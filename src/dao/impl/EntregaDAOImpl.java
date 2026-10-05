package dao.impl;

import dao.EntregaDAO;
import model.Entrega;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAOImpl implements EntregaDAO {

    @Override
    public boolean create(Entrega entrega) {

        String sql =
                "INSERT INTO entregas " +
                        "(id_pedido, id_repartidor, fecha, hora) " +
                        "VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, Date.valueOf(entrega.getFecha()));
            ps.setTime(4, Time.valueOf(entrega.getHora()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al registrar entrega: " + e.getMessage()
            );

            return false;
        }
    }

    @Override
    public List<Entrega> readAll() {

        List<Entrega> entregas = new ArrayList<>();

        String sql = "SELECT * FROM entregas";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Entrega entrega = new Entrega(
                        rs.getInt("id"),
                        rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime()
                );

                entregas.add(entrega);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar entregas: " + e.getMessage()
            );
        }

        return entregas;
    }

    @Override
    public boolean update(Entrega entrega) {

        String sql =
                "UPDATE entregas " +
                        "SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? " +
                        "WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, Date.valueOf(entrega.getFecha()));
            ps.setTime(4, Time.valueOf(entrega.getHora()));
            ps.setInt(5, entrega.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar entrega: " + e.getMessage()
            );

            return false;
        }
    }

    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM entregas WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar entrega: " + e.getMessage()
            );

            return false;
        }
    }

    @Override
    public boolean existePedido(int idPedido) {

        String sql =
                "SELECT COUNT(*) FROM entregas WHERE id_pedido = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al verificar pedido: " + e.getMessage()
            );
        }

        return false;
    }
}