package com.example.chatapi.springbootfirebase.service;

import com.example.chatapi.springbootfirebase.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.utilities.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private FirebaseService firebaseService;

    private static final String COLLECTION_NAME = "users" ;
    private static final String TOKEN_INVALID = "Token is invalid!" ;

    public String registerUser(User user) throws ExecutionException, InterruptedException, FirebaseAuthException, IOException {

        Firestore dbFirestore = FirestoreClient.getFirestore();
        Pair<String, String> registerInfo = firebaseService.registerUser(user);
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(registerInfo.getSecond()).set(user);

        verification(String.valueOf(registerInfo.getFirst()));
        return "Created at: " + collectionApiFuture.get().getUpdateTime().toString();
    }

    public String logIn(User user) {
        try {
            String token = firebaseService.signInUser(user).toString();
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        } catch (Exception exception) {
            System.out.println("Invalid user!");
        }
        return "User in logged!";
    }

    public Object getUserByName(String name, String token) throws ExecutionException, InterruptedException {
        if(isTokenValid(token)) {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(name);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(User.class);
            } else {
                return null;
            }
        }
        return TOKEN_INVALID;
    }

    public List<User> getAllUsers(String token) throws ExecutionException, InterruptedException {

        if(isTokenValid(token)) {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            Iterable<DocumentReference> documentReference = dbFirestore.collection(COLLECTION_NAME).listDocuments();
            Iterator<DocumentReference> iterator = documentReference.iterator();
            List<User> userList = new ArrayList<>();

            while (iterator.hasNext()) {
                DocumentReference nextDoc = iterator.next();
                ApiFuture<DocumentSnapshot> future = nextDoc.get();
                DocumentSnapshot document = future.get();
                userList.add(document.toObject(User.class));
            }
            return userList;
        } else {
            return null;
        }
    }

    public String updateUser(User user, String token) throws ExecutionException, InterruptedException {
        if(isTokenValid(token)) {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document().set(user);
            return "Updated at: " + collectionApiFuture.get().getUpdateTime().toString();
        } else {
            return "Token is invalid!";
        }
    }

    // when user wants to delete account
    // check if user is deleting himself
    public String deleteUser(String documentName, String token) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            if(decodedToken.getUid().equals(documentName)) {
                Firestore dbFirestore = FirestoreClient.getFirestore();
                dbFirestore.collection(COLLECTION_NAME).document(documentName).delete();
                deleteRecordFromJunctionTable(documentName);
                return "Document with User ID" + documentName + " has been deleted successfully";
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return TOKEN_INVALID;
    }

    public String verification(String token) {
        try {
            if(isTokenValid(token)) {
                firebaseService.verifyEmail(token);
                return "Verification request sent on e-mail!";
            } else {
                return TOKEN_INVALID;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return "Problem with verification request!";
        }
    }

    private void deleteRecordFromJunctionTable(String name) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("junction_user_room").whereEqualTo("userId", name).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (DocumentSnapshot document : documents) {
            db.collection("junction_user_room").document(document.getId()).delete();
        }
    }

    private boolean isTokenValid(String token) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            return true;
        } catch (FirebaseAuthException firebaseAuthException) {
            return false;
        }
    }

    public boolean checkIfUserIsVerified(String id) {
        return firebaseService.checkIfUserIsVerified(id);
    }
}