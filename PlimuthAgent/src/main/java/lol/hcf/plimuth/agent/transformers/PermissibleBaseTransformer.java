package lol.hcf.plimuth.agent.transformers;

import lol.hcf.agentmanager.transformer.MethodTransformer;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class PermissibleBaseTransformer extends MethodTransformer {

    @Override
    protected void transform(ClassNode owner, MethodNode node) {
        switch (node.name) {
            case "isPermissionSet":
                this.transformConditionalFixed(node, true);

        }
    }

    private void transformForwardMethod(ClassNode owner, MethodNode node, String interfaceName, String fieldDescriptor, String fieldName) {
        InsnList list = new InsnList();
        list.add(new FieldInsnNode(GETFIELD, owner.name, fieldName, fieldDescriptor));
        list.add(new MethodInsnNode(INVOKEINTERFACE, interfaceName, node.name, node.desc, true));
        list.add(new InsnNode(Type.getType(node.desc.substring(node.desc.lastIndexOf(')') + 1)).getOpcode(IRETURN)));
    }

    private void transformConditionalFixed(MethodNode node, boolean value) {
        InsnList list = new InsnList();
        list.add(new InsnNode(value ? ICONST_1 : ICONST_0));
        list.add(new InsnNode(IRETURN));
        node.instructions = list;
    }


    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!className.equals("org/bukkit/permissions/PermissibleBase")) return null;
        return super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
    }
}
