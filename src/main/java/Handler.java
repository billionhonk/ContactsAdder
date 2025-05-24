/*=============================================================================
 |   Assignment:  White Tower Project
 |       Author:  Owen, William, Jiyan
 |        Period: 1
 |
 |  Course Name:  AP Computer Science A
 |   Instructor:  Mr. Jonathan Virak
 |     Due Date:  5/23/25 @ 11:59 PM
 |
 |      Purpose:  The purpose of the program is to address a collaboration
 |                inconvenience within Google apps in LBUSD, after a change on
 |                the district's end that led to students not being able to
 |                search for each other by name when using the Share function
 |                in Google products like Google Docs, Sheets, Drive, etc.
 |                Instead of having to add other student's emails manually (a
 |                24-digit email), we seek to automate this process by creating
 |                a program that centralizes emails with student names,
 |                allowing users to easily import contacts into their Google
 |                Contacts. This will significantly reduce the time and effort
 |                required to share documents with classmates, especially in
 |                large groups (like IDP!) and when collaborating with students
 |                who have not previously collaborated. This application allows
 |                users to upload contacts (via .csv file exported from Google
 |                Contacts) from their Contacts into a centralized Firebase
 |                database and retrieve the contacts that they do not have in
 |                their Contacts as a .csv file. This file can then be imported
 |                into their Google Contacts, allowing them to share documents
 |                with other students by name instead of having to manually
 |                enter their 24-digit email addresses. Additionally, the app
 |                is designed to be able to used across multiple schools in the
 |                district, where users only see contacts from their own
 |                school, and not from other schools.
 |
 |     Language:  Java
 |                
 | Deficiencies:  No known deficiencies.
 *===========================================================================*/

import com.opencsv.CSVReader;
import javax.swing.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.firebase.database.*;

// Use countdownlatch to wait for firebase to load (currently unused)
// Ensure we don't jump (due to Firebase being async) until after the people have been fetched
// import java.util.concurrent.CountDownLatch;

import java.awt.Desktop;
import java.io.FileWriter;
import com.opencsv.CSVWriter;

// State Machine to handle accessing Firebase, reading CSV, and writing to Firebase
// Backend of application
// Written by Owen and edited by William
public class Handler {
    // Create an Interface in the Handler class
    public static Interface app = new Interface();

    // Create a HashMap (unique keys) of Person objects to store the contacts as they are created/loaded
    // Might be slightly redundant but ensures that we have a unique list of emails
    public static HashMap<String, Person> emailsToExport = new HashMap<>();

    // ARRAYLISTS: Person objects to export to CSV (new contacts) --> import to user's Google Contacts
    public static ArrayList<Person> peopleToCSV = new ArrayList<>();
    public static ArrayList<String> databaseEmails = new ArrayList<>(); // emails in the database --> avoid duplicates
    public static ArrayList<String> userEmails = new ArrayList<>(); // emails that the user has (from uploaded CSV)
    public static ArrayList<Person> newPeople = new ArrayList<>(); // people from the person's contacts

    // public static CountDownLatch latch = new CountDownLatch(1); // latch to keep project running until done

    // Instantiate our STATE MACHINE because the firebase in async but the file processing is sync 
    // Prevents instant jumps (and prevents the program from not working)
    enum State {
        FETCH, AWAITING_FILE, PROCESSING_CSV, PUSH, DONE
    }
    public static State currentState;

    // VARIABLES: File to store the CSV file that the user uploads
    static File csvFile;

    // This is commented out because we added the Interface - no longer necessary
    // change from main to throws InterruptedException to let latch work
    // interrupted exception is thrown when the thread is interrupted
    // public static void main(String[] args) throws InterruptedException {
    //     handleState(State.FETCH);
    //     latch.await(); // Program keeps running until counted down
    // }

