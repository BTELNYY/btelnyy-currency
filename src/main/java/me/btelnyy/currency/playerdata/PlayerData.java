package me.btelnyy.currency.playerdata;

import java.util.ArrayList;
//import java.util.List;

public class PlayerData {
    public String PlayerUuid = "00000000-0000-0000-0000-000000000000";
    public int PlayerBalance = 0;
    public boolean PlayerCanPay = true;
    public boolean PlayerCanBePaid = true;
    public boolean PlayerCanWithdraw = true;
    //non functional as of 5/27/2022, may change later
    //now functional, ignore above message 6/2/2022
    public ArrayList<String> Transactions = new ArrayList<String>();

    public String getUniqueId(){
        return this.PlayerUuid;
    }
}
