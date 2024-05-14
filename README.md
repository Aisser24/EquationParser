# EQ Parser
This is a boolean equation simulation program written in Java. It implements a very simple GUI with the SWING framework.

## How to use it
The standard time steps are given in the `standardInputs.txt` file and used by default, you can use your own file by changing it in the dropdown-menu and clicking `Add new...`

To evaluate an equation, simply enter it in the text-field and click `calculate`.
Valid equations use the following convention:

* Inputs - `E` followed by the corresponding integer value, e.g. `E1`
* Parethesies - used like normal `( ... )`
* Logical operators - the following logical operators can be used by typing them out:
  - `and`
  - `or`
  - `not`
  - `nor`
  - `nand`
  - `xor`
  - `xnor`
 
Here's an example for a valid equation:
`Y = (E1 xor E2) or ((E4 nor E5) and E3)`

> [!TIP]
> ## Installation:
>
> **MacOS / Linux**
> ```
> git clone https://github.com/Aisser24/EquationParser
> ```
> ```
> cd ./EquationParser/src && java Gleichungssim.java
> ```
>
> **Windows**
> ```
> git clone https://github.com/Aisser24/EquationParser
> ```
> ```
> cd .\EquationParser\src
> java .\Gleichungssim.java
> ```



> [!CAUTION]
> In order to use the standard-inputs you may have to move the `standardInputs.txt` file into the same folder as the `Gleichungssim.java` file.

## How it works
The program loops over each bit of the input and replaces the `E1`, `E2`, ... with the corresponding boolean value:

`Y = (E1 xor E2) or ((E4 nor E5) and E3)` --> `Y = (false xor true) or ((false nor false) and true)`

After this step, the equation string is simplified:

`Y = (false xor true) or ((false nor false) and true)` --> `(falsexortrue)or((falsenorfalse)andtrue)`

Now it begins breaking up this string into the smallest possible equations with only two parts, evaluating those and replacing them in the string with the result:

`(falsexortrue)or((falsenorfalse)andtrue)` --> `(falsexortrue)or(trueandtrue)`

It does this recursively until there is only one boolean value left in the string. This value is then added to the output and it moves on to the next bit in the input.
