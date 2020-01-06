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

	private boolean inTransform = false;

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformedName.equals("net.minecraftforge.common.ForgeHooks")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					if (name.equals("onLivingDeath")) {
						MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
						mv.visitCode();
						Label start = new Label();
						mv.visitLabel(start);
						mv.visitVarInsn(Opcodes.ALOAD, 0);
						mv.visitVarInsn(Opcodes.ALOAD, 1);
						mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "onLivingDeath", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/util/DamageSource;)Z", false);
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
						mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "onLivingUpdate", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false);
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
		} else if (transformedName.equals("net.minecraft.entity.EntityLivingBase")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
					if (name.equals("aT") || name.equals("aE")) {
						access = Opcodes.ACC_PUBLIC;
					}
					return cv.visitField(access, name, desc, signature, value);
				}

				@Override
				public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
					super.visit(version, access, name, signature, superName, interfaces);
					if (!LoliPickaxeCore.debug) {
						FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC, "loliDead", "Z", null, null);
						fv.visitEnd();
						fv = cv.visitField(Opcodes.ACC_PUBLIC, "loliDeathTime", "I", null, null);
						fv.visitEnd();
						fv = cv.visitField(Opcodes.ACC_PUBLIC, "loliCool", "Z", null, null);
						fv.visitEnd();
					}
					MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, LoliPickaxeCore.debug ? "getHealth" : "cd", "()F", null, null);
					mv.visitCode();
					Label start = new Label();
					mv.visitLabel(start);
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "getHealth", "(Lnet/minecraft/entity/EntityLivingBase;)F", false);
					mv.visitInsn(Opcodes.FRETURN);
					Label end = new Label();
					mv.visitLabel(end);
					mv.visitLocalVariable("this", "Lnet/minecraft/entity/EntityLivingBase;", null, start, end, 0);
					mv.visitMaxs(1, 1);
					mv.visitEnd();
					mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, LoliPickaxeCore.debug ? "getMaxHealth" : "cj", "()F", null, null);
					mv.visitCode();
					start = new Label();
					mv.visitLabel(start);
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "getMaxHealth", "(Lnet/minecraft/entity/EntityLivingBase;)F", false);
					mv.visitInsn(Opcodes.FRETURN);
					end = new Label();
					mv.visitLabel(end);
					mv.visitLocalVariable("this", "Lnet/minecraft/entity/EntityLivingBase;", null, start, end, 0);
					mv.visitMaxs(1, 1);
					mv.visitEnd();
				}

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					if (name.equals("cd") && desc.equals("()F") || name.equals("getHealth")) {
						return cv.visitMethod(access, "getHealth2", desc, signature, exceptions);
					} else if (name.equals("cj") && desc.equals("()F") || name.equals("getMaxHealth")) {
						return cv.visitMethod(access, "getMaxHealth2", desc, signature, exceptions);
					} else if (name.equals("getHealth2") || name.equals("getMaxHealth2")) {
						return null;
					} else if (name.equals("B_") && desc.equals("()V") || name.equals("onUpdate")) {
						return new MethodVisitor(Opcodes.ASM4, cv.visitMethod(access, name, desc, signature, exceptions)) {

							public void visitInsn(int opcode) {
								if (opcode == Opcodes.RETURN) {
									mv.visitVarInsn(Opcodes.ALOAD, 0);
									mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "onUpdate", "(Lnet/minecraft/entity/EntityLivingBase;)V", false);
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
		} else if (transformedName.equals("net.minecraft.entity.Entity")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
					if (name.equals("aF")) {
						access = Opcodes.ACC_PUBLIC;
					}
					return cv.visitField(access, name, desc, signature, value);
				}

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					if (name.equals("a") && desc.equals("(DF)Lbhc;") || name.equals("rayTrace")) {
						MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
						mv.visitCode();
						Label start = new Label();
						mv.visitLabel(start);
						mv.visitVarInsn(Opcodes.ALOAD, 0);
						mv.visitVarInsn(Opcodes.DLOAD, 1);
						mv.visitVarInsn(Opcodes.FLOAD, 3);
						mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "rayTrace", "(Lnet/minecraft/entity/Entity;DF)Lnet/minecraft/util/math/RayTraceResult;", false);
						mv.visitInsn(Opcodes.ARETURN);
						Label end = new Label();
						mv.visitLabel(end);
						mv.visitLocalVariable("this", "Lnet/minecraft/entity/Entity;", null, start, end, 0);
						mv.visitLocalVariable("blockReachDistance", "D", null, start, end, 1);
						mv.visitLocalVariable("partialTicks", "F", null, start, end, 3);
						mv.visitMaxs(4, 4);
						mv.visitEnd();
						return null;
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (transformedName.equals("net.minecraft.entity.player.EntityPlayer")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
					super.visit(version, access, name, signature, superName, interfaces);
					if (!LoliPickaxeCore.debug) {
						FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC, "hodeLoli", "I", null, null);
						fv.visitEnd();
					}
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (transformedName.equals("net.minecraft.entity.player.InventoryPlayer")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
					super.visit(version, access, name, signature, superName, interfaces);
					MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, LoliPickaxeCore.debug ? "dropAllItems" : "o", "()V", null, null);
					mv.visitCode();
					Label start = new Label();
					mv.visitLabel(start);
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "dropAllItems", "(Lnet/minecraft/entity/player/InventoryPlayer;)V", false);
					mv.visitInsn(Opcodes.RETURN);
					Label end = new Label();
					mv.visitLabel(end);
					mv.visitLocalVariable("this", "Lnet/minecraft/entity/player/InventoryPlayer;", null, start, end, 0);
					mv.visitMaxs(1, 1);
					mv.visitEnd();
					mv = cv.visitMethod(Opcodes.ACC_PUBLIC, LoliPickaxeCore.debug ? "clearMatchingItems" : "a", LoliPickaxeCore.debug ? "(Lnet/minecraft/item/Item;IILnet/minecraft/nbt/NBTTagCompound;)I" : "(Lain;IILfy;)I", null, null);
					mv.visitCode();
					start = new Label();
					mv.visitLabel(start);
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitVarInsn(Opcodes.ALOAD, 1);
					mv.visitVarInsn(Opcodes.ILOAD, 2);
					mv.visitVarInsn(Opcodes.ILOAD, 3);
					mv.visitVarInsn(Opcodes.ALOAD, 4);
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "clearMatchingItems", "(Lnet/minecraft/entity/player/InventoryPlayer;Lnet/minecraft/item/Item;IILnet/minecraft/nbt/NBTTagCompound;)I", false);
					mv.visitInsn(Opcodes.IRETURN);
					end = new Label();
					mv.visitLabel(end);
					mv.visitLocalVariable("this", "Lnet/minecraft/entity/player/InventoryPlayer;", null, start, end, 0);
					mv.visitLocalVariable("item", "Lnet/minecraft/item/Item;", null, start, end, 1);
					mv.visitLocalVariable("metadata", "I", null, start, end, 2);
					mv.visitLocalVariable("removeCount", "I", null, start, end, 3);
					mv.visitLocalVariable("itemNBT", "Lnet/minecraft/nbt/NBTTagCompound;", null, start, end, 4);
					mv.visitMaxs(5, 5);
					mv.visitEnd();
				}

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					if (name.equals("o") && desc.equals("()V") || name.equals("dropAllItems")) {
						return cv.visitMethod(access, "dropAllItems2", desc, signature, exceptions);
					} else if (name.equals("a") && desc.equals("(Lain;IILfy;)I") || name.equals("clearMatchingItems")) {
						return cv.visitMethod(access, "clearMatchingItems2", desc, signature, exceptions);
					} else if (name.equals("dropAllItems2") || name.equals("clearMatchingItems2")) {
						return null;
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (transformedName.equals("net.minecraft.network.NetHandlerPlayServer")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
					super.visit(version, access, name, signature, superName, interfaces);
					MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, LoliPickaxeCore.debug ? "disconnect" : "b", LoliPickaxeCore.debug ? "(Lnet/minecraft/util/text/ITextComponent;)V" : "(Lhh;)V", null, null);
					mv.visitCode();
					Label start = new Label();
					mv.visitLabel(start);
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitVarInsn(Opcodes.ALOAD, 1);
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "disconnect", "(Lnet/minecraft/network/NetHandlerPlayServer;Lnet/minecraft/util/text/ITextComponent;)V", false);
					mv.visitInsn(Opcodes.RETURN);
					Label end = new Label();
					mv.visitLabel(end);
					mv.visitLocalVariable("this", "Lnet/minecraft/network/NetHandlerPlayServer;", null, start, end, 0);
					mv.visitLocalVariable("message", "Lnet/minecraft/util/text/ITextComponent;", null, start, end, 1);
					mv.visitMaxs(2, 2);
					mv.visitEnd();
				}

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					if (name.equals("b") && desc.equals("(Lhh;)V") || name.equals("disconnect")) {
						return cv.visitMethod(access, "disconnect2", desc, signature, exceptions);
					} else if (name.equals("disconnect2")) {
						return null;
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (transformedName.equals("net.minecraft.world.storage.SaveHandler")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
					super.visit(version, access, name, signature, superName, interfaces);
					MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, LoliPickaxeCore.debug ? "readPlayerData" : "b", LoliPickaxeCore.debug ? "(Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/nbt/NBTTagCompound;" : "(Laed;)Lfy;", null, null);
					mv.visitCode();
					Label start = new Label();
					mv.visitLabel(start);
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitVarInsn(Opcodes.ALOAD, 1);
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "readPlayerData", "(Lnet/minecraft/world/storage/SaveHandler;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/nbt/NBTTagCompound;", false);
					mv.visitInsn(Opcodes.ARETURN);
					Label end = new Label();
					mv.visitLabel(end);
					mv.visitLocalVariable("this", "Lnet/minecraft/world/storage/SaveHandler;", null, start, end, 0);
					mv.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, start, end, 1);
					mv.visitMaxs(2, 2);
					mv.visitEnd();
					mv = cv.visitMethod(Opcodes.ACC_PUBLIC, LoliPickaxeCore.debug ? "writePlayerData" : "a", LoliPickaxeCore.debug ? "(Lnet/minecraft/entity/player/EntityPlayer;)V" : "(Laed;)V", null, null);
					mv.visitCode();
					start = new Label();
					mv.visitLabel(start);
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitVarInsn(Opcodes.ALOAD, 1);
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "writePlayerData", "(Lnet/minecraft/world/storage/SaveHandler;Lnet/minecraft/entity/player/EntityPlayer;)V", false);
					mv.visitInsn(Opcodes.RETURN);
					end = new Label();
					mv.visitLabel(end);
					mv.visitLocalVariable("this", "Lnet/minecraft/world/storage/SaveHandler;", null, start, end, 0);
					mv.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, start, end, 1);
					mv.visitMaxs(2, 2);
					mv.visitEnd();
				}

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					if (name.equals("b") && desc.equals("(Laed;)Lfy;") || name.equals("readPlayerData")) {
						return cv.visitMethod(access, "readPlayerData2", desc, signature, exceptions);
					} else if (name.equals("a") && desc.equals("(Laed;)V") || name.equals("writePlayerData")) {
						return cv.visitMethod(access, "writePlayerData2", desc, signature, exceptions);
					} else if (name.equals("readPlayerData2") || name.equals("writePlayerData2")) {
						return null;
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (transformedName.equals("net.minecraft.server.management.PlayerList")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					if (name.equals("a") && desc.equals("(Loq;IZ)Loq;") || name.equals("recreatePlayerEntity")) {
						return new MethodVisitor(Opcodes.ASM4, cv.visitMethod(access, name, desc, signature, exceptions)) {

							public void visitCode() {
								mv.visitVarInsn(Opcodes.ALOAD, 1);
								mv.visitInsn(Opcodes.ICONST_0);
								mv.visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayerMP", "loliDead", "Z");
								mv.visitVarInsn(Opcodes.ALOAD, 1);
								mv.visitInsn(Opcodes.ICONST_0);
								mv.visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayerMP", "loliCool", "Z");
								mv.visitVarInsn(Opcodes.ALOAD, 1);
								mv.visitInsn(Opcodes.ICONST_0);
								mv.visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayerMP", "loliDeathTime", "I");
							};

						};
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (transformedName.equals("net.minecraft.entity.player.PlayerCapabilities")) {
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
		} else if (transformedName.equals("net.minecraft.util.ChatAllowedCharacters")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					if (name.equals("a") && desc.equals("(C)Z") || name.equals("isAllowedCharacter")) {
						return new MethodVisitor(Opcodes.ASM4, cv.visitMethod(access, name, desc, signature, exceptions)) {

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
		} else if (transformedName.equals("net.minecraft.world.World")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					if (name.equals("k") && desc.equals("()V") || name.equals("updateEntities")) {
						return new MethodVisitor(Opcodes.ASM4, cv.visitMethod(access, name, desc, signature, exceptions)) {

							public void visitCode() {
								mv.visitVarInsn(Opcodes.ALOAD, 0);
								mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "updateEntities", "(Lnet/minecraft/world/World;)V", false);
							};

						};
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (transformedName.equals("net.minecraft.client.multiplayer.WorldClient")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					if (name.equals("e") && desc.equals("(I)Lvg;") || name.equals("removeEntityFromWorld")) {
						return new MethodVisitor(Opcodes.ASM4, cv.visitMethod(access, name, desc, signature, exceptions)) {

							public void visitCode() {
								mv.visitVarInsn(Opcodes.ALOAD, 0);
								mv.visitVarInsn(Opcodes.ILOAD, 1);
								mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "removeEntityFromWorld", "(Lnet/minecraft/client/multiplayer/WorldClient;I)V", false);
							};

						};
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (transformedName.equals("net.minecraft.block.Block")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
					super.visit(version, access, name, signature, superName, interfaces);
					if (!LoliPickaxeCore.debug) {
						MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "getLoliSilkTouchDrop", "(Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/item/ItemStack;", null, null);
						mv.visitCode();
						Label start = new Label();
						mv.visitLabel(start);
						mv.visitVarInsn(Opcodes.ALOAD, 0);
						mv.visitVarInsn(Opcodes.ALOAD, 1);
						mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "aow", "u", "(Lawt;)Laip;", false);
						mv.visitInsn(Opcodes.ARETURN);
						Label end = new Label();
						mv.visitLabel(end);
						mv.visitLocalVariable("this", "Lnet/minecraft/block/Block;", null, start, end, 0);
						mv.visitLocalVariable("message", "Lnet/minecraft/block/state/IBlockState;", null, start, end, 1);
						mv.visitMaxs(2, 2);
						mv.visitEnd();
					}
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (transformedName.equals("net.minecraft.client.Minecraft")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
					if (name.equals("aS")) {
						access = Opcodes.ACC_PUBLIC;
					}
					return cv.visitField(access, name, desc, signature, value);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (transformedName.equals("net.minecraft.entity.player.EntityPlayerMP")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
					super.visit(version, access, name, signature, superName, interfaces);
					MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, LoliPickaxeCore.debug ? "sendAllContents" : "a", LoliPickaxeCore.debug ? "(Lnet/minecraft/inventory/Container;Lnet/minecraft/util/NonNullList;)V" : "(Lafr;Lfi;)V", null, null);
					mv.visitCode();
					Label start = new Label();
					mv.visitLabel(start);
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitVarInsn(Opcodes.ALOAD, 1);
					mv.visitVarInsn(Opcodes.ALOAD, 2);
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/anotherstar/core/util/EventUtil", "sendAllContents", "(Lnet/minecraft/entity/player/EntityPlayerMP;Lnet/minecraft/inventory/Container;Lnet/minecraft/util/NonNullList;)V", false);
					mv.visitInsn(Opcodes.RETURN);
					Label end = new Label();
					mv.visitLabel(end);
					mv.visitLocalVariable("this", "Lnet/minecraft/entity/player/EntityPlayerMP;", null, start, end, 0);
					mv.visitLocalVariable("container", "Lnet/minecraft/inventory/Container;", null, start, end, 1);
					mv.visitLocalVariable("stackList", "Lnet/minecraft/util/NonNullList;", null, start, end, 2);
					mv.visitMaxs(3, 3);
					mv.visitEnd();
				}

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					if (name.equals("a") && desc.equals("(Lafr;Lfi;)V") || name.equals("sendAllContents")) {
						return cv.visitMethod(access, "sendAllContents2", desc, signature, exceptions);
					} else if (name.equals("sendAllContents2")) {
						return null;
					}
					return cv.visitMethod(access, name, desc, signature, exceptions);
				}

			};
			classReader.accept(classVisitor, Opcodes.ASM4);
			return classWriter.toByteArray();
		} else if (transformedName.equals("net.minecraft.client.renderer.entity.RenderManager")) {
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
					if (name.equals("<init>")) {
						return new MethodVisitor(Opcodes.ASM4, cv.visitMethod(access, name, desc, signature, exceptions)) {

							public void visitTypeInsn(int opcode, String type) {
								if (opcode == Opcodes.NEW && (type.equals("net/minecraft/client/renderer/entity/RenderItemFrame") || type.equals("bzv"))) {
									type = "com/anotherstar/client/render/RenderLoliCardFrame";
								}
								super.visitTypeInsn(opcode, type);
							}

							public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
								if ((owner.equals("net/minecraft/client/renderer/entity/RenderItemFrame") || owner.equals("bzv")) && name.equals("<init>")) {
									owner = "com/anotherstar/client/render/RenderLoliCardFrame";
								}
								super.visitMethodInsn(opcode, owner, name, desc, itf);
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
