package com.xloger.xlib.tool;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 2017/7/30 16:24.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */
public class XToolTest {
    @Test
    public void isBlankString() throws Exception {
        assertTrue(XTool.INSTANCE.isBlankString(null));
        assertTrue(XTool.INSTANCE.isBlankString(""));
        assertTrue(XTool.INSTANCE.isBlankString("    "));
        assertTrue(XTool.INSTANCE.isBlankString("            "));
        assertFalse(XTool.INSTANCE.isBlankString("23"));
        assertFalse(XTool.INSTANCE.isBlankString("  2 3 "));
        assertFalse(XTool.INSTANCE.isBlankString("    2  3 "));
    }

    @Test
    public void randomInt() throws Exception {
        assertEquals(1, XTool.INSTANCE.randomInt(1));
        boolean isEqualMax = false;
        for (int i = 0; i < 100; i++) {
            if (5== XTool.INSTANCE.randomInt(5)){
                isEqualMax=true;
            }
        }
        assertTrue(isEqualMax);
    }

}