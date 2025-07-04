
package Ventanas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileOutputStream;
import java.io.FileReader;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;
import java.text.DecimalFormat;

/**
 *
 * @author José Ángel Trevilla
 */
public class Depositar extends javax.swing.JFrame {
    
    Thread Hilo_deposito;
    Thread Hilo_lectura;
    Thread Hilo_clean;

    /**
     * Creates new form Depositar
     */
    public Depositar() {
        initComponents();
        //   LecturaDatos();
        setLocationRelativeTo(null);
        
    }
    
    public void mostrar() {
        
        Hilo_lectura = new Thread(new Runnable() {
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
                    //Verificamos que existe la cadena de texto
                    if (nemm.startsWith(numeroE)) {
                        if (cadena != null) {
                            
                            //Extraemos la cadena de texto y la separamos
                            String ns = cadena;
                            ns = ns.substring(0, 2);
                            String ss = cadena;
                            ss = ss.substring(3);
                            //Damos formato a los decimales
                            DecimalFormat df = new DecimalFormat("#.00");
                            //Realizamos un pareso a double
                            Double sd = Double.parseDouble(ss);
                            Pantalla.setText("$" + df.format(sd));
                            break;
                        }
                        
                    } else if (!nemm.startsWith(numeroE)) {
                        
                        PantallaReg.setText("No se puede reealizar el deposito");
                        
                    }
                }//END WHILE
                PantallaReg.setText("");
                almacenamiento.close();
                leer.close();
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "No existe el registro 3" + e);
            }//END CAPTCHA
            
        } else {
            
        }//END ELSE
                
            }//END método run
        });
        Hilo_lectura.start();
    }//END MÉTODO Mostrar()
    
    public void DepositarSaldo() {
        Hilo_deposito = new Thread(new Runnable() {
            @Override
            public void run() {
                
                String num = TXTNumE.getText();
                
                String saldos = TXTDeposito.getText();
                
                //Creación de archivos 
                File ArchivoAntiguo = new File("src\\registros\\empleados.txt");
                File archivoNuevo = new File("src\\registros\\provisional.txt");
                
                try {
                    
                    //verifica si esiste el archivo
                    if (ArchivoAntiguo.exists()) {
                        
                        BufferedReader Flee = new BufferedReader(new FileReader(ArchivoAntiguo));
                        String Slinea;
                        
                        while ((Slinea = Flee.readLine()) != null) {
                            
                            //Verifica si la cadena de texto existe
                            if (Slinea.startsWith(num)) {
                                
                                //Extraemos la cadena de texto
                                String nE = Slinea;
                                nE = nE.substring(0, 2);
                                
                                String sI = Slinea;
                                sI = sI.substring(3);
                                
                                //REalizamos Parseo Para convertir a tipo de dato Double
                                double cuenta = Double.parseDouble(sI);
                                
                                double saldoI = Double.parseDouble(saldos);
                                
                                double TotalC = cuenta + saldoI;
                                //Agregamos Formato a los decimales
                                DecimalFormat df = new DecimalFormat("#.00");

                                Pantalla.setText("$" + df.format(TotalC));
   
                                //Escribimos la nueva linea
                                String nuevaLinea = Slinea;
                                String nl = nE + " " + TotalC;
                                //Sustitimosla linea antigua por la linea nueva
                                String NewLinea = nuevaLinea.replace(Slinea, nl);
                                //Enviamos los datos al método escribir fichero
                                EscribirFichero(archivoNuevo, NewLinea);
                                
                            } else {
                                //Enviamos la linea antigua al método escribir fichero
                                EscribirFichero(archivoNuevo, Slinea);
   
                                
                            }//END ELSE
                            
                        }//END WHILE
                        //Cerramos la conexión
                        Flee.close();
                        
                        //borramos fichero antiguo
                        BorrarFichero(ArchivoAntiguo);
                        //creamos un nuevo archivo y lo renombramos con el mismo nombre del archivo anterior
                        archivoNuevo.renameTo(ArchivoAntiguo);
                        //Borramos el archivo nuevo
                        archivoNuevo.delete();
                    } else {
                        JOptionPane.showMessageDialog(null, "No existe el fichero");
                    }//END ELSE
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                
            }//END Método run()
        });
        Hilo_deposito.start();
        
    }//END Método Depositar saldo()
    
    public void BorrarFichero(File Efichero) {
        
        try {
            
            if (Efichero.exists()) {
                Efichero.delete();
                //      JOptionPane.showMessageDialog(null, "Fichero borrado");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            
        }
        
    }//END método BorraFichero()
    
    public void EscribirFichero(File Efichero, String CadenaLibro) {
        
        try {
            
            if (!Efichero.exists()) {
                Efichero.createNewFile();
            }
            
            BufferedWriter Fescribe = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Efichero, true), "utf-8"));
            
            Fescribe.write(CadenaLibro + "\r\n");
            
            Fescribe.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
    }//END Método EscribirFicheroUsuarios()
    
    public void Clean() {
        Hilo_clean = new Thread(new Runnable() {
            @Override
            public void run() {
              TXTNumE.setText(null);
        TXTDeposito.setText(null);
        Pantalla.setText("");
        PantallaReg.setText("");
            }//END METODO run()
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

        Fondo = new javax.swing.JPanel();
        Sombra = new javax.swing.JPanel();
        Formulario = new javax.swing.JPanel();
        BarraTop = new javax.swing.JPanel();
        Depositar = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TXTNumE = new javax.swing.JTextField();
        Balance = new javax.swing.JLabel();
        TXTDeposito = new javax.swing.JTextField();
        BTNDepositar = new javax.swing.JButton();
        Exit = new javax.swing.JButton();
        BTNGuardar = new javax.swing.JButton();
        PantallaReg = new javax.swing.JLabel();
        Pantalla = new javax.swing.JLabel();
        Balance1 = new javax.swing.JLabel();
        BTNGuardar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        Fondo.setBackground(new java.awt.Color(18, 22, 53));

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

        Depositar.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        Depositar.setForeground(new java.awt.Color(204, 204, 204));
        Depositar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Depositar.setText("Depositar");

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
        Balance.setText("Cantidad a depositar");

        TXTDeposito.setBackground(new java.awt.Color(35, 41, 75));
        TXTDeposito.setForeground(new java.awt.Color(153, 153, 153));
        TXTDeposito.setBorder(null);

        BTNDepositar.setBackground(new java.awt.Color(234, 66, 137));
        BTNDepositar.setForeground(new java.awt.Color(255, 255, 255));
        BTNDepositar.setText("Depositar");
        BTNDepositar.setBorder(null);
        BTNDepositar.setBorderPainted(false);
        BTNDepositar.setFocusPainted(false);
        BTNDepositar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNDepositarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FormularioLayout = new javax.swing.GroupLayout(Formulario);
        Formulario.setLayout(FormularioLayout);
        FormularioLayout.setHorizontalGroup(
            FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Depositar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(BarraTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(FormularioLayout.createSequentialGroup()
                .addGroup(FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FormularioLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Balance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TXTNumE, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TXTDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(FormularioLayout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(BTNDepositar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        FormularioLayout.setVerticalGroup(
            FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FormularioLayout.createSequentialGroup()
                .addComponent(BarraTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Depositar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TXTNumE, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(FormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TXTDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Balance))
                .addGap(26, 26, 26)
                .addComponent(BTNDepositar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        Exit.setBackground(new java.awt.Color(27, 33, 65));
        Exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir.png"))); // NOI18N
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
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

        PantallaReg.setBackground(new java.awt.Color(255, 255, 255));
        PantallaReg.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        PantallaReg.setForeground(new java.awt.Color(204, 204, 204));
        PantallaReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Pantalla.setBackground(new java.awt.Color(35, 41, 75));
        Pantalla.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        Pantalla.setForeground(new java.awt.Color(204, 204, 204));

        Balance1.setForeground(new java.awt.Color(204, 204, 204));
        Balance1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Balance1.setText("Saldo Actual:");

        BTNGuardar1.setBackground(new java.awt.Color(212, 15, 62));
        BTNGuardar1.setForeground(new java.awt.Color(11, 16, 42));
        BTNGuardar1.setText("Borrar");
        BTNGuardar1.setBorder(null);
        BTNGuardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNGuardar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FondoLayout = new javax.swing.GroupLayout(Fondo);
        Fondo.setLayout(FondoLayout);
        FondoLayout.setHorizontalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondoLayout.createSequentialGroup()
                        .addGap(0, 43, Short.MAX_VALUE)
                        .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(FondoLayout.createSequentialGroup()
                                .addComponent(Balance1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Pantalla, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Sombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46))
                    .addGroup(FondoLayout.createSequentialGroup()
                        .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PantallaReg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(FondoLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(BTNGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(BTNGuardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Exit, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        FondoLayout.setVerticalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(PantallaReg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Pantalla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Balance1, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(Sombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 38, Short.MAX_VALUE)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Exit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BTNGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BTNGuardar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fondo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTNGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNGuardarActionPerformed
        // LLamada al método depositar
        //  depositar();
        DepositarSaldo();
    }//GEN-LAST:event_BTNGuardarActionPerformed

    private void BTNDepositarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNDepositarActionPerformed
        // TODO add your handling code here:

        mostrar();

    }//GEN-LAST:event_BTNDepositarActionPerformed

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        dispose();
    }//GEN-LAST:event_ExitActionPerformed

    private void TXTNumEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TXTNumEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TXTNumEActionPerformed

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
            java.util.logging.Logger.getLogger(Depositar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Depositar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Depositar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Depositar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Depositar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTNDepositar;
    private javax.swing.JButton BTNGuardar;
    private javax.swing.JButton BTNGuardar1;
    private javax.swing.JLabel Balance;
    private javax.swing.JLabel Balance1;
    private javax.swing.JPanel BarraTop;
    private javax.swing.JLabel Depositar;
    private javax.swing.JButton Exit;
    private javax.swing.JPanel Fondo;
    private javax.swing.JPanel Formulario;
    private javax.swing.JLabel Pantalla;
    private javax.swing.JLabel PantallaReg;
    private javax.swing.JPanel Sombra;
    private javax.swing.JTextField TXTDeposito;
    private javax.swing.JTextField TXTNumE;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
