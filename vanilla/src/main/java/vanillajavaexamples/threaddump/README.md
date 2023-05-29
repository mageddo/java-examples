```
2023-05-29 10:56:16
Full thread dump OpenJDK 64-Bit Server VM (11.0.14.1+1 mixed mode):

Threads class SMR info:
_java_thread_list=0x00006000035b1f60, length=11, elements={
0x00007f8593010000, 0x00007f8595808800, 0x00007f859580d800, 0x00007f859307d800,
0x00007f858e8fe000, 0x00007f859680a800, 0x00007f858e8ff000, 0x00007f859580e000,
0x00007f8592817800, 0x00007f8592834800, 0x00007f8596014800
}

"main" #1 prio=5 os_prio=31 cpu=158102.27ms elapsed=158.69s tid=0x00007f8593010000 nid=0x1703 runnable  [0x0000700001dad000]
   java.lang.Thread.State: RUNNABLE
	at vanillajavaexamples.threaddump.ThreadDumpMain.main(ThreadDumpMain.java:14)

   Locked ownable synchronizers:
	- None

"Reference Handler" #2 daemon prio=10 os_prio=31 cpu=0.11ms elapsed=158.67s tid=0x00007f8595808800 nid=0x4803 waiting on condition  [0x00007000024c3000]
   java.lang.Thread.State: RUNNABLE
	at java.lang.ref.Reference.waitForReferencePendingList(java.base@11.0.14.1/Native Method)
	at java.lang.ref.Reference.processPendingReferences(java.base@11.0.14.1/Reference.java:241)
	at java.lang.ref.Reference$ReferenceHandler.run(java.base@11.0.14.1/Reference.java:213)

   Locked ownable synchronizers:
	- None

"Finalizer" #3 daemon prio=8 os_prio=31 cpu=0.26ms elapsed=158.67s tid=0x00007f859580d800 nid=0x3803 in Object.wait()  [0x00007000025c6000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(java.base@11.0.14.1/Native Method)
	- waiting on <0x000000070ff09008> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(java.base@11.0.14.1/ReferenceQueue.java:155)
	- waiting to re-lock in wait() <0x000000070ff09008> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(java.base@11.0.14.1/ReferenceQueue.java:176)
	at java.lang.ref.Finalizer$FinalizerThread.run(java.base@11.0.14.1/Finalizer.java:170)

   Locked ownable synchronizers:
	- None

"Signal Dispatcher" #4 daemon prio=9 os_prio=31 cpu=0.31ms elapsed=158.66s tid=0x00007f859307d800 nid=0x4003 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"Service Thread" #5 daemon prio=9 os_prio=31 cpu=0.07ms elapsed=158.66s tid=0x00007f858e8fe000 nid=0x5603 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"C2 CompilerThread0" #6 daemon prio=9 os_prio=31 cpu=96.12ms elapsed=158.66s tid=0x00007f859680a800 nid=0x5803 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

   Locked ownable synchronizers:
	- None

"C1 CompilerThread0" #14 daemon prio=9 os_prio=31 cpu=37.43ms elapsed=158.66s tid=0x00007f858e8ff000 nid=0x5a03 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE
   No compile task

   Locked ownable synchronizers:
	- None

"Sweeper thread" #18 daemon prio=9 os_prio=31 cpu=0.11ms elapsed=158.66s tid=0x00007f859580e000 nid=0x5b03 runnable  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"Common-Cleaner" #19 daemon prio=8 os_prio=31 cpu=0.18ms elapsed=158.63s tid=0x00007f8592817800 nid=0x5d03 in Object.wait()  [0x0000700002de1000]
   java.lang.Thread.State: TIMED_WAITING (on object monitor)
	at java.lang.Object.wait(java.base@11.0.14.1/Native Method)
	- waiting on <0x000000070fe1eb98> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(java.base@11.0.14.1/ReferenceQueue.java:155)
	- waiting to re-lock in wait() <0x000000070fe1eb98> (a java.lang.ref.ReferenceQueue$Lock)
	at jdk.internal.ref.CleanerImpl.run(java.base@11.0.14.1/CleanerImpl.java:148)
	at java.lang.Thread.run(java.base@11.0.14.1/Thread.java:829)
	at jdk.internal.misc.InnocuousThread.run(java.base@11.0.14.1/InnocuousThread.java:161)

   Locked ownable synchronizers:
	- None

"the sleeper" #20 prio=5 os_prio=31 cpu=0.86ms elapsed=158.62s tid=0x00007f8592834800 nid=0x5e03 waiting on condition  [0x0000700002ee4000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(java.base@11.0.14.1/Native Method)
	at vanillajavaexamples.threaddump.ThreadDumpMain.sleeperJob(ThreadDumpMain.java:28)
	at vanillajavaexamples.threaddump.ThreadDumpMain.lambda$main$0(ThreadDumpMain.java:9)
	at vanillajavaexamples.threaddump.ThreadDumpMain$$Lambda$1/0x0000000800060840.run(Unknown Source)
	at java.lang.Thread.run(java.base@11.0.14.1/Thread.java:829)

   Locked ownable synchronizers:
	- None

"Attach Listener" #21 daemon prio=9 os_prio=31 cpu=0.94ms elapsed=126.81s tid=0x00007f8596014800 nid=0x6003 waiting on condition  [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"VM Thread" os_prio=31 cpu=4.69ms elapsed=158.68s tid=0x00007f859201b800 nid=0x4903 runnable  

"GC Thread#0" os_prio=31 cpu=0.31ms elapsed=158.69s tid=0x00007f858e810800 nid=0x2c03 runnable  

"G1 Main Marker" os_prio=31 cpu=0.16ms elapsed=158.69s tid=0x00007f858e844800 nid=0x2e03 runnable  

"G1 Conc#0" os_prio=31 cpu=0.02ms elapsed=158.69s tid=0x00007f858e845000 nid=0x3003 runnable  

"G1 Refine#0" os_prio=31 cpu=0.16ms elapsed=158.69s tid=0x00007f858e8fa000 nid=0x3203 runnable  

"G1 Young RemSet Sampling" os_prio=31 cpu=13.46ms elapsed=158.69s tid=0x00007f858e8fa800 nid=0x4d03 runnable  
"VM Periodic Task Thread" os_prio=31 cpu=61.72ms elapsed=158.63s tid=0x00007f859280c800 nid=0xa403 waiting on condition  

JNI global refs: 14, weak refs: 0


```
