use serde_json::Value;
use strum::IntoEnumIterator;
use strum_macros::EnumIter;
use substring::Substring;

use std::fs::File;
use std::io;
use std::io::BufRead;
use std::io::Read;
use std::path::Path;

#[derive(Debug, EnumIter)]
enum FileExtension {
    Text,
    Json,
}

impl FileExtension {
    fn as_str(&self) -> &'static str {
        match self {
            FileExtension::Text => ".txt",
            FileExtension::Json => ".json",
        }
    }
}

pub struct TripleGenerator {}

impl TripleGenerator {
    pub fn new() -> TripleGenerator {
        TripleGenerator {}
    }

    pub fn generate_triples(
        &mut self,
        filepath: String,
        data_owner_name: String,
        cohesion: i32,
    ) -> Vec<String> {
        let mut triple_list = Vec::new();

        let mut file_extension = FileExtension::Text;
        for ext in FileExtension::iter() {
            let file_format =
                filepath.substring(filepath.len() - ext.as_str().len(), filepath.len());
            if file_format == ext.as_str() {
                file_extension = ext;
                println!("Supported file format found: {}", file_format);
                break;
            }
        }
        match file_extension {
            FileExtension::Json => {
                let mut f = File::open(filepath).expect("file not found");

                let mut contents = String::new();
                f.read_to_string(&mut contents)
                    .expect("Fail to read the given JSON data file.");

                let contents_str: Value =
                    serde_json::from_str(contents.as_str()).expect("Fail to parse JSON contents.");
                // TODO: Replace hard-coded text.
                let contents_value = &contents_str["AlexCalendar2024"];
                let month_list = contents_value
                    .as_object()
                    .expect("Fail to convert to an object.")
                    .keys();
                for month in month_list {
                    let event_type_list = contents_value[month]
                        .as_object()
                        .expect("Fail to extract schedule types from a month.")
                        .keys();
                    for event_type in event_type_list {
                        if contents_value[month][event_type].as_array().unwrap().len() == 0 {
                            continue;
                        }

                        let schedule_list = contents_value[month][event_type].as_array().unwrap();
                        for schedule in schedule_list {
                            // println!("schedule = {}", schedule);
                            let event_name = schedule["event"].to_string();
                            // TODO: Revise the specific predicate if needed.
                            let mut ownership_triple = String::from("wd:Alex wd:has wd:");
                            ownership_triple.push_str(&event_name);
                            ownership_triple.push_str(" .");
                            println!("{}", ownership_triple);

                            let mut time_instance_triple = String::from("wd:");
                            time_instance_triple.push_str(&event_name);
                            // TODO: Revise the specific predicate if needed.
                            time_instance_triple.push_str(" wd:\"is on\" wd:");
                            let data_len = schedule["date"].to_string().len();
                            time_instance_triple
                                .push_str(&schedule["date"].to_string().substring(0, data_len - 1));
                            time_instance_triple.push_str(" ");
                            let time_len = schedule["time"].to_string().len();
                            time_instance_triple
                                .push_str(&schedule["time"].to_string().substring(1, time_len));
                            time_instance_triple.push_str(" .");
                            println!("{}", time_instance_triple);
                        }
                    }
                }
            }
            FileExtension::Text => {
                if let Ok(lines) = Self::read_lines(filepath) {
                    // Consumes the iterator, returns an (Optional) String
                    for line in lines.flatten() {
                        if line.trim().len() == 0 {
                            continue;
                        }

                        // println!("{}", line.trim());
                        let preprocessed_line =
                            Self::preprocess_line(line.trim(), data_owner_name.clone(), cohesion);
                    }
                }
            }
        }

        triple_list
    }

    fn read_lines<P>(filepath: P) -> io::Result<io::Lines<io::BufReader<File>>>
    where
        P: AsRef<Path>,
    {
        let file = File::open(filepath)?;
        Ok(io::BufReader::new(file).lines())
    }

