package com.gmail.gogobebe2.shiftkits.requirements;

import co.insou.xcore.Main;
import co.insou.xcore.utils.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Cost extends Requirement {
    private int price;

    public Cost(int price) {
        super(price + " XP");
        this.price = price;
    }

    @Override
    public boolean satisfies(Player player) {
        PlayerInfo playerInfo = getPlayerInfo(player);
        if (playerInfo.getXP() >= price) return true;
        return false;
    }

    public void takeXP(Player player) {
        PlayerInfo playerInfo = getPlayerInfo(player);
        playerInfo.removeXP(price);
    }

    private PlayerInfo getPlayerInfo(Player player) {
        return new PlayerInfo((Main) Bukkit.getPluginManager().getPlugin("XCore"), player);
    }
}
