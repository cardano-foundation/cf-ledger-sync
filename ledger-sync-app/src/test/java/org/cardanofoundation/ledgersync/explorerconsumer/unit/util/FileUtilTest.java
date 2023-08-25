package org.cardanofoundation.ledgersync.explorerconsumer.unit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

import org.cardanofoundation.ledgersync.explorerconsumer.util.FileUtil;


public class FileUtilTest {

    @Test
    public void NoSuchFileOrDirectory() {

        Assertions.assertFalse(FileUtil.readFile("noPath").contains("FakeText"));
        Assertions.assertTrue(FileUtil.readFile("noPath").contains(""));
    }

    @Test
    public void FileOrDirectory() {

        String typeToCheck = "ByronMainBlock";
        String hashToCheck = "820258a358a1010000332323232322225333004332323002233002002001230022330020020012253335573e002294054ccc018cdd798040008028a51130023007001323758600e64600e600e600e600e600e600e600e600e0026010002600c600e00229210e4964656e74697479496e666f4c5400165734ae895d0918011baa0015573c98011e581c04130a76da46e6f49f13f9782b0a845ec010197353b803ffdf40a40c0001";
        //@Todo: Dynamic source
        String url = "src/test/resources/blocks/test_blocks.json";
        Assertions.assertTrue(FileUtil.readFile(url).contains(typeToCheck));
        Assertions.assertTrue(FileUtil.readFile(url).contains(hashToCheck));
    }

    @Test
    public void NoUrl() {

        Assertions.assertThrows(NullPointerException.class, () -> {
            FileUtil.readFile(null);
        });
    }
}
