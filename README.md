# LunarLander
Lunar lander model for the assignment

## Instructions

To build the java application with `make`
```shell-session
$ make
```

To run the application, with `make`
```shell-session
$ make run
```
or with java directly
```shell-session
$ java -jar LunarLander.jar
```

## Protocol Description
The Lander listens for UDP messages on 

+ 127.0.0.1
+ port 65200


1. Messages are formatted as lines of text ending with newlines.
2. Lines are Key-value pairs with a colon separating key and value

### command message
The command message starts with a line 

        command:

It is followed by action lines, either 

        throttle: 34

Where the number is the throttle percentage to set  
or  

        roll: 2.3

Where the number is the roll-rate in degrees-per-second

#### examples
```
command:
throttle: 50
```
sets the throttle to 50%

```
command:
roll:-4
```
sets the roll-rate to -4°/s, which is anti-clockwise.

```
command:
throttle:20
roll:1
```
sets both the throttle and roll-rate.

### reply
the message send back in response is

```
command:=
altitude:34.4
fuel:33.2
flying:1
crashed:0
orientation:34.2
Vx:23.2
Vy:-23,4
```

All values are formatted as signed floating point numbers, except for `flying`
and `crashed` with are the integers 0 or 1 indicating a boolean value.

### State message
The state request message queries the state of the controls, throttle, and
roll-rate.  The message is

```
state:
```
followed by query lines for the throttle and roll-rate,
```
throttle:
```
and/or
```
roll:
```
#### example
```
state:
throttle:
roll:
```

### reply
The reply to the state message is
```state:=
```
with the throttle and/or roll values filled in.

#### example
```
state:=
throttle:20
roll:1
```

