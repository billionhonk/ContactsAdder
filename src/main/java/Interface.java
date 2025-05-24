import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//This file was made by Jiyan
public class Interface extends JFrame {
  public Interface() {
    super();
    setResizable(true);
    setFocusable(true);
  }

  public void createInterface() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(100, 100);

    // Containers of components
    JPanel home = new JPanel();
    home.setAlignmentX(Component.LEFT_ALIGNMENT);
    


    JPanel instructions = new JPanel();
    instructions.setAlignmentX(Component.LEFT_ALIGNMENT);

    String[] options = {"CAMS"};
    JComboBox<String> schools = new JComboBox<>(options); // Dropdown menu

    JLabel gif = new JLabel(new ImageIcon("src/main/assets/upload.gif"));
    gif.setSize(50,50);
    
    // Arrange drop down and steps vertically
    instructions.setLayout(new BoxLayout(instructions, BoxLayout.Y_AXIS));
    
    JLabel step1 = new JLabel("1) Press upload button");
    JLabel step2 = new JLabel("2) Choose Contacts .csv to upload");
    JLabel step3 = new JLabel("3) New contacts will be automatically downloaded as a .csv");
    
    instructions.add(step1);
    instructions.add(step2);
    instructions.add(step3);
    
    JButton upbtn = new JButton("Run!");

    home.add(instructions);
    home.add(schools);
    home.add(upbtn);
    home.add(gif);
    
    upbtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        System.out.println("UPbtn pressed");
        Handler.handleState(Handler.currentState);
      }
    });
    
    // JButton dwbtn = new JButton("Download");
    
    // dwbtn.addActionListener(new ActionListener() {
    //     @Override
    //     public void actionPerformed(ActionEvent evt) {
    //         System.out.println("DOWNbtn pressed");
    //       }
    //     });
        
    Container pane = getContentPane();
    pane.add(home, BorderLayout.PAGE_START);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(dim.width/2, dim.height/2);
    pack();
    setVisible(true);
  }
      
  public static void main(String[] args) {
    Interface contAdder = new Interface();
    contAdder.createInterface();
  }
}

