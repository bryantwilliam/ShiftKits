package com.gmail.gogobebe2.shiftkits.requirements;

import co.insou.xcore.utils.PlayerInfo;
import org.bukkit.entity.Player;

public class Cost extends Requirement {
    private int price;

    public Cost(int price) {
        super(price + " XP");
        this.price = price;
    }

    @Override
    public boolean satisfies(Player player) {
        if (PlayerInfo.getPlayerInfo(player).getXP() >= price) return true;
        return false;
    }

    public void takeXP(Player player) {
        PlayerInfo.getPlayerInfo(player).removeXP(price);
    }
}
