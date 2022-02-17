package com.example.chatapi.springbootfirebase.service;

import com.example.chatapi.springbootfirebase.entity.Room;
import com.example.chatapi.springbootfirebase.entity.User;
import com.example.chatapi.springbootfirebase.entity.UserRoom;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserRoomService {

    //add user to room
    // myb check if room/user exists when adding but might not be needed in project
    // delete user from room

    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;

    private static final String COLLECTION_NAME = "junction_user_room" ;

    public String addUserToRoom(UserRoom userRoom) throws ExecutionException, InterruptedException {
        // if room doesnt exis, add it to rooms collection
        // but this can be solved with uid
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = db.collection(COLLECTION_NAME)
                        .document(userRoom.getUserId().toString() + "_" + userRoom.getRoomId().toString()).set(userRoom);
        return "User added to room at: " + collectionApiFuture.get().getUpdateTime().toString();
    }

    public String deleteUserFromRoom(String name) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(name).delete();
        return "Record with id" + name + " has been deleted successfully.";
    }

    public List<Room> getRoomsForUser(String name) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).whereEqualTo("userId", name).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<String> roomIds = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            roomIds.add(document.toObject(UserRoom.class).getRoomId());
        }

        List<Room> rooms = new ArrayList<>();
        for (String room : roomIds) {
            rooms.add(roomService.getRoomByName(room));
        }
        return rooms;
    }

    public List<User> getMembersForRoom(String name, String token) throws ExecutionException, InterruptedException {

        Firestore db = FirestoreClient.getFirestore();
        //asynchronously retrieve multiple documents
        ApiFuture<QuerySnapshot> future =
                db.collection(COLLECTION_NAME).whereEqualTo("roomId", name).get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<String> usernames = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            usernames.add(document.toObject(UserRoom.class).getUserId());
        }

        List<User> members = new ArrayList<>();
        for (String username : usernames)
        {
            members.add((User) userService.getUserByName(username, token));
        }
        return members;
    }
}
