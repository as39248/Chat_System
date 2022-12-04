package server.frameworks_and_drivers.database;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Database {
    private final CSVReader userReader;
    private final CSVWriter userWriter;
    private final CSVWriter chatWriter;
    private final CSVReader chatReader;
    private int newUserUid = 0;
    private int newChatUid = 0;
    private HashMap<Integer, Integer> msgUid;

    private final int CHAT_LIST_NON_MSG_CELLS_LENGTH = 6;

    private final int MAX_UPDATE = 1;

    private int chatUpdateCount = 0;

    private int userUpdateCount = 0;


    private final List<String[]> chatDatabase;
    private final List<String[]> userDatabase;

    private final String userURL;
    private final String chatURL;
    public Database(String userURL, String chatURL)
    {
        try
        {
            this.userURL = userURL;
            this.chatURL = chatURL;
            // Initialize all readers
            this.userReader = new CSVReader(new FileReader(userURL));
            this.chatReader = new CSVReader(new FileReader(chatURL));

            // Initialize all writers
            this.userWriter = new CSVWriter(new FileWriter(userURL, true));
            this.chatWriter = new CSVWriter(new FileWriter(chatURL, true));

            // Save the correct value to uid variables
            List<String[]> userDatabase = userReader.readAll();
            this.userDatabase = userDatabase;
            newUserUid = userDatabase.size();

            List<String[]> chatDatabase = chatReader.readAll();
            this.chatDatabase = chatDatabase;
            for (String[] row : chatDatabase)
            {
                msgUid.put(newChatUid, row.length - CHAT_LIST_NON_MSG_CELLS_LENGTH);
                newChatUid = newChatUid + 1;
            }

        }
        catch (IOException | CsvException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void newUser(int userUid, String name, String description, String ip, String password, int port)
    {
        String[] content = {Integer.toString(userUid), name, description, "N/A", "T", "", "", ip, password, "", String.valueOf(port)};
        userUpdateHelper(userUid, content);
    }

    public void updateUser(int uid, String[] newContent)
    {
        userDatabase.remove(uid);
        userUpdateHelper(uid, newContent);
    }
    public void updateUser(int uid, int whichCell, String newContent)
    {
        String[] content = readUser(uid);
        content[whichCell] = newContent;
        userUpdateHelper(uid, content);
    }

    private void userUpdateHelper(int userUid, String[] content)
    {
        userDatabase.add(userUid, content);

        userUpdateCount += 1;
        if (userUpdateCount == MAX_UPDATE)
        {
            // clear the file
            try
            {
                new FileWriter(userURL, false).close();
                userWriter.writeAll(userDatabase);
                userWriter.flush();
                userUpdateCount = 0;
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public void newChat(int chatUid, String name, String description, int adminUid, int[] memberUid)
    {
        String strArr = String.join("-", Arrays.stream(memberUid).mapToObj(String::valueOf).toArray(String[]::new));
        String[] content = {Integer.toString(chatUid), name, description, "", String.valueOf(adminUid), strArr, ""};
        chatUpdateHelper(chatUid, content);
    }

    public void updateChat(int uid, String[] newContent)
    {
        chatDatabase.remove(uid);
        chatUpdateHelper(uid, newContent);
    }

    public void updateChat(int uid, int whichCell, String newContent)
    {
        String[] content = readChat(uid);
        content[whichCell] = newContent;
        chatUpdateHelper(uid, content);
    }

    private void chatUpdateHelper(int chatUid, String[] content)
    {
        chatDatabase.add(chatUid, content);

        chatUpdateCount += 1;
        if (chatUpdateCount == MAX_UPDATE)
        {
            // clear the file
            try {
                new FileWriter(chatURL, false).close();
                chatWriter.writeAll(chatDatabase);
                chatWriter.flush();
                chatUpdateCount = 0;
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public String[] readUser(int uid)
    {
        return userDatabase.get(uid).clone();
    }

    public String[] readChat(int uid)
    {
        return chatDatabase.get(uid).clone();
    }


    public boolean checkUserExist(int UserUid)
    {
        return UserUid < newUserUid;
    }

    public boolean checkChatExist(int ChatUid)
    {
        return ChatUid < newChatUid;
    }




    public int returnNewUserUid() {
        int oldValue = newUserUid;
        newUserUid += 1;
        return oldValue;
    }

    public int returnNewChatUid() {
        int oldValue = newChatUid;
        msgUid.put(oldValue, 0);
        newChatUid += 1;
        return oldValue;
    }

    public int returnNewMsgUid(int chatUid) {
        int oldValue = msgUid.get(chatUid);
        msgUid.put(chatUid, oldValue + 1);
        return oldValue;
    }





}
