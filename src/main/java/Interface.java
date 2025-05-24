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

//This file was made by Jiyan
// INHERITANCE: extends from JFrame to create a GUI window
public class Interface extends JFrame {
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

    // Containers of components
    JPanel home = new JPanel();
    home.setAlignmentX(Component.LEFT_ALIGNMENT);

    JPanel instructions = new JPanel();
    instructions.setAlignmentX(Component.LEFT_ALIGNMENT);

    String[] options = {"CAMS", "Browning", "EPHS", "McBride", "Jordan", "Cabrillo", "Lakewood", "Poly", "LBSA", "Millikan", "Renaissance", "Sato", "Reid", "Wilson"};
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

