package view;

import controller.BibliotecaController;
import model.Estudiante;
import model.Libro;
import model.Prestamo;
import model.Usuario;
import service.PrestamoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PrestamosPanel extends JPanel {
    private final BibliotecaController controller;
    private final Usuario usuario;
    private final JTable table = new JTable();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID","Estudiante","Libro","Préstamo","Vencimiento","Devuelto"},0) {
        public boolean isCellEditable(int r,int c){return false;}
    };

    public PrestamosPanel(BibliotecaController controller, Usuario usuario) {
        this.controller=controller; this.usuario=usuario;
        setLayout(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JLabel t=new JLabel("Préstamos y devoluciones");t.setFont(UI.TITLE);add(t,BorderLayout.NORTH);
        table.setModel(model);table.setAutoCreateRowSorter(true);add(new JScrollPane(table),BorderLayout.CENTER);

        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton prestar=UI.button("Registrar préstamo"), devolver=UI.button("Registrar devolución"), actualizar=UI.button("Actualizar");
        buttons.add(prestar);buttons.add(devolver);buttons.add(actualizar);add(buttons,BorderLayout.SOUTH);
        prestar.addActionListener(e->nuevoPrestamo());devolver.addActionListener(e->devolver());actualizar.addActionListener(e->cargar());
        cargar();
    }

    private void cargar(){
        try{
            model.setRowCount(0);
            List<Prestamo> lista;
            if("estudiante".equalsIgnoreCase(usuario.getRol())){
                Estudiante propio=null;
                for(Estudiante e:controller.estudiantes())
                    if(e.getCorreo().equalsIgnoreCase(usuario.getCorreo())){propio=e;break;}
                lista=propio==null?List.of():controller.historial(propio.getId());
            }else lista=controller.prestamos();

            for(Prestamo p:lista)
                model.addRow(new Object[]{p.getId(),p.getEstudianteNombre(),p.getLibroTitulo(),
                        p.getFechaPrestamo(),p.getFechaDevolucion(),p.isDevuelto()?"Sí":"No"});
        }catch(Exception e){UI.error(this,e.getMessage());}
    }

    private void nuevoPrestamo(){
        try{
            List<Estudiante> estudiantes=controller.estudiantes();
            List<Libro> libros=controller.libros();
            JComboBox<Estudiante> ce=new JComboBox<>();
            JComboBox<Libro> cl=new JComboBox<>();
            for(Estudiante e:estudiantes)ce.addItem(e);
            for(Libro l:libros)if(l.getStock()>0)cl.addItem(l);

            if(ce.getItemCount()==0||cl.getItemCount()==0){UI.info(this,"No hay estudiantes o libros con stock disponible.");return;}
            if("estudiante".equalsIgnoreCase(usuario.getRol())){
                for(int i=0;i<ce.getItemCount();i++)
                    if(ce.getItemAt(i).getCorreo().equalsIgnoreCase(usuario.getCorreo()))ce.setSelectedIndex(i);
                ce.setEnabled(false);
            }

            JPanel p=new JPanel(new GridLayout(0,2,6,6));
            p.add(new JLabel("Estudiante:"));p.add(ce);p.add(new JLabel("Libro:"));p.add(cl);
            int ok=JOptionPane.showConfirmDialog(this,p,"Registrar préstamo",JOptionPane.OK_CANCEL_OPTION);
            if(ok!=JOptionPane.OK_OPTION)return;
            Estudiante e=(Estudiante)ce.getSelectedItem(); Libro l=(Libro)cl.getSelectedItem();
            controller.prestar(e.getId(),l.getId(),new PrestamoService.OperationCallback(){
                public void onSuccess(String m){SwingUtilities.invokeLater(()->{UI.info(PrestamosPanel.this,m);cargar();});}
                public void onError(String m){SwingUtilities.invokeLater(()->UI.error(PrestamosPanel.this,m));}
            });
            UI.info(this,"Operación enviada a segundo plano. La interfaz seguirá disponible.");
        }catch(Exception ex){UI.error(this,ex.getMessage());}
    }

    private void devolver(){
        int r=table.getSelectedRow();
        if(r<0){UI.info(this,"Seleccione un préstamo.");return;}
        int id=Integer.parseInt(table.getValueAt(table.convertRowIndexToModel(r),0).toString());
        boolean devuelto="Sí".equals(table.getValueAt(table.convertRowIndexToModel(r),5).toString());
        if(devuelto){UI.info(this,"Ese préstamo ya está devuelto.");return;}
        controller.devolver(id,new PrestamoService.OperationCallback(){
            public void onSuccess(String m){SwingUtilities.invokeLater(()->{UI.info(PrestamosPanel.this,m);cargar();});}
            public void onError(String m){SwingUtilities.invokeLater(()->UI.error(PrestamosPanel.this,m));}
        });
        UI.info(this,"Devolución enviada a segundo plano.");
    }
}