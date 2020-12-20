package lol.hcf.plimuth.agent;

import lol.hcf.plimuth.agent.generator.PlayerPermissionConsumerGenerator;
import lol.hcf.plimuth.agent.transformers.CraftHumanEntityTransformer;
import lol.hcf.plimuth.agent.transformers.PermissibleBaseTransformer;

import java.lang.instrument.Instrumentation;
import java.util.function.BiConsumer;

public class PlimuthAgent {
    private static BiConsumer<Object, Object> consumer;

    @SuppressWarnings("unchecked")
    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new PermissibleBaseTransformer());
        instrumentation.addTransformer(new CraftHumanEntityTransformer());

        Class<?> consumerClass = new PlayerPermissionConsumerGenerator().defineClass(PlimuthAgent.class.getClassLoader());
        try {
            PlimuthAgent.consumer = (BiConsumer<Object, Object>) consumerClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BiConsumer<Object, Object> getConsumer() {
        return consumer;
    }
}
