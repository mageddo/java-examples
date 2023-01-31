# Options 
## 1. Commons Cli

Features

* Produce programs with a syntax unix, java like
* Very simple usage
* No reflection

Limitations

* Too few contributions (last version was released 2021)
* Don't have support to subcommands 


## 2. JCommander

Features

* All default cli stuff
* Converters to parse simple args to complex objects, like Path object.
* Subcommands

Limitations

* Uses Reflections, not a good option for native image programs

## 1. Pico Cli
TBD


# Dictionary
* Subcommands: A command like `git status` or `docker ps` different than `git --status`
