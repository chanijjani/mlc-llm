mod triple_generator;

use std::error::Error;

use crate::triple_generator::TripleGenerator;

pub struct Config {
    pub function: String,
    pub json_data_filename: String,
    pub data_owner_name: String,
    pub cohesion: i32,
}

impl Config {
    pub fn new(args: &[String]) -> Result<Config, &'static str> {
        let function = args[1].clone();
        let json_data_filename = args[2].clone();
        let mut data_owner_name = "Chanhee".to_owned();
        let mut cohesion = 1;
        if args.len() >= 4 {
            data_owner_name = args[3].clone();
            println!("\nDATA OWNER is set to {}", data_owner_name);
        }
        if args.len() >= 5 {
            cohesion = args[4].clone().parse::<i32>().unwrap();
            println!("\nCOHESION is set to {}\n", cohesion);
        }
        match function.as_str() {
            "triple_gen" => {
                if args.len() < 3 {
                    return Err("Not enough arguments, USAGE: triple_gen [JSON data file]");
                }
            }
            "rag" => {
                if args.len() < 4 {
                    return Err(
                        "Not enough arguments, USAGE: rag [JSON data file] [event_message]",
                    );
                }
            }
            _ => {
                return Err("Not enough arguments, USAGE: [triple_gen|rag] [JSON data file] [event_message (rag only)]");
            }
        }

        Ok(Config {
            function,
            json_data_filename,
            data_owner_name,
            cohesion,
        })
    }
}

pub fn run(config: Config) -> Result<(), Box<dyn Error>> {
    match config.function.as_str() {
        "triple_gen" => {
            println!("[TRIPLE GENERATION]");
            let mut triple_generator = TripleGenerator::new();
            let triples = triple_generator.generate_triples(
                config.json_data_filename.clone(),
                config.data_owner_name,
                config.cohesion,
            );
            println!("{:?}", triples);
        }
        _ => {
            println!("[TODO] [Retrival Augmented Generation]");
        }
    }

    Ok(())
}
