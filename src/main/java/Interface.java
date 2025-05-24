import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.google.protobuf.Empty;

import org.checkerframework.checker.guieffect.qual.UI;

//This file was made by Jiyan
public class Interface extends JFrame {
  public JPanel boxInst;
  public JPanel boxDone;
  public JButton btnRun;

  public JLabel gif;

  public String school;

  public Interface() {
    super();
    setResizable(true);
    setFocusable(true);
  }

  public void createInterface() {
    UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 14));
    UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 14));
    UIManager.put("ComboBox.font", new Font("Arial", Font.PLAIN, 14));

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(100, 100);

    // Create containers
    JPanel boxHome = new JPanel();
    boxHome.setLayout(new BoxLayout(boxHome, BoxLayout.Y_AXIS));
    boxHome.setBorder(new EmptyBorder(10, 10, 10, 10));
  
    JPanel boxTop = new JPanel();
    boxTop.setLayout(new BoxLayout(boxTop, BoxLayout.X_AXIS));
    boxTop.setAlignmentX(Component.LEFT_ALIGNMENT);

    boxInst = new JPanel();
    boxInst.setLayout(new BoxLayout(boxInst, BoxLayout.Y_AXIS));
    
    JLabel lblStep1 = new JLabel("1) Press upload button");
    JLabel lblStep2 = new JLabel("2) Choose Contacts .csv to upload");
    JLabel lblStep3 = new JLabel("3) New contacts will be automatically downloaded as a .csv");

    boxDone = new JPanel();
    boxDone.setLayout(new BoxLayout(boxDone, BoxLayout.X_AXIS));

    JLabel lblDone1 = new JLabel("Contacts successfully imported! Check the project folder for your contacts .csv file.");
    JLabel lblDone2 = new JLabel("The GIF below has been updated with further instructions.");
    
    // Create components
    String[] schools = {"CAMS", "Browning", "EPHS", "McBride", "Jordan", "Cabrillo", "Lakewood", "Poly", "LBSA", "Millikan", "Renaissance", "Sato", "Reid", "Wilson"};
    school = schools[0];
    JComboBox<String> ddMenu = new JComboBox<>(schools); // Dropdown menu

    btnRun = new JButton("Run!");

    gif = new JLabel(new ImageIcon("src/main/assets/download.gif"));
    gif.setSize(50,50);

    // Add components and containers
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
        Handler.handleState(Handler.currentState);
      }
    });

    ddMenu.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        school = (String) ddMenu.getSelectedItem();
        System.out.println("Selected school: " + school);
      }
    });
    
    // Add containers to window
    Container pane = getContentPane();
    pane.add(boxHome, BorderLayout.PAGE_START);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(dim.width/2, dim.height/2);
    pack();
    setVisible(true);
  }
}
