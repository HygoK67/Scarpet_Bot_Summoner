# Scarpet_Bot_Summoner
A scarpet script that helps server admins for easier carpet bot management

1. Use `/bot summon bot_name` and `/bot kick bot_name` for basic summon/kick
3. Also, players can `/bot attack <bot_name> <interval>` and `/bot use <bot_name> <interval>`  
`<interval>` is a interger greater than `-1` indicating the tick interval that the bot attack/use  
`-1` means `continuous`, `0` means `once`
3. Now players can save and load their bots using `/bot savebot bot_name` and `/bot load bot_name`

Make sure the following rules are properly set:  
`CommandPlayer` is set to `ops`  
`ScriptsAutoload` is set to `True`  
`allowSpawningOfflinePlayers` is set to `True`  

You can change `global_spawning_limit`, `global_summon_prefix`, `global_saving_limit` at the top of the script file

Enjoy!
