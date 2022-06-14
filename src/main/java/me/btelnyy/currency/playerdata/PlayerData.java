package me.btelnyy.currency.playerdata;

import java.util.ArrayList;

import me.btelnyy.currency.constant.Globals;

public class PlayerData {
    public String PlayerUuid = "00000000-0000-0000-0000-000000000000";
    public int PlayerBalance = 0;
    public int MaximumBalance = Globals.MaximumBalance;
    public boolean PlayerCanPay = true;
    public boolean PlayerCanBePaid = true;
    public boolean PlayerCanWithdraw = true;
    public ArrayList<String> Transactions = new ArrayList<String>();

    public String getUniqueId(){
        return this.PlayerUuid;
    }
}
