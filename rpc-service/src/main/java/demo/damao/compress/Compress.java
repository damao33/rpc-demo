package demo.damao.compress;

import demo.damao.extension.SPI;

/**
 * @author zhanggeng
 * @date 2021/8/18 13:14
 */
@SPI
public interface Compress {

    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
