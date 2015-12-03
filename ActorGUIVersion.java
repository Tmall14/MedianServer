package Com.Company;


/**
 * Created by TM on 27-09-2015.
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActorGUIVersion extends JFrame implements ActionListener {
    int kernel;

    private JMenuItem k3 = new JMenuItem("3x3");
    private JMenuItem k5 = new JMenuItem("5x5");
    private JMenuItem k7 = new JMenuItem("7x7");
    private JMenuItem k9 = new JMenuItem("9x9");

    JTextArea messageField = new JTextArea("please select kernel and then upload image");
    //JTextArea kernelselection = new JTextArea(String.valueOf(kernel));
    JPanel container = new JPanel();
    JMenuBar menubar = new JMenuBar();
    JMenu menu1 = new JMenu ("select kernel");
    JButton okButton = new JButton("Upload Image");

    public ActorGUIVersion() {
        add(container);
        messageField.setSize(100, 100);
        //messageField.setEnabled(false);
        messageField.setEditable(false);
        //kernelselection.setSize(100, 100);
        //kernelselection.setEnabled(false);
        //kernelselection.setEditable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container.add(messageField);



        menubar.add(menu1);
        //menu1.setLayout(null);
        //menu1.setSize(300,30);
        menu1.add(k3);
        menu1.add(k5);
        menu1.add(k7);
        menu1.add(k9);
        //container.add(kernelselection,BOTTOM_ALIGNMENT);

        container.add(menubar, LEFT_ALIGNMENT);
        container.add(okButton, RIGHT_ALIGNMENT);


        k3.addActionListener(this);
        k5.addActionListener(this);
        k7.addActionListener(this);
        k9.addActionListener(this);
        okButton.addActionListener(this);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public static void main() {
        ActorGUIVersion AGUIV = new ActorGUIVersion();
        AGUIV.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == k3){
            kernel = 3;
        } else if(e.getSource() == k5){
            kernel = 5;
        } else if(e.getSource() == k7){
            kernel = 7;
        } else if(e.getSource() == k9){
            kernel = 9;
        }
        menu1.setText("         " + kernel + "x" + kernel + "         ");
        //kernelselection.setText(kernel + "");
        if(e.getSource() == okButton) {
            System.out.println("Select the image you want");
            EchoClient c = new EchoClient(kernel);
        }
    }
}