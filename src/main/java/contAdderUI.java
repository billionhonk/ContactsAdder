import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//This file was made by Jiyan
public class contAdderUI extends JFrame{
  public contAdderUI()
  {
    super();
    setResizable(true);
    setFocusable(true);
  }

  public void createInterFace(){
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1000, 1000);
    String[] options = {"CAMS"};
    //ImageIcon test = new ImageIcon("testImg.png");

    //Containers of components
    JPanel home = new JPanel();
    JPanel instructions = new JPanel();
    JPanel conta1 = new JPanel();

    //Drop-down (I only put CAMS)
    JComboBox schools = new JComboBox<>(options);

    //Arrange drop down and steps vertically
    conta1.setLayout(new BoxLayout(conta1, BoxLayout.Y_AXIS));
    instructions.setLayout(new BoxLayout(instructions, BoxLayout.Y_AXIS));

    JLabel step1 = new JLabel("1)yada yada yada");
    JLabel step2 = new JLabel("2)aaaaaaaaaaa");
    JLabel step3 = new JLabel("3)fuck");
    JButton upbtn = new JButton("Upload");
    JButton dwbtn = new JButton("Download");

    upbtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        System.out.println("UPbtn pressed");
      }
    });
    
    dwbtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        System.out.println("DOWNbtn pressed");
      }
    });

    instructions.add(step1);
    instructions.add(step2);
    instructions.add(step3);
    instructions.add(upbtn);
    instructions.add(dwbtn);

    conta1.add(instructions);
    conta1.add(schools);

    //JLabel gif = new JLabel(test);
    //gif.setSize(100,100);
    
    //home.add(gif);
    home.add(conta1);
    
    Container pane = getContentPane();
    pane.add(home,  BorderLayout.PAGE_START);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(dim.width/2 - 100, dim.height/2 - 100);
    pack();
    setVisible(true);
    //gif.setVisible(false);
  }
}

class test{
  public static void main(String[] args) {
      contAdderUI contAdder = new contAdderUI();
      contAdder.createInterFace();
  }
}
