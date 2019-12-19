package com.anotherstar.client.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import com.anotherstar.common.LoliPickaxe;
import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;

public class LoliCardUtil {

	public static String[] customArtNames = null;
	public static int[] customArtHeights = null;
	public static int[] customArtWidths = null;
	public static ResourceLocation[] customArtResources = null;

	public static void updateCustomArtDatas() {
		customArtNames = getListOfPaintings();
		if (customArtNames != null) {
			customArtResources = getPaintingsResourceLocations(customArtNames);
			customArtHeights = getPaintingSetHeights(customArtResources);
			customArtWidths = getPaintingSetWidths(customArtResources);
		}
	}

	private static ResourceLocation[] getPaintingsResourceLocations(String[] names) {
		ResourceLocation[] paintings = new ResourceLocation[names.length];
		for (int i = 0; i < names.length; i++) {
			paintings[i] = new ResourceLocation("lolipickaxe", "lolicards/" + names[i]);
		}
		return paintings;
	}

	private static int[] getPaintingSetHeights(ResourceLocation[] resources) {
		int[] heights = new int[resources.length];
		IResourceManager rm = Minecraft.getMinecraft().getResourceManager();
		try {
			for (int i = 0; i < resources.length; i++) {
				IResource theThing = rm.getResource(resources[i]);
				if (theThing != null) {
					Image img = ImageIO.read(theThing.getInputStream());
					if (img != null) {
						heights[i] = img.getHeight(null);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return heights;
	}

	private static int[] getPaintingSetWidths(ResourceLocation[] resources) {
		int[] widths = new int[resources.length];
		IResourceManager rm = Minecraft.getMinecraft().getResourceManager();
		try {
			for (int i = 0; i < resources.length; i++) {
				IResource theThing = rm.getResource(resources[i]);
				if (theThing != null) {
					Image img = ImageIO.read(theThing.getInputStream());
					if (img != null) {
						widths[i] = img.getWidth(null);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return widths;
	}

	private static String[] getListOfPaintings() {
		String[] resources = getJarPaintings();
		String[] resourcePacks = getResourcePackPaintings();
		if ((resources != null) && (resources.length > 0) && (resourcePacks != null) && (resourcePacks.length > 0)) {
			String[] masterList = new String[resources.length + resourcePacks.length];
			int indexAdjuster = 0;
			for (int i = 0; i < masterList.length; i++) {
				if (i < resources.length) {
					masterList[i] = resources[i];
				} else {
					masterList[i] = resourcePacks[(i - resources.length)];
				}
			}
			return masterList;
		}
		if (((resources == null) || (resources.length < 1)) && (resourcePacks != null) && (resourcePacks.length > 0)) {
			return resourcePacks;
		}
		if ((resources != null) && (resources.length > 0) && ((resourcePacks == null) || (resourcePacks.length < 1))) {
			return resources;
		}
		return null;
	}

	private static String[] getJarPaintings() {
		String path = "assets/lolipickaxe/lolicards/";
		URL dirURL = LoliPickaxe.instance.getClass().getResource("/assets/lolipickaxe/lolicards/");
		if ((dirURL != null) && (dirURL.getProtocol().equals("file"))) {
			try {
				String[] newSet = new File(dirURL.toURI()).list();
				int setSize = 0;
				ArrayList<String> thePNGs = new ArrayList();
				for (int i = 0; i < newSet.length; i++) {
					if (newSet[i].contains(".png")) {
						thePNGs.add(newSet[i]);
					}
				}
				String[] finalSet = new String[thePNGs.size()];
				for (int i = 0; i < thePNGs.size(); i++) {
					finalSet[i] = ((String) thePNGs.get(i));
				}
				return finalSet;
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		if ((dirURL != null) && (dirURL.getProtocol().equals("jar"))) {
			String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
			try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));) {
				Enumeration<JarEntry> entries = jar.entries();
				Set<String> result = Sets.newHashSet();
				while (entries.hasMoreElements()) {
					String name = ((JarEntry) entries.nextElement()).getName();
					if (name.startsWith(path)) {
						String entry = name.substring(path.length());
						if (entry.contains(".png")) {
							result.add(entry);
						}
					}
				}
				return (String[]) result.toArray(new String[result.size()]);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static String[] getResourcePackPaintings() {
		ResourcePackRepository pack = Minecraft.getMinecraft().getResourcePackRepository();
		File[] packFileList = pack.getDirResourcepacks().listFiles();
		String[] currentlyUsedPacks = getCurrentResoucePackList(pack);
		if ((packFileList != null) && (currentlyUsedPacks != null)) {
			ArrayList<String> packPaintings = new ArrayList();
			for (int i = 0; i < currentlyUsedPacks.length; i++) {
				for (int j = 0; j < packFileList.length; j++) {
					if (packFileList[j].getAbsolutePath().contains(currentlyUsedPacks[i])) {
						String[] paintingsInThisPack = getListFromResourcePack(packFileList[j]);
						if ((paintingsInThisPack == null) || (paintingsInThisPack.length <= 0)) {
							break;
						}
						for (int k = 0; k < paintingsInThisPack.length; k++) {
							packPaintings.add(paintingsInThisPack[k]);
						}
						break;
					}
				}
			}
			if ((!packPaintings.isEmpty()) && (packPaintings.size() > 0)) {
				String[] fullPackPaintingList = new String[packPaintings.size()];
				for (int i = 0; i < packPaintings.size(); i++) {
					fullPackPaintingList[i] = ((String) packPaintings.get(i));
				}
				return fullPackPaintingList;
			}
		}
		return null;
	}

	private static String[] getCurrentResoucePackList(ResourcePackRepository pack) {
		List lst = pack.getRepositoryEntries();
		if (lst.size() > 0) {
			String[] currentPacks = new String[lst.size()];
			for (int i = 0; i < lst.size(); i++) {
				ResourcePackRepository.Entry entry = (ResourcePackRepository.Entry) lst.get(i);
				IResourcePack packet = entry.getResourcePack();
				currentPacks[i] = packet.getPackName();
			}
			return currentPacks;
		}
		return null;
	}

	private static String[] getListFromResourcePack(File resourceZipFile) {
		try {
			String ext = FilenameUtils.getExtension(resourceZipFile.getName());
			if (ext.equals("zip")) {
				String path = "assets/lolipickaxe/lolicards/";
				ZipFile zippy = new ZipFile(resourceZipFile);
				Enumeration packEntries = zippy.entries();
				String fileName = "";
				Set<String> result = Sets.newHashSet();
				while (packEntries.hasMoreElements()) {
					fileName = ((ZipEntry) packEntries.nextElement()).getName();
					if (fileName.startsWith(path)) {
						String entry = fileName.substring(path.length());
						if (entry.contains(".png")) {
							result.add(entry);
						}
					}
				}
				zippy.close();
				if ((!result.isEmpty()) && (result.size() > 0)) {
					return (String[]) result.toArray(new String[result.size()]);
				}
			} else if (ext.equals("")) {
				File paintingsLoc = new File(resourceZipFile, "assets/lolipickaxe/lolicards/");
				String[] finalSet = new String[0];
				if (paintingsLoc.exists()) {
					String[] newSet = paintingsLoc.list();
					if (newSet.length > 0) {
						ArrayList<String> thePNGs = new ArrayList();
						for (int i = 0; i < newSet.length; i++) {
							if (newSet[i].contains(".png")) {
								thePNGs.add(newSet[i]);
							}
						}
						finalSet = new String[thePNGs.size()];
						for (int i = 0; i < thePNGs.size(); i++) {
							finalSet[i] = ((String) thePNGs.get(i));
						}
					}
				}
				return finalSet;
			}
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
