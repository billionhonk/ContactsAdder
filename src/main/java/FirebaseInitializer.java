import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

// Connect to Firebase 
public class FirebaseInitializer {
    public static void initialize() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            // Use environment variable for service account JSON
            String serviceAccountJson = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            if (serviceAccountJson == null || serviceAccountJson.isEmpty()) {
                throw new IllegalStateException("GOOGLE_APPLICATION_CREDENTIALS environment variable is not set or empty.");
            }

            FileInputStream serviceAccountStream = new FileInputStream(serviceAccountJson);

            // Initialize Firebase with the service account
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .setDatabaseUrl("https://contactsadder-ea69e-default-rtdb.firebaseio.com/")
                .build();

            FirebaseApp.initializeApp(options);
        }
    }
}