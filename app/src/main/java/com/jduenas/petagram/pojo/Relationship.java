package com.jduenas.petagram.pojo;

/**
 * Created by jduenas on 10/02/2017.
 */

public class Relationship {
    private String outgoing_status = "";
    private String incoming_status = "";
    private boolean target_user_is_private;

    public String getOutgoing_status() {
        return outgoing_status;
    }

    public void setOutgoing_status(String outgoing_status) {
        this.outgoing_status = outgoing_status;
    }

    public String getIncoming_status() {
        return incoming_status;
    }

    public void setIncoming_status(String incoming_status) {
        this.incoming_status = incoming_status;
    }

    public boolean isTarget_user_is_private() {
        return target_user_is_private;
    }

    public void setTarget_user_is_private(boolean target_user_is_private) {
        this.target_user_is_private = target_user_is_private;
    }

    @Override
    public String toString() {
        return "outgoing_status: "+outgoing_status+"\n"
                +"incoming_status: "+incoming_status+"\n"
                +"target_user_is_private: "+target_user_is_private+"\n";
    }
}
