package com.example.chatapi.springbootfirebase.service;

import com.example.chatapi.springbootfirebase.entity.Room;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class RoomService {
    // create room inside fire based database
    private static final String COLLECTION_NAME ="rooms" ;

    public String saveRoom(Room room) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = db.collection(COLLECTION_NAME).document(room.getName()).set(room);

        return "Created at: " + collectionApiFuture.get().getUpdateTime().toString();
    }

    //    A DocumentSnapshot contains data read from a document in your Cloud Firestore database.
    //    The data can be extracted with the getData() or get(String) methods.
    public Room getRoomByName(String name) throws ExecutionException, InterruptedException {

        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(name);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        if(document.exists()) {
            return document.toObject(Room.class);
        } else {
            return null;
        }
    }

    public List<Room> getAllRooms() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> documentReference = dbFirestore.collection(COLLECTION_NAME).listDocuments();
        Iterator<DocumentReference> iterator=documentReference.iterator();
        List<Room> productList = new ArrayList<>();

        while(iterator.hasNext()) {
            DocumentReference documentReference1 = iterator.next();
            ApiFuture<DocumentSnapshot> future = documentReference1.get();
            DocumentSnapshot document = future.get();
            productList.add(document.toObject(Room.class));
        }
        return productList;
    }

    public String updateRoom(Room room) throws ExecutionException, InterruptedException {

        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture=dbFirestore.collection(COLLECTION_NAME).document(room.getName()).set(room);
        return "Updated at: " + collectionApiFuture.get().getUpdateTime().toString();

    }

    public String deleteRoom(String name) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(name).delete();
        return "Document with Product ID" + name + " has been deleted successfully";
    }
}
