package view;

import controller.BibliotecaController;
import model.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportesPanel extends JPanel {
    private final BibliotecaController controller;
    private final JTable table = new JTable();
    private final DefaultTableModel model = new DefaultTableModel();

    public ReportesPanel(BibliotecaController controller) {
        this.controller=controller;
        setLayout(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JLabel t=new JLabel("Reportes");t.setFont(UI.TITLE);add(t,BorderLayout.NORTH);
        table.setModel(model);table.setAutoCreateRowSorter(true);add(new JScrollPane(table),BorderLayout.CENTER);

        JPanel b=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton mas=UI.button("Libros más prestados"), activos=UI.button("Préstamos activos"), historial=UI.button("Historial estudiante");
        b.add(mas);b.add(activos);b.add(historial);add(b,BorderLayout.SOUTH);
        mas.addActionListener(e->masPrestados());activos.addActionListener(e->activos());historial.addActionListener(e->historial());
    }

    private void masPrestados(){
        try{List<String[]> rows=controller.librosMasPrestados();setData(new String[]{"Libro","Cantidad"},rows);}
        catch(Exception e){UI.error(this,e.getMessage());}
    }

    private void activos(){
        try{List<String[]> rows=controller.prestamosActivos();setData(new String[]{"Estudiante","Libro","Préstamo","Vencimiento"},rows);}
        catch(Exception e){UI.error(this,e.getMessage());}
    }

    private void historial(){
        try{
            JComboBox<Estudiante> cb=new JComboBox<>();
            for(Estudiante e:controller.estudiantes())cb.addItem(e);
            if(cb.getItemCount()==0)return;
            int ok=JOptionPane.showConfirmDialog(this,cb,"Seleccione estudiante",JOptionPane.OK_CANCEL_OPTION);
            if(ok!=JOptionPane.OK_OPTION)return;
            Estudiante e=(Estudiante)cb.getSelectedItem();
            setData(new String[]{"Libro","Préstamo","Vencimiento","Devuelto"},controller.historialReporte(e.getId()));
        }catch(Exception e){UI.error(this,e.getMessage());}
    }

    private void setData(String[] headers,List<String[]> rows){
        model.setColumnIdentifiers(headers);model.setRowCount(0);
        for(String[] row:rows)model.addRow(row);
    }
}