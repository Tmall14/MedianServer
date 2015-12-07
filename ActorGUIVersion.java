package Com.Company;


/**
 * Created by med3-6 on 27-09-2015.
 */
// the different packages needed for the gui to work
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActorGUIVersion extends JFrame implements ActionListener {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////The gui where you select the kernel and the upload image /////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    int kernel;

    // defining the different kernels.
    private JMenuItem k3 = new JMenuItem("3x3");
    private JMenuItem k5 = new JMenuItem("5x5");
    private JMenuItem k7 = new JMenuItem("7x7");
    private JMenuItem k9 = new JMenuItem("9x9");

    // making the different sections of the GUI.
    JTextArea messageField = new JTextArea("please select kernel and then upload image");
    JPanel container = new JPanel();
    JMenuBar menubar = new JMenuBar();
    JMenu menu1 = new JMenu ("select kernel");
    JButton okButton = new JButton("Upload Image");

    public ActorGUIVersion() {
        // adding containers that represents the GUI.
        add(container);
        messageField.setSize(100, 100);
        messageField.setEditable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container.add(messageField);
        //options for the drop down menu.
        menubar.add(menu1);
        menu1.add(k3);
        menu1.add(k5);
        menu1.add(k7);
        menu1.add(k9);
        // container that forms the buttons
        container.add(menubar, LEFT_ALIGNMENT);
        container.add(okButton, RIGHT_ALIGNMENT);
        // listens for the buttons being pressed
        k3.addActionListener(this);
        k5.addActionListener(this);
        k7.addActionListener(this);
        k9.addActionListener(this);
        okButton.addActionListener(this);
        // makes the size and visibility of the GUI.
        setSize(300, 200);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public static void main() {
        // creates the GUI.
        ActorGUIVersion AGUIV = new ActorGUIVersion();
        AGUIV.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    // what the different button does when they are activated
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == k3){
            kernel = 3; // makes kernel 3x3
        } else if(e.getSource() == k5){
            kernel = 5;// makes kernel 5x5
        } else if(e.getSource() == k7){
            kernel = 7; // makes kernel 7x7
        } else if(e.getSource() == k9){
            kernel = 9;// makes kernel 9x9
        }
        // changes the button so it remains the same size.
        menu1.setText("         " + kernel + "x" + kernel + "         ");
        if(e.getSource() == okButton) {
            System.out.println("Select the image you want");
            // initiates the rest of the client.
            EchoClient c = new EchoClient(kernel);
        }
    }
}