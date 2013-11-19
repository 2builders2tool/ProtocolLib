package com.comphenix.protocol.injector;

import org.bukkit.Bukkit;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.concurrency.IntegerSet;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.utility.MinecraftVersion;

/**
 * Packets that are known to be transmitted during login. 
 * <p>
 * This may be dynamically extended later.
 * @author Kristian
 */
class LoginPackets {
	private IntegerSet clientSide = new IntegerSet(Packets.PACKET_COUNT);
	private IntegerSet serverSide = new IntegerSet(Packets.PACKET_COUNT);
	
	public LoginPackets(MinecraftVersion version) {
		// Ordinary login
		clientSide.add(Packets.Client.HANDSHAKE);
		serverSide.add(Packets.Server.KEY_REQUEST);
		clientSide.add(Packets.Client.KEY_RESPONSE);
		serverSide.add(Packets.Server.KEY_RESPONSE);
		clientSide.add(Packets.Client.CLIENT_COMMAND);
		serverSide.add(Packets.Server.LOGIN);
		
		// List ping
		clientSide.add(Packets.Client.GET_INFO);
		
		// In 1.6.2, Minecraft started sending CUSTOM_PAYLOAD in the server list protocol
		if (version.compareTo(MinecraftVersion.HORSE_UPDATE) >= 0) {
			clientSide.add(Packets.Client.CUSTOM_PAYLOAD);
		}
		serverSide.add(Packets.Server.KICK_DISCONNECT);
		
		// MCPC++ contains Forge, which uses packet 250 during login
		if (isMCPC()) {
			clientSide.add(Packets.Client.CUSTOM_PAYLOAD);
		}
	}
	
	/**
	 * Determine if we are runnign MCPC.
	 * @return TRUE if we are, FALSE otherwise.
	 */
	private static boolean isMCPC() {
		return Bukkit.getServer().getVersion().contains("MCPC-Plus");
	}
	
	/**
	 * Determine if a packet may be sent during login from a given direction.
	 * @param packetId - the ID of the packet.
	 * @param side - the direction.
	 * @return TRUE if it may, FALSE otherwise.
	 */
	public boolean isLoginPacket(int packetId, ConnectionSide side) {
		switch (side) {
			case CLIENT_SIDE:
				return clientSide.contains(packetId);
			case SERVER_SIDE:
				return serverSide.contains(packetId);
			case BOTH:
				return clientSide.contains(packetId) || 
					   serverSide.contains(packetId);
			default:
				throw new IllegalArgumentException("Unknown connection side: " + side);
		}
	}	
}
