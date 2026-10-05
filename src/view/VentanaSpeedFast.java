package view;

import controller.SpeedFastController;
import model.Entrega;
import model.Pedido;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class VentanaSpeedFast extends JFrame {

    private final SpeedFastController controller;

    // repartidores
    private JTextField txtNombreRepartidor;
    private JTable tablaRepartidores;
    private DefaultTableModel modeloRepartidores;

    // pedidos
    private JTextField txtDireccion;
    private JComboBox<String> cbTipo;
    private JComboBox<String> cbEstado;
    private JTable tablaPedidos;
    private DefaultTableModel modeloPedidos;

    // entregas
    private JComboBox<Pedido> cbPedido;
    private JComboBox<Repartidor> cbRepartidor;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTable tablaEntregas;
    private DefaultTableModel modeloEntregas;

    public VentanaSpeedFast() {

        controller = new SpeedFastController();

        setTitle("SpeedFast");
        setSize(850, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

        panelPrincipal.add(crearPanelRepartidores());
        panelPrincipal.add(crearPanelPedidos());
        panelPrincipal.add(crearPanelEntregas());

        JScrollPane scroll = new JScrollPane(panelPrincipal);
        setContentPane(scroll);

        cargarRepartidores();
        cargarPedidos();
        cargarEntregas();
        cargarCombos();
    }

    // repartidores

    private JPanel crearPanelRepartidores() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Repartidores"));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtNombreRepartidor = new JTextField(20);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombreRepartidor);
        formulario.add(btnRegistrar);
        formulario.add(btnEditar);
        formulario.add(btnEliminar);

        modeloRepartidores = new DefaultTableModel(
                new Object[]{"ID", "Nombre"}, 0
        );

        tablaRepartidores = new JTable(modeloRepartidores);

        tablaRepartidores.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaRepartidores.getSelectedRow();

            if (fila >= 0) {
                txtNombreRepartidor.setText(
                        modeloRepartidores.getValueAt(fila, 1).toString()
                );
            }
        });

        btnRegistrar.addActionListener(e -> registrarRepartidor());
        btnEditar.addActionListener(e -> editarRepartidor());
        btnEliminar.addActionListener(e -> eliminarRepartidor());

        panel.add(formulario, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaRepartidores), BorderLayout.CENTER);

        panel.setPreferredSize(new Dimension(800, 220));

        return panel;
    }

    private void registrarRepartidor() {

        String nombre = txtNombreRepartidor.getText();

        if (controller.registrarRepartidor(nombre)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor registrado correctamente."
            );

            txtNombreRepartidor.setText("");

            cargarRepartidores();
            cargarCombos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el repartidor."
            );
        }
    }

    private void editarRepartidor() {

        int fila = tablaRepartidores.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un repartidor."
            );
            return;
        }

        int id = (int) modeloRepartidores.getValueAt(fila, 0);
        String nombre = txtNombreRepartidor.getText();

        if (controller.actualizarRepartidor(id, nombre)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor actualizado."
            );

            cargarRepartidores();
            cargarCombos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar."
            );
        }
    }

    private void eliminarRepartidor() {

        int fila = tablaRepartidores.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un repartidor."
            );
            return;
        }

        int id = (int) modeloRepartidores.getValueAt(fila, 0);

        if (controller.eliminarRepartidor(id)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor eliminado."
            );

            txtNombreRepartidor.setText("");

            cargarRepartidores();
            cargarCombos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar."
            );
        }
    }

    private void cargarRepartidores() {

        modeloRepartidores.setRowCount(0);

        for (Repartidor repartidor : controller.listarRepartidores()) {

            modeloRepartidores.addRow(new Object[]{
                    repartidor.getId(),
                    repartidor.getNombre()
            });
        }
    }

    // pedidos

    private JPanel crearPanelPedidos() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Pedidos"));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtDireccion = new JTextField(18);

        cbTipo = new JComboBox<>(new String[]{
                "COMIDA",
                "ENCOMIENDA",
                "EXPRESS"
        });

        cbEstado = new JComboBox<>(new String[]{
                "PENDIENTE",
                "EN_REPARTO",
                "ENTREGADO"
        });

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        formulario.add(new JLabel("Dirección:"));
        formulario.add(txtDireccion);

        formulario.add(new JLabel("Tipo:"));
        formulario.add(cbTipo);

        formulario.add(new JLabel("Estado:"));
        formulario.add(cbEstado);

        formulario.add(btnRegistrar);
        formulario.add(btnEditar);
        formulario.add(btnEliminar);

        modeloPedidos = new DefaultTableModel(
                new Object[]{"ID", "Dirección", "Tipo", "Estado"}, 0
        );

        tablaPedidos = new JTable(modeloPedidos);

        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {

            int fila = tablaPedidos.getSelectedRow();

            if (fila >= 0) {

                txtDireccion.setText(
                        modeloPedidos.getValueAt(fila, 1).toString()
                );

                cbTipo.setSelectedItem(
                        modeloPedidos.getValueAt(fila, 2).toString()
                );

                cbEstado.setSelectedItem(
                        modeloPedidos.getValueAt(fila, 3).toString()
                );
            }
        });

        btnRegistrar.addActionListener(e -> registrarPedido());
        btnEditar.addActionListener(e -> editarPedido());
        btnEliminar.addActionListener(e -> eliminarPedido());

        panel.add(formulario, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        panel.setPreferredSize(new Dimension(800, 240));

        return panel;
    }

    private void registrarPedido() {

        String direccion = txtDireccion.getText();
        String tipo = cbTipo.getSelectedItem().toString();
        String estado = cbEstado.getSelectedItem().toString();

        if (controller.registrarPedido(direccion, tipo, estado)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido registrado correctamente."
            );

            txtDireccion.setText("");

            cargarPedidos();
            cargarCombos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el pedido."
            );
        }
    }

    private void editarPedido() {

        int fila = tablaPedidos.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un pedido."
            );
            return;
        }

        int id = (int) modeloPedidos.getValueAt(fila, 0);

        String direccion = txtDireccion.getText();
        String tipo = cbTipo.getSelectedItem().toString();
        String estado = cbEstado.getSelectedItem().toString();

        if (controller.actualizarPedido(id, direccion, tipo, estado)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido actualizado."
            );

            cargarPedidos();
            cargarCombos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar."
            );
        }
    }

    private void eliminarPedido() {

        int fila = tablaPedidos.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un pedido."
            );
            return;
        }

        int id = (int) modeloPedidos.getValueAt(fila, 0);

        if (controller.eliminarPedido(id)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido eliminado."
            );

            cargarPedidos();
            cargarCombos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar."
            );
        }
    }

    private void cargarPedidos() {

        modeloPedidos.setRowCount(0);

        for (Pedido pedido : controller.listarPedidos()) {

            modeloPedidos.addRow(new Object[]{
                    pedido.getId(),
                    pedido.getDireccion(),
                    pedido.getTipo(),
                    pedido.getEstado()
            });
        }
    }

    // entregas

    private JPanel crearPanelEntregas() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Entregas"));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));

        cbPedido = new JComboBox<>();
        cbRepartidor = new JComboBox<>();

        txtFecha = new JTextField(10);
        txtHora = new JTextField(8);

        txtFecha.setText(LocalDate.now().toString());

        txtHora.setText(
                LocalTime.now()
                        .withSecond(0)
                        .withNano(0)
                        .toString()
        );

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        formulario.add(new JLabel("Pedido:"));
        formulario.add(cbPedido);

        formulario.add(new JLabel("Repartidor:"));
        formulario.add(cbRepartidor);

        formulario.add(new JLabel("Fecha:"));
        formulario.add(txtFecha);

        formulario.add(new JLabel("Hora:"));
        formulario.add(txtHora);

        formulario.add(btnRegistrar);
        formulario.add(btnEditar);
        formulario.add(btnEliminar);

        modeloEntregas = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Pedido",
                        "Repartidor",
                        "Fecha",
                        "Hora"
                },
                0
        );

        tablaEntregas = new JTable(modeloEntregas);

        tablaEntregas.getSelectionModel().addListSelectionListener(e -> {

            int fila = tablaEntregas.getSelectedRow();

            if (fila >= 0) {

                int idPedido = (int) modeloEntregas.getValueAt(fila, 1);
                int idRepartidor = (int) modeloEntregas.getValueAt(fila, 2);

                seleccionarPedido(idPedido);
                seleccionarRepartidor(idRepartidor);

                txtFecha.setText(
                        modeloEntregas.getValueAt(fila, 3).toString()
                );

                txtHora.setText(
                        modeloEntregas.getValueAt(fila, 4).toString()
                );
            }
        });

        btnRegistrar.addActionListener(e -> registrarEntrega());
        btnEditar.addActionListener(e -> editarEntrega());
        btnEliminar.addActionListener(e -> eliminarEntrega());

        panel.add(formulario, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaEntregas), BorderLayout.CENTER);

        panel.setPreferredSize(new Dimension(800, 250));

        return panel;
    }

    private void registrarEntrega() {

        try {

            Pedido pedido = (Pedido) cbPedido.getSelectedItem();
            Repartidor repartidor =
                    (Repartidor) cbRepartidor.getSelectedItem();

            if (pedido == null || repartidor == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debes seleccionar pedido y repartidor."
                );

                return;
            }

            LocalDate fecha =
                    LocalDate.parse(txtFecha.getText().trim());

            LocalTime hora =
                    LocalTime.parse(txtHora.getText().trim());

            if (controller.registrarEntrega(
                    pedido.getId(),
                    repartidor.getId(),
                    fecha,
                    hora
            )) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entrega registrada correctamente."
                );

                cargarEntregas();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar la entrega."
                );
            }

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fecha u hora incorrecta.\n" +
                            "Fecha: AAAA-MM-DD\n" +
                            "Hora: HH:MM"
            );
        }
    }

    private void editarEntrega() {

        int fila = tablaEntregas.getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una entrega."
            );

            return;
        }

        try {

            int id = (int) modeloEntregas.getValueAt(fila, 0);

            Pedido pedido = (Pedido) cbPedido.getSelectedItem();
            Repartidor repartidor =
                    (Repartidor) cbRepartidor.getSelectedItem();

            LocalDate fecha =
                    LocalDate.parse(txtFecha.getText().trim());

            LocalTime hora =
                    LocalTime.parse(txtHora.getText().trim());

            if (pedido == null || repartidor == null) {
                return;
            }

            if (controller.actualizarEntrega(
                    id,
                    pedido.getId(),
                    repartidor.getId(),
                    fecha,
                    hora
            )) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entrega actualizada."
                );

                cargarEntregas();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar."
                );
            }

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fecha u hora incorrecta."
            );
        }
    }

    private void eliminarEntrega() {

        int fila = tablaEntregas.getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una entrega."
            );

            return;
        }

        int id = (int) modeloEntregas.getValueAt(fila, 0);

        if (controller.eliminarEntrega(id)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega eliminada."
            );

            cargarEntregas();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar."
            );
        }
    }

    private void cargarEntregas() {

        modeloEntregas.setRowCount(0);

        for (Entrega entrega : controller.listarEntregas()) {

            modeloEntregas.addRow(new Object[]{
                    entrega.getId(),
                    entrega.getIdPedido(),
                    entrega.getIdRepartidor(),
                    entrega.getFecha(),
                    entrega.getHora()
            });
        }
    }

    // combos

    private void cargarCombos() {

        cbPedido.removeAllItems();
        cbRepartidor.removeAllItems();

        for (Pedido pedido : controller.listarPedidos()) {
            cbPedido.addItem(pedido);
        }

        for (Repartidor repartidor : controller.listarRepartidores()) {
            cbRepartidor.addItem(repartidor);
        }
    }

    private void seleccionarPedido(int id) {

        for (int i = 0; i < cbPedido.getItemCount(); i++) {

            Pedido pedido = cbPedido.getItemAt(i);

            if (pedido.getId() == id) {
                cbPedido.setSelectedIndex(i);
                break;
            }
        }
    }

    private void seleccionarRepartidor(int id) {

        for (int i = 0; i < cbRepartidor.getItemCount(); i++) {

            Repartidor repartidor = cbRepartidor.getItemAt(i);

            if (repartidor.getId() == id) {
                cbRepartidor.setSelectedIndex(i);
                break;
            }
        }
    }
}
