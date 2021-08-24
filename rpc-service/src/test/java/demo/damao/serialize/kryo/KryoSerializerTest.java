package demo.damao.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhanggeng
 * @date 2021/8/23 14:14
 */
class KryoSerializerTest {

    private static int i = 0;
    private ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(()->{
        Integer integer = i++;
        return integer;
    });

    @Test
    void serialize() {
        System.out.println(threadLocal.get());
        System.out.println(threadLocal.get());
        threadLocal.remove();
        System.out.println(threadLocal.get());
        threadLocal.remove();
    }
}