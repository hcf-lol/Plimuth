package lol.hcf.plimuth.agent.transformers;

import lol.hcf.agentmanager.transformer.ClassListTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class PermissibleBaseTransformer extends ClassListTransformer {

    private static final String PERMISSIBLE_DESCRIPTOR = "Lorg/bukkit/permissions/Permissible;";
    private static final List<String> REMOVE_FIELDS = Arrays.asList("attachments", "permissions");

    private static final List<String> EMPTY_METHODS = Arrays.asList("clearPermissions", "calculateChildPermissions");
    private static final List<String> IGNORE_METHODS = Arrays.asList("isOp", "setOp");

    public PermissibleBaseTransformer() {
        super("org/bukkit/permissions/PermissibleBase");
    }

    @Override
    protected void transform(ClassLoader loader, ClassNode node) {
        node.fields.removeIf(field -> PermissibleBaseTransformer.REMOVE_FIELDS.contains(field.name));

        node.fields.add(new FieldNode(ACC_PUBLIC, "childPermissible", PERMISSIBLE_DESCRIPTOR, null, null));

        node.methods.forEach((method) -> {
            if (method.name.equals("<init>") || IGNORE_METHODS.contains(method.name)) return;
            if (EMPTY_METHODS.contains(method.name)) {
                InsnList empty = new InsnList();
                empty.add(new InsnNode(RETURN));
                method.instructions = empty;
                return;
            }

            Type returnType = Type.getReturnType(method.desc);
            Type[] arguments = Type.getArgumentTypes(method.desc);

            InsnList list = new InsnList();

            LabelNode end = new LabelNode();
            if (method.name.equals("hasPermission")) {
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, node.name, "isOp", "()Z"));
                list.add(new JumpInsnNode(IFEQ, end));
                list.add(new InsnNode(ICONST_1));
                list.add(new InsnNode(IRETURN));
            }

            list.add(end);
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new FieldInsnNode(GETFIELD, node.name, "childPermissible", PERMISSIBLE_DESCRIPTOR));

            for (int i = 0; i < arguments.length; i++) {
                int opcode = arguments[i].getOpcode(ILOAD);
                list.add(new VarInsnNode(opcode, i + 1));
            }

            list.add(new MethodInsnNode(INVOKEINTERFACE, "org/bukkit/permissions/Permissible", method.name, method.desc, true));
            list.add(new InsnNode(returnType.getOpcode(IRETURN)));

            method.instructions = list;
        });

        MethodNode constructor = node.methods.stream().filter((method) -> method.name.equals("<init>")).findFirst().orElseThrow(RuntimeException::new);

        InsnList initField = new InsnList();

        initField.add(new VarInsnNode(ALOAD, 0));
        initField.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/Object", "<init>", "()V"));
        initField.add(new VarInsnNode(ALOAD, 0));
        initField.add(new VarInsnNode(ALOAD, 1));
        initField.add(new FieldInsnNode(PUTFIELD, node.name, "opable", "Lorg/bukkit/permissions/ServerOperator;"));

        initField.add(new VarInsnNode(ALOAD, 0));
        initField.add(new InsnNode(DUP));
        initField.add(new FieldInsnNode(PUTFIELD, node.name, "parent", PERMISSIBLE_DESCRIPTOR));

        LabelNode end = new LabelNode();

        initField.add(new VarInsnNode(ALOAD, 1));
        initField.add(new TypeInsnNode(INSTANCEOF, "org/bukkit/permissions/Permissible"));
        initField.add(new JumpInsnNode(IFEQ, end));

        initField.add(new VarInsnNode(ALOAD, 0));
        initField.add(new VarInsnNode(ALOAD, 1));
        initField.add(new TypeInsnNode(CHECKCAST, "org/bukkit/permissions/Permissible"));
        initField.add(new FieldInsnNode(PUTFIELD, node.name, "parent", PERMISSIBLE_DESCRIPTOR));

        initField.add(end);
        initField.add(new VarInsnNode(ALOAD, 0));
        initField.add(new InsnNode(ACONST_NULL));
        initField.add(new FieldInsnNode(PUTFIELD, node.name, "childPermissible", PERMISSIBLE_DESCRIPTOR));
        initField.add(new InsnNode(RETURN));

        constructor.instructions = initField;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            return super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
