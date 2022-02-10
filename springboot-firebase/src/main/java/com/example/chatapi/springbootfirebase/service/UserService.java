package com.example.chatapi.springbootfirebase.service;

import com.example.chatapi.springbootfirebase.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.*;
import com.google.firebase.cloud.FirestoreClient;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    @Autowired
    private EmailSenderService emailSenderService;

    private static final String COLLECTION_NAME = "users" ;
    public String saveUser(User user) throws ExecutionException, InterruptedException, FirebaseAuthException, MessagingException, IOException {
        Firestore db = FirestoreClient.getFirestore();

        UserRecord userRecord = addUserToFirebaseAuthentication(user);

        ApiFuture<WriteResult> collectionApiFuture = db.collection(COLLECTION_NAME).document(userRecord.getUid()).set(user);

        registerUser(user);
        Object token = signUpUser(user);

        String link = FirebaseAuth.getInstance().generateEmailVerificationLink(user.getEmail());
//        SSLEmail sslEmail = new SSLEmail();
//        sslEmail.sendMail(link);
        verifyEmail(userRecord, String.valueOf(token));
        return "Created at: " + collectionApiFuture.get().getUpdateTime().toString();
    }

    private Object signUpUser(User user) throws IOException {
        JSONObject json = new JSONObject();
        json.put("email", user.getEmail());
        json.put("password", user.getPassword());
        json.put("returnSecureToken", true);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        JSONObject result = null;

        try {
            HttpPost request2 = new HttpPost(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyD0lfUZNxCpC2BydNDEpXgnpcewi0dAdiE");
            StringEntity params = new StringEntity(json.toString());
            request2.addHeader("content-type", "application/json");
            request2.setEntity(params);
            HttpResponse response = httpClient.execute(request2);
            HttpEntity entity = response.getEntity();
            // Read the contents of an entity and return it as a String.
            // CONVERT RESPONSE TO STRING
            String content = EntityUtils.toString(entity);
            // CONVERT RESPONSE STRING TO JSON ARRAY
            JSONParser parser = new JSONParser();
            result = (JSONObject) parser.parse(content);
            System.out.println(String.valueOf(result.get("idToken")));
            httpClient.close();
            return result.get("idToken");
        } catch (Exception ex) {
            // handle exception here
        }
//        } finally {
//            httpClient.close();
////            return result.get("idToken").toString();
//        }
        httpClient.close();
        return null;
    }

    private UserRecord addUserToFirebaseAuthentication(User user) throws ExecutionException, InterruptedException, IOException, FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(user.getEmail())
                .setEmailVerified(false)
                .setPassword(user.getPassword())
                .setDisplayName(user.getUsername());

        ApiFuture<UserRecord> userRecord = FirebaseAuth.getInstance().createUserAsync(request);
        return userRecord.get();
    }

    private void registerUser(User user) throws IOException {
        JSONObject json = new JSONObject();
        json.put("email", user.getEmail());
        json.put("password", user.getPassword());
        json.put("returnSecureToken", true);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost request2 = new HttpPost(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=AIzaSyD0lfUZNxCpC2BydNDEpXgnpcewi0dAdiE");
            StringEntity params = new StringEntity(json.toString());
            request2.addHeader("content-type", "application/json");
            request2.setEntity(params);
            httpClient.execute(request2);
        // handle response here...
        } catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.close();
        }
    }

    private void verifyEmail(UserRecord userRecord, String customToken) throws IOException, FirebaseAuthException {
        FirebaseAuth.getInstance().revokeRefreshTokens(userRecord.getUid());
        JSONObject json = new JSONObject();
        System.out.println(customToken);
        json.put("requestType", "VERIFY_EMAIL");
        json.put("idToken", customToken);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost request2 = new HttpPost(
                    "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=AIzaSyD0lfUZNxCpC2BydNDEpXgnpcewi0dAdiE");
            StringEntity params = new StringEntity(json.toString());
            request2.addHeader("content-type", "application/json");
            request2.setEntity(params);
            HttpResponse response = httpClient.execute(request2);

            HttpEntity entity = response.getEntity();

            // Read the contents of an entity and return it as a String.
            String content = EntityUtils.toString(entity);
            System.out.println(content);
            // handle response here...
        } catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.close();
        }
    }

    public User getUserByName(String name) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(name);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if(document.exists()) {
            return document.toObject(User.class);
        } else {
            return null;
        }
    }

    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> documentReference = dbFirestore.collection(COLLECTION_NAME).listDocuments();
        Iterator<DocumentReference> iterator=documentReference.iterator();
        List<User> userList = new ArrayList<>();

        while(iterator.hasNext()) {
            DocumentReference documentReference1 = iterator.next();
            ApiFuture<DocumentSnapshot> future = documentReference1.get();
            DocumentSnapshot document = future.get();
            userList.add(document.toObject(User.class));
        }
        return userList;
    }

    public String updateUser(User user) throws ExecutionException, InterruptedException {

        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture=dbFirestore.collection(COLLECTION_NAME).document(user.getUsername()).set(user);
        return "Updated at: " + collectionApiFuture.get().getUpdateTime().toString();

    }

    public String deleteUser(String name) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(name).delete();
        return "Document with User ID" + name + " has been deleted successfully";
    }

    public void generateEmailVerificationLink(UserRecord userRecord) {
        final ActionCodeSettings actionCodeSettings = initActionCodeSettings();
        // [START email_verification_link]
        try {
            System.out.println("-------------------------------");
            String link = FirebaseAuth.getInstance().generateEmailVerificationLink(userRecord.getEmail());
            System.out.println("link is" + link);
            // Construct email verification template, embed the link and send
            // using custom SMTP server.
            sendCustomEmail(userRecord.getEmail(), userRecord.getDisplayName(), link);
        } catch (FirebaseAuthException e) {
            System.out.println("Error generating email link: " + e.getMessage());
        }
        // [END email_verification_link]
    }

    private ActionCodeSettings initActionCodeSettings() {
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.builder()
                .setUrl("https://www.example.com/checkout?cartId=1234")
                // URL which is accessible after the user clicks the email link.
                .setHandleCodeInApp(true)
//                .setIosBundleId("com.example.ios")
//                .setAndroidPackageName("com.example.android")
//                .setAndroidInstallApp(true)
//                .setAndroidMinimumVersion("12")
                .setDynamicLinkDomain("coolapp.page.link")
                .build();
        // [END init_action_code_settings]
        return actionCodeSettings;
    }

    private void sendCustomEmail(String email, String displayName, String link) {}
}
