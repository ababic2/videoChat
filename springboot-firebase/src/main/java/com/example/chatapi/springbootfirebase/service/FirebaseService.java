package com.example.chatapi.springbootfirebase.service;

import com.example.chatapi.springbootfirebase.entity.User;
import com.example.chatapi.springbootfirebase.utils.LoginInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.utilities.Pair;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@Service
public class FirebaseService {

    private static final String API_KEY = "AIzaSyD0lfUZNxCpC2BydNDEpXgnpcewi0dAdiE" ;
    private static final String SIGN_IN_URI = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" ;
    private static final String REGISTER_URI = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" ;
    private static final String VERIFY_MAIL_URI = "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=" ;
    private static final String USER_INFO_URI = "https://identitytoolkit.googleapis.com/v1/accounts:lookup?key=";
    private static final String CHANGE_EMAIL_PASSWORD = "https://identitytoolkit.googleapis.com/v1/accounts:update?key=";
    private static final String DELETE_ACCOUNT = "https://identitytoolkit.googleapis.com/v1/accounts:delete?key=";

    protected Pair<String, String> registerUser(User user) throws IOException {

        JSONObject json = new JSONObject();
        json.put("email", user.getEmail());
        json.put("password", user.getPassword());
        json.put("returnSecureToken", true);
        JSONObject result = null;
        try {
            result = getJSONObject(json, REGISTER_URI + API_KEY);
            System.out.println("TOKEN");
            System.out.println(result);
            System.out.println(String.valueOf(result.get("idToken")));
            return new Pair<>(result.get("idToken").toString(), result.get("localId").toString());
        } catch (Exception ex) {
            System.out.println("Exception in register!");
        }
        return null;
    }

    protected Object signInUser(LoginInfo loginInfo) throws IOException {

        JSONObject json = new JSONObject();
        json.put("email", loginInfo.getEmail());
        json.put("password", loginInfo.getPassword());
        json.put("returnSecureToken", true);
        JSONObject result = null;
        try {
            result = getJSONObject(json, SIGN_IN_URI + API_KEY);
            System.out.println(String.valueOf(result.get("idToken")));
            return result.get("idToken");
        } catch (Exception ex) {
            System.out.println("Exception in sign up!");
        }
        return null;
    }

    protected Boolean checkIfUserIsVerified(String token) {
        JSONObject json = new JSONObject();
        json.put("idToken", token);
        JSONObject result = null;
        try {
            result = getJSONObject(json, USER_INFO_URI + API_KEY);
            System.out.println(result);
            ArrayList<JSONObject> array = (ArrayList<JSONObject>) result.get("users");
            return (boolean) array.get(0).get("emailVerified");
        } catch (Exception ex) {
            System.out.println("Exception in sign up!");
            return null;
        }
    }

    protected void verifyEmail(String customToken) throws IOException, FirebaseAuthException {
        JSONObject json = new JSONObject();
        System.out.println(customToken);
        json.put("requestType", "VERIFY_EMAIL");
        json.put("idToken", customToken);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost verifyMailRequest = new HttpPost(VERIFY_MAIL_URI + API_KEY);
            setUpParams(json, verifyMailRequest);
            HttpResponse response = httpClient.execute(verifyMailRequest);
            HttpEntity entity = response.getEntity();

            // Read the contents of an entity and return it as a String.
            String content = EntityUtils.toString(entity);
            System.out.println(content);
        } catch (Exception ex) {
            System.out.println("Exception in verify Email!");
        } finally {
            httpClient.close();
        }
    }

    protected void changeMail(String token, String newMail) throws IOException {
        JSONObject json = new JSONObject();
        json.put("idToken", token);
        json.put("email", newMail);
        json.put("returnSecureToken", true);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost verifyMailRequest = new HttpPost(CHANGE_EMAIL_PASSWORD + API_KEY);
            setUpParams(json, verifyMailRequest);
            HttpResponse response = httpClient.execute(verifyMailRequest);
            System.out.println("Mail changed!");
        } catch (Exception ex) {
            System.out.println("Exception in change Email!");
        } finally {
            httpClient.close();
        }
    }

    protected void deleteAccount(String token) throws IOException {
        JSONObject json = new JSONObject();
        json.put("idToken", token);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost deleteAccountRequest = new HttpPost(DELETE_ACCOUNT + API_KEY);
            setUpParams(json, deleteAccountRequest);
            HttpResponse response = httpClient.execute(deleteAccountRequest);
            System.out.println("Account deleted!");
        } catch (Exception ex) {
            System.out.println("Exception in account delete!");
        } finally {
            httpClient.close();
        }
    }

    private void setUpParams(JSONObject json, HttpPost registerRequest) throws UnsupportedEncodingException {
        StringEntity params = new StringEntity(json.toString());
        registerRequest.addHeader("content-type", "application/json");
        registerRequest.setEntity(params);
    }

    private JSONObject getJSONObject(JSONObject json, String URI) throws IOException, ParseException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost registerRequest = new HttpPost(URI);
        setUpParams(json, registerRequest);
        HttpResponse response = httpClient.execute(registerRequest);

        JSONObject result = null;
        HttpEntity entity = response.getEntity();

        // Read the contents of an entity and return it as a String.
        // CONVERT RESPONSE TO STRING
        String content = EntityUtils.toString(entity);
        // CONVERT RESPONSE STRING TO JSON ARRAY
        JSONParser parser = new JSONParser();
        result = (JSONObject) parser.parse(content);
        httpClient.close();
        return result;
    }
}
