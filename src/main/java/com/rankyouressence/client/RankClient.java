package com.rankyouressence.client;

import com.rankyouressence.Rank;
import com.rankyouressence.RankPayload;
import com.rankyouressence.RankStore;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class RankClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(RankPayload.TYPE, (payload, context) -> {
            Rank[] all = Rank.values();
            if (payload.rank() >= 0 && payload.rank() < all.length) {
                RankStore.CLIENT.put(payload.player(), all[payload.rank()]);
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> RankStore.CLIENT.clear());
    }
}
