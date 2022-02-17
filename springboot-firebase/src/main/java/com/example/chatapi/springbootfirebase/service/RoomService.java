package com.example.chatapi.springbootfirebase.service;

import com.example.chatapi.springbootfirebase.entity.Room;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
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
        ApiFuture<WriteResult> collectionApiFuture = db.collection(COLLECTION_NAME).document().set(room);

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

    public String deleteRoom(String documentName) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(documentName).delete();
        deleteRecordFromJunctionTable(documentName);
        // delete messages in this room
        deleteMessages(documentName);
        return "Document with Product ID" + documentName + " has been deleted successfully";
    }

    private void deleteMessages(String documentName) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("messages").whereEqualTo("roomId", documentName).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (DocumentSnapshot document : documents) {
            db.collection("messages").document(document.getId()).delete();
        }
    }

    private void deleteRecordFromJunctionTable(String documentName) throws ExecutionException, InterruptedException {
        // find all rooms and delete those records in junction
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("junction_user_room").whereEqualTo("roomId", documentName).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (DocumentSnapshot document : documents) {
            db.collection("junction_user_room").document(document.getId()).delete();
        }
    }
}
