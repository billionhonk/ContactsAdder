import com.opencsv.CSVReader;
import javax.swing.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.database.*;

import java.io.FileInputStream;

// Use countdownlatch to wait for firebase to load
import java.util.concurrent.CountDownLatch;

import java.awt.Desktop;
import java.io.FileWriter;
import java.io.IOException;
import com.opencsv.CSVWriter;

public class NewStart {
    // Create an arraylist of Person objects to store the contacts as they are created/loaded
    public static ArrayList<Person> peopleToCSV = new ArrayList<>();
    public static ArrayList<String> fullNames = new ArrayList<>(); // people in the database --> avoid duplicates
    public static ArrayList<Person> newPeople = new ArrayList<>(); // people from the person's contacts
    private static CountDownLatch latch = new CountDownLatch(1);

    // Instantiate STATE MACHINE because the firebase in async but the file processing is sync
    // This is annoying
    enum State {
        FETCH, AWAITING_FILE, PROCESSING_CSV, PUSH, DONE
    }
    public static State currentState = State.FETCH;

    static File csvFile;

    // change from main to throws InterruptedException to let latch work
    // interrupted exception is thrown when the thread is interrupted
    public static void main(String[] args) throws InterruptedException {
        handleState(State.FETCH);
        latch.await(); // Wait for Firebase to load
    }

    public static void handleState(State state) {
        switch (state) {
            case FETCH:
                System.out.println("Loading Firebase...");
                try {
                    FirebaseInitializer.initialize();
                } catch (IOException e) {
                    System.out.println("Failed to initialize Firebase: " + e.getMessage());
                    return;
                }

                // Retrieve data from Firebase Realtime Database
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("people");

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Person person = child.getValue(Person.class);
                            if (person != null) {
                                peopleToCSV.add(person);
                                // Add to fullNames to avoid duplicates.. subclass of Person --> FullName
                                fullNames.add(person.getFirstName() + " " + person.getLastName());
                                System.out.println("Fetched person: " + person.getEmail() + ", " + person.getFirstName() + " " + person.getLastName());
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
            case AWAITING_FILE:
                System.out.println("Awaiting file...");
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
                    fileChooser.setSelectedFile(new File("contacts.csv")); // Set default file name
                    int result = fileChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        csvFile = fileChooser.getSelectedFile();
                        // Set the current state to PROCESSING_CSV after selecting the file
                        currentState = State.PROCESSING_CSV;
                        handleState(currentState);
                    }
                    });

                break;
            case PROCESSING_CSV:
                System.out.println("Processing CSV...");
                try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
                            List<String[]> rows = reader.readAll();
                            for (String[] row : rows) {
                                Person person = createPersonFromCSVRow(row);
                                
                                // Skip if the person is null
                                if (person == null) {
                                    continue;
                                }

                                // Check if the email ends with @lbschools.net 
                                // TODO: check if the email is already in the arraylist
                                if (person.getEmail().endsWith("@lbschools.net") && !peopleToCSV.contains(person)) {
                                    // nested for loop to check if the person is already in the arraylist
                                    boolean alreadyExists = false;
                                    for (Person p : peopleToCSV) {
                                        if (p.getEmail().equals(person.getEmail())) {
                                            System.out.println("Person with email: " + person.getEmail() + " already exists in the database.");
                                            alreadyExists = true;

                                            // Remove from peopleToCSV to avoid duplicates
                                            for (int i = 0; i < peopleToCSV.size(); i++) {
                                                if (peopleToCSV.get(i).getEmail().equals(person.getEmail())) {
                                                    peopleToCSV.remove(i);
                                                    System.out.println("Removed person with email: " + person.getEmail() + " from the list.");
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                    }

                                    if (!alreadyExists) {
                                        // add person to the arraylist
                                        System.out.println("Adding person with email: " + person.getEmail());
                                        
                                        // Add to newPeople to add later to firebase after all are added
                                        newPeople.add(person);
                                    }

                                }
                            }

                            // Set the current state to PROCESSING_CSV after reading the file
                            currentState = State.PUSH;
                            System.out.println("CSV file read successfully. Number of people: " + peopleToCSV.size());
                            handleState(currentState);
                        } catch (Exception e) {
                            System.out.println("Something went wrong while reading the CSV file.");
                            e.printStackTrace();
                        }
                break;
            case PUSH:
                System.out.println("Uploading...");
                DatabaseReference peopleFB = FirebaseDatabase.getInstance().getReference("people");
                // Add each person to the Firebase Realtime Database
                for (Person person: newPeople) {
                    peopleFB.push().setValueAsync(person);
                    System.out.println("Added person to Firebase: " + person.getEmail());
                }

                currentState = State.DONE;
                handleState(currentState);
                break;
            case DONE:
                createCSV(peopleToCSV);
                System.out.println("Done.");
                latch.countDown();
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
}