package Ventanas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author José Ángel Trevilla
 */
public class Retirar extends javax.swing.JFrame {

    Thread Hilo_mostrar;
    Thread Hilo_retiro;
    Thread Hilo_clean;

    /**
     * Creates new form Retirar
     */
    public Retirar() {
        initComponents();
        setLocationRelativeTo(null);
    }//END MÉTODO CONSTRUCTOR()

    public void mostrar() {

        Hilo_mostrar = new Thread(new Runnable() {
            @Override
            public void run() {

                File Archivo = new File("src\\registros\\empleados.txt");
                FileReader leer;
                BufferedReader almacenamiento;
                String cadena = "", nemm = "";
                String numeroE = TXTNumE.getText();
                Pantalla.setText("");

                if (Archivo.exists()) {

                    try {

                        leer = new FileReader(Archivo);
                        almacenamiento = new BufferedReader(leer);

                        while (cadena != null) {
                            cadena = almacenamiento.readLine();
                            nemm = cadena;
                            //Verificamos que exita la cdena de texto
                            if (nemm.startsWith(numeroE)) {
                                if (cadena != null) {

                                    //Extraemos la cedena de texto
                                    String ns = cadena;
                                    ns = ns.substring(0, 2);
                                    String ss = cadena;
                                    ss = ss.substring(3);
                                    //Damos formato a los decimales
                                    DecimalFormat df = new DecimalFormat("#.00");
                                    //hacemos parseo a tipo de dato double
                                    Double sd = Double.parseDouble(ss);
                                    Pantalla.setText("$" + df.format(sd));
                                    break;
                                }

                            } else {

                                PantallaReg.setText("El empleado no existe");

                            }//END IF
                        }//END WHILE
                        PantallaReg.setText("");

                        almacenamiento.close();
                        leer.close();

                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "No existe el registro " + e);
                    }//END CAPTCHA

                } else {

                }//END ELSE

            }//END METODO RUN()
        });

