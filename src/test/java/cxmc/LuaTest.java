package cxmc;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.md_5.bungee.api.ChatColor;
/**
 * Unit test for simple App.
 */
public class LuaTest
{
    /**
     * Rigorous Test :-)
     */

    @Test
    public void shouldAnswerWithTrue()
    {
        for(ChatColor color: ChatColor.class.getEnumConstants()){
            System.out.println(color.getName());
        }
        assertTrue( true );
    }
}