    fn preprocess_line(line: &str, data_owner_name: String, cohesion: i32) -> String {
        let mut words: Vec<&str> = line.split(' ').filter(|x|
            {
                x.to_lowercase() != "a" && x.to_lowercase() != "an" && x.to_lowercase() != "the" &&    // Article
                x.to_lowercase() != "be" && x.to_lowercase() != "is" && x.to_lowercase() != "was" &&
                x.to_lowercase() != "are" && x.to_lowercase() != "were" &&
                x.to_lowercase() != "do" && x.to_lowercase() != "did" && x.to_lowercase() != "done" && // Auxiliary verb
                x.to_lowercase() != "can" && x.to_lowercase() != "could" &&                            
                x.to_lowercase() != "may" && x.to_lowercase() != "might" &&
                x.to_lowercase() != "must" && x.to_lowercase() != "should" &&
                x.to_lowercase() != "and" && x.to_lowercase() != "too" &&                              // Conjunction
                x.to_lowercase() != "so" && x.to_lowercase() != "therefore" &&
                x.to_lowercase() != "but" && x.to_lowercase() != "however" &&
                x.to_lowercase() != "at" && x.to_lowercase() != "in" && x.to_lowercase() != "on" &&    // Preposition
                x.to_lowercase() != "for" && x.to_lowercase() != "to" && x.to_lowercase() != "of" &&
                x.to_lowercase() != "it" && x.to_lowercase() != "that" &&                               // Pronoun
                x.to_lowercase() != "this" && x.to_lowercase() != "these" &&
                x.to_lowercase() != "he" && x.to_lowercase() != "his" && x.to_lowercase() != "him" &&
                x.to_lowercase() != "she" && x.to_lowercase() != "her" && x.to_lowercase() != "hers" &&
                x.to_lowercase() != "they" && x.to_lowercase() != "those" && x.to_lowercase() != "them"
            }
        ).collect();
        if words.len() < 2 {
            return "".to_owned();
        }

        words[0] = words[0].substring(0, words[0].len() - 1);
        let mut remaining_words_count = cohesion;
        let mut grouped_words: Vec<String> = vec![];
        let mut proceeded_index = 1;
        for i in 1..words.len() {
            // println!("Start i = {} = {}", i, words[i]);
            if i < proceeded_index {
                // println!("proceeded_index = {}", proceeded_index);
                continue;
            }

            if words[i].to_lowercase() == "you" {
                words[i] = &data_owner_name;
            }
            if words[i].to_lowercase() == "i" || words[i].to_lowercase() == "me" {
                words[i] = &words[0];
            }

            let first_character = words[i].to_string().chars().collect::<Vec<char>>()[0];
            if first_character.is_lowercase() {
                grouped_words.push(words[i].to_string());
                proceeded_index += 1;
                continue;
            }

            let last_character = words[i].substring(words[i].len() - 1, words[i].len());
            // println!("LAST CHARACTER = {}", last_character);
            if last_character == "."
                || last_character == ","
                || last_character == "!"
                || last_character == "?"
            {
                grouped_words.push(words[i].to_string());
                proceeded_index += 1;
                continue;
            }

            let mut merged_word = words[i].to_owned();
            while proceeded_index + 1 < words.len()
                && words[proceeded_index + 1]
                    .to_string()
                    .chars()
                    .collect::<Vec<char>>()[0]
                    .is_uppercase()
            {
                let next_word_first_character = words[proceeded_index + 1]
                    .to_string()
                    .chars()
                    .collect::<Vec<char>>()[0];
                if next_word_first_character.is_uppercase() {
                    print!("MERGING happens!! => {}", &words[proceeded_index]);
                    merged_word += words[proceeded_index + 1];
                    println!(" + {} => {}", &words[proceeded_index + 1], merged_word);
                    // words[i+1] = &merged_word.clone();
                    // *i += 1;
                    proceeded_index += 1;
                    // println!("proceeded_index is now = {}", proceeded_index);
                    let last_index = words[proceeded_index].len();
                    let last_character =
                        words[proceeded_index].substring(last_index - 1, last_index);
                    // println!("LAST CHARACTER = {}", last_character);
                    if last_character == "."
                        || last_character == ","
                        || last_character == "!"
                        || last_character == "?"
                    {
                        break;
                    }
                }
            }
            grouped_words.push(merged_word.clone());
            proceeded_index += 1;
        }
        println!("{:?}\n", grouped_words);
        
        words[0].to_string()
    }
}