        Hilo_mostrar.start();
    }//END MÉTODO BUSCAR()

    public void RetirarSaldo() {
        Hilo_retiro = new Thread(new Runnable() {
            @Override
            public void run() {

                String num = TXTNumE.getText();

                String saldos = TXTRetiro.getText();
                File ArchivoAntiguo = new File("src\\registros\\empleados.txt");
                File archivoNuevo = new File("src\\registros\\provisional.txt");

                try {
                    //verificamos si existe el archivo
                    if (ArchivoAntiguo.exists()) {

                        BufferedReader Flee = new BufferedReader(new FileReader(ArchivoAntiguo));
                        String Slinea;

                        while ((Slinea = Flee.readLine()) != null) {

                            //Verificamos si existe la linea en el archivo
                            if (Slinea.startsWith(num)) {
                                //esxtraemos los datos
                                String nE = Slinea;
                                nE = nE.substring(0, 2);

                                String sI = Slinea;
                                sI = sI.substring(3);
                                //Realizamos un parseo a tipo de dato double
                                double cuenta = Double.parseDouble(sI);

                                double saldoR = Double.parseDouble(saldos);
                                //Verificamos si el saldo es mayor a la cantidad a depositar
                                if (cuenta >= saldoR) {

                                    double TotalC = cuenta - saldoR;
                                    //damos formato a los decimales
                                    DecimalFormat df = new DecimalFormat("#.00");

                                    
                                    Pantalla.setText("$" + df.format(TotalC));

                                    //Escribimos la nueva linea del archivo
                                    String nuevaLinea = Slinea;
                                    String nl = nE + " " + TotalC;
                                  
                                    //remplazamos la linea del archivo
                                    String NewLinea = nuevaLinea.replace(Slinea, nl);
                                    //Enviamos los datos al método escribir fichero
                                    EscribirFichero(archivoNuevo, NewLinea);

                                } else {
                                    //EscribirFichero(archivoNuevo, Slinea);
                                    // JOptionPane.showMessageDialog(null, "No existe: " );
                                    PantallaReg.setText("No se puede efectuar el retiro");
                                }//END ELSE

                            } else {
                                //Enviamos las lineas del fichero  que no se mofdifican
                                EscribirFichero(archivoNuevo, Slinea);
                                //PantallaReg.setText("No existe el empleado");
                            }//END ELSE

                        }//END WHILE
                        //cierra conexión
                        Flee.close();
                        
                        //borra fichero antiguo
                        BorrarFichero(ArchivoAntiguo);
                        //crea y enombra fichero nuevo
                        archivoNuevo.renameTo(ArchivoAntiguo);
                        //Elimina fivhero nuevo
                        archivoNuevo.delete();

                    } else {
                        JOptionPane.showMessageDialog(null, "No existe el empleado");
                    }//END ELSE

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error " + e);
                }

            }//END Método run()
        });
        Hilo_retiro.start();

    }//END MÉTODO RETIRARSALDO()

    public void BorrarFichero(File EficheroB) {

        try {

            if (EficheroB.exists()) {
                EficheroB.delete();
                //      JOptionPane.showMessageDialog(null, "Fichero borrado");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }//END CATCH

    }//END método BorraFichero()

    public void EscribirFichero(File Efichero, String CadenaF) {

        try {
            if (!Efichero.exists()) {
                Efichero.createNewFile();
            }//END IF

            BufferedWriter FescribeFichero = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Efichero, true), "utf-8"));

            FescribeFichero.write(CadenaF + "\r\n");

            FescribeFichero.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }//END CATCH

    }//END Método EscribirFichero()

    public void Clean() {

        Hilo_clean = new Thread(new Runnable() {
            @Override
            public void run() {
                TXTNumE.setText(null);
                TXTRetiro.setText(null);
                Pantalla.setText("");
                PantallaReg.setText("");
            }
        });
        Hilo_clean.start();

    }//END METODO Clean()

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Sombra = new javax.swing.JPanel();
        Formulario = new javax.swing.JPanel();
        BarraTop = new javax.swing.JPanel();
        Retirar = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TXTNumE = new javax.swing.JTextField();
        Balance = new javax.swing.JLabel();
        TXTRetiro = new javax.swing.JTextField();
        BTNRetiro = new javax.swing.JButton();
        BTNSalir = new javax.swing.JButton();
        BTNGuardar = new javax.swing.JButton();
        Balancete = new javax.swing.JLabel();
        Pantalla = new javax.swing.JLabel();
        PantallaReg = new javax.swing.JLabel();
        BTNGuardar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Retirar");
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(18, 22, 53));

        Sombra.setBackground(new java.awt.Color(11, 16, 42));

        Formulario.setBackground(new java.awt.Color(27, 33, 65));

        BarraTop.setBackground(new java.awt.Color(79, 217, 137));

        javax.swing.GroupLayout BarraTopLayout = new javax.swing.GroupLayout(BarraTop);
        BarraTop.setLayout(BarraTopLayout);
        BarraTopLayout.setHorizontalGroup(
            BarraTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        BarraTopLayout.setVerticalGroup(
            BarraTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        Retirar.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        Retirar.setForeground(new java.awt.Color(204, 204, 204));
        Retirar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Retirar.setText("Retirar");

        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("Número empleado");

        TXTNumE.setBackground(new java.awt.Color(35, 41, 75));
        TXTNumE.setForeground(new java.awt.Color(153, 153, 153));
        TXTNumE.setBorder(null);
        TXTNumE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TXTNumEActionPerformed(evt);
            }
        });

        Balance.setForeground(new java.awt.Color(204, 204, 204));
        Balance.setText("Cantidad a retirar");

        TXTRetiro.setBackground(new java.awt.Color(35, 41, 75));
        TXTRetiro.setForeground(new java.awt.Color(153, 153, 153));
        TXTRetiro.setBorder(null);

        BTNRetiro.setBackground(new java.awt.Color(234, 66, 137));
        BTNRetiro.setForeground(new java.awt.Color(255, 255, 255));
        BTNRetiro.setText("Retirar");
        BTNRetiro.setBorder(null);
        BTNRetiro.setBorderPainted(false);
        BTNRetiro.setFocusPainted(false);
        BTNRetiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNRetiroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FormularioLayout = new javax.swing.GroupLayout(Formulario);
        Formulario.setLayout(FormularioLayout);
        FormularioLayout.setHorizontalGroup(
            FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Retirar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(BarraTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(FormularioLayout.createSequentialGroup()
                .addGroup(FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FormularioLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Balance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TXTNumE, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                            .addComponent(TXTRetiro)))
                    .addGroup(FormularioLayout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(BTNRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        FormularioLayout.setVerticalGroup(
            FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FormularioLayout.createSequentialGroup()
                .addComponent(BarraTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Retirar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TXTNumE, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TXTRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Balance))
                .addGap(26, 26, 26)
                .addComponent(BTNRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout SombraLayout = new javax.swing.GroupLayout(Sombra);
        Sombra.setLayout(SombraLayout);
        SombraLayout.setHorizontalGroup(
            SombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Formulario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        SombraLayout.setVerticalGroup(
            SombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SombraLayout.createSequentialGroup()
                .addComponent(Formulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        BTNSalir.setBackground(new java.awt.Color(27, 33, 65));
        BTNSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir.png"))); // NOI18N
        BTNSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNSalirActionPerformed(evt);
            }
        });

        BTNGuardar.setBackground(new java.awt.Color(38, 169, 94));
        BTNGuardar.setForeground(new java.awt.Color(11, 16, 42));
        BTNGuardar.setText("Guardar");
        BTNGuardar.setBorder(null);
        BTNGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNGuardarActionPerformed(evt);
            }
        });

        Balancete.setForeground(new java.awt.Color(204, 204, 204));
        Balancete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Balancete.setText("Saldo Actual:");

        Pantalla.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        Pantalla.setForeground(new java.awt.Color(204, 204, 204));
        Pantalla.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        PantallaReg.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        PantallaReg.setForeground(new java.awt.Color(204, 204, 204));
        PantallaReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        BTNGuardar1.setBackground(new java.awt.Color(212, 15, 62));
        BTNGuardar1.setForeground(new java.awt.Color(11, 16, 42));
        BTNGuardar1.setText("Borrar");
        BTNGuardar1.setBorder(null);
        BTNGuardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNGuardar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(Balancete, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Pantalla, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95))
            .addComponent(PantallaReg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(BTNGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BTNGuardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BTNSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Sombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(PantallaReg, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Pantalla, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Balancete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Sombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(BTNGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(BTNGuardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(BTNSalir, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TXTNumEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTNumEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXTNumEActionPerformed

    private void BTNRetiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNRetiroActionPerformed
        // TODO add your handling code here:

        mostrar();
    }//GEN-LAST:event_BTNRetiroActionPerformed

    private void BTNSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNSalirActionPerformed
        dispose();
    }//GEN-LAST:event_BTNSalirActionPerformed

    private void BTNGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNGuardarActionPerformed
        // LLamada al método depositar
        //  depositar();
        RetirarSaldo();
    }//GEN-LAST:event_BTNGuardarActionPerformed

    private void BTNGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNGuardar1ActionPerformed
        // TODO add your handling code here:
        Clean();
    }//GEN-LAST:event_BTNGuardar1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Retirar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Retirar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Retirar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Retirar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Retirar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTNGuardar;
    private javax.swing.JButton BTNGuardar1;
    private javax.swing.JButton BTNRetiro;
    private javax.swing.JButton BTNSalir;
    private javax.swing.JLabel Balance;
    private javax.swing.JLabel Balancete;
    private javax.swing.JPanel BarraTop;
    private javax.swing.JPanel Formulario;
    private javax.swing.JLabel Pantalla;
    private javax.swing.JLabel PantallaReg;
    private javax.swing.JLabel Retirar;
    private javax.swing.JPanel Sombra;
    private javax.swing.JTextField TXTNumE;
    private javax.swing.JTextField TXTRetiro;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
