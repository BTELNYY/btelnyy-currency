package me.btelnyy.currency.playerdata;

public class PlayerData {

    public String PlayerUuid = "00000000-0000-0000-0000-000000000000";
    public int PlayerBalance = 0;
    public boolean PlayerCanPay = true;
    public boolean PlayerCanBePaid = true;


    public String getUniqueId(){
        return this.PlayerUuid;
    }
}
