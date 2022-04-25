package com.example.chatapi.springbootfirebase.service;

import com.example.chatapi.springbootfirebase.entity.Message;
import com.example.chatapi.springbootfirebase.entity.Room;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class MessageService {
    private static final String COLLECTION_NAME ="messages" ;

    public String saveMessage(Message message) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        message.setCreatedAt(Timestamp.now());
        ApiFuture<WriteResult> collectionApiFuture = db.collection(COLLECTION_NAME).document().set(message);

        return "Sent at: " + collectionApiFuture.get().getUpdateTime().toString();
    }

    //    A DocumentSnapshot contains data read from a document in your Cloud Firestore database.
    //    The data can be extracted with the getData() or get(String) methods.
//    public Message getMessageBySender(String name) throws ExecutionException, InterruptedException {
//
//        Firestore dbFirestore = FirestoreClient.getFirestore();
//        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(name);
//        ApiFuture<DocumentSnapshot> future = documentReference.get();
//        DocumentSnapshot document = future.get();
//
//        if(document.exists()) {
//            return document.toObject(Room.class);
//        } else {
//            return null;
//        }
//    }

    public List<Message> getAllMessagesInRoom(String roomId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> documentReference = dbFirestore.collection(COLLECTION_NAME).listDocuments();
        Iterator<DocumentReference> iterator=documentReference.iterator();
        List<Message> messageList = new ArrayList<>();

        while(iterator.hasNext()) {
            DocumentReference documentReference1 = iterator.next();
            ApiFuture<DocumentSnapshot> future = documentReference1.get();
            DocumentSnapshot document = future.get();
            Message message = document.toObject(Message.class);
            if(message.getRoomId().equals(roomId))
                messageList.add(message);
        }
        return messageList;
    }

    public String updateMessage(Message message) throws ExecutionException, InterruptedException {

        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture=dbFirestore.collection(COLLECTION_NAME).document().set(message);
        return "Updated at: " + collectionApiFuture.get().getUpdateTime().toString();

    }

    public String deleteMessage(String name) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(name).delete();
        return "Message " + name + " has been deleted successfully.";
    }
}
