package com.anotherstar.client.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;

public class LoliCardOnlineUtil {

	private static Set<String> loading = Sets.newHashSet();
	private static Map<String, Integer> urlToTexture = Maps.newHashMap();
	private static Map<String, Integer> urlToWidth = Maps.newHashMap();
	private static Map<String, Integer> urlToHeight = Maps.newHashMap();

	public static boolean isLoad(String url) {
		return urlToTexture.containsKey(url);
	}

	public static void load(String url) {
		if (!loading.contains(url)) {
			add(url);
			new Thread(() -> {
				try {
					URLConnection connection = new URL(url).openConnection();
					connection.setDoOutput(true);
					BufferedImage src = ImageIO.read(connection.getInputStream());
					Minecraft.getMinecraft().addScheduledTask(() -> {
						int texture = TextureUtil.glGenTextures();
						TextureUtil.uploadTextureImage(texture, src);
						urlToTexture.put(url, texture);
						urlToWidth.put(url, src.getWidth());
						urlToHeight.put(url, src.getHeight());
						remove(url);
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}

	public synchronized static void add(String url) {
		loading.add(url);
	}

	public synchronized static void remove(String url) {
		loading.remove(url);
	}

	public static void unload(String url) {
		if (urlToTexture.containsKey(url)) {
			TextureUtil.deleteTexture(urlToTexture.remove(url));
		}
	}

	public static void bind(String url) {
		if (urlToTexture.containsKey(url)) {
			GlStateManager.bindTexture(urlToTexture.get(url));
		}
	}

	public static int getWidth(String url) {
		if (urlToWidth.containsKey(url)) {
			return urlToWidth.get(url);
		}
		return 0;
	}

	public static int getHeight(String url) {
		if (urlToHeight.containsKey(url)) {
			return urlToHeight.get(url);
		}
		return 0;
	}

}
