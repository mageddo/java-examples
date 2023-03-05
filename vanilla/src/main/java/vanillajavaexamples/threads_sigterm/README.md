Java will wait to not daemon threads to complete and stop before exit the program when receives a sigterm signal or
the main thread finish its execution.

The daemon thread difference it's that Java won't wait for them. 
