# Scarpet_Bot_Summoner
A scarpet script that helps server admins to limit the bot numbers each player can spawn

Use `/bot summon bot_name` and `/bot kick bot_name`

2022.1.23 Update:  
Now `/bot attack bot_name interval` and `/bot use bot_name interval` is supported  
`interval` is a interger greater than `-1` indicating the tick interval that the bot attack/use  
`-1` means `continuous`, `0` means `once`, 

Make sure the following rules are properly set:  
`CommandPlayer` is set to `ops`  
`ScriptsAutoload` is set to `True`  
`allowSpawningOfflinePlayers` is set to `True`  

You can change `global_spawning_limit` and `global_summon_prefix` at the begining of the script file

Enjoy!
