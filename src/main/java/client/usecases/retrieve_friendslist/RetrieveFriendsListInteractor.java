package client.usecases.retrieve_friendslist;

import server.entities.CommonUser;
import server.usecases.retrieve_friendslist.*;
import server.usecases.retrieve_friendslist.RetrieveFriendsListDBGateway;
import server.usecases.retrieve_friendslist.RetrieveFriendsListInputData;

import java.util.ArrayList;

public class RetrieveFriendsListInteractor implements RetrieveFriendsListInputBoundary {
    private server.usecases.retrieve_friendslist.RetrieveFriendsListDBGateway database;
    private RetrieveFriendsListOutputBoundary output_adapter;

    public RetrieveFriendsListInteractor(
            RetrieveFriendsListDBGateway database, RetrieveFriendsListOutputBoundary output_adapter){
        this.output_adapter =  output_adapter;
        this.database = database;
    }

    @Override
    public void execute(RetrieveFriendsListInputData input) {
        int UID = input.getUID();
        ArrayList<Integer> UIDs = new ArrayList<>();
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<CommonUser> friendslist = database.getFriendsListByUID(input.getUID());
        for(CommonUser u : friendslist){
            UIDs.add(u.getUid());
            //TODO: usernames.add(u.getUsername());
        }
        RetrieveFriendsListOutputData output = new RetrieveFriendsListOutputData(UID, UIDs, usernames);
        output_adapter.prepareView(output);
    }
}
