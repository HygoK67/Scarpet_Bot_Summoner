global_spawning_limit = 3;
global_json_name_1 = 'bot_summoned_num_dict';
global_json_name_2 = 'summoned_by';
global_summon_prefix = 'bot_';

__config() -> {
	'stay_loaded' -> 'true',
	'commands' -> {
		'summon <bot_name>' -> 'summon',
		'kick <bot_name>' -> 'kick',
	},
	'arguments' -> {
		'bot_name' -> {'type' -> 'term','suggest' -> []},
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
	botname = global_summon_prefix + originalbotname;
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
					print('You have summoned 1 Bot now');
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
					print(str('You have summoned %d Bots now',spawned_player_num+1));
					put(spawned_list,player_name,spawned_player_num+1);
					put(summoned_by_list,botname,player_name);
					run(str('player %s spawn in survival',botname));
					run(str('tp %s %s',botname,player_name));
					write_file(global_json_name_1,'shared_json',spawned_list);
					write_file(global_json_name_2,'shared_json',summoned_by_list);
				),
				spawned_player_num == global_spawning_limit,
				(
					print(str('You have already summoned %d Bots!',global_spawning_limit));
				),
			);
		),
		//如果该bot已经被调用命令的玩家生成
		summoned_by == player_name,
		(
			print('That Bot has been summoned by you!');
		),
		//如果该bot已经被其他玩家生成
		summoned_by != player_name,
		(
			print('That Bot has been summoned by others!')
		),
		
	);
);

kick(originalbotname) -> (
	botname = global_summon_prefix + originalbotname;
	p = player();
	player_name = p ~ 'name';
	summoned_by_list = decode_json(read_file(global_json_name_2,'shared_json'));
	summoned_by = get(summoned_by_list,botname);
	if
	(
		summoned_by == null,
		(
			print('The Bot has not ever been summoned!');
		),
		summoned_by == player_name,
		(
			spawned_list = decode_json(read_file(global_json_name_1,'shared_json'));
			spawned_player_num = get(spawned_list,player_name);
			put(spawned_list,player_name,spawned_player_num-1);
			print(str('Kicked %s',botname));
			run(str('player %s kill',botname));
			delete(summoned_by_list,botname);
			write_file(global_json_name_1,'shared_json',spawned_list);
			write_file(global_json_name_2,'shared_json',summoned_by_list);
		),
		summoned_by != player_name,
		(
			print('None of your business!');
		),
	),
);

__on_player_dies(player) -> {
	botname = player ~ 'name';
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