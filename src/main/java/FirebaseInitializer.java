// Libraries to access Firebase Realtime Database
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.FileInputStream;

// Connect to Firebase 
// Reference: https://firebase.google.com/docs/database/admin/start
public class FirebaseInitializer {
    // Initializes Firebase with the service account credentials 
    public static void initialize() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            // REMOVE BEFORE GOING TO PROD
            // Use environment variable for service account JSON
            // export GOOGLE_APPLICATION_CREDENTIALS="tokens/key.json"

            // Use environmental variable instead of hardcoding the path
            String serviceAccountJson = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            if (serviceAccountJson == null || serviceAccountJson.isEmpty()) {
                throw new IllegalStateException("GOOGLE_APPLICATION_CREDENTIALS environment variable is not set or empty.");
            }

            FileInputStream serviceAccountStream = new FileInputStream(serviceAccountJson);

            // Initialize Firebase with the service account
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .setDatabaseUrl("https://contactsadder-ea69e-default-rtdb.firebaseio.com/") // specific database URL
                .build();

            FirebaseApp.initializeApp(options);
        }
    }
}