    // Move between states
    public static void handleState(State state) {
        switch (state) { 
            /**
             * FETCHING DATA FROM FIREBASE
             */
            case FETCH:
                System.out.println("Loading Firebase...");

                // Initialize Firebase
                try {
                    FirebaseInitializer.initialize();
                } catch (IOException e) {
                    System.out.println("Failed to initialize Firebase: " + e.getMessage());
                    return;
                }

                // Retrieve data from Firebase Realtime Database
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("people");
                
                // Listener to read data from Firebase
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // LOOPS: Iterates through Firebase data entries
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Person person = child.getValue(Person.class);

                            // Check if duplicate email
                            // NESTED ITERATIONS: contains() method uses iteration, which is in a for loop
                            if (databaseEmails.contains(person.getEmail())) {
                                System.out.println("- Person with email: " + person.getEmail() + " already exists in the database.");
                            
                            // Check if person is valid
                            } else if (person != null) {
                                peopleToCSV.add(person);
                                emailsToExport.put(person.getEmail(), person);
                                databaseEmails.add(person.getEmail());
                                
                                System.out.println("+ Fetched person: " + person.getEmail() + ", " + person.getFirstName() + " " + person.getLastName());
                            }
                        }
                    
                        // Set the current state to AWAITING_FILE after fetching data
                        currentState = State.AWAITING_FILE;
                        handleState(currentState);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Firebase read failed: " + error.getMessage());
                    }
                });
                break;
            
            /**
            * WAITING FOR CSV UPLOAD
            */
            case AWAITING_FILE:
                System.out.println("Awaiting file...");
                // invokeLater() ensures that the file chooser actually opens
                // without this, the file chooser might not open properly for the user
                SwingUtilities.invokeLater(() -> {
                    System.out.println("Opening file chooser...");

                    // Prompt the user to select a CSV file via file chooser
                    JFileChooser fileChooser = new JFileChooser();
                    System.out.println("File chooser opened.");

                    // Tell the user to select their Google Contacts CSV file
                    fileChooser.setDialogTitle("Select your Google Contacts CSV file");

                    // Restrain to csv
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));

                    // Start at home directory
                    String userHome = System.getProperty("user.home");

                    // Check if the OS is Mac to use Downloads (for greater convenience)
                    String os = System.getProperty("os.name").toLowerCase();
                    File initialDir;
                    if (os.contains("mac")) {
                        initialDir = new File(userHome, "Downloads");
                    } else {
                        initialDir = new File(userHome);
                    }
                    fileChooser.setCurrentDirectory(initialDir);

                    // Set the default file name to contacts.csv
                    fileChooser.setSelectedFile(new File("contacts.csv")); 

                    // Select the file and move to process it
                    int result = fileChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        csvFile = fileChooser.getSelectedFile();
                        // Set the current state to PROCESSING_CSV after selecting the file
                        currentState = State.PROCESSING_CSV;
                        handleState(currentState);
                    }
                });
                break;
            
            /**
             * READ DATA FROM UPLOADED CSV
             */
            case PROCESSING_CSV:
                System.out.println("Processing CSV...");

                // Check if the csvFile is null
                try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
                    // Go row-by-row and create Person objects
                    List<String[]> rows = reader.readAll();

                    // Enhanced for loop - don't need to use index
                    for (String[] row : rows) {
                        // Create Person object from the CSV row
                        Person person = createPersonFromCSVRow(row);
                        
                        // Skip if the person is null
                        if (person == null) continue;

                        // Check if the email is already in the emailsToExport map (prevent duplicates)
                        if (emailsToExport.keySet().contains(person.getEmail())) {
                            emailsToExport.remove(person.getEmail());
                            System.out.println("> Person with email: " + person.getEmail() + " already exists in the user's contacts.");
                        }

                        // Check if duplicate email
                        if (databaseEmails.contains(person.getEmail())) {
                            System.out.println("- Person with email: " + person.getEmail() + " already exists in the database.");
                        
                        // Check if the email ends with @lbschools.net
                        } else if (person.getEmail().endsWith("@lbschools.net")) {
                            newPeople.add(person);
                            databaseEmails.add(person.getEmail());

                            System.out.println("+ Adding person with email: " + person.getEmail());
                        }
                    }

                    // Set the current state to PUSH after processing the CSV --> push to Firebase
                    currentState = State.PUSH;
                    System.out.println("CSV file read successfully. Number of people: " + emailsToExport.size());
                    handleState(currentState);
                } catch (Exception e) {
                    System.out.println("Something went wrong while reading the CSV file.");
                    e.printStackTrace();
                }
                break;
            
            /**
             * UPLOADING CSV TO FIREBASE
             */
            case PUSH:
                System.out.println("Uploading...");

                DatabaseReference peopleFB = FirebaseDatabase.getInstance().getReference("people");
                // Add each person to the Firebase Realtime Database
                for (Person person : newPeople) {
                    peopleFB.push().setValueAsync(person);
                    System.out.println("Added person to Firebase: " + person.getEmail());
                }

                currentState = State.DONE;
                handleState(currentState);
                break;
            
            /**
             * CREATE CSV FILE WITH NEW CONTACTS
             */
            case DONE:
                createCSV(new ArrayList<>(emailsToExport.values()));
                System.out.println("Done.");

                app.boxInst.setVisible(false);
                app.btnRun.setVisible(false);
                app.boxDone.setVisible(true);
                app.pack();

                app.gif.setIcon(new ImageIcon("src/main/assets/upload.gif"));

                // latch.countDown(); // Count down the latch to signal that the process is complete 
                break;
        }
    }

    // Extract the contact information from the CSV file 
    // and create a Person object for each contact
    public static Person createPersonFromCSVRow(String[] row) {
        // First name: 0, middle: 1, last: 2, email is last: 19
        // prefix is 6, suffix is 7
        // based on google contacts CSV format 
        String firstName = row[0];
        String middleName = row[1];
        String lastName = row[2];
        String email = row[18];
        String namePrefix = row[6];
        String nameSuffix = row[7];

        // Remove spaces, capitalization from emails
        email = email.strip().toLowerCase();

        // Ensure the first name, last name, and email are not empty
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            return null; 
        }

        System.out.println("Creating person with first name: " + firstName + ", middle name: " + middleName + ", last name: " + lastName + ", email: " + email);
        return new Person(firstName, middleName, lastName, email, namePrefix, nameSuffix);
    }

    // Create a CSV file with the contacts from Firebase
    // Following format for Google Contacts CSV
    public static void createCSV(ArrayList<Person> people) {
        // HEADER: First Name,Middle Name,Last Name,Phonetic First Name,Phonetic Middle Name,Phonetic Last Name,Name Prefix,Name Suffix,Nickname,File As,Organization Name,Organization Title,Organization Department,Birthday,Notes,Photo,Labels,E-mail 1 - Label,E-mail 1 - Value
        String[] header = {"First Name", "Middle Name", "Last Name", "Phonetic First Name", "Phonetic Middle Name", "Phonetic Last Name", "Name Prefix", "Name Suffix", "Nickname", "File As", "Organization Name", "Organization Title", "Organization Department", "Birthday", "Notes", "Photo", "Labels", "E-mail 1 - Label", "E-mail 1 - Value"};

        // Create a new CSV file
        File csvFile = new File("ContactsAdder - New Contacts.csv");
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
            // Write the header to the CSV file
            writer.writeNext(header);

            // Write each person to the CSV file
            for (Person person : people) {
                String[] row = {person.getFirstName(), person.getMiddleName(), person.getLastName(), "", "", "", person.getNamePrefix(), person.getNameSuffix(), "", "", "", "", "", "", "", "", "", "", person.getEmail()};
                writer.writeNext(row);
            }
        } catch (IOException e) {
            System.out.println("Failed to create CSV file: " + e.getMessage());
        }
        System.out.println("CSV file created successfully: " + csvFile.getAbsolutePath());
        // Open the CSV file in the default application 
        try {
            Desktop.getDesktop().open(csvFile);
        } catch (IOException e) {
            System.out.println("Failed to open CSV file: " + e.getMessage());
        }

    }

    // Main method to create interface
    public static void main(String[] args) {
        Handler.app.createInterface();
    }
}