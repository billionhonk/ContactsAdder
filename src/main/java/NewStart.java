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

public class NewStart {
    // Create an arraylist of Person objects to store the contacts as they are created/loaded
    public static ArrayList<Person> people = new ArrayList<>();

    public static void main(String[] args) {
        // Initialize Firebase
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
                        people.add(person);
                        System.out.println("Fetched person: " + person.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Firebase read failed: " + error.getMessage());
            }
        });
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
            File csvFile = fileChooser.getSelectedFile();
            try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
                List<String[]> rows = reader.readAll();
                for (String[] row : rows) {
                    Person person = createPersonFromCSVRow(row);

                    // Check if the email ends with @lbschools.net 
                    // TODO: check if the email is already in the arraylist
                    if (person.getEmail().endsWith("@lbschools.net") ) {
                        // add person to the arraylist
                        System.out.println("Adding person with email: " + person.getEmail());
                        people.add(person);
                    }
                }
            } catch (Exception e) {
                System.out.println("Something went wrong while reading the CSV file.");
                e.printStackTrace();
            }
        }
        });

        // Add the contacts to Firebase
        DatabaseReference peopleFB = FirebaseDatabase.getInstance().getReference("people");


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
        System.out.println("Creating person with first name: " + firstName + ", middle name: " + middleName + ", last name: " + lastName + ", email: " + email);
        return new Person(firstName, middleName, lastName, email, namePrefix, nameSuffix);
    }
}