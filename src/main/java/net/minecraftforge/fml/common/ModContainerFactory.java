package net.minecraftforge.fml.common;

import java.io.File;
import java.lang.reflect.Constructor;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.annotation.Nullable;
import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.objectweb.asm.Type;

import com.google.common.collect.Maps;

import net.minecraftforge.fml.common.discovery.ModCandidate;
import net.minecraftforge.fml.common.discovery.asm.ASMModParser;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;

public class ModContainerFactory {

	public static Map<Type, Constructor<? extends ModContainer>> modTypes = Maps.newHashMap();
	private static ModContainerFactory INSTANCE = new ModContainerFactory();
	private static Cipher cipher;

	static {
		try {
			RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decodeBase64("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxl5wedUaAhpKOAoKNOzJl1owgYBqi8KnP5R3jAm2vJceSRyDSWhAUvU4izPP1Q9pwgvupr978BKM5hot4mBsnpSsbJoTe1rRuVmbbSKRxanLTQeSMVFEgY/Ukhrr8N1AsxSHQVRCSdGuRhnlbqicavJWK4KitlHwKknh995JM6wIDAQAB")));
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, pubKey);
		} catch (Exception e) {
			System.exit(0);
		}
	}

	private ModContainerFactory() {
		registerContainerType(Type.getType(Mod.class), FMLModContainer.class);
	}

	public static ModContainerFactory instance() {
		return INSTANCE;
	}

	public void registerContainerType(Type type, Class<? extends ModContainer> container) {
		try {
			Constructor<? extends ModContainer> constructor = container.getConstructor(new Class<?>[] { String.class, ModCandidate.class, Map.class });
			modTypes.put(type, constructor);
		} catch (Exception e) {
			throw new RuntimeException("Critical error : cannot register mod container type " + container.getName() + ", it has an invalid constructor", e);
		}
	}

	@Nullable
	public ModContainer build(ASMModParser modParser, File modSource, ModCandidate container) {
		String className = modParser.getASMType().getClassName();
		for (ModAnnotation ann : modParser.getAnnotations()) {
			if (modTypes.containsKey(ann.getASMType())) {
				FMLLog.log.debug("Identified a mod of type {} ({}) - loading", ann.getASMType(), className);
				try {
					/*if (ann.getASMType().equals(Type.getType(Mod.class))) {
						String signature = (String) ann.getValues().get("signature");
						if (signature == null || signature.isEmpty()) {
							FMLLog.log.debug("Skipping mod {}, signature is empty.", className);
							return null;
						}
						signature = new String(cipher.doFinal(Base64.decodeBase64(signature)), "UTF-8");
						if (!signature.equals(ann.getValues().get("modid"))) {
							FMLLog.log.debug("Skipping mod {}, signature authentication error.", className);
							return null;
						}
					}*/
					ModContainer ret = modTypes.get(ann.getASMType()).newInstance(className, container, ann.getValues());
					if (!ret.shouldLoadInEnvironment()) {
						FMLLog.log.debug("Skipping mod {}, container opted to not load.", className);
						return null;
					}
					return ret;
				} catch (Exception e) {
					FMLLog.log.error("Unable to construct {} container", ann.getASMType().getClassName(), e);
					return null;
				}
			}
		}

		return null;
	}

}