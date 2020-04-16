require 'scriptmc'
function Run(Jmap)
    scriptmc.servermsg("Loaded scriptmc!");
    print(scriptmc.time());
    print("LUA!");
    if(not Jmap:containsKey("cnt"))
    then
        Jmap:put("cnt",0);
    end
    local cnt = Jmap:get("cnt");
    print("LUA:cnt "..cnt);
    Jmap:put("cnt",cnt+1);
end