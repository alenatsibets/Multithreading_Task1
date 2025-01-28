Task1. Multithreading
Requirements:

 Any entity that wants to access a shared resource must be a
thread.
 Classes and other application entities must be properly structured into packages
and have a name that reflects their functionality.
 Use the State pattern to describe object states, unless there are more than two of these states.
 Callable may be used to create threads, if possible.
 Only TimeUnit enumeration capabilities may be used instead of Thread.sleep.
 Read object initialization data from a file. The data in the file is correct.
 Use Log4J2 or any other to write logs.
 It is allowed to use the main method to start.

14. Task Scheduler: Develop a system for executing tasks on a schedule.
Description: Create a scheduler that will run tasks at a specific time or at specified intervals. Use a thread pool to execute multiple tasks in parallel to ensure efficiency.
Key Concepts
Task Scheduler:
o The central component that manages tasks and their execution.
o Allows users to add tasks with a specified time or interval
of execution.
Thread Pool:
o Used to manage task execution threads.
o Provides efficient resource allocation and parallel execution
of tasks.
Tasks:
o Represent pieces of code that need to be executed. Can be implemented using an interface or an abstract class.
Key Components
Task Interface:
o Defines a method for executing a task.
TaskScheduler Class:
o Manages adding tasks, scheduling them, and executing them.
o Uses ScheduledExecutorService to manage tasks according to a schedule.
ScheduledTask Class:
o Stores information about the task, including the execution time and the task itself.
Operation Algorithm
Initialization:
o An instance of the scheduler is created, which initializes the thread pool.
Adding tasks:
o The user adds tasks with a specified execution time or interval.
o Tasks are placed in a queue for execution.
Executing tasks:
o The scheduler uses a thread pool to execute tasks in parallel according to the schedule.
o Each task is executed in its own thread.
Cleaning up completed tasks:
o When a task is finished, it is removed from the queue.
