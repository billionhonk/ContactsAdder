import com.opencsv.CSVReader;
import javax.swing.*;
import java.io.FileReader;
import java.io.File;
import java.util.List;

public class NewStart {

    public static void main(String[] args) {
        // Prompt the user to select a CSV file via file chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a CSV file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        String userHome = System.getProperty("user.home");
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
                    // Process each row
                    System.out.println(String.join(", ", row));
                    createPersonFromCSVRow(row);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Extract the contact information from the CSV file 
    // and create a Person object for each contact
    public static Person createPersonFromCSVRow(String[] row) {
        // First name: 0, middle: 1, last: 2, email is last: 19
        // prefix is 6, suffix is 7
        String firstName = row[0];
        String middleName = row[1];
        String lastName = row[2];
        String email = row[18];
        String namePrefix = row[6];
        String nameSuffix = row[7];
        System.out.println("Creating person with first name: " + firstName + ", middle name: " + middleName + ", last name: " + lastName + ", email: " + email);
        return new Person(firstName, middleName, lastName, email, namePrefix, nameSuffix);
    }
}