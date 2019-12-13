package com.anotherstar.core.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.anotherstar.core.LoliPickaxeCore;

import net.minecraft.launchwrapper.IClassTransformer;

public class LoliPickaxeTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (name.equals("net.minecraftforge.common.ForgeHooks")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					if (name.equals("onLivingDeath")) {
						MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
						mv.visitCode();
						Label start = new Label();
						mv.visitLabel(start);
						mv.visitVarInsn(Opcodes.ALOAD, 0);
						mv.visitVarInsn(Opcodes.ALOAD, 1);
						mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "onLivingDeath",
								"(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/util/DamageSource;)Z", false);
						mv.visitInsn(Opcodes.IRETURN);
						Label end = new Label();
						mv.visitLabel(end);
						mv.visitLocalVariable("entity", "Lnet/minecraft/entity/EntityLivingBase;", null, start, end, 0);
						mv.visitLocalVariable("src", "Lnet/minecraft/util/DamageSource;", null, start, end, 1);
						mv.visitMaxs(2, 2);
						mv.visitEnd();
						return null;
					} else if (name.equals("onLivingUpdate")) {
						MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
						mv.visitCode();
						Label start = new Label();
						mv.visitLabel(start);
						mv.visitVarInsn(Opcodes.ALOAD, 0);
						mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil",
								"onLivingUpdate", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false);
						mv.visitInsn(Opcodes.IRETURN);
						Label end = new Label();
						mv.visitLabel(end);
						mv.visitLocalVariable("entity", "Lnet/minecraft/entity/EntityLivingBase;", null, start, end, 0);
						mv.visitMaxs(1, 1);
						mv.visitEnd();
						return null;
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (name.equals("sv") || name.equals("net.minecraft.entity.EntityLivingBase")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public void visit(int version, int access, String name, String signature, String superName,
						String[] interfaces) {
					super.visit(version, access, name, signature, superName, interfaces);
					if (!LoliPickaxeCore.debug) {
						FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC, "loliDead", "Z", null, null);
						fv.visitEnd();
						fv = cv.visitField(Opcodes.ACC_PUBLIC, "loliDeathTime", "I", null, null);
						fv.visitEnd();
						fv = cv.visitField(Opcodes.ACC_PUBLIC, "loliCool", "Z", null, null);
						fv.visitEnd();
					}
					MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
							LoliPickaxeCore.debug ? "getHealth" : "aS", "()F", null, null);
					mv.visitCode();
					Label start = new Label();
					mv.visitLabel(start);
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "getHealth",
							"(Lnet/minecraft/entity/EntityLivingBase;)F", false);
					mv.visitInsn(Opcodes.FRETURN);
					Label end = new Label();
					mv.visitLabel(end);
					mv.visitLocalVariable("this", "Lnet/minecraft/entity/EntityLivingBase;", null, start, end, 0);
					mv.visitMaxs(1, 1);
					mv.visitEnd();
					mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
							LoliPickaxeCore.debug ? "getMaxHealth" : "aY", "()F", null, null);
					mv.visitCode();
					start = new Label();
					mv.visitLabel(start);
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "getMaxHealth",
							"(Lnet/minecraft/entity/EntityLivingBase;)F", false);
					mv.visitInsn(Opcodes.FRETURN);
					end = new Label();
					mv.visitLabel(end);
					mv.visitLocalVariable("this", "Lnet/minecraft/entity/EntityLivingBase;", null, start, end, 0);
					mv.visitMaxs(1, 1);
					mv.visitEnd();
				}

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					if (name.equals("aS") && desc.equals("()F") || name.equals("getHealth")) {
						return cv.visitMethod(access, "getHealth2", desc, signature, exceptions);
					} else if (name.equals("aY") && desc.equals("()F") || name.equals("getMaxHealth")) {
						return cv.visitMethod(access, "getMaxHealth2", desc, signature, exceptions);
					} else if (name.equals("getHealth2")) {
						return null;
					} else if (name.equals("getMaxHealth2")) {
						return null;
					} else if (name.equals("h") && desc.equals("()V") || name.equals("onUpdate")) {
						return new MethodVisitor(Opcodes.ASM4,
								cv.visitMethod(access, name, desc, signature, exceptions)) {

							public void visitInsn(int opcode) {
								if (opcode == Opcodes.RETURN) {
									mv.visitVarInsn(Opcodes.ALOAD, 0);
									mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil",
											"onUpdate", "(Lnet/minecraft/entity/EntityLivingBase;)V", false);
								}
								mv.visitInsn(opcode);
							};

						};
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (name.equals("sa") || name.equals("net.minecraft.entity.Entity")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
					if (name.equals("i")) {
						access = Opcodes.ACC_PUBLIC;
					}
					return cv.visitField(access, name, desc, signature, value);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (name.equals("yz") || name.equals("net.minecraft.entity.player.EntityPlayer")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public void visit(int version, int access, String name, String signature, String superName,
						String[] interfaces) {
					super.visit(version, access, name, signature, superName, interfaces);
					if (!LoliPickaxeCore.debug) {
						FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC, "hodeLoli", "I", null, null);
						fv.visitEnd();
					}
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (name.equals("yx") || name.equals("net.minecraft.entity.player.InventoryPlayer")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					if (name.equals("m") && desc.equals("()V") || name.equals("dropAllItems")) {
						MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
						mv.visitCode();
						Label start = new Label();
						mv.visitLabel(start);
						mv.visitVarInsn(Opcodes.ALOAD, 0);
						mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "dropAllItems",
								"(Lnet/minecraft/entity/player/InventoryPlayer;)V", false);
						mv.visitInsn(Opcodes.RETURN);
						Label end = new Label();
						mv.visitLabel(end);
						mv.visitLocalVariable("this", "Lnet/minecraft/entity/player/InventoryPlayer;", null, start, end,
								0);
						mv.visitMaxs(1, 1);
						mv.visitEnd();
						return null;
					} else if (name.equals("a") && desc.equals("(Ladb;I)I") || name.equals("clearInventory")) {
						MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
						mv.visitCode();
						Label start = new Label();
						mv.visitLabel(start);
						mv.visitVarInsn(Opcodes.ALOAD, 0);
						mv.visitVarInsn(Opcodes.ALOAD, 1);
						mv.visitVarInsn(Opcodes.ILOAD, 2);
						mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil",
								"clearInventory",
								"(Lnet/minecraft/entity/player/InventoryPlayer;Lnet/minecraft/item/Item;I)I", false);
						mv.visitInsn(Opcodes.IRETURN);
						Label end = new Label();
						mv.visitLabel(end);
						mv.visitLocalVariable("this", "Lnet/minecraft/entity/player/InventoryPlayer;", null, start, end,
								0);
						mv.visitLocalVariable("item", "Lnet/minecraft/item/Item;", null, start, end, 1);
						mv.visitLocalVariable("damage", "I", null, start, end, 2);
						mv.visitMaxs(3, 3);
						mv.visitEnd();
						return null;
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (name.equals("nh") || name.equals("net.minecraft.network.NetHandlerPlayServer")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					if (name.equals("c") && desc.equals("(Ljava/lang/String;)V")
							|| name.equals("kickPlayerFromServer")) {
						MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
						mv.visitCode();
						Label start = new Label();
						mv.visitLabel(start);
						mv.visitVarInsn(Opcodes.ALOAD, 0);
						mv.visitVarInsn(Opcodes.ALOAD, 1);
						mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil",
								"kickPlayerFromServer",
								"(Lnet/minecraft/network/NetHandlerPlayServer;Ljava/lang/String;)V", false);
						mv.visitInsn(Opcodes.RETURN);
						Label end = new Label();
						mv.visitLabel(end);
						mv.visitLocalVariable("this", "Lnet/minecraft/network/NetHandlerPlayServer;", null, start, end,
								0);
						mv.visitLocalVariable("message", "Ljava/lang/String;", null, start, end, 1);
						mv.visitMaxs(2, 2);
						mv.visitEnd();
						return null;
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (name.equals("ayq") || name.equals("net.minecraft.world.storage.SaveHandler")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
					if (name.equals("c")) {
						access = Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL;
					} else if (name.equals("a")) {
						access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL;
					}
					return cv.visitField(access, name, desc, signature, value);
				}

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					if (name.equals("b") && desc.equals("(Lyz;)Ldh;") || name.equals("readPlayerData")) {
						MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
						mv.visitCode();
						Label start = new Label();
						mv.visitLabel(start);
						mv.visitVarInsn(Opcodes.ALOAD, 0);
						mv.visitVarInsn(Opcodes.ALOAD, 1);
						mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil",
								"readPlayerData",
								"(Lnet/minecraft/world/storage/SaveHandler;Lnet/minecraft/entity/player/EntityPlayer;)"
										+ "Lnet/minecraft/nbt/NBTTagCompound;",
								false);
						mv.visitInsn(Opcodes.ARETURN);
						Label end = new Label();
						mv.visitLabel(end);
						mv.visitLocalVariable("this", "Lnet/minecraft/world/storage/SaveHandler;", null, start, end, 0);
						mv.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, start, end,
								1);
						mv.visitMaxs(2, 2);
						mv.visitEnd();
						return null;
					} else if (name.equals("a") && desc.equals("(Lyz;)V") || name.equals("writePlayerData")) {
						MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
						mv.visitCode();
						Label start = new Label();
						mv.visitLabel(start);
						mv.visitVarInsn(Opcodes.ALOAD, 0);
						mv.visitVarInsn(Opcodes.ALOAD, 1);
						mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil",
								"writePlayerData",
								"(Lnet/minecraft/world/storage/SaveHandler;Lnet/minecraft/entity/player/EntityPlayer;)V",
								false);
						mv.visitInsn(Opcodes.RETURN);
						Label end = new Label();
						mv.visitLabel(end);
						mv.visitLocalVariable("this", "Lnet/minecraft/world/storage/SaveHandler;", null, start, end, 0);
						mv.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, start, end,
								1);
						mv.visitMaxs(2, 2);
						mv.visitEnd();
						return null;
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (name.equals("oi") || name.equals("net.minecraft.server.management.ServerConfigurationManager")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					if (name.equals("a") && desc.equals("(Lmw;IZ)Lmw;") || name.equals("respawnPlayer")) {
						return new MethodVisitor(Opcodes.ASM4,
								cv.visitMethod(access, name, desc, signature, exceptions)) {

							public void visitCode() {
								mv.visitVarInsn(Opcodes.ALOAD, 1);
								mv.visitInsn(Opcodes.ICONST_0);
								mv.visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayerMP",
										"loliDead", "Z");
								mv.visitVarInsn(Opcodes.ALOAD, 1);
								mv.visitInsn(Opcodes.ICONST_0);
								mv.visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayerMP",
										"loliCool", "Z");
								mv.visitVarInsn(Opcodes.ALOAD, 1);
								mv.visitInsn(Opcodes.ICONST_0);
								mv.visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayerMP",
										"loliDeathTime", "I");
							};

						};
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (name.equals("yw") || name.equals("net.minecraft.entity.player.PlayerCapabilities")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
					if (name.equals("f") || name.equals("g")) {
						access = Opcodes.ACC_PUBLIC;
					}
					return cv.visitField(access, name, desc, signature, value);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (name.equals("t") || name.equals("net.minecraft.util.ChatAllowedCharacters")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					if (name.equals("a") && desc.equals("(C)Z") || name.equals("isAllowedCharacter")) {
						return new MethodVisitor(Opcodes.ASM4,
								cv.visitMethod(access, name, desc, signature, exceptions)) {

							public void visitIntInsn(int opcode, int operand) {
								if (opcode == Opcodes.SIPUSH && operand == 167) {
									operand = 127;
								}
								super.visitIntInsn(opcode, operand);
							};

						};
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		}
		return basicClass;
	}

}
