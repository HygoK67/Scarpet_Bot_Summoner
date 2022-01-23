global_spawning_limit = 3;
global_json_name_1 = 'bot_summoned_num_dict';
global_json_name_2 = 'summoned_by';
global_summon_prefix = 'bot_';

__config() -> {
	'stay_loaded' -> 'true',
	'commands' -> {
		'summon <bot_name>' -> 'summon',
		'kick <bot_name>' -> 'kick',
		'attack <bot_name> <interval>' -> 'attack',
		'use <bot_name> <interval>' -> 'use',
	},
	'arguments' -> {
		'bot_name' -> {'type' -> 'term','suggest' -> []},
		'interval' -> {'type' -> 'int','suggest' -> [-1,0,11]},
	},
	'scope' -> 'global',
};


__on_start() -> {
	write_file(global_json_name_1,'shared_json',{});
	write_file(global_json_name_2,'shared_json',{});
};


__command() -> {
	
};

summon(originalbotname) -> (
	botname = lower(global_summon_prefix + originalbotname);
	p = player();
	player_name = p ~ 'name';
	summoned_by_list = decode_json(read_file(global_json_name_2,'shared_json'));
	summoned_by = get(summoned_by_list,botname);
	if
	(
		//如果该bot没有被任何人生成
		summoned_by == null,
		(
			spawned_list = decode_json(read_file(global_json_name_1,'shared_json'));
			spawned_player_num = get(spawned_list,player_name);
			if
			(
				//该玩家未生成任何bot
				spawned_player_num == null,
				(
					display_title(player_name, 'clear');
					display_title(player_name, 'actionbar', format('d You have summoned 1 Bots now'), 5,5,5);
					put(spawned_list,player_name,1);
					put(summoned_by_list,botname,player_name);
					run(str('player %s spawn in survival',botname));
					run(str('tp %s %s',botname,player_name));
					write_file(global_json_name_1,'shared_json',spawned_list);
					write_file(global_json_name_2,'shared_json',summoned_by_list);
				),
				//该玩家生成的bot量未达到上限
				spawned_player_num < global_spawning_limit,
				(
					display_title(player_name, 'clear');
					display_title(player_name, 'actionbar', format('d '+str('You have summoned %d Bots now',spawned_player_num+1)), 5,5,5);
					put(spawned_list,player_name,spawned_player_num+1);
					put(summoned_by_list,botname,player_name);
					run(str('player %s spawn in survival',botname));
					run(str('tp %s %s',botname,player_name));
					write_file(global_json_name_1,'shared_json',spawned_list);
					write_file(global_json_name_2,'shared_json',summoned_by_list);
				),
				spawned_player_num == global_spawning_limit,
				(
					display_title(player_name, 'clear');
					display_title(player_name, 'actionbar', format('rb [Warning!!] '+str('You have already summoned %d Bots!',global_spawning_limit)),1,40,1);
				),
			);
		),
		//如果该bot已经被调用命令的玩家生成
		summoned_by == player_name,
		(
			display_title(player_name, 'clear');
			display_title(player_name, 'actionbar', format('lb [Warning!!] That Bot has been summoned by you!'),1,40,1);
		),
		//如果该bot已经被其他玩家生成
		summoned_by != player_name,
		(
			display_title(player_name, 'clear');
			display_title(player_name, 'actionbar', format('vb [Warning!!] That Bot has been summoned by others!'),1,40,1);
		),
		
	);
);

kick(originalbotname) -> (
	botname = lower(global_summon_prefix + originalbotname);
	p = player();
	player_name = p ~ 'name';
	summoned_by_list = decode_json(read_file(global_json_name_2,'shared_json'));
	summoned_by = get(summoned_by_list,botname);
	if
	(
		summoned_by == null,
		(
			display_title(player_name, 'clear');
			display_title(player_name, 'actionbar', format('lb [Warning!!] The Bot has not been summoned!'),1,40,1);
		),
		summoned_by == player_name,
		(
			spawned_list = decode_json(read_file(global_json_name_1,'shared_json'));
			spawned_player_num = get(spawned_list,player_name);
			put(spawned_list,player_name,spawned_player_num-1);
			display_title(player_name, 'clear');
			display_title(player_name, 'actionbar', format('d '+str('Kicked %s',botname)),1,40,1);
			run(str('player %s kill',botname));
			delete(summoned_by_list,botname);
			write_file(global_json_name_1,'shared_json',spawned_list);
			write_file(global_json_name_2,'shared_json',summoned_by_list);
		),
		summoned_by != player_name,
		(
			display_title(player_name, 'clear');
			display_title(player_name, 'actionbar', format('rb [Warning!!] None of your business!'),1,40,1);
		),
	),
);

