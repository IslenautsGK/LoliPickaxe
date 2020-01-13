package com.anotherstar.client.util.obj;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import com.anotherstar.common.LoliPickaxe;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ObjModelManager {

	private static WavefrontObject defaultModel;
	private static final ResourceLocationRaw resourceDefaultModel = new ResourceLocationRaw(LoliPickaxe.MODID, "models/entity/loli/loli.obj");

	private static LoadingCache<ResourceLocationRaw, WavefrontObject> cache;

	static {
		defaultModel = new WavefrontObject(resourceDefaultModel);

		cache = CacheBuilder.newBuilder().build(CacheLoader.asyncReloading(new CacheLoader<ResourceLocationRaw, WavefrontObject>() {

			@Override
			public WavefrontObject load(ResourceLocationRaw key) throws Exception {
				try {
					return new WavefrontObject(key);
				} catch (Exception e) {
					return defaultModel;
				}
			}
		}, Executors.newCachedThreadPool()));
	}

	public static void reload() {
		cache.invalidateAll();
		defaultModel = new WavefrontObject(resourceDefaultModel);
	}

	public static WavefrontObject getModel(ResourceLocationRaw loc) {
		try {
			return cache.get(loc);
		} catch (ExecutionException e) {
			e.printStackTrace();
			return defaultModel;
		}
	}

}
