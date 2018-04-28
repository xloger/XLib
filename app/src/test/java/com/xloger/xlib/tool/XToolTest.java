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
        assertTrue(XTool.isBlankString(null));
        assertTrue(XTool.isBlankString(""));
        assertTrue(XTool.isBlankString("    "));
        assertTrue(XTool.isBlankString("            "));
        assertFalse(XTool.isBlankString("23"));
        assertFalse(XTool.isBlankString("  2 3 "));
        assertFalse(XTool.isBlankString("    2  3 "));
    }

    @Test
    public void randomInt() throws Exception {
        assertEquals(1,XTool.randomInt(1));
        boolean isEqualMax = false;
        for (int i = 0; i < 100; i++) {
            if (5==XTool.randomInt(5)){
                isEqualMax=true;
            }
        }
        assertTrue(isEqualMax);
    }

}