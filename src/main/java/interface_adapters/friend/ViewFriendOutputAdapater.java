package interface_adapters.friend;

import entities.CommonUser;
import usecases.friendinteractors.viewfriend.view_friend_output_boundary;

import java.util.ArrayList;

public class ViewFriendOutputAdapater implements view_friend_output_boundary {

    @Override
    public void pushFriendList(ArrayList<CommonUser> arraylist){
        //comManager.send(arraylist)
    }

    @Override
    public ArrayList<Integer> pushFriendList_test(ArrayList<Integer> arraylist) {
        return arraylist;
    }
}