attack(originalbotname,interval) -> (
	p = player();
	player_name = p ~ 'name';
	botname = lower(global_summon_prefix + originalbotname);
	summoned_by_list = decode_json(read_file(global_json_name_2,'shared_json'));
	summoned_by = get(summoned_by_list,botname);
	if
	(
		summoned_by == null,
		(
			display_title(player_name, 'clear');
			display_title(player_name, 'actionbar', format('lb [Warning!!] The Bot has not been summoned!'),1,40,1);
		),
		player_name == summoned_by,
		(
			if
			(
				interval == -1,
				(
					display_title(player_name, 'clear');
					display_title(player_name, 'actionbar', format('d '+str('%s is attacking continuously!',botname)),1,40,1);
					run(str('player %s attack continuous',botname));
				),
				interval == 0,
				(
					display_title(player_name, 'clear');
					display_title(player_name, 'actionbar', format('d '+str('%s attacked once!',botname)),1,40,1);
					run(str('player %s attack once',botname));
				),
				interval > 0,
				(
					display_title(player_name, 'clear');
					display_title(player_name, 'actionbar', format('d '+str('%s will attack every %d tick!',botname,interval)),1,40,1);
					run(str('player %s attack interval %d',botname,interval));
				),
				
			),
		),
		player_name != summoned_by,
		(
			display_title(player_name, 'clear');
			display_title(player_name, 'actionbar', format('rb [Warning!!] None of your business!'),1,40,1);
		),
	),
);

use(originalbotname,interval) -> (
	p = player();
	player_name = p ~ 'name';
	botname = lower(global_summon_prefix + originalbotname);
	summoned_by_list = decode_json(read_file(global_json_name_2,'shared_json'));
	summoned_by = get(summoned_by_list,botname);
	if
	(
		summoned_by == null,
		(
			display_title(player_name, 'clear');
			display_title(player_name, 'actionbar', format('lb [Warning!!] The Bot has not been summoned!'),1,40,1);
		),
		player_name == summoned_by,
		(
			if
			(
				interval == -1,
				(
					display_title(player_name, 'clear');
					display_title(player_name, 'actionbar', format('d '+str('%s is using continuously!',botname)),1,40,1);
					run(str('player %s use continuous',botname));
				),
				interval == 0,
				(
					display_title(player_name, 'clear');
					display_title(player_name, 'actionbar', format('d '+str('%s used once!',botname)),1,40,1);
					run(str('player %s use once',botname));
				),
				interval > 0,
				(
					display_title(player_name, 'clear');
					display_title(player_name, 'actionbar', format('d '+str('%s will use every %d tick!',botname,interval)),1,40,1);
					run(str('player %s use interval %d',botname,interval));
				),
				
			),
		),
		player_name != summoned_by,
		(
			display_title(player_name, 'clear');
			display_title(player_name, 'actionbar', format('rb [Warning!!] None of your business!'),1,40,1);
		),
	),
);

__on_player_dies(player) -> {
	botname = lower(player ~ 'name');
	summoned_by_list = decode_json(read_file(global_json_name_2,'shared_json'));
	summoned_by = get(summoned_by_list,botname);
	if
	(
		summoned_by != null,
		(
			spawned_list = decode_json(read_file(global_json_name_1,'shared_json'));
			spawned_player_num = get(spawned_list,summoned_by);
			put(spawned_list,summoned_by,spawned_player_num-1);
			delete(summoned_by_list,botname);
			write_file(global_json_name_1,'shared_json',spawned_list);
			write_file(global_json_name_2,'shared_json',summoned_by_list);
		),	
	),
};