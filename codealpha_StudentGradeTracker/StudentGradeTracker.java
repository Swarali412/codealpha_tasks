import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
class Student{
    String name,grade,result;
    double percentage;
    Student(String n,double o,double t){
        name=n;
        percentage=(o/t)*100;
        if(percentage>=90)
            grade="A+";
        else if(percentage>=75)
            grade="A";
        else if(percentage>=60)
            grade="B";
        else if(percentage>=40)
            grade="C";
        else
            grade="Fail";
        result=percentage>=40?"Pass":"Fail";
    }
}
public class StudentGradeTracker extends JFrame{
    JTextField name=new JTextField(10);
    JTextField obtained=new JTextField(5);
    JTextField total=new JTextField(5);
    JTextArea summary=new JTextArea(7,65);
    String[] cols={
            "Name","Percentage",
            "Grade","Result"
    };
    DefaultTableModel model=
            new DefaultTableModel(cols,0);
    JTable table=new JTable(model);
    ArrayList<Student> students=
            new ArrayList<>();
    Color blue=new Color(135,206,235);
    StudentGradeTracker(){
        setTitle("Student Grade Tracker");
        setSize(900,650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel main=new JPanel();
        main.setLayout(new BoxLayout(
                main,BoxLayout.Y_AXIS));
        main.setBackground(Color.BLACK);
        JLabel h=new JLabel(
                "Student Grade Tracker");
        h.setFont(new Font(
                "Arial",Font.BOLD,28));
        h.setForeground(blue);
        h.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(Box.createVerticalStrut(15));
        main.add(h);
        JPanel p=new JPanel();
        p.setBackground(Color.BLACK);
        JLabel[] l={
                new JLabel("Name"),
                new JLabel("Obtained"),
                new JLabel("Total")
        };
        for(JLabel x:l)
            x.setForeground(Color.WHITE);
        p.add(l[0]); p.add(name);
        p.add(l[1]); p.add(obtained);
        p.add(l[2]); p.add(total);
        main.add(p);
        JPanel bp=new JPanel();
        bp.setBackground(Color.BLACK);
        String[] btns={
                "Add","Update",
                "Delete","Summary"
        };
        JButton[] b=new JButton[4];
        for(int i=0;i<4;i++){
            b[i]=new JButton(btns[i]);
            b[i].setBackground(blue);
            b[i].setForeground(Color.BLACK);
            b[i].setPreferredSize(
                    new Dimension(110,40));
            bp.add(b[i]);
        }
        main.add(bp);
        table.setBackground(Color.BLACK);
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.WHITE);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(
                Color.BLACK);
        table.getTableHeader().setForeground(
                Color.WHITE);
        JScrollPane pane=
                new JScrollPane(table);
        pane.setPreferredSize(
                new Dimension(850,300));
        JPanel tp=new JPanel();
        tp.setBackground(Color.BLACK);
        tp.add(pane);
        main.add(tp);
        summary.setBackground(Color.BLACK);
        summary.setForeground(Color.WHITE);
        summary.setFont(new Font(
                "Arial",Font.BOLD,15));
        JPanel sp=new JPanel();
        sp.setBackground(Color.BLACK);
        sp.add(new JScrollPane(summary));
        main.add(sp);
        b[0].addActionListener(e->addStudent());
        b[1].addActionListener(e->updateStudent());
        b[2].addActionListener(e->deleteStudent());
        b[3].addActionListener(e->showSummary());
        add(main);
        setVisible(true);
    }
    void addStudent(){
        try{
            Student s=new Student(
                    name.getText(),
                    Double.parseDouble(
                            obtained.getText()),
                    Double.parseDouble(
                            total.getText())
            );
            students.add(s);
            model.addRow(new Object[]{
                    s.name,
                    String.format("%.2f%%", s.percentage)
                    ,s.grade, s.result});
            clear();
        }catch(Exception e){
            JOptionPane.showMessageDialog(
                    this,"Invalid Input");
        }
    }
    void updateStudent(){
        int r=table.getSelectedRow();
        if(r>=0){
            Student s=new Student(
                    name.getText(),
                    Double.parseDouble(
                            obtained.getText()),
                    Double.parseDouble(
                            total.getText())
            );
            students.set(r,s);
            model.setValueAt(s.name,r,0);
            model.setValueAt(
                    String.format("%.2f%%",
                            s.percentage),r,1);
            model.setValueAt(s.grade,r,2);
            model.setValueAt(s.result,r,3);
            clear();
        }
    }
    void deleteStudent(){
        int r=table.getSelectedRow();
        if(r>=0){
            students.remove(r);
            model.removeRow(r);
        }
    }
    void showSummary(){
        if(students.size()==0)
            return;
        double total=0;
        Student high=students.get(0);
        Student low=students.get(0);
        for(Student s:students){
            total+=s.percentage;
            if(s.percentage>high.percentage)
                high=s;
            if(s.percentage<low.percentage)
                low=s;
        }
        summary.setText(
                "Average : "
                +String.format("%.2f",
                total/students.size())
                +"%\n\nHighest : "
                +high.name+" ("
                +String.format("%.2f",
                high.percentage)
                +"%)\n\nLowest : "
                +low.name+" ("
                +String.format("%.2f",
                low.percentage)
                +"%)"
        );
    }
    void clear(){
        name.setText("");
        obtained.setText("");
        total.setText("");
    }
    public static void main(String[] args){
        new StudentGradeTracker();
    }
}