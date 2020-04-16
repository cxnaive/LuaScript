package cxmc;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import cxmc.lua.*;
import cxmc.file.*;
import cxmc.h2.*;
/**
 * Unit test for simple App.
 */
public class LuaTest
{
    class TestClass implements PluginDef{
        private LuaLoader lualoader;
        private LuaRunner luarunner;
        private FileLoader fileLoader;
        private H2Manager h2Manager;
        @Override
        public LuaRunner getLuaRunner() {
            return luarunner;
        }

        @Override
        public LuaLoader getLuaLoader() {
            return lualoader;
        }

        @Override
        public H2Manager getH2Manager() {
            return h2Manager;
        }
        @Override
        public FileLoader getFileLoader(){
            return fileLoader;
        }
        public void init(){
            lualoader = new LuaLoader(this);
            fileLoader = new FileLoader("script");
            luarunner = new LuaRunner();
            h2Manager = new H2Manager("./H2/H2", "luascript", "luascript");
            h2Manager.TryConnect();
        }
        public void test(){

        }
        public void end(){
            lualoader.close();
            luarunner.close();
            h2Manager.CloseConnect();
        }
    }
    /**
     * Rigorous Test :-)
     */

    @Test
    public void shouldAnswerWithTrue()
    {
        TestClass now = new TestClass();
        now.init();
        now.test();
        now.end();
        assertTrue( true );
    }
}
