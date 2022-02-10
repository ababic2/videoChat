package com.example.chatapi.springbootfirebase.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Service
public class FirebaseInitalization {
    private FirebaseApp firebaseApp;

    @PostConstruct
    // with this annotation, wheemever appis started it will initially connect to firebase
    public void initalization() {
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("./serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options);
//            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
//            firebaseAuth = FirebaseAuth.getInstance(firebaseInitalization.getFirebaseApp());
        }
        catch (Exception e) {
            System.out.println("File not found in initalization method or IOException!");
//            e.printStackTrace();
        }
    }

    public FirebaseApp getFirebaseApp() {
        return firebaseApp;
    }
}
