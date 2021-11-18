package com.tmser.selenium.tools;

import com.google.common.collect.Lists;

import java.awt.*;
import static  java.awt.event.KeyEvent.*;

import java.awt.event.KeyEvent;
import java.util.List;

public class KeyParse {

    public static List<Integer> parseCode(String keys){
        char[] chars = keys.toCharArray();
        List<Integer> keyCodes = Lists.newArrayList();
        for (char c: chars) {
            if (c >= VK_0 && c <= VK_9){
                keyCodes.add(c-0);
                continue;
            }

            if (c >= 'a' && c <= 'z') {
                keyCodes.add(c-32);
                continue;
            }

            if (c >= VK_A && c <= VK_Z) {
                keyCodes.add(KeyEvent.VK_SHIFT);
                keyCodes.add(c-0);
                continue;
            }

            switch (c){
                case '\n': keyCodes.add(VK_ENTER); break;
                case ' ': keyCodes.add(VK_SPACE); break;
                case 17: keyCodes.add(VK_CONTROL); break;
                case 0x74: keyCodes.add(VK_F5); break;

                // punctuation
                case '<': keyCodes.add(VK_SHIFT);
                case ',': keyCodes.add(VK_COMMA); break;

                case '>': keyCodes.add(VK_SHIFT);
                case '.': keyCodes.add(VK_PERIOD); break;

                case '?': keyCodes.add(VK_SHIFT);
                case '/': keyCodes.add(VK_SLASH); break;

                case ':': keyCodes.add(VK_SHIFT);
                case ';': keyCodes.add(VK_SEMICOLON); break;

                case '=': keyCodes.add(VK_EQUALS); break;

                case '{': keyCodes.add(VK_SHIFT);
                case '[': keyCodes.add(VK_OPEN_BRACKET); break;

                case '|': keyCodes.add(VK_SHIFT);
                case '\\':keyCodes.add(VK_BACK_SLASH); break;

                case '}': keyCodes.add(VK_SHIFT);
                case ']': keyCodes.add(VK_CLOSE_BRACKET); break;
            }
        }

        return keyCodes;
    }
}
