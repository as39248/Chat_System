package server.usecases.create_gc;

import server.entities.GroupChat;
import server.entities.User;

import java.util.ArrayList;

/**
 * This class is the interactor for the usecase responsible for where the users'
 * selected friends from their FriendsList are taken and a
 * GroupChat is created with them as the members.
 * @author Brenden McFarlane
 */
public class CreateGCInteractor implements CreateGCInputBoundary {
    /**
     * the Database access class that CreateGCInteractor will use to gather required information
     */
    private final CreateGCDBGateway database;
    /**
     * the output boundary that CreateGCInteractor will access when finished making the new GroupChat.
     */
    private final CreateGCOutputBoundary output_adapter;

    public CreateGCInteractor(CreateGCDBGateway database, CreateGCOutputBoundary output_adapter){
        this.database = database;
        this.output_adapter = output_adapter;
    }

    /**
     * Creates a new GroupChat, adds it to the database, and informs the CreateGCOutputAdapter of its success.
     *
     * <p>The new GroupChat will have the admin and members from input_data, and the UID will be a new, unique UID
     * generated by database.
     *
     * @param input_data contains List<User> of those to be added as members of the GroupChat.
     */
    @Override
    public void create(CreateGCInputData input_data) {
        User admin = database.getUserByUID(input_data.getAdmin());
        ArrayList<User> members = new ArrayList<>();
        members.add(database.getUserByUID(input_data.getAdmin()));
        for (int i: input_data.getMembers()) {
            if(database.getUserByUID(i) != null){
                members.add(database.getUserByUID(i));
            }
        }
        GroupChat gc = new GroupChat(this.database.getNewUID(), admin, members);
        database.addGC(gc);
        int peerPort = database.getPeerPortFromUID(input_data.getAdmin());
        String peerID = database.getPeerIDFromUID(input_data.getAdmin());
        CreateGCOutputData output = new CreateGCOutputData(
                input_data.getAdmin(), input_data.getMembers(), peerID, peerPort);
        output_adapter.prepareSuccessView(output);
    }
}
