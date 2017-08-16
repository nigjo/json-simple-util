/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  de.nigjo.json.util.JSONMappingManager
 *  org.junit.Assert
 *  org.junit.Test
 */
package de.nigjo.json.util.test;

import de.nigjo.json.util.JSONMappingManager;
import de.nigjo.json.util.test.data.AutoGuiDefinition;
import de.nigjo.json.util.test.data.TestDataGenerator;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Test;

public class JSONMappingManagerTest {
    public static void setDebugMode() throws Exception {
        Field debug = JSONMappingManager.class.getDeclaredField("debug");
        debug.setAccessible(true);
        debug.set(null, true);
    }

    @Test
    public void testParse_Path_Class() throws Exception {
        AutoGuiDefinition expResult = TestDataGenerator.createExpectedDemoResult();
        URL resource = JSONMappingManagerTest.class.getResource("demo.autogui");
        Path input = Paths.get(resource.toURI());
        Object result = JSONMappingManager.parse((Path)input, AutoGuiDefinition.class);
        Assert.assertEquals((Object)expResult, (Object)result);
    }

    @Test
    public void testParse_Reader_Class() throws Exception {
        AutoGuiDefinition expResult = TestDataGenerator.createExpectedDemoResult();
        InputStreamReader in = new InputStreamReader(JSONMappingManagerTest.class.getResourceAsStream("demo.autogui"), StandardCharsets.UTF_8);
        Throwable throwable = null;
        try {
            Object result = JSONMappingManager.parse((Reader)in, AutoGuiDefinition.class);
            Assert.assertEquals((Object)expResult, (Object)result);
        }
        catch (Throwable result) {
            throwable = result;
            throw result;
        }
        finally {
            if (in != null) {
                if (throwable != null) {
                    try {
                        in.close();
                    }
                    catch (Throwable result) {
                        throwable.addSuppressed(result);
                    }
                } else {
                    in.close();
                }
            }
        }
    }
}

