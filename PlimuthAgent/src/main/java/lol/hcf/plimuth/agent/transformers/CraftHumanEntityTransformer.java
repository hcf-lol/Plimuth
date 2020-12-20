package lol.hcf.plimuth.agent.transformers;

import lol.hcf.agentmanager.transformer.PatternTransformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import static org.objectweb.asm.Opcodes.*;

public class CraftHumanEntityTransformer extends PatternTransformer {

    public CraftHumanEntityTransformer() {
        super("org\\/bukkit\\/craftbukkit\\/(.*)\\/CraftHumanEntity");
    }

    @Override
    protected void transform(ClassLoader loader, ClassNode node) {
        FieldNode fieldNode = node.fields.stream().filter((field) -> field.name.equals("perm")).findFirst().orElseThrow(RuntimeException::new);
        fieldNode.access = ACC_PUBLIC | ACC_FINAL;
    }
}
