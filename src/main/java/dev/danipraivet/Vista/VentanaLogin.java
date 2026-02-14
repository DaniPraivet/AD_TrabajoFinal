package dev.danipraivet.Vista;

import dev.danipraivet.Modelo.ConexionDAOBrawlhalla;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana de inicio de sesión del sistema Brawlhalla
 */
public class VentanaLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;

    public VentanaLogin() {
        setTitle("Login - Brawlhalla Gestor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Brawlhalla Gestor");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panelPrincipal.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        txtUsuario = new JTextField(15);
        panelPrincipal.add(txtUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelPrincipal.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        txtContrasena = new JPasswordField(15);
        panelPrincipal.add(txtContrasena, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setPreferredSize(new Dimension(120, 30));
        panelPrincipal.add(btnLogin, gbc);

        add(panelPrincipal, BorderLayout.CENTER);

        ActionListener loginAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario   = txtUsuario.getText();
                String contrasena = new String(txtContrasena.getPassword());

                if (ConexionDAOBrawlhalla.validarUsuario(usuario, contrasena)) {
                    JOptionPane.showMessageDialog(VentanaLogin.this,
                            "¡Bienvenido!", "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new VentanaPrincipal().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(VentanaLogin.this,
                            "Usuario o contraseña incorrectos", "Error de Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        btnLogin.addActionListener(loginAction);
        txtContrasena.addActionListener(loginAction);
    }
}
