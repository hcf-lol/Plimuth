package lol.hcf.plimuth.agent.generator;

import lol.hcf.agentmanager.generator.ClassGenerator;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class PlayerPermissionConsumerGenerator extends ClassGenerator {

    private static final String CRAFT_BUKKIT = "org/bukkit/craftbukkit/v1_8_R3/";

    private static final String CRAFT_HUMAN_ENTITY_DESCRIPTOR = 'L' + CRAFT_BUKKIT + "entity/CraftHumanEntity" + ';';

    private static final String PERMISSIBLE_DESCRIPTOR = "Lorg/bukkit/permissions/Permissible;";
    private static final String PERMISSIBLE_BASE_DESCRIPTOR = "Lorg/bukkit/permissions/PermissibleBase;";

    public PlayerPermissionConsumerGenerator() {
        super("org/bukkit/permissions/PermissibleBase$Consumer", ACC_PUBLIC);

        super.createDefaultConstructor(ACC_PUBLIC, "()V");
        super.node.superName = "java/lang/Object";
        super.node.signature = "Ljava/lang/Object;Ljava/util/function/BiConsumer<" + CRAFT_HUMAN_ENTITY_DESCRIPTOR + PERMISSIBLE_DESCRIPTOR + ">;";
        super.node.interfaces.add("java/util/function/BiConsumer");

        MethodNode constructor = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.instructions.add(new VarInsnNode(ALOAD, 0));
        constructor.instructions.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/Object", "<init>", "()V"));
        constructor.instructions.add(new InsnNode(RETURN));

        MethodNode accept = new MethodNode(ACC_PUBLIC, "accept", "(" + CRAFT_HUMAN_ENTITY_DESCRIPTOR + PERMISSIBLE_DESCRIPTOR + ")V", null, null);
        InsnList list = accept.instructions;

        list.add(new VarInsnNode(ALOAD, 1));
        list.add(new FieldInsnNode(GETFIELD, CRAFT_BUKKIT + "entity/CraftHumanEntity", "perm", PERMISSIBLE_BASE_DESCRIPTOR));
        list.add(new VarInsnNode(ALOAD, 2));
        list.add(new FieldInsnNode(PUTFIELD, "org/bukkit/permissions/PermissibleBase", "childPermissible", PERMISSIBLE_DESCRIPTOR));
        list.add(new InsnNode(RETURN));

        MethodNode bridge = new MethodNode(ACC_PUBLIC | ACC_BRIDGE | ACC_SYNTHETIC, "accept", "(Ljava/lang/Object;Ljava/lang/Object;)V", null, null);
        list = bridge.instructions;
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new VarInsnNode(ALOAD, 1));
        list.add(new TypeInsnNode(CHECKCAST, CRAFT_BUKKIT + "entity/CraftHumanEntity"));
        list.add(new VarInsnNode(ALOAD, 2));
        list.add(new TypeInsnNode(CHECKCAST, "org/bukkit/permissions/Permissible"));
        list.add(new MethodInsnNode(INVOKEVIRTUAL, super.node.name, accept.name, accept.desc));
        list.add(new InsnNode(RETURN));

        super.node.methods.add(constructor);
        super.node.methods.add(accept);
        super.node.methods.add(bridge);
    }

}
