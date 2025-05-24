/*=============================================================================
 |   Assignment:  White Tower Project
 |       Author:  Owen, William, Jiyan
         Period: 1

 |  Course Name:  AP Computer Science A
 |   Instructor:  Mr. Jonathan Virak
 |     Due Date:  5/23/25
 |
 |      Purpose:  The purpose of the program is to address a collaboration inconvenience within Google apps in LBUSD, after a change on the district's end that led to students not being able to search for each other by name when using the Share function in Google products like Google Docs, Sheets, Drive, etc. Instead of having to add other student's emails manually (a 24-digit email), we seek to automate this process by creating a program that centralizes emails with student names, allowing users to easily import contacts into their Google Contacts. This will significantly reduce the time and effort required to share documents with classmates, especially in large groups (like IDP!) and when collaborating with students who have not previously collaborated. This application allows users to upload contacts (via .csv file exported from Google Contacts) from their Contacts into a centralized Firebase database and retrieve the contacts that they do not have in their Contacts as a .csv file. This file can then be imported into their Google Contacts, allowing them to share documents with other students by name instead of having to manually enter their 24-digit email addresses. Additionally, the app is designed to be able to used across multiple schools in the district, where users only see contacts from their own school, and not from other schools. 
 |     Language:  [java]           
 |                
 | Deficiencies:  No known deficiencies.
 *===========================================================================*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.google.protobuf.Empty;

import org.checkerframework.checker.guieffect.qual.UI;

//This file was made by Jiyan
// INHERITANCE: extends from JFrame to create a GUI window
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
    boxTop.setBorder(new EmptyBorder(0, 0, 10, 0));

    boxInst = new JPanel();
    boxInst.setLayout(new BoxLayout(boxInst, BoxLayout.Y_AXIS));
    
    JLabel lblStep1 = new JLabel("1) Press upload button");
    JLabel lblStep2 = new JLabel("2) Choose Contacts .csv to upload");
    JLabel lblStep3 = new JLabel("3) New contacts will be automatically downloaded as a .csv");

    boxDone = new JPanel();
    boxDone.setLayout(new BoxLayout(boxDone, BoxLayout.Y_AXIS));

    JLabel lblDone1 = new JLabel("Contacts successfully imported! Check the project folder for your contacts");
    JLabel lblDone2 = new JLabel(".csv file. The GIF below has been updated with further instructions.");
    
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
