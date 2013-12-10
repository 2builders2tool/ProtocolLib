package com.comphenix.protocol.wrappers;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.comphenix.protocol.BukkitInitialization;
import com.comphenix.protocol.wrappers.WrappedServerPing.CompressedImage;
import com.google.common.io.Resources;

public class WrappedServerPingTest {
	@BeforeClass
	public static void initializeBukkit() throws IllegalAccessException {
		BukkitInitialization.initializePackage();
	}
	
	@Test
	public void test() throws IOException {
		CompressedImage tux = CompressedImage.fromPng(Resources.getResource("tux.png").openStream());
		byte[] original = tux.getDataCopy();
		
		WrappedServerPing serverPing = new WrappedServerPing();
		serverPing.setMotD("Hello, this is a test.");
		serverPing.setPlayersOnline(5);
		serverPing.setPlayersMaximum(10);
		serverPing.setVersionName("Minecraft 123");
		serverPing.setVersionProtocol(4);
		serverPing.setFavicon(tux);

		assertEquals(5, serverPing.getPlayersOnline());
		assertEquals(10, serverPing.getPlayersMaximum());
		assertEquals("Minecraft 123", serverPing.getVersionName());
		assertEquals(4, serverPing.getVersionProtocol());
		
		assertArrayEquals(original, serverPing.getFavicon().getDataCopy());
	}

}
