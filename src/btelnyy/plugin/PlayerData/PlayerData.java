package btelnyy.plugin.PlayerData;

public class PlayerData {
	String player_uuid;
	int player_balance;
	
	public void setplayer_uuid(String UUID) {
		player_uuid = UUID;
	}
	public void setplayer_balance(int Balance) {
		player_balance = Balance;
	}
	public String getplayer_uuid() {
		return player_uuid;
	}
	public int getplayer_balance() {
		return player_balance;
	}
}
