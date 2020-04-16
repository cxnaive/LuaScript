require 'scriptmc'
function Run()
    local cnt = 0;
    while true do
        scriptmc.servermsg("Running "..cnt.."!");
        scriptmc.sleep(1000);
        cnt = cnt + 1;
    end
end