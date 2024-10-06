extern crate preference_analyzer;

use std::env;
use std::process;

fn main() {
    let args: Vec<String> = env::args().collect();
    println!("{:?}", args);

    let config = preference_analyzer::Config::new(&args).unwrap_or_else(|err| {
        println!("Problems occur during parsing arguments: {}", err);
        process::exit(1);
    });

    if let Err(e) = preference_analyzer::run(config) {
        println!("Run-time errors occur: {}", e);

        process::exit(1);
    }
}
