import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.checkerframework.checker.guieffect.qual.UI;

// Frontend of application
// INHERITANCE: extends from JFrame to create a GUI window
// Written by Jiyan and edited by William
public class Interface extends JFrame {
  // Public components to be accessed and modified in Handler
  public JPanel boxInst;
  public JPanel boxDone;

  public JButton btnRun;
  public JLabel gif;

  public static String school = "CAMS"; 

  // Initialize interface
  public Interface() {
    super();
    setResizable(true);
    setFocusable(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(100, 100);
  }

  // Create, add, and format all components to screen
  public void createInterface() {
    // Change label, button, and dropdown menu font
    UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 14));
    UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 14));
    UIManager.put("ComboBox.font", new Font("Arial", Font.PLAIN, 14));

    // Create panels and components
    JPanel boxHome = new JPanel();
    boxHome.setLayout(new BoxLayout(boxHome, BoxLayout.Y_AXIS));
    boxHome.setBorder(new EmptyBorder(10, 10, 10, 10));
  
    JPanel boxTop = new JPanel();
    boxTop.setLayout(new BoxLayout(boxTop, BoxLayout.X_AXIS));
    boxTop.setAlignmentX(Component.LEFT_ALIGNMENT);
    boxTop.setBorder(new EmptyBorder(0, 0, 10, 0));

    boxInst = new JPanel();
    boxInst.setLayout(new BoxLayout(boxInst, BoxLayout.Y_AXIS));
    boxInst.setBorder(new EmptyBorder(0, 0, 0, 10));
    
    JLabel lblStep1 = new JLabel("1) Download your Google Contacts .csv file (see GIF)!");
    JLabel lblStep2 = new JLabel("2) Click 'Run!' and select the .csv file from Contacts" );
    JLabel lblStep3 = new JLabel("3) New contacts will be automatically downloaded as a .csv");

    boxDone = new JPanel();
    boxDone.setLayout(new BoxLayout(boxDone, BoxLayout.Y_AXIS));

    JLabel lblDone1 = new JLabel("Contacts successfully imported! Check the project folder for your contacts");
    JLabel lblDone2 = new JLabel(".csv file. The GIF below has been updated with further instructions.");
    
    String[] schools = {"CAMS", "Browning", "EPHS", "McBride", "Jordan", "Cabrillo", "Lakewood", "Poly", "LBSA", "Millikan", "Renaissance", "Sato", "Reid", "Wilson"};
    school = schools[0];
    JComboBox<String> ddMenu = new JComboBox<>(schools); // Dropdown menu

    btnRun = new JButton("Run!");

    gif = new JLabel(new ImageIcon("src/main/assets/download.gif"));
    gif.setSize(50,50);

    // Add components to panels
    boxInst.add(lblStep1);
    boxInst.add(lblStep2);
    boxInst.add(lblStep3);

    boxDone.add(lblDone1);
    boxDone.add(lblDone2);

    boxTop.add(boxInst);
    boxTop.add(boxDone);
    boxDone.setVisible(false);
    boxTop.add(btnRun);
    boxTop.add(ddMenu);

    boxHome.add(boxTop);
    boxHome.add(gif);
    
    // Add button and menu functionality
    btnRun.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        System.out.println("Run button pressed");
        Handler.handleState(Handler.State.FETCH);
      }
    });

    ddMenu.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        school = (String) ddMenu.getSelectedItem();
        System.out.println("Selected school: " + school);
      }
    });
    
    // Add panels to window
    Container pane = getContentPane();
    pane.add(boxHome, BorderLayout.PAGE_START);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(dim.width/2, dim.height/2);

    pack();
    setVisible(true);
  }
}
