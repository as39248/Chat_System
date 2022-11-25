package client.usecases.delete_message;

import server.usecases.delete_message.DeleteOutputModel;

public interface DeleteOutputBoundary
{
    /**
     * Converts use case output to a String
     * @param delete_model output model of delete message
     */
    void DeleteMessage(DeleteOutputModel delete_model);
}
