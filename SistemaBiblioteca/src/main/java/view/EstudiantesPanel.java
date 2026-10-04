package view;

import controller.BibliotecaController;
import model.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EstudiantesPanel extends JPanel {
    private final BibliotecaController controller;
    private final JTable table = new JTable();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID","Nombre","RUT","Curso","Correo"},0) {
        public boolean isCellEditable(int r,int c){return false;}
    };

    public EstudiantesPanel(BibliotecaController controller) {
        this.controller=controller;
        setLayout(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JLabel t=new JLabel("Gestión de estudiantes"); t.setFont(UI.TITLE); add(t,BorderLayout.NORTH);
        table.setModel(model); table.setAutoCreateRowSorter(true); add(new JScrollPane(table),BorderLayout.CENTER);

        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton nuevo=UI.button("Nuevo"), editar=UI.button("Editar"), eliminar=UI.button("Eliminar"), actualizar=UI.button("Actualizar");
        buttons.add(nuevo);buttons.add(editar);buttons.add(eliminar);buttons.add(actualizar);
        add(buttons,BorderLayout.SOUTH);
        nuevo.addActionListener(e->formulario(null)); editar.addActionListener(e->editar()); eliminar.addActionListener(e->eliminar()); actualizar.addActionListener(e->cargar());
        cargar();
    }

    private void cargar(){
        try{
            model.setRowCount(0);
            for(Estudiante e:controller.estudiantes())
                model.addRow(new Object[]{e.getId(),e.getNombre(),e.getRut(),e.getCurso(),e.getCorreo()});
        }catch(Exception e){UI.error(this,e.getMessage());}
    }

    private Estudiante seleccionado() throws Exception{
        int r=table.getSelectedRow(); if(r<0)return null;
        int id=Integer.parseInt(table.getValueAt(table.convertRowIndexToModel(r),0).toString());
        for(Estudiante e:controller.estudiantes())if(e.getId()==id)return e;
        return null;
    }

    private void formulario(Estudiante existente){
        JTextField nombre=UI.field(18),rut=UI.field(12),curso=UI.field(12),correo=UI.field(18);
        if(existente!=null){nombre.setText(existente.getNombre());rut.setText(existente.getRut());curso.setText(existente.getCurso());correo.setText(existente.getCorreo());}
        JPanel p=new JPanel(new GridLayout(0,2,6,6));
        p.add(new JLabel("Nombre:"));p.add(nombre);p.add(new JLabel("RUT:"));p.add(rut);
        p.add(new JLabel("Curso:"));p.add(curso);p.add(new JLabel("Correo:"));p.add(correo);
        int ok=JOptionPane.showConfirmDialog(this,p,existente==null?"Nuevo estudiante":"Editar estudiante",JOptionPane.OK_CANCEL_OPTION);
        if(ok!=JOptionPane.OK_OPTION)return;
        try{
            Estudiante e=existente==null?new Estudiante():existente;
            e.setNombre(nombre.getText().trim());e.setRut(rut.getText().trim());e.setCurso(curso.getText().trim());e.setCorreo(correo.getText().trim());
            if(existente==null)controller.crearEstudiante(e);else controller.editarEstudiante(e);
            cargar();
        }catch(Exception ex){UI.error(this,ex.getMessage());}
    }

    private void editar(){try{Estudiante e=seleccionado();if(e==null){UI.info(this,"Seleccione un estudiante.");return;}formulario(e);}catch(Exception e){UI.error(this,e.getMessage());}}
    private void eliminar(){
        try{Estudiante e=seleccionado();if(e==null){UI.info(this,"Seleccione un estudiante.");return;}
            if(JOptionPane.showConfirmDialog(this,"¿Eliminar al estudiante?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                controller.borrarEstudiante(e.getId());cargar();
            }
        }catch(Exception ex){UI.error(this,ex.getMessage());}
    }
